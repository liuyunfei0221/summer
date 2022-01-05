import com.blue.mail.api.conf.MailConfParams;
import com.blue.mail.common.MailSender;
import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.email.EmailPopulatingBuilder;
import org.simplejavamail.email.EmailBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

import static com.blue.mail.api.generator.BlueMailSenderGenerator.generateMailSender;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.simplejavamail.api.mailer.config.LoadBalancingStrategy.ROUND_ROBIN;
import static org.simplejavamail.api.mailer.config.TransportStrategy.SMTP;

/**
 * @author liuyunfei
 * @date 2021/12/27
 * @apiNote
 */
public class NewMailTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NewMailTest.class);

    private static final String HOST = "outlook.office365.com";
    private static final int PORT = 587;
    private static final String USER_NAME = "";
    private static final String PASSWORD = "";


    private static final MailSender MAIL_SENDER;

    static {
        MailConfParams params = new MailConfParams();

        params.setSmtpServerHost(HOST);
        params.setSmtpServerPort(PORT);
        params.setSmtpUsername(USER_NAME);
        params.setSmtpPassword(PASSWORD);

        String threadNamePre = "temp-message-send-executor-";

        ThreadFactory threadFactory = r ->
                new Thread(r, threadNamePre + randomAlphabetic(4));

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.error("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution, : r = {}", r);
            r.run();
        };

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(4,
                8,
                128, SECONDS,
                new ArrayBlockingQueue<>(4096),
                threadFactory, rejectedExecutionHandler);

        params.setExecutorService(threadPoolExecutor);
        params.setConnectionPoolCoreSize(32);
        params.setConnectionPoolMaxSize(64);
        params.setConnectionPoolClaimTimeoutMillis(6000);
        params.setConnectionPoolExpireAfterMillis(6000);
        params.setSessionTimeout(6000);
        params.setConnectionPoolLoadBalancingStrategy(ROUND_ROBIN);
        params.setTransportStrategy(SMTP);
        params.setAsync(true);
        params.setWithDKIM(false);
        params.setDebugLogging(true);

        Map<String, Object> props = new HashMap<>(4, 1.0f);
        props.put("mail.smtp.ssl", true);
        props.put("mail.smtp.starttls.enable", true);

        params.setProperties(props);

        //protected Boolean withDKIM;
        //protected String domainKeyFile;
        //protected String domain;
        //protected String selector;

        MAIL_SENDER = generateMailSender(params);
    }

    private static void test1() {
        String to1 = "liuyunfei0221@outlook.com";
        String to2 = "liuyunfei19890221@gmail.com";

        EmailPopulatingBuilder builder = EmailBuilder.startingBlank();

        Email email = EmailBuilder.startingBlank()
                .from(USER_NAME)
                .to(to1)
                .to(to2)
                //.ccWithFixedName("C. Bo group", "chocobo1@candyshop.org", "chocobo2@candyshop.org")
                //.withRecipientsWithFixedName("Tasting Group", BCC,
                //        "taster1@cgroup.org;taster2@cgroup.org;tester <taster3@cgroup.org>")
                //.bcc("Mr Sweetnose <snose@candyshop.org>")
                //.withReplyTo("lollypop", "lolly.pop@othermail.com")
                .withSubject("hey")
                //.withHTMLText("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>")
                .withPlainText("Please view this email in a modern email client!")
                //.withCalendarText(CalendarMethod.REQUEST, iCalendarText)
                //.withEmbeddedImage("wink1", imageByteArray, "image/png")
                //.withEmbeddedImage("wink2", imageDatesource)
                //.withAttachment("invitation", pdfByteArray, "application/pdf")
                //.withAttachment("dresscode", odfDatasource)
                .withHeader("X-Priority", 5)
                .withReturnReceiptTo()
                //.withDispositionNotificationTo("notify-read-emails@candyshop.com")
                //.withBounceTo("tech@candyshop.com")
                //.signWithDomainKey(privateKeyData, "somemail.com", "selector") // DKIM
                //.signWithSmime(pkcs12Config)
                //.encryptWithSmime(x509Certificate)
                .buildEmail();

        MAIL_SENDER.sendMail(email, true);
    }


    public static void main(String[] args) {
        test1();
    }

}
