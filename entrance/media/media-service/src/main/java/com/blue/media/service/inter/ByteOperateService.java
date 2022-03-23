package com.blue.media.service.inter;

import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * byte operate service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface ByteOperateService {

    /**
     * upload
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> upload(ServerRequest serverRequest);

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    Mono<ServerResponse> download(ServerRequest serverRequest);

}
