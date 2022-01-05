package com.blue.media.common.request.body;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * request body getter
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface RequestBodyGetter {

    /**
     * handle type
     *
     * @return
     */
    String getContentType();

    /**
     * handle
     *
     * @param exchange
     * @return
     */
    Mono<String> processor(ServerWebExchange exchange);

}
