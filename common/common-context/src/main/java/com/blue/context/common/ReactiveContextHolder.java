package com.blue.context.common;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.blue.context.constant.BlueContextKey.SERVER_WEB_EXCHANGE;
import static reactor.core.publisher.Mono.deferContextual;
import static reactor.core.publisher.Mono.just;

/**
 * context holder for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavadocDeclaration", "unused"})
public final class ReactiveContextHolder {

    /**
     * get current exchange
     *
     * @return
     */
    public static Mono<ServerWebExchange> getServerWebExchange() {
        return deferContextual(contextView -> just(contextView.get(SERVER_WEB_EXCHANGE)));
    }

    /**
     * get current request
     *
     * @return
     */
    public static Mono<ServerHttpRequest> getServerHttpRequest() {
        return getServerWebExchange().flatMap(serverWebExchange -> just(serverWebExchange.getRequest()));
    }

    /**
     * get current response
     *
     * @return
     */
    public static Mono<ServerHttpResponse> getServerHttpResponse() {
        return getServerWebExchange().flatMap(serverWebExchange -> just(serverWebExchange.getResponse()));
    }

}
