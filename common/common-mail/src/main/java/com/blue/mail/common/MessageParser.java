package com.blue.mail.common;

import jakarta.mail.BodyPart;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMultipart;

/**
 * message parser
 *
 * @author liuyunfei
 */
@SuppressWarnings("AlibabaUndefineMagicConstant")
public class MessageParser {

    public static void parseMessage(Message message) {
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
