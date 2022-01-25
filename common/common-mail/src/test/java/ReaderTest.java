import com.blue.mail.api.conf.MailReaderConfParams;
import com.blue.mail.common.MailReader;
import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMultipart;

public class ReaderTest {

    public static void main(String[] args) {
        
        MailReaderConfParams params = new MailReaderConfParams();
        params.setImapHost("outlook.office365.com");
        params.setImapPort(993);
        params.setImapSslEnable(true);
        params.setUser("yunfei0221@outlook.com");
        params.setPassword("Fei19890116");
        params.setFolderName("INBOX");
        params.setMaxWaitingMillisForRefresh(5000);
        params.setDebug(false);

        MailReader mailReader = new MailReader(params);

        System.err.println("COUNT = " + mailReader.getMessageCount());

        Message[] messages = mailReader.getMessages();

        for (Message msg : messages) {
            messageParse(msg);
        }

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
                System.err.println("邮件的parts：" + count);

                for (int i = 0; i < count; i++) {
                    BodyPart bodyPart = mimeMultipart.getBodyPart(i);
                    Object content = bodyPart.getContent();

                    System.err.println("邮件的内容" + i + ": " + content);
                }
            } else {
                Object content = message.getContent();
                System.err.println("邮件的内容：" + content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
