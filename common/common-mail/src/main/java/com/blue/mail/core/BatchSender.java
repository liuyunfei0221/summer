package com.blue.mail.core;

import com.blue.base.common.base.BlueCheck;
import com.blue.base.model.exps.BlueException;
import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.api.conf.SmtpAttr;
import com.blue.mail.common.SenderComponentProcessor;
import jakarta.mail.Message;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import reactor.util.Logger;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.blue.base.common.base.OriginalThrowableGetter.getOriginalThrowable;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.mail.common.SenderComponentProcessor.*;
import static java.lang.Integer.bitCount;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.runAsync;
import static reactor.util.Loggers.getLogger;

/**
 * batch sender
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "AlibabaLowerCamelCaseVariableNaming"})
public final class BatchSender {

    private static final Logger LOGGER = getLogger(BatchSender.class);

    private final ExecutorService executorService;

    private final Transporter[] transporters;

    private final int indexMask;

    private final AtomicInteger CURSOR = new AtomicInteger(-1);

    private Set<String> throwableForRetry;

    private int retryTimes;

    private final boolean withDKIM;

    private byte[] domainKey;

    private String domain;

    private String selector;

    public BatchSender(MailSenderConf conf) {
        confAsserter(conf);

        this.executorService = generateExecutorService(conf);

        List<SmtpAttr> smtpAttrs = conf.getSmtpAttrs();

        List<Session> sessions = smtpAttrs.stream().map(SenderComponentProcessor::generateSession)
                .collect(Collectors.toList());

        int sessionSize = sessions.size();

        int bufferSize = conf.getBufferSize();
        if (bufferSize > MAXIMUM_BUFFER_SIZE)
            bufferSize = MAXIMUM_BUFFER_SIZE;

        bufferSize = bufferSize * sessionSize;

        if (bufferSize < 0L || bitCount(bufferSize) != 1) {
            int n = -1 >>> Integer.numberOfLeadingZeros(bufferSize - 1);
            bufferSize = (n < 0) ? 1 : n;
        }

        this.indexMask = bufferSize - 1;

        this.transporters = new Transporter[bufferSize];

        for (int i = 0; i < bufferSize; i++) {
            Transporter transporter = new Transporter(sessions.get(i % sessionSize));
            this.transporters[i] = transporter;
            this.executorService.submit(transporter::init);
        }

        this.throwableForRetry = ofNullable(conf.getThrowableForRetry())
                .filter(BlueCheck::isNotEmpty).map(HashSet::new).orElseGet(HashSet::new);

        this.retryTimes = ofNullable(conf.getRetryTimes()).filter(rt -> rt >= 0).orElse(0);

        this.withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);
        if (this.withDKIM) {
            this.domainKey = getDomainKey(conf.getDomainKeyFile());
            this.domain = conf.getDomain();
            this.selector = conf.getSelector();
        }
    }


    private final Predicate<Throwable> RETRY_PREDICATE = throwable ->
            throwable != null && throwableForRetry.contains(getOriginalThrowable(throwable).getClass().getName());

    private final BiConsumer<Transporter, Message> MESSAGE_SENDER = (transport, message) -> {
        try {
            transport.sendMessage(message);
        } catch (Throwable throwable) {
            if (RETRY_PREDICATE.test(throwable)) {
                LOGGER.error("CompletableFuture<Void> sendMessage(Message message) failed, retry, throwable = {0}", throwable);
                for (int i = 0; i < retryTimes; i++)
                    try {
                        transport.sendMessage(message);
                        break;
                    } catch (Exception ex) {
                        LOGGER.error("CompletableFuture<Void> sendMessage(Message message) retry failed, retryTimes = {}, throwable = {}", retryTimes, throwable);
                    }
            }
            LOGGER.error("CompletableFuture<Void> sendMessage(Message message) failed, throwable = {0}", throwable);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "Consumer<Message> MESSAGE_SENDER, failed");
        }
    };

    public MimeMessage initMessage() {
        return transporters[indexMask & CURSOR.get()].initMessage();
    }

    public void signDkim(Message message) {
        if (withDKIM)
            signDkimForMessage(message, domainKey, domain, selector);
    }

    public CompletableFuture<Void> sendMessage(Message message) {
        if (message == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "message can't be null");

        signDkim(message);
        return runAsync(() -> MESSAGE_SENDER.accept(transporters[indexMask & CURSOR.incrementAndGet()], message), executorService);
    }

}
