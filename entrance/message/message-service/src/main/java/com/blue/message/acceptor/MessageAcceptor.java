package com.blue.message.acceptor;

import com.blue.message.handler.api.MessageHandler;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.RSocket;
import io.rsocket.SocketAcceptor;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * message acceptor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Component
public class MessageAcceptor implements SocketAcceptor {

    private static final Logger LOGGER = getLogger(MessageAcceptor.class);

    @Override
    @NonNull
    public Mono<RSocket> accept(@NonNull ConnectionSetupPayload connectionSetupPayload, @NonNull RSocket rSocket) {
        LOGGER.warn("connectionSetupPayload = {}", connectionSetupPayload);

        String data = connectionSetupPayload.getDataUtf8();
        LOGGER.warn("data = {}", data);

        return just(new MessageHandler());
    }

}
