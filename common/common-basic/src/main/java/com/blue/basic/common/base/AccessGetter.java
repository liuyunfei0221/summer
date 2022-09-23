package com.blue.basic.common.base;

import com.blue.basic.model.common.Access;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.access.AccessProcessor.jsonToAccess;
import static com.blue.basic.constant.common.BlueHeader.AUTHORIZATION;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;

/**
 * access getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class AccessGetter {

    /**
     * get access from request
     *
     * @param serverRequest
     * @return
     */
    public static Access getAccess(ServerRequest serverRequest) {
        return jsonToAccess(serverRequest.headers().firstHeader(AUTHORIZATION.name));
    }

    /**
     * get access/refresh from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Access getAccess(ServerHttpRequest serverHttpRequest) {
        return jsonToAccess(serverHttpRequest.getHeaders().getFirst(AUTHORIZATION.name));
    }

    /**
     * get access/refresh mono from serverRequest
     *
     * @param serverRequest
     * @return
     */
    public static Mono<Access> getAccessReact(ServerRequest serverRequest) {
        return just(getAccess(serverRequest));
    }

    /**
     * get access/refresh mono from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<Access> getAccessReact(ServerHttpRequest serverHttpRequest) {
        return just(getAccess(serverHttpRequest));
    }

    /**
     * get access/refresh from request
     *
     * @param serverRequest
     * @return
     */
    public static String getAuthorization(ServerRequest serverRequest) {
        return ofNullable(serverRequest.headers().firstHeader(AUTHORIZATION.name)).orElse(EMPTY_VALUE.value);
    }

    /**
     * get access/refresh from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static String getAuthorization(ServerHttpRequest serverHttpRequest) {
        return ofNullable(serverHttpRequest.getHeaders().getFirst(AUTHORIZATION.name)).orElse(EMPTY_VALUE.value);
    }

    /**
     * get access/refresh mono from serverRequest
     *
     * @param serverRequest
     * @return
     */
    public static Mono<String> getAuthorizationReact(ServerRequest serverRequest) {
        return just(getAuthorization(serverRequest));
    }

    /**
     * get access/refresh mono from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<String> getAuthorizationReact(ServerHttpRequest serverHttpRequest) {
        return just(getAuthorization(serverHttpRequest));
    }

}
