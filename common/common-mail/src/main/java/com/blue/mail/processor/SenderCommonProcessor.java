package com.blue.mail.processor;

import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.api.conf.SenderAttr;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import net.openhft.affinity.AffinityThreadFactory;
import reactor.util.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Base64;
import java.util.Properties;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.FileGetter.getFile;
import static com.blue.basic.constant.common.Symbol.HYPHEN;
import static jakarta.mail.Session.getInstance;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaLowerCamelCaseVariableNaming", "DuplicatedCode", "UnusedReturnValue"})
public final class SenderCommonProcessor {

    private static final Logger LOGGER = getLogger(SenderCommonProcessor.class);

    private static final String DEFAULT_THREAD_NAME_PRE = "blue-mail-sender-thread" + HYPHEN.identity;
    private static final int RANDOM_LEN = 6;

    public static final int MAXIMUM_BUFFER_SIZE = 16;

    private static final String PRI_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
    private static final String PRI_KEY_END = "-----END PRIVATE KEY-----";

    /**
     * generate a session
     *
     * @param senderAttr
     * @return
     */
    public static Session generateSession(SenderAttr senderAttr) {
        try {
            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);

            Properties props = new Properties();
            props.put("mail.smtp.ssl.socketFactory", sf);
            props.putAll(senderAttr.getProps());

            return getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(senderAttr.getUser(), senderAttr.getPassword());
                }
            });

        } catch (Exception e) {
            LOGGER.error("Session generateSession(MailSenderConf mailSenderConf) failed, e = {}", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * generate executor
     *
     * @param conf
     * @return
     */
    public static ExecutorService generateExecutorService(MailSenderConf conf) {
        assertConf(conf);

        String threadNamePre = ofNullable(conf.getThreadNamePre())
                .map(p -> p + HYPHEN.identity)
                .orElse(DEFAULT_THREAD_NAME_PRE);

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.warn("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution");
            r.run();
        };

        return new ThreadPoolExecutor(conf.getCorePoolSize(),
                conf.getMaximumPoolSize(), conf.getKeepAliveSeconds(), SECONDS,
                new ArrayBlockingQueue<>(conf.getBlockingQueueCapacity()),
                new AffinityThreadFactory(threadNamePre + randomAlphabetic(RANDOM_LEN), SAME_CORE),
                rejectedExecutionHandler);
    }

    /**
     * parse domain key
     *
     * @param uri
     * @return
     */
    public static byte[] getDomainKey(String uri) {
        File priKeyFile = getFile(uri);

        try (FileReader fr = new FileReader(priKeyFile);
             BufferedReader br = new BufferedReader(fr)) {

            String line;
            StringBuilder sb = null;
            while (isNotNull((line = br.readLine())))
                if (line.equals(PRI_KEY_BEGIN))
                    sb = new StringBuilder();
                else if (line.equals(PRI_KEY_END))
                    break;
                else if (isNotNull(sb)) sb.append(line);

            if (isNull(sb) || isNull(line))
                throw new RuntimeException("private key is invalid");

            return Base64.getDecoder().decode(sb.toString());
        } catch (Exception e) {
            LOGGER.error("get domain key failed， e = {]", e);
            throw new RuntimeException("get domain key failed");
        }
    }

    /**
     * sign for message
     *
     * @param message
     * @param domainKey
     * @param domain
     * @param selector
     * @return
     */
    public static Message signDkimForMessage(Message message, byte[] domainKey, String domain, String selector) {

        return message;
    }

    /**
     * assert params
     *
     * @param conf
     */
    public static void assertConf(MailSenderConf conf) {
        if (isNull(conf))
            throw new RuntimeException("conf can't be null");

        if (isEmpty(conf.getSmtpAttrs()))
            throw new RuntimeException("smtpAttrs can't be blank");

        Integer corePoolSize = conf.getCorePoolSize();
        if (isNull(corePoolSize) || corePoolSize < 1)
            throw new RuntimeException("corePoolSize can't be null or less than 1");

        Integer maximumPoolSize = conf.getMaximumPoolSize();
        if (isNull(maximumPoolSize) || maximumPoolSize < 1 || maximumPoolSize < corePoolSize)
            throw new RuntimeException("maximumPoolSize can't be null or less than 1 or less than corePoolSize");

        Long keepAliveTime = conf.getKeepAliveSeconds();
        if (isNull(keepAliveTime) || keepAliveTime < 1L)
            throw new RuntimeException("keepAliveTime can't be null or less than 1");

        Integer blockingQueueCapacity = conf.getBlockingQueueCapacity();
        if (isNull(blockingQueueCapacity) || blockingQueueCapacity < 1)
            throw new RuntimeException("blockingQueueCapacity can't be null or less than 1");

        if (isNull(conf.getBufferSize()))
            throw new RuntimeException("bufferSize can't be null");

        Boolean withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);
        if (withDKIM) {
            if (isBlank(conf.getDomainKeyFile()))
                throw new RuntimeException("domain key file path can't be blank");

            if (isBlank(conf.getDomain()))
                throw new RuntimeException("domain can't be blank");

            if (isBlank(conf.getSelector()))
                throw new RuntimeException("selector can't be blank");
        }
    }

}
