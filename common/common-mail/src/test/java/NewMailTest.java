//import com.blue.mail.api.conf.MailConfParams;
//import com.blue.mail.common.MailSender;
//import com.sanctionco.jmail.EmailValidator;
//import org.simplejavamail.api.email.Email;
//import org.simplejavamail.email.EmailBuilder;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.CompletableFuture;
//
//import static com.blue.mail.api.generator.BlueMailSenderGenerator.generateMailSender;
//import static org.simplejavamail.api.mailer.config.LoadBalancingStrategy.ROUND_ROBIN;
//import static org.simplejavamail.api.mailer.config.TransportStrategy.SMTP;
//
///**
// * @author liuyunfei
// * @date 2021/12/27
// * @apiNote
// */
//public class NewMailTest {
//
//    private static final Logger LOGGER = LoggerFactory.getLogger(NewMailTest.class);
//
//    private static final String HOST = "outlook.office365.com";
//    private static final int PORT = 587;
//    private static final String USER_NAME = "";
//    private static final String PASSWORD = "";
//
//
//    private static final MailSender MAIL_SENDER;
//
//    static {
//        MailConfParams params = new MailConfParams() {
//            @Override
//            public EmailValidator getEmailValidator() {
//                return null;
//            }
//        };
//
//        params.setSmtpServerHost(HOST);
//        params.setSmtpServerPort(PORT);
//        params.setSmtpUsername(USER_NAME);
//        params.setSmtpPassword(PASSWORD);
//
//        params.setCorePoolSize(2);
//        params.setMaximumPoolSize(8);
//        params.setKeepAliveSeconds(128L);
//        params.setBlockingQueueCapacity(4096);
//
//        params.setConnectionPoolCoreSize(32);
//        params.setConnectionPoolMaxSize(64);
//        params.setConnectionPoolClaimTimeoutMillis(6000);
//        params.setConnectionPoolExpireAfterMillis(6000);
//        params.setSessionTimeout(6000);
//        params.setConnectionPoolLoadBalancingStrategy(ROUND_ROBIN);
//        params.setTransportStrategy(SMTP);
//        params.setWithDKIM(false);
//        params.setDebugLogging(true);
//
//        Map<String, String> props = new HashMap<>(4, 1.0f);
//        props.put("mail.smtp.ssl", "true");
//        props.put("mail.smtp.starttls.enable", "true");
//
//        params.setProps(props);
//
//        //protected Boolean withDKIM;
//        //protected String domainKeyFile;
//        //protected String domain;
//        //protected String selector;
//
//        MAIL_SENDER = generateMailSender(params);
//    }
//
//    private static void test1() {
//        String to1 = "liuyunfei0221@outlook.com";
//        String to2 = "liuyunfei19890221@gmail.com";
//        String to3 = "liuyunfei19890221@163.com";
//
//        Email email = EmailBuilder.startingBlank()
//                .from(USER_NAME)
//                .to(to1)
//                .to(to2)
//                .to(to3)
//                //.ccWithFixedName("C. Bo group", "chocobo1@candyshop.org", "chocobo2@candyshop.org")
//                //.withRecipientsWithFixedName("Tasting Group", BCC,
//                //        "taster1@cgroup.org;taster2@cgroup.org;tester <taster3@cgroup.org>")
//                //.bcc("Mr Sweetnose <snose@candyshop.org>")
//                //.withReplyTo("lollypop", "lolly.pop@othermail.com")
//                .withSubject("hey")
//                //.withHTMLText("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>")
//                .withPlainText("Please view this email in a modern email client!")
//                //.withCalendarText(CalendarMethod.REQUEST, iCalendarText)
//                //.withEmbeddedImage("wink1", imageByteArray, "image/png")
//                //.withEmbeddedImage("wink2", imageDatesource)
//                //.withAttachment("invitation", pdfByteArray, "application/pdf")
//                //.withAttachment("dresscode", odfDatasource)
//                .withHeader("X-Priority", 5)
//                .withReturnReceiptTo()
//                //.withDispositionNotificationTo("notify-read-emails@candyshop.com")
//                //.withBounceTo("tech@candyshop.com")
//                //.signWithDomainKey(privateKeyData, "somemail.com", "selector") // DKIM
//                //.signWithSmime(pkcs12Config)
//                //.encryptWithSmime(x509Certificate)
//                .buildEmail();
//
//        CompletableFuture<Void> future = MAIL_SENDER.sendMail(email);
//        future.join();
//    }
//
//
////    public static void main(String[] args) {
////        test1();
////    }
//
//}
