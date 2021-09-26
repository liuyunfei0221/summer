package com.blue.gateway.remote.consumer;

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
 * rpc接口异步化封装
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcSecureServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcSecureServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-secure"}, methods = {
            @Method(name = "assertAuth", async = true)
    })
    private RpcSecureService rpcSecureService;

    /**
     * 认证并鉴权
     *
     * @param assertAuth
     * @return
     */
    public Mono<AuthAsserted> assertAuth(AssertAuth assertAuth) {
        LOGGER.info("Mono<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return fromFuture(rpcSecureService.assertAuth(assertAuth));
    }

}
