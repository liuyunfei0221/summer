package com.blue.risk.remote.consumer;

import com.blue.base.model.base.Access;
import com.blue.auth.api.inter.RpcAuthService;
import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc auth reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcAuthServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcAuthServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-auth"}, methods = {
            @Method(name = "assertAccess", async = true),
            @Method(name = "invalidateAuthByAccess", async = true),
            @Method(name = "invalidateAuthByJwt", async = true),
            @Method(name = "invalidateAuthByMemberId", async = true)
    })
    private RpcAuthService rpcAuthService;

    /**
     * authentication and authorization
     *
     * @param accessAssert
     * @return
     */
    public Mono<AccessAsserted> assertAccess(AccessAssert accessAssert) {
        LOGGER.info("Mono<AuthAsserted> assertAccess(AssertAuth assertAuth), assertAuth = {}", accessAssert);
        return fromFuture(rpcAuthService.assertAccess(accessAssert));
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    public Mono<Boolean> invalidateAuthByAccess(Access access) {
        LOGGER.info("Mono<Boolean> invalidateAuthByAccess(Access access), access = {}", access);
        return fromFuture(rpcAuthService.invalidateAuthByAccess(access));
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    public Mono<Boolean> invalidateAuthByJwt(String jwt) {
        LOGGER.info("Mono<Boolean> invalidateAuthByJwt(String jwt), jwt = {}", jwt);
        return fromFuture(rpcAuthService.invalidateAuthByJwt(jwt));
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    public Mono<Boolean> invalidateAuthByMemberId(Long memberId) {
        LOGGER.info("Mono<Boolean> invalidateAuthByMemberId(Long memberId), memberId = {}", memberId);
        return fromFuture(rpcAuthService.invalidateAuthByMemberId(memberId));
    }

}
