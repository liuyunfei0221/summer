package com.blue.mail.core;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.exps.BlueException;
import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.api.conf.SenderAttr;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
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

import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.OriginalThrowableGetter.getOriginalThrowable;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.mail.processor.SenderCommonProcessor.*;
import static java.lang.Integer.bitCount;
import static java.lang.Integer.numberOfLeadingZeros;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.runAsync;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;

/**
 * batch sender
 *
 * @author liuyunfei
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
        assertConf(conf);

        this.executorService = generateExecutorService(conf);

        List<SenderAttr> senderAttrs = conf.getSmtpAttrs();

        List<Transporter> transporters = senderAttrs.stream().map(attr ->
                new Transporter(generateSession(attr), attr.getUser())
        ).collect(toList());

        int transporterSize = transporters.size();

        int bufferSize = conf.getBufferSize();
        if (bufferSize > MAXIMUM_BUFFER_SIZE)
            bufferSize = MAXIMUM_BUFFER_SIZE;

        bufferSize = bufferSize * transporterSize;

        if (bufferSize < 0L || bitCount(bufferSize) != 1) {
            int n = -1 >>> numberOfLeadingZeros(bufferSize - 1);
            bufferSize = (n < 0) ? 1 : n;
        }

        this.indexMask = bufferSize - 1;

        this.transporters = new Transporter[bufferSize];

        int c = 0;
        Transporter transporter;
        for (int i = 0; i < bufferSize; i++) {
            transporter = transporters.get((c++) % transporterSize);
            this.executorService.submit(transporter::init);
            this.transporters[i] = transporter;
        }

        this.throwableForRetry = ofNullable(conf.getThrowableForRetry())
                .filter(BlueChecker::isNotEmpty).map(HashSet::new).orElseGet(HashSet::new);

        this.retryTimes = ofNullable(conf.getRetryTimes()).filter(rt -> rt >= 0).orElse(0);

        this.withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);
        if (this.withDKIM) {
            this.domainKey = getDomainKey(conf.getDomainKeyFile());
            this.domain = conf.getDomain();
            this.selector = conf.getSelector();
        }
    }

    private final Predicate<Throwable> RETRY_PREDICATE = t ->
            isNotNull(t) && throwableForRetry.contains(getOriginalThrowable(t).getClass().getName());

    private final BiConsumer<Transporter, Message> MESSAGE_SENDER = (transport, message) -> {
        try {
            message.addFrom(new InternetAddress[]{new InternetAddress(transport.from())});
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
            throw new RuntimeException("Consumer<Message> MESSAGE_SENDER, failed");
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
        if (isNull(message))
            throw new BlueException(BAD_REQUEST);

        signDkim(message);
        return runAsync(() -> MESSAGE_SENDER.accept(transporters[indexMask & CURSOR.incrementAndGet()], message), executorService);
    }

}
