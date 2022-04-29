package com.blue.base.common.reactive;

import com.blue.base.model.base.Access;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.access.AccessProcessor.jsonToAccess;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;

/**
 * access getter for reactive
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class AccessGetterForReactive {

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
     * get access from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Access getAccess(ServerHttpRequest serverHttpRequest) {
        return jsonToAccess(serverHttpRequest.getHeaders().getFirst(AUTHORIZATION.name));
    }

    /**
     * get access mono from serverRequest
     *
     * @param serverRequest
     * @return
     */
    public static Mono<Access> getAccessReact(ServerRequest serverRequest) {
        return just(jsonToAccess(serverRequest.headers().firstHeader(AUTHORIZATION.name)));
    }

    /**
     * get access mono from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<Access> getAccessReact(ServerHttpRequest serverHttpRequest) {
        return just(jsonToAccess(serverHttpRequest.getHeaders().getFirst(AUTHORIZATION.name)));
    }

    /**
     * get refresh token from request
     *
     * @param serverRequest
     * @return
     */
    public static String getAuthorization(ServerRequest serverRequest) {
        return ofNullable(serverRequest.headers().firstHeader(AUTHORIZATION.name)).orElse("");
    }

    /**
     * get refresh token from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static String getAuthorization(ServerHttpRequest serverHttpRequest) {
        return ofNullable(serverHttpRequest.getHeaders().getFirst(AUTHORIZATION.name)).orElse("");
    }

    /**
     * get refresh token mono from serverRequest
     *
     * @param serverRequest
     * @return
     */
    public static Mono<String> getAuthorizationReact(ServerRequest serverRequest) {
        return just(ofNullable(serverRequest.headers().firstHeader(AUTHORIZATION.name)).orElse(""));
    }

    /**
     * get refresh token mono from serverHttpRequest
     *
     * @param serverHttpRequest
     * @return
     */
    public static Mono<String> getAuthorizationReact(ServerHttpRequest serverHttpRequest) {
        return just(ofNullable(serverHttpRequest.getHeaders().getFirst(AUTHORIZATION.name)).orElse(""));
    }

}
