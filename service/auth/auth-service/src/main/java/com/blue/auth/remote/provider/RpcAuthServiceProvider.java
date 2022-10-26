package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcAuthService;
import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.auth.api.model.MemberPayload;
import com.blue.auth.service.inter.AuthService;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.Session;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

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
                @Method(name = "invalidateAuthByMemberId", async = true),
                @Method(name = "parsePayload", async = true),
                @Method(name = "parseAccess", async = true),
                @Method(name = "parseSession", async = true)
        })
public class RpcAuthServiceProvider implements RpcAuthService {

    private final AuthService authService;

    public RpcAuthServiceProvider(AuthService authService) {
        this.authService = authService;
    }

    /**
     * authentication and authorization
     *
     * @param accessAssert
     * @return
     */
    @Override
    public CompletableFuture<AccessAsserted> assertAccess(AccessAssert accessAssert) {
        return just(accessAssert).flatMap(authService::assertAccessMono).toFuture();
    }

    /**
     * invalid auth by accessInfo
     *
     * @param access
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidateAuthByAccess(Access access) {
        return just(access).flatMap(authService::invalidateAuthByAccess).toFuture();
    }

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidateAuthByJwt(String jwt) {
        return just(jwt).flatMap(authService::invalidateAuthByJwt).toFuture();
    }

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidateAuthByMemberId(Long memberId) {
        return just(memberId).flatMap(authService::invalidateAuthByMemberId).toFuture();
    }

    /**
     * jwt -> payload
     *
     * @param authentication
     * @return
     */
    @Override
    public CompletableFuture<MemberPayload> parsePayload(String authentication) {
        return just(authentication).flatMap(authService::parsePayload).toFuture();
    }

    /**
     * jwt -> access
     *
     * @param authentication
     * @return
     */
    @Override
    public CompletableFuture<Access> parseAccess(String authentication) {
        return just(authentication).flatMap(authService::parseAccess).toFuture();
    }

    /**
     * jwt -> session
     *
     * @param authentication
     * @return
     */
    @Override
    public CompletableFuture<Session> parseSession(String authentication) {
        return just(authentication).flatMap(authService::parseSession).toFuture();
    }

}
