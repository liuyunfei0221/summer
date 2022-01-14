package com.blue.gateway.remote.consumer;

import com.blue.secure.api.inter.RpcSecureService;
import com.blue.secure.api.model.AssertAuth;
import com.blue.secure.api.model.AuthAsserted;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc secure reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcSecureServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcSecureServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-secure"},
            methods = {
                    @Method(name = "assertAuth", async = true, retries = 2)
            })
    private RpcSecureService rpcSecureService;

    private final Scheduler scheduler;

    public RpcSecureServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * authentication and authorization
     *
     * @param assertAuth
     * @return
     */
    public Mono<AuthAsserted> assertAuth(AssertAuth assertAuth) {
        LOGGER.info("Mono<AuthAsserted> assertAuth(AssertAuth assertAuth), assertAuth = {}", assertAuth);
        return fromFuture(rpcSecureService.assertAuth(assertAuth)).publishOn(scheduler);
    }

}
