import com.sun.mail.util.BASE64DecoderStream;
import com.sun.mail.util.MailSSLSocketFactory;
import jakarta.mail.*;

import java.io.*;
import java.util.Enumeration;
import java.util.Properties;

/**
 * @author liuyunfei
 */

@SuppressWarnings({"ForLoopReplaceableByForEach", "unused"})
public class FetchingEmail {

    public static void main(String[] args) {
        String protocol = "imaps";
        String host = "outlook.office365.com";
        int port = 993;
        String username = "yunfei0221@outlook.com";
        String password = "Fei19890116";

        fetch(protocol, host, port, username, password);
    }

    public static void fetch(String protocol, String host, int port, String username, String password) {
        try {
            Properties props = new Properties();

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            props.put("mail.imap.ssl.socketFactory", sf);

            props.setProperty("mail.imap.host", host);
            props.setProperty("mail.imap.port", String.valueOf(port));
            props.setProperty("mail.imapStore.protocol", protocol);
            props.setProperty("mail.imap.ssl.enable", "true");
            props.setProperty("mail.debug", "false");

            Session session = Session.getInstance(props);

            Store store = session.getStore("imaps");

            store.connect(host, port, username, password);



            // create the folder object and open it
            Folder emailFolder = store.getFolder("INBOX");
            emailFolder.open(Folder.READ_ONLY);


            // retrieve the messages from the folder in an array and print it

            int count = emailFolder.getMessageCount();
            Message[] messages = emailFolder.getMessages(1, count);


            System.err.println(messages.length);
            System.err.println(messages.length);
            System.err.println(messages.length);

            System.out.println("messages.length---" + messages.length);
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                writePart(message);
                message.writeTo(System.out);

                Enumeration<Header> allHeaders = message.getAllHeaders();
            }
            // close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * This method checks for content-type
     * based on which, it processes and
     * fetches the content of the message
     */
    public static void writePart(Part p) throws Exception {
        if (p instanceof Message)
            //Call methods writeEnvelope
            writeEnvelope((Message) p);

        System.out.println("----------------------------");
        System.out.println("CONTENT-TYPE: " + p.getContentType());

        //check if the content is plain text
        if (p.isMimeType("text/plain")) {
            System.out.println("This is plain text");
            System.out.println("---------------------------");
            System.out.println((String) p.getContent());
        }
        //check if the content has attachment
        else if (p.isMimeType("multipart/*")) {
            System.out.println("This is a Multipart");
            System.out.println("---------------------------");
            Multipart mp = (Multipart) p.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++)
                writePart(mp.getBodyPart(i));
        }
        //check if the content is a nested message
        else if (p.isMimeType("message/rfc822")) {
            System.out.println("This is a Nested Message");
            System.out.println("---------------------------");
            writePart((Part) p.getContent());
        }
        //check if the content is an inline image
        else if (p.isMimeType("image/jpeg")) {
            System.out.println("--------> image/jpeg");
            Object o = p.getContent();

            try (InputStream inputStream = (InputStream) o;
                 BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                 FileOutputStream fileOutputStream = new FileOutputStream("E:/tempFile/disc/image.jpg")) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = bufferedInputStream.read(buffer)) >= 0)
                    fileOutputStream.write(buffer, 0, len);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (p.getContentType().contains("image/")) {
            System.out.println("content type" + p.getContentType());
            File file = new File("image" + System.currentTimeMillis() + ".jpg");

            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
                 BASE64DecoderStream decoderStream = (BASE64DecoderStream) p.getContent()) {

                byte[] buffer = new byte[1024];
                int len;
                while ((len = decoderStream.read(buffer)) >= 0)
                    bufferedOutputStream.write(buffer, 0, len);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            Object o = p.getContent();
            if (o instanceof String) {
                System.out.println("This is a string");
                System.out.println((String) o);
            } else if (o instanceof InputStream) {
                System.out.println("This is just an input stream");
                System.out.println("---------------------------");
                InputStream inputStream = (InputStream) o;
                int c;
                while ((c = inputStream.read()) >= 0)
                    System.out.write(c);
            } else {
                System.out.println("This is an unknown type");
                System.out.println("---------------------------");
                System.out.println(o.toString());
            }
        }
    }

    /*
     * This method would print FROM,TO and SUBJECT of the message
     */
    public static void writeEnvelope(Message m) throws Exception {
        System.out.println("This is the message envelope");
        System.out.println("---------------------------");
        Address[] a;
        // FROM
        if ((a = m.getFrom()) != null) {
            for (Address address : a) System.out.println("FROM: " + address.toString());
        }
        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            for (Address address : a) System.out.println("TO: " + address.toString());
        }
        // SUBJECT
        if (m.getSubject() != null)
            System.out.println("SUBJECT: " + m.getSubject());
    }
}
