package com.blue.mail.common;

import com.blue.mail.api.conf.MailReaderConfParams;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.internet.MimeMultipart;

public class ReaderTest {

    public static void main(String[] args) {

        MailReader mailReader = new MailReader(new MailReaderConfParams());

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
