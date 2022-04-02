package com.blue.base.common.reactive;

import com.blue.base.model.base.Access;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.auth.AuthProcessor.jsonToAccess;
import static com.blue.base.constant.base.BlueHeader.AUTHORIZATION;
import static com.blue.base.constant.base.BlueHeader.REFRESH;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;

/**
 * access getter for reactive
 *
 * @author DarkBlue
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
     * get access mono from access
     *
     * @param serverRequest
     * @return
     */
    public static Mono<Access> getAccessReact(ServerRequest serverRequest) {
        return just(jsonToAccess(serverRequest.headers().firstHeader(AUTHORIZATION.name)));
    }

    /**
     * get refresh token from request
     *
     * @param serverRequest
     * @return
     */
    public static String getRefresh(ServerRequest serverRequest) {
        return ofNullable(serverRequest.headers().firstHeader(REFRESH.name)).orElse("");
    }

    /**
     * get refresh token mono from access
     *
     * @param serverRequest
     * @return
     */
    public static Mono<String> getRefreshReact(ServerRequest serverRequest) {
        return just(ofNullable(serverRequest.headers().firstHeader(REFRESH.name)).orElse(""));
    }

}
