package com.blue.media.handler.api;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * generate qr code api handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "Duplicates"})
@Component
public class QrCodeGenerateApiHandler {

    /**
     * upload
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> memberInfo(ServerRequest serverRequest) {

        return null;
    }

}
