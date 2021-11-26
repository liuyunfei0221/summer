package com.blue.base.test;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import static org.springframework.http.MediaType.IMAGE_JPEG_VALUE;

/**
 * @author liuyunfei
 * @date 2021/11/18
 * @apiNote
 */
public class MailTest {

    private static final String CHARSET = StandardCharsets.UTF_8.name();

    private static final String HOST = "outlook.office365.com";
    private static final int PORT = 587;
    private static final String USER_NAME = "liuyunfei0221@outlook.com";
    private static final String PASSWORD = "yiTian0116";

    private static final Properties PROPS;
    private static final Authenticator AUTHENTICATOR;
    private static final Session SESSION;

    private static final JavaMailSender MAIL_SENDER;

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

        JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
        senderImpl.setJavaMailProperties(PROPS);
        senderImpl.setSession(SESSION);

        MAIL_SENDER = senderImpl;
    }

    private static void sendSimpleMailMessage(String from, List<String> to, String subject, String text) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to.toArray(String[]::new));
        message.setSubject(subject);
        message.setText(text);
        message.setSentDate(new Date());
        message.setFrom(from);

        MAIL_SENDER.send(message);
    }

    private static void sendMimeMailMessage(String from, List<String> to, String subject, String text) {
        MimeMessage message = MAIL_SENDER.createMimeMessage();

        String attachment = "E:\\tempFile\\source\\walls\\a_beautiful_view_of_colorful_autumn_trees-wallpaper-2560x1440.jpg";

        try (FileInputStream fileInputStream = new FileInputStream(attachment);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            ByteArrayResource byteArrayResource = new ByteArrayResource(bufferedInputStream.readAllBytes());

            MimeMessageHelper helper = new MimeMessageHelper(message, true, CHARSET);
            helper.setTo(to.toArray(String[]::new));
            helper.setSubject(subject);
            helper.setText(text);
            helper.setSentDate(new Date());
            helper.setFrom(from);

            helper.addAttachment("a_beautiful_view_of_colorful_autumn_trees-wallpaper-2560x1440.jpg", byteArrayResource);

            MAIL_SENDER.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendMimeMailMessageByHtml(String from, List<String> to, String subject, String text) {

        long start = System.currentTimeMillis();

        MimeMessage message = MAIL_SENDER.createMimeMessage();

        String attachment = "E:\\tempFile\\source\\walls\\a_beautiful_view_of_colorful_autumn_trees-wallpaper-2560x1440.jpg";

        try (FileInputStream fileInputStream = new FileInputStream(attachment);
             BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream)) {

            ByteArrayResource byteArrayResource = new ByteArrayResource(bufferedInputStream.readAllBytes());

            MimeMessageHelper helper = new MimeMessageHelper(message, true, CHARSET);
            helper.setTo(to.toArray(String[]::new));
            helper.setSubject(subject);

            helper.setText("<html><body>" + text + "<img src=\"cid:tar.jpg\"></body></html>", true);

            helper.setSentDate(new Date());
            helper.setFrom(from);

            helper.addInline("tar.jpg", byteArrayResource, IMAGE_JPEG_VALUE);

            MAIL_SENDER.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.err.println(System.currentTimeMillis() - start);
    }


    //public static void main(String[] args) {
    //    sendSimpleMailMessage("liuyunfei0221@outlook.com", singletonList("yunfei0221@outlook.com"), "a test message", "this is a test message");
    //    sendMimeMailMessage("liuyunfei0221@outlook.com", singletonList("yunfei0221@outlook.com"), "a test message", "this is a test message");
    //    sendMimeMailMessageByHtml("liuyunfei0221@outlook.com", singletonList("yunfei0221@outlook.com"), "a test message", "this is a test message");
    //}


}
