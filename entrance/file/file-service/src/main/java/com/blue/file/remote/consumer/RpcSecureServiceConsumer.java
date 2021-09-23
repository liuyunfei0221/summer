package com.blue.file.remote.consumer;

import com.blue.secure.api.inter.RpcSecureService;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.converterFutureToMono;
import static reactor.util.Loggers.getLogger;

/**
 * rpc接口异步话封装
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcSecureServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcSecureServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-secure"}, methods = {
            @Method(name = "assertAuth", async = true)
    })
    private RpcSecureService rpcSecureService;

    private final ExecutorService executorService;

    public RpcSecureServiceConsumer(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * 认证并鉴权
     *
     * @param assertAuth
     * @return
     */
    public Mono<AuthAsserted> assertAuth(AssertAuth assertAuth) {
        LOGGER.info("Mono<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return converterFutureToMono(rpcSecureService.assertAuth(assertAuth), executorService);
    }

}
