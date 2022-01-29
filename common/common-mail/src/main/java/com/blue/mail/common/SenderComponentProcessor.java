package com.blue.mail.common;

import com.blue.base.model.exps.BlueException;
import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.api.conf.SenderAttr;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
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

import static com.blue.base.common.base.BlueCheck.isBlank;
import static com.blue.base.common.base.BlueCheck.isEmpty;
import static com.blue.base.common.base.FileProcessor.getFile;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
import static jakarta.mail.Session.getInstance;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static reactor.util.Loggers.getLogger;

/**
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaLowerCamelCaseVariableNaming", "DuplicatedCode", "UnusedReturnValue"})
public final class SenderComponentProcessor {

    private static final Logger LOGGER = getLogger(SenderComponentProcessor.class);

    private static final String DEFAULT_THREAD_NAME_PRE = "blue-mail-sender-thread" + PAR_CONCATENATION_DATABASE_URL.identity;
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
        confAsserter(conf);

        String threadNamePre = ofNullable(conf.getThreadNamePre())
                .map(p -> p + PAR_CONCATENATION_DATABASE_URL.identity)
                .orElse(DEFAULT_THREAD_NAME_PRE);

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.warn("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution");
            r.run();
        };

        return new ThreadPoolExecutor(conf.getCorePoolSize(),
                conf.getMaximumPoolSize(), conf.getKeepAliveSeconds(), SECONDS,
                new ArrayBlockingQueue<>(conf.getBlockingQueueCapacity()),
                r ->
                        new Thread(r, threadNamePre + randomAlphabetic(RANDOM_LEN)),
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
            while ((line = br.readLine()) != null)
                if (line.equals(PRI_KEY_BEGIN))
                    sb = new StringBuilder();
                else if (line.equals(PRI_KEY_END))
                    break;
                else if (sb != null) sb.append(line);

            if (sb == null || line == null)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "private key is invalid");

            return Base64.getDecoder().decode(sb.toString());
        } catch (Exception e) {
            LOGGER.error("get domain key failed， e = {]", e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "get domain key failed");
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
    public static void confAsserter(MailSenderConf conf) {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");

        if (isEmpty(conf.getSmtpAttrs()))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpAttrs can't be blank");

        Integer corePoolSize = conf.getCorePoolSize();
        if (corePoolSize == null || corePoolSize < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "corePoolSize can't be null or less than 1");

        Integer maximumPoolSize = conf.getMaximumPoolSize();
        if (maximumPoolSize == null || maximumPoolSize < 1 || maximumPoolSize < corePoolSize)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maximumPoolSize can't be null or less than 1 or less than corePoolSize");

        Long keepAliveTime = conf.getKeepAliveSeconds();
        if (keepAliveTime == null || keepAliveTime < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "keepAliveTime can't be null or less than 1");

        Integer blockingQueueCapacity = conf.getBlockingQueueCapacity();
        if (blockingQueueCapacity == null || blockingQueueCapacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "blockingQueueCapacity can't be null or less than 1");

        if (conf.getBufferSize() == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "bufferSize can't be null");

        Boolean withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);
        if (withDKIM) {
            if (isBlank(conf.getDomainKeyFile()))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "domain key file path can't be blank");

            if (isBlank(conf.getDomain()))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "domain can't be blank");

            if (isBlank(conf.getSelector()))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "selector can't be blank");
        }
    }

}
