package com.blue.secure.remote.provider;

import com.blue.base.model.base.Access;
import com.blue.secure.api.inter.RpcSecureService;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import com.blue.secure.api.model.Authority;
import com.blue.secure.service.inter.SecureService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;

import static reactor.util.Loggers.getLogger;


/**
 * 认证鉴权RPC实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcSecureService.class, version = "1.0", methods = {
        @Method(name = "assertAuth", async = true),
        @Method(name = "invalidAuthByAccess", async = true),
        @Method(name = "invalidAuthByJwt", async = true),
        @Method(name = "getAuthorityByAccess", async = true),
        @Method(name = "getAuthorityByMemberId", async = true)
})
public class RpcSecureServiceProvider implements RpcSecureService {

    private static final Logger LOGGER = getLogger(RpcSecureServiceProvider.class);

    private final SecureService secureService;

    public RpcSecureServiceProvider(SecureService secureService) {
        this.secureService = secureService;
    }

    /**
     * 认证并鉴权
     *
     * @param assertAuth
     * @return
     */
    @Override
    public CompletableFuture<AuthAsserted> assertAuth(AssertAuth assertAuth) {
        LOGGER.info("CompletableFuture<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return secureService.assertAuth(assertAuth).toFuture();
    }

    /**
     * 根据accessInfo清除认证信息
     *
     * @param access
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidAuthByAccess(Access access) {
        LOGGER.info("CompletableFuture<Boolean> invalidAuthByAccess(Access access), access = {}", access);
        return secureService.invalidAuthByAccess(access).toFuture();
    }

    /**
     * 根据jwt清除认证信息
     *
     * @param jwt
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidAuthByJwt(String jwt) {
        LOGGER.info("CompletableFuture<Boolean> invalidAuthByJwt(String jwt), jwt = {}", jwt);
        return secureService.invalidAuthByJwt(jwt).toFuture();
    }

    /**
     * 根据access查询成员的角色及权限信息
     *
     * @param access
     * @return
     */
    @Override
    public CompletableFuture<Authority> getAuthorityByAccess(Access access) {
        LOGGER.info("CompletableFuture<Authority> getAuthorityByAccess(Access access), access = {}", access);
        return secureService.getAuthorityByAccess(access).toFuture();
    }

    /**
     * 根据memberId查询成员的角色及权限信息
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<Authority> getAuthorityByMemberId(Long memberId) {
        LOGGER.info("CompletableFuture<Authority> getAuthorityByMemberId(Long memberId), memberId = {}", memberId);
        return secureService.getAuthorityByMemberId(memberId).toFuture();
    }

}
