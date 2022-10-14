package com.blue.verify.component.sender.impl;

import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.exps.BlueException;
import com.blue.mail.component.MailSender;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.api.model.VerifyTemplateInfo;
import com.blue.verify.component.sender.inter.VerifyMessageSender;
import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.BiFunction;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.assertVerifyBusinessType;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.basic.constant.verify.VerifyType.MAIL;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * mail verify message sender impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class MailVerifyMessageSender implements VerifyMessageSender {

    private static final Logger LOGGER = getLogger(MailVerifyMessageSender.class);

    private MailSender mailSender;

    public MailVerifyMessageSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    private final BiFunction<VerifyMessage, VerifyTemplateInfo, Mono<Boolean>> SENDER = (verifyMessage, verifyTemplateInfo) -> {
        if (isNull(verifyTemplateInfo))
            throw new BlueException(INVALID_PARAM);

        String businessType = verifyMessage.getBusinessType();
        assertVerifyBusinessType(businessType, false);

        String destination = verifyMessage.getDestination();
        //TODO check email

        
        String title = verifyTemplateInfo.getTitle();
        String content = String.format(verifyTemplateInfo.getContent(), verifyMessage.getVerify());

        try {
            Message message = mailSender.initMessage();

            message.setRecipients(Message.RecipientType.TO, new InternetAddress[]{new InternetAddress(destination)});
            message.setSubject(title);
            message.setText(content);

            return fromFuture(mailSender.sendMessage(message)
                    .thenAcceptAsync(v ->
                            LOGGER.info("mail send success, verifyMessage = {}", verifyMessage))
                    .exceptionally(t -> {
                        LOGGER.error("mail send failed, verifyMessage = {}, t = {}", verifyMessage, t);
                        throw new RuntimeException(t.getMessage());
                    }))
                    .then(just(true));
        } catch (Exception e) {
            LOGGER.error("mail send failed, verifyMessage = {}, e = {}", verifyMessage, e);
            return just(false);
        }
    };

    @Override
    public Mono<Boolean> send(VerifyMessage verifyMessage, VerifyTemplateInfo verifyTemplateInfo) {
        LOGGER.info("Mono<Boolean> send(), verifyMessage = {}, verifyTemplateInfo = {}", verifyMessage, verifyTemplateInfo);

        LOGGER.warn("verifyMessage = {}, verifyTemplateInfo = {}", verifyMessage, verifyTemplateInfo);

        return SENDER.apply(verifyMessage, verifyTemplateInfo);
    }

    @Override
    public VerifyType targetType() {
        return MAIL;
    }

}
