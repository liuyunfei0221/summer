package com.blue.verify.service.impl;

import com.blue.mail.component.MailSender;
import com.blue.verify.service.inter.MailService;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.base.constant.media.MailHeader.LIST_UNSUBSCRIBE;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * mail service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class MailServiceImpl implements MailService {

    private static final Logger LOGGER = getLogger(MailServiceImpl.class);

    private final MailSender mailSender;

    public MailServiceImpl(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * send email verify
     *
     * @param email
     * @param text
     * @return
     */
    @Override
    public Mono<Boolean> send(String email, String text) {
        LOGGER.warn("send email verify, email = {}, text = {}", email, text);

        try {
            Message message = mailSender.initMessage();
            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(email)});
            message.addHeader(LIST_UNSUBSCRIBE.name, "https://www.baidu.com/");
            message.setSubject("email verify code");
            message.setText("verify code is " + text);

            mailSender.sendMessage(message).thenAcceptAsync(v ->
                            System.err.println("SEND SUCCESS!!!")
                    )
                    .exceptionally(t -> {
                        LOGGER.error("SEND FAILED!!!");
                        LOGGER.error("t = {}", t);
                        return null;
                    });

            return just(true);
        } catch (Exception e) {
            LOGGER.error("Mono<Boolean> send(String email, String text) failed, email = {}, text = {}, e = {}", email, text, e);
            return just(false);
        }
    }

}
