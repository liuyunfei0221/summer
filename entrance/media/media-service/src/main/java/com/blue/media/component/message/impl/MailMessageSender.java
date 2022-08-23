package com.blue.media.component.message.impl;

import com.blue.basic.constant.media.MessageType;
import com.blue.media.api.model.Message;
import com.blue.media.component.message.inter.MessageSender;
import reactor.core.publisher.Mono;

/**
 * mail message sender
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavadocDeclaration")
public class MailMessageSender implements MessageSender {

    /**
     * send message
     *
     * @param message
     * @return
     */
    @Override
    public Mono<Boolean> send(Message message) {
        return null;
    }

    /**
     * target message type to process
     *
     * @return
     */
    @Override
    public MessageType targetType() {
        return null;
    }

}
