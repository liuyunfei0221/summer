import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import static org.simplejavamail.api.mailer.config.LoadBalancingStrategy.ROUND_ROBIN;

/**
 * @author liuyunfei
 * @date 2021/12/27
 * @apiNote
 */
public class NewMailTest {

    private static final String HOST = "outlook.office365.com";
    private static final int PORT = 587;
    private static final String USER_NAME = "";
    private static final String PASSWORD = "";


    private static final Mailer MAILER;

    static {

        MAILER = MailerBuilder
                .withSMTPServer(HOST, PORT, USER_NAME, PASSWORD)
                .withSessionTimeout(1000 * 100000)
                .clearEmailValidator()
                .withProperty("mail.smtp.ssl", true)
                .withProperty("mail.smtp.starttls.enable", true)
                .withDebugLogging(true)

                .withThreadPoolSize(10)
                .withThreadPoolKeepAliveTime(10)



                .withConnectionPoolExpireAfterMillis(5000)
                .withConnectionPoolClaimTimeoutMillis(5000)
                .withConnectionPoolLoadBalancingStrategy(ROUND_ROBIN)



                .async()
                .buildMailer();
    }

    private static void test1() {
        String to1 = "liuyunfei0221@outlook.com";
        String to2 = "liuyunfei19890221@gmail.com";

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


        MAILER.sendMail(email, true);
    }


    public static void main(String[] args) {
        test1();
    }

}
