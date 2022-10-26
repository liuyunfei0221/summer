package com.blue.analyze.remote.consumer;

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
