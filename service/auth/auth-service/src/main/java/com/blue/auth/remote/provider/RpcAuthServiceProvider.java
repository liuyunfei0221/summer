package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcAuthService;
import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.auth.service.inter.AuthService;
import com.blue.basic.model.common.Access;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;

import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;


/**
 * rpc auth provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcAuthService.class,
        version = "1.0",
        methods = {
                @Method(name = "assertAccess", async = true),
                @Method(name = "invalidateAuthByAccess", async = true),
                @Method(name = "invalidateAuthByJwt", async = true),
                @Method(name = "invalidateAuthByMemberId", async = true)
        })
public class RpcAuthServiceProvider implements RpcAuthService {

    private final AuthService authService;

    private final Scheduler scheduler;

    public RpcAuthServiceProvider(AuthService authService, Scheduler scheduler) {
        this.authService = authService;
        this.scheduler = scheduler;
    }

    /**
     * authentication and authorization
     *
     * @param accessAssert
     * @return
     */
    @Override
    public CompletableFuture<AccessAsserted> assertAccess(AccessAssert accessAssert) {
        return just(accessAssert).publishOn(scheduler).flatMap(authService::assertAccessMono).toFuture();
    }

    /**
     * invalid auth by accessInfo
     *
     * @param access
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidateAuthByAccess(Access access) {
        return just(access).publishOn(scheduler).flatMap(authService::invalidateAuthByAccess).toFuture();
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidateAuthByJwt(String jwt) {
        return just(jwt).publishOn(scheduler).flatMap(authService::invalidateAuthByJwt).toFuture();
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidateAuthByMemberId(Long memberId) {
        return just(memberId).publishOn(scheduler).flatMap(authService::invalidateAuthByMemberId).toFuture();
    }

}
