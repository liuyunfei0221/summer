package com.blue.mail.component;

import com.blue.mail.api.conf.MailSenderConf;
import com.blue.mail.core.BatchSender;
import jakarta.mail.Message;
import jakarta.mail.internet.MimeMessage;

import java.util.concurrent.CompletableFuture;


/**
 * mail sender
 *
 * @author liuyunfei
 */
public final class MailSender {

    private final BatchSender batchSender;

    public MailSender(MailSenderConf conf) {
        this.batchSender = new BatchSender(conf);
    }

    public MimeMessage initMessage() {
        return batchSender.initMessage();
    }

    public CompletableFuture<Void> sendMessage(Message message) {
        return batchSender.sendMessage(message);
    }

}
