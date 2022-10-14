package com.blue.verify.component.sender.impl;

import com.blue.basic.constant.verify.VerifyType;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.api.model.VerifyTemplateInfo;
import com.blue.verify.component.sender.inter.VerifyMessageSender;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static com.blue.basic.constant.verify.VerifyType.SMS;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * sms verify message sender impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public class SmsVerifyMessageSender implements VerifyMessageSender {

    private static final Logger LOGGER = getLogger(SmsVerifyMessageSender.class);

    @Override
    public Mono<Boolean> send(VerifyMessage verifyMessage, VerifyTemplateInfo verifyTemplateInfo) {
        LOGGER.info("Mono<Boolean> send(), verifyMessage = {}, verifyTemplateInfo = {}", verifyMessage, verifyTemplateInfo);

        LOGGER.warn("verifyMessage = {}", verifyMessage);

        return just(true);
    }

    @Override
    public VerifyType targetType() {
        return SMS;
    }

}
