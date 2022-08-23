package com.blue.media.component.message.inter;

import com.blue.basic.constant.media.MessageType;
import com.blue.media.api.model.Message;
import reactor.core.publisher.Mono;

/**
 * message sender interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MessageSender {

    /**
     * send message
     *
     * @param message
     * @return
     */
    Mono<Boolean> send(Message message);

    /**
     * target message type to process
     *
     * @return
     */
    MessageType targetType();

}
