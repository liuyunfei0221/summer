package com.blue.lake.remote.consumer;

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
            @Method(name = "invalidAuthByAccess", async = true),
            @Method(name = "invalidAuthByJwt", async = true),
            @Method(name = "invalidAuthByMemberId", async = true)
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
    public Mono<Boolean> invalidAuthByAccess(Access access) {
        LOGGER.info("Mono<Boolean> invalidAuthByAccess(Access access), access = {}", access);
        return fromFuture(rpcAuthService.invalidAuthByAccess(access));
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    public Mono<Boolean> invalidAuthByJwt(String jwt) {
        LOGGER.info("Mono<Boolean> invalidAuthByJwt(String jwt), jwt = {}", jwt);
        return fromFuture(rpcAuthService.invalidAuthByJwt(jwt));
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    public Mono<Boolean> invalidAuthByMemberId(Long memberId) {
        LOGGER.info("Mono<Boolean> invalidAuthByMemberId(Long memberId), memberId = {}", memberId);
        return fromFuture(rpcAuthService.invalidAuthByMemberId(memberId));
    }

}
