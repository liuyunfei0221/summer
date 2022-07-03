package com.blue.media.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * qr code generate service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface QrCodeGenerateService {

    /**
     * generate qr code
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> generate(ServerRequest serverRequest);

}