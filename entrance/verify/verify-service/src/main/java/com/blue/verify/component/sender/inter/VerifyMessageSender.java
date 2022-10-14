package com.blue.verify.component.sender.inter;

import com.blue.basic.constant.verify.VerifyType;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.api.model.VerifyTemplateInfo;
import reactor.core.publisher.Mono;

/**
 * verify message sender interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface VerifyMessageSender {

    /**
     * send message
     *
     * @param verifyMessage
     * @param verifyTemplateInfo
     * @return
     */
    Mono<Boolean> send(VerifyMessage verifyMessage, VerifyTemplateInfo verifyTemplateInfo);

    /**
     * target verify type to send
     *
     * @return
     */
    VerifyType targetType();

}
