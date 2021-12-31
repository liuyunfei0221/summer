package com.blue.base.test;

import org.simplejavamail.api.email.Email;
import org.simplejavamail.api.mailer.Mailer;
import org.simplejavamail.email.EmailBuilder;
import org.simplejavamail.mailer.MailerBuilder;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

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

    private static final Properties PROPS;
    private static final Authenticator AUTHENTICATOR;
    private static final Session SESSION;

    static {

        PROPS = new Properties();

        PROPS.put("mail.transport.protocol", "smtp");
        PROPS.put("mail.smtp.host", HOST);
        PROPS.put("mail.smtp.port", PORT);
        PROPS.put("mail.smtp.auth", true);
        PROPS.put("mail.smtp.ssl", true);
        PROPS.put("mail.smtp.starttls.enable", true);
        PROPS.put("mail.debug", true);

        AUTHENTICATOR = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER_NAME, PASSWORD);
            }
        };

        SESSION = Session.getInstance(PROPS, AUTHENTICATOR);

        MAILER = MailerBuilder
                .usingSession(SESSION)
                .withSessionTimeout(1000 * 1000)
                .clearEmailAddressCriteria()
                .withDebugLogging(true)
                .async()
                .buildMailer();

        //MAILER = MailerBuilder
        //        .withSMTPServer(HOST, 587, USER_NAME, PASSWORD)
        //        .withSessionTimeout(1000 * 1000)
        //        .clearEmailAddressCriteria()
        //        .withProperty("mail.smtp.ssl", true)
        //        .withProperty("mail.smtp.starttls.enable", true)
        //        .withDebugLogging(true)
        //        .async()
        //        .buildMailer();
    }

    private static void test1() {

        String to1 = "";
        String to2 = "";

        Email email = EmailBuilder.startingBlank()
                .from(USER_NAME)
                .to(to1)
                .to(to2)
                //.to(toName2, to2)
                .cc("", "")
                //.withReplyTo("lollypop", "lolly.pop@othermail.com")
                .withSubject("text")
                //.withHTMLText("<img src='cid:wink1'><b>We should meet up!</b><img src='cid:wink2'>")
                .withPlainText("What would you like for Lunch?")
                //.withEmbeddedImage("wink1", imageByteArray, "image/png")
                //.withEmbeddedImage("wink2", imageDatesource)
                //.withAttachment("invitation", pdfByteArray, "application/pdf")
                //.withAttachment("dresscode", odfDatasource)
                //.withHeader("X-Priority", 5)
                //.withReturnReceiptTo()
                //.withDispositionNotificationTo("notify-read-emails@candyshop.com")
                //.withBounceTo("tech@candyshop.com")
                //.signWithDomainKey(privateKeyData, "somemail.com", "selector") // DKIM
                //.signWithSmime(pkcs12Config)
                //.encryptWithSmime(x509Certificate)
                .buildEmail();

        MAILER.sendMail(email);
    }


    //public static void main(String[] args) {
    //    test1();
    //}

}
