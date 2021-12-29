package com.blue.secure.remote.provider;

import com.blue.base.model.base.Access;
import com.blue.secure.api.inter.RpcSecureService;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import com.blue.secure.service.inter.SecureService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;

import static reactor.util.Loggers.getLogger;


/**
 * rpc secure provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcSecureService.class, version = "1.0", methods = {
        @Method(name = "assertAuth", async = true),
        @Method(name = "invalidAuthByAccess", async = true),
        @Method(name = "invalidAuthByJwt", async = true),
        @Method(name = "invalidAuthByMemberId", async = true)
})
public class RpcSecureServiceProvider implements RpcSecureService {

    private static final Logger LOGGER = getLogger(RpcSecureServiceProvider.class);

    private final SecureService secureService;

    public RpcSecureServiceProvider(SecureService secureService) {
        this.secureService = secureService;
    }

    /**
     * authentication and authorization
     *
     * @param assertAuth
     * @return
     */
    @Override
    public CompletableFuture<AuthAsserted> assertAuth(AssertAuth assertAuth) {
        LOGGER.info("CompletableFuture<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return secureService.assertAuthMono(assertAuth).toFuture();
    }

    /**
     * invalid auth by accessInfo
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
     * invalid auth by jwt
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
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    @Override
    public CompletableFuture<Boolean> invalidAuthByMemberId(Long memberId) {
        LOGGER.info("CompletableFuture<Boolean> invalidAuthByMemberId(Long memberId), memberId = {}", memberId);
        return secureService.invalidAuthByMemberId(memberId).toFuture();
    }

}
