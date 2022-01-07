package com.blue.lake.remote.consumer;

import com.blue.base.model.base.Access;
import com.blue.secure.api.inter.RpcSecureService;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc secure reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcSecureServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcSecureServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-secure"}, methods = {
            @Method(name = "assertAuth", async = true),
            @Method(name = "invalidAuthByAccess", async = true),
            @Method(name = "invalidAuthByJwt", async = true),
            @Method(name = "invalidAuthByMemberId", async = true)
    })
    private RpcSecureService rpcSecureService;

    /**
     * authentication and authorization
     *
     * @param assertAuth
     * @return
     */
    public Mono<AuthAsserted> assertAuth(AssertAuth assertAuth) {
        LOGGER.info("Mono<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return fromFuture(rpcSecureService.assertAuth(assertAuth));
    }

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    public Mono<Boolean> invalidAuthByAccess(Access access) {
        LOGGER.info("Mono<Boolean> invalidAuthByAccess(Access access), access = {}", access);
        return fromFuture(rpcSecureService.invalidAuthByAccess(access));
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    public Mono<Boolean> invalidAuthByJwt(String jwt) {
        LOGGER.info("Mono<Boolean> invalidAuthByJwt(String jwt), jwt = {}", jwt);
        return fromFuture(rpcSecureService.invalidAuthByJwt(jwt));
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    public Mono<Boolean> invalidAuthByMemberId(Long memberId) {
        LOGGER.info("Mono<Boolean> invalidAuthByMemberId(Long memberId), memberId = {}", memberId);
        return fromFuture(rpcSecureService.invalidAuthByMemberId(memberId));
    }

}
