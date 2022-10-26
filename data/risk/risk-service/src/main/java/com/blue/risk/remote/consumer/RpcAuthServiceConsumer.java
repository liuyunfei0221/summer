package com.blue.risk.remote.consumer;

import com.blue.auth.api.inter.RpcAuthService;
import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.Session;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc auth reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcAuthServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "assertAccess", async = true),
                    @Method(name = "invalidateAuthByAccess", async = true),
                    @Method(name = "invalidateAuthByJwt", async = true),
                    @Method(name = "invalidateAuthByMemberId", async = true),
                    @Method(name = "parseAccess", async = true),
                    @Method(name = "parseSession", async = true)
            })
    private RpcAuthService rpcAuthService;

    /**
     * authentication and authorization
     *
     * @param accessAssert
     * @return
     */
    public Mono<AccessAsserted> assertAccess(AccessAssert accessAssert) {
        return fromFuture(rpcAuthService.assertAccess(accessAssert));
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    public Mono<Boolean> invalidateAuthByAccess(Access access) {
        return fromFuture(rpcAuthService.invalidateAuthByAccess(access));
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    public Mono<Boolean> invalidateAuthByJwt(String jwt) {
        return fromFuture(rpcAuthService.invalidateAuthByJwt(jwt));
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    public Mono<Boolean> invalidateAuthByMemberId(Long memberId) {
        return fromFuture(rpcAuthService.invalidateAuthByMemberId(memberId));
    }

    /**
     * jwt -> access
     *
     * @param authentication
     * @return
     */
    public Mono<Access> parseAccess(String authentication) {
        return fromFuture(rpcAuthService.parseAccess(authentication));
    }

    /**
     * jwt -> session
     *
     * @param authentication
     * @return
     */
    public Mono<Session> parseSession(String authentication) {
        return fromFuture(rpcAuthService.parseSession(authentication));
    }

}
