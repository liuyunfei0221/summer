package com.blue.base.common.reactive;

import com.blue.base.constant.base.BlueHeader;
import com.blue.base.model.base.Access;
import org.springframework.web.reactive.function.server.ServerRequest;
import reactor.core.publisher.Mono;

import static com.blue.base.common.auth.AuthProcessor.jsonToAccess;
import static reactor.core.publisher.Mono.just;

/**
 * 用于reactive项目的用户获取工具
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class AccessGetterForReactive {

    private static final String AUTHORIZATION = BlueHeader.AUTHORIZATION.name;

    /**
     * 由request中获取用户信息
     *
     * @param serverRequest
     * @return
     */
    public static Access getAccess(ServerRequest serverRequest) {
        return jsonToAccess(serverRequest.headers().firstHeader(AUTHORIZATION));
    }

    /**
     * 由request中获取用户信息
     *
     * @param serverRequest
     * @return
     */
    public static Mono<Access> getAccessReact(ServerRequest serverRequest) {
        return just(jsonToAccess(serverRequest.headers().firstHeader(AUTHORIZATION)));
    }

}
