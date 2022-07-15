//import com.blue.basic.common.base.BlueCheck;
//import com.blue.basic.model.exps.BlueException;
//import com.blue.mail.api.conf.MailSenderConf;
//import jakarta.mail.Message;
//import jakarta.mail.Session;
//import jakarta.mail.Transport;
//import jakarta.mail.internet.MimeMessage;
//import reactor.util.Logger;
//
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileReader;
//import java.util.Base64;
//import java.util.HashSet;
//import java.util.Set;
//import java.util.concurrent.*;
//import java.util.function.Consumer;
//import java.util.function.Predicate;
//
//import static com.blue.basic.common.base.BlueCheck.isBlank;
//import static com.blue.basic.common.base.FileProcessor.getFile;
//import static com.blue.basic.common.base.OriginalThrowableGetter.getOriginalThrowable;
//import static com.blue.basic.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
//import static com.blue.basic.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
//import static com.blue.mail.common.SenderComponentGenerator.generateSession;
//import static java.util.Optional.ofNullable;
//import static java.util.concurrent.CompletableFuture.runAsync;
//import static java.util.concurrent.TimeUnit.SECONDS;
//import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
//import static reactor.util.Loggers.getLogger;
//
//@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaLowerCamelCaseVariableNaming"})
//public final class MailSender {
//
//    private static final Logger LOGGER = getLogger(MailSender.class);
//
//    private Session session;
//
//    private final ExecutorService executorService;
//
//    private Set<String> throwableForRetry;
//
//    private int retryTimes;
//
//    private final boolean withDKIM;
//
//    private byte[] domainKey;
//
//    private String domain;
//
//    private String selector;
//
//    private static final String DEFAULT_THREAD_NAME_PRE = "blue-mail-sender-thread" + PAR_CONCATENATION_DATABASE_URL.identity;
//    private static final int RANDOM_LEN = 6;
//
//    public MailSender(MailSenderConf conf) {
//        confAsserter(conf);
//
//        this.session = generateSession(conf);
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
//        this.executorService = new ThreadPoolExecutor(conf.getCorePoolSize(),
//                conf.getMaximumPoolSize(), conf.getKeepAliveSeconds(), SECONDS,
//                new ArrayBlockingQueue<>(conf.getBlockingQueueCapacity()),
//                r ->
//                        new Thread(r, threadNamePre + randomAlphabetic(RANDOM_LEN)),
//                rejectedExecutionHandler);
//
//        this.throwableForRetry = ofNullable(conf.getThrowableForRetry())
//                .filter(BlueCheck::isNotEmpty).map(HashSet::new).orElseGet(HashSet::new);
//
//        this.retryTimes = ofNullable(conf.getRetryTimes()).filter(rt -> rt >= 0).orElse(0);
//
//        this.withDKIM = ofNullable(conf.getWithDKIM()).orElse(false);
//        if (this.withDKIM) {
//            this.domainKey = getDomainKey(conf.getDomainKeyFile());
//            this.domain = conf.getDomain();
//            this.selector = conf.getSelector();
//        }
//    }
//
//    private final Predicate<Throwable> RETRY_PREDICATE = throwable ->
//            throwable != null && throwableForRetry.contains(getOriginalThrowable(throwable).getClass().getName());
//
//    private final Consumer<Message> MESSAGE_SENDER = message -> {
//        try {
//            Transport transport = session.getTransport();
//            transport.connect();
//
////            Transport.send(message, message.getAllRecipients());
//            transport.sendMessage(message, message.getAllRecipients());
//
//            transport.close();
//        } catch (Throwable throwable) {
//            if (RETRY_PREDICATE.test(throwable)) {
//                LOGGER.error("CompletableFuture<Void> sendMessage(Message message) failed, retry, throwable = {0}", throwable);
//                for (int i = 0; i < retryTimes; i++)
//                    try {
//                        Transport.send(message);
//                        break;
//                    } catch (Exception ex) {
//                        LOGGER.error("CompletableFuture<Void> sendMessage(Message message) retry failed, retryTimes = {}, throwable = {}", retryTimes, throwable);
//                    }
//            }
//            LOGGER.error("CompletableFuture<Void> sendMessage(Message message) failed, throwable = {0}", throwable);
//            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "Consumer<Message> MESSAGE_SENDER, failed");
//        }
//    };
//
//    public MimeMessage initMessage() {
//        return new MimeMessage(session);
//    }
//
//    /**
//     * send message
//     *
//     * @param message
//     * @return
//     */
//    public CompletableFuture<Void> sendMessage(Message message) {
//        return runAsync(() -> MESSAGE_SENDER.accept(message), executorService);
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
