import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.*;
import jakarta.mail.internet.MimeMultipart;

import java.util.Properties;

public class MailReadTest {


    final static String USER = "yunfei0221@outlook.com";
    final static String PASSWORD = "Fei19890116";

    public static void main(String[] args) {
        readMail1();
    }

    private static void messageParse(Message message) {
        try {
            String subject = message.getSubject();
            String from = (message.getFrom()[0]).toString();
            System.err.println("邮件的主题：" + subject);
            System.err.println("邮件的发件人地址：" + from);

            String contentType = message.getContentType();
            System.err.println("邮件的内容类型：" + contentType);

            if (contentType.startsWith("multipart")) {
                MimeMultipart mimeMultipart = (MimeMultipart) message.getContent();

                int count = mimeMultipart.getCount();
                System.err.println("邮件的count：" + count);

                for (int i = 0; i < count; i++) {
                    BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                    Object content = bodyPart.getContent();

                    System.err.println("邮件的内容"+i+": " + content);
                }
            } else {
                Object content = message.getContent();
                System.err.println("邮件的内容：" + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readMail1() {
        try {
            Properties props = new Properties();

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.imap.ssl.socketFactory", sf);

            props.setProperty("mail.imap.host", "outlook.office365.com");
            props.setProperty("mail.imap.port", "993");
            props.setProperty("mail.imapStore.protocol", "imap");
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.debug", "false");

            Session session = Session.getInstance(props);
            Store store = session.getStore("imaps");
            store.connect("outlook.office365.com", 993, USER, PASSWORD);


            Folder folder = store.getFolder("inbox");
//            Folder folder = store.getFolder("sent");

//            Folder folder = store.getFolder("spam");

            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                System.err.println();
                System.err.println();
                System.err.println();

                System.err.println("第 " + (i + 1) + "封邮件的信息：");
                messageParse(messages[i]);

                System.err.println();
                System.err.println();
                System.err.println();
            }

            if (messages.length > 0) {
                folder.setFlags(messages, new Flags(Flags.Flag.SEEN), true);
            }

            // 5、关闭
            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void readMail2() {
        try {
            Properties props = new Properties();

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.pop3.ssl.socketFactory", sf);

            props.setProperty("mail.pop3.host", "outlook.office365.com");
            props.setProperty("mail.pop3.port", "995");
            props.setProperty("mail.popStore.protocol", "pop3");
            props.setProperty("mail.pop3.ssl.enable", "true");
            props.setProperty("mail.debug", "true");

            Session session = Session.getInstance(props);
            Store store = session.getStore("pop3");
            store.connect("outlook.office365.com", 995, USER, PASSWORD);
            Folder folder = store.getFolder("inbox");
            folder.open(Folder.READ_WRITE);
            Message[] messages = folder.getMessages();
            for (int i = 0; i < messages.length; i++) {
                System.err.println();
                System.err.println();
                System.err.println();

                System.err.println("第 " + (i + 1) + "封邮件的信息：");
                messageParse(messages[i]);

                System.err.println();
                System.err.println();
                System.err.println();
            }
            // 5、关闭
            folder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
