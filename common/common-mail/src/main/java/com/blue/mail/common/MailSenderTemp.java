//package com.blue.mail.common;
//
//import com.blue.base.model.exps.BlueException;
//import com.blue.mail.api.conf.MailSenderConf;
//import org.simplejavamail.api.email.Email;
//import org.simplejavamail.api.email.EmailPopulatingBuilder;
//import org.simplejavamail.api.mailer.Mailer;
//import org.simplejavamail.mailer.MailerBuilder;
//import org.simplejavamail.mailer.internal.MailerRegularBuilderImpl;
//import reactor.util.Logger;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.Base64;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.CompletableFuture;
//import java.util.concurrent.RejectedExecutionHandler;
//import java.util.concurrent.ThreadPoolExecutor;
//
//import static com.blue.base.common.base.Check.isBlank;
//import static com.blue.base.common.base.FileProcessor.getFile;
//import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
//import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
//import static java.util.Optional.ofNullable;
//import static java.util.concurrent.TimeUnit.SECONDS;
//import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
//import static reactor.util.Loggers.getLogger;
//
///**
// * mail sender
// *
// * @author liuyunfei
// * @date 2022/1/4
// * @apiNote
// */
//@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "AlibabaLowerCamelCaseVariableNaming", "DuplicatedCode"})
//public final class MailSenderTemp {
//
//    private static final Logger LOGGER = getLogger(MailSenderTemp.class);
//
//    private final boolean withDKIM;
//
//    private byte[] domainKey;
//
//    private String domain;
//
//    private String selector;
//
//    private final Mailer MAILER;
//
//    private static final String DEFAULT_THREAD_NAME_PRE = "blue-mail-sender-thread" + PAR_CONCATENATION_DATABASE_URL.identity;
//    private static final int RANDOM_LEN = 6;
//
//    public MailSenderTemp(MailSenderConf conf) {
//        confAsserter(conf);
//
//        MailerRegularBuilderImpl builder =
//                MailerBuilder.withSMTPServer(conf.getSmtpServerHost(), conf.getSmtpServerPort(), conf.getSmtpUsername(), conf.getSmtpPassword());
//
//        ofNullable(conf.getEmailValidator())
//                .ifPresent(builder::withEmailValidator);
//
//        String threadNamePre = ofNullable(conf.getThreadNamePre())
//                .map(p -> p + PAR_CONCATENATION_DATABASE_URL.identity)
//                .orElse(DEFAULT_THREAD_NAME_PRE);
//
//        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
//            LOGGER.warn("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution");
//            r.run();
//        };
//
//        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(conf.getCorePoolSize(),
//                conf.getMaximumPoolSize(), conf.getKeepAliveSeconds(), SECONDS,
//                new ArrayBlockingQueue<>(conf.getBlockingQueueCapacity()),
//                r ->
//                        new Thread(r, threadNamePre + randomAlphabetic(RANDOM_LEN)),
//                rejectedExecutionHandler);
//
//        builder.withExecutorService(threadPoolExecutor);
//
//        ofNullable(conf.getConnectionPoolCoreSize()).filter(n -> n > 0)
//                .ifPresent(builder::withConnectionPoolCoreSize);
//
//        ofNullable(conf.getConnectionPoolMaxSize()).filter(n -> n > 0)
//                .ifPresent(builder::withConnectionPoolMaxSize);
//
//        ofNullable(conf.getConnectionPoolClaimTimeoutMillis()).filter(m -> m > 0)
//                .ifPresent(builder::withConnectionPoolClaimTimeoutMillis);
//
//        ofNullable(conf.getConnectionPoolExpireAfterMillis()).filter(m -> m > 0)
//                .ifPresent(builder::withConnectionPoolExpireAfterMillis);
//
//        ofNullable(conf.getSessionTimeout()).filter(t -> t > 0)
//                .ifPresent(builder::withSessionTimeout);
//
//        ofNullable(conf.getConnectionPoolLoadBalancingStrategy())
//                .ifPresent(builder::withConnectionPoolLoadBalancingStrategy);
//
//        ofNullable(conf.getTransportStrategy())
//                .ifPresent(builder::withTransportStrategy);
//
//        ofNullable(conf.getDebugLogging())
//                .ifPresent(builder::withDebugLogging);
//
//        ofNullable(conf.getProps())
//                .ifPresent(builder::withProperties);
//
//        builder.async();
//
//        this.MAILER = builder.buildMailer();
//
//        this.withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);
//        if (this.withDKIM) {
//            this.domainKey = getDomainKey(conf.getDomainKeyFile());
//            this.domain = conf.getDomain();
//            this.selector = conf.getSelector();
//        }
//    }
//
//    /**
//     * DKIM
//     *
//     * @param emailPopulatingBuilder
//     */
//    public void signWithDomainKey(EmailPopulatingBuilder emailPopulatingBuilder) {
//        if (withDKIM)
//            emailPopulatingBuilder.signWithDomainKey(domainKey, domain, selector);
//    }
//
//    /**
//     * send mail
//     *
//     * @param email
//     * @return
//     */
//    public CompletableFuture<Void> sendMail(Email email) {
//        LOGGER.info("CompletableFuture<Void> sendMail(Email email), email = {}", email);
//        return MAILER.sendMail(email, true);
//    }
//
//    private static final String PRI_KEY_BEGIN = "-----BEGIN PRIVATE KEY-----";
//    private static final String PRI_KEY_END = "-----END PRIVATE KEY-----";
//
//    public byte[] getDomainKey(String uri) {
//        File priKeyFile = getFile(uri);
//
//        try (FileReader fr = new FileReader(priKeyFile);
//             BufferedReader br = new BufferedReader(fr)) {
//
//            String line;
//            StringBuilder sb = null;
//            while ((line = br.readLine()) != null)
//                if (line.equals(PRI_KEY_BEGIN))
//                    sb = new StringBuilder();
//                else if (line.equals(PRI_KEY_END))
//                    break;
//                else if (sb != null) sb.append(line);
//
//            if (sb == null || line == null)
//                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "private key is invalid");
//
//            return Base64.getDecoder().decode(sb.toString());
//        } catch (Exception e) {
//            LOGGER.error("get domain key failed， e = {]", e);
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "get domain key failed");
//        }
//    }
//
//    /**
//     * assert params
//     *
//     * @param conf
//     */
//    private static void confAsserter(MailSenderConf conf) {
//        if (conf == null)
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");
//
//        if (isBlank(conf.getSmtpServerHost()))
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpServerHost can't be blank");
//
//        Integer smtpServerPort = conf.getSmtpServerPort();
//        if (smtpServerPort == null || smtpServerPort < 1)
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpServerPort can't be null or less than 1");
//
//        if (isBlank(conf.getSmtpUsername()))
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpUsername can't be blank");
//
//        if (isBlank(conf.getSmtpPassword()))
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "smtpPassword can't be blank");
//
//        Integer corePoolSize = conf.getCorePoolSize();
//        if (corePoolSize == null || corePoolSize < 1)
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "corePoolSize can't be null or less than 1");
//
//        Integer maximumPoolSize = conf.getMaximumPoolSize();
//        if (maximumPoolSize == null || maximumPoolSize < 1 || maximumPoolSize < corePoolSize)
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maximumPoolSize can't be null or less than 1 or less than corePoolSize");
//
//        Long keepAliveTime = conf.getKeepAliveSeconds();
//        if (keepAliveTime == null || keepAliveTime < 1L)
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "keepAliveTime can't be null or less than 1");
//
//        Integer blockingQueueCapacity = conf.getBlockingQueueCapacity();
//        if (blockingQueueCapacity == null || blockingQueueCapacity < 1)
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "blockingQueueCapacity can't be null or less than 1");
//
//        Boolean withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);
//        if (withDKIM) {
//            if (isBlank(conf.getDomainKeyFile()))
//                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "domain key file path can't be blank");
//
//            if (isBlank(conf.getDomain()))
//                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "domain can't be blank");
//
//            if (isBlank(conf.getSelector()))
//                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "selector can't be blank");
//        }
//    }
//
//}
