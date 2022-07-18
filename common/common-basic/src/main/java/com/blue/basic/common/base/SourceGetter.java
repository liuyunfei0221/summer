package com.blue.basic.common.base;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.basic.constant.common.BlueHeader.SOURCE;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.basic.constant.member.SourceType.APP;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;

/**
 * source getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class SourceGetter {

    private static final String DEFAULT_SOURCE = APP.identity;

    /**
     * get source from request
     *
     * @param serverRequest
     * @return
     */
    public static String getSource(ServerRequest serverRequest) {
        return ofNullable(serverRequest.headers().firstHeader(SOURCE.name)).orElse(EMPTY_DATA.value);
    }

    /**
     * get source from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static String getSource(ServerHttpRequest serverHttpRequest) {
        return ofNullable(serverHttpRequest.getHeaders().getFirst(SOURCE.name)).orElse(EMPTY_DATA.value);
    }

    /**
     * get source mono from serverRequest
     *
     * @param serverRequest
     * @return
     */
    public static Mono<String> getSourceReact(ServerRequest serverRequest) {
        return just(getSource(serverRequest));
    }

    /**
     * get source from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<String> getSourceReact(ServerHttpRequest serverHttpRequest) {
        return just(getSource(serverHttpRequest));
    }

}
