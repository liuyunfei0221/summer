package com.blue.gateway.remote.consumer;

import com.blue.auth.api.inter.RpcAuthService;
import com.blue.auth.api.model.AssertAuth;
import com.blue.auth.api.model.AuthAsserted;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc auth reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public class RpcAuthServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcAuthServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "assertAuth", async = true, retries = 2)
            })
    private RpcAuthService rpcAuthService;

    private final Scheduler scheduler;

    public RpcAuthServiceConsumer(Scheduler scheduler) {
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
        return fromFuture(rpcAuthService.assertAuth(assertAuth)).publishOn(scheduler);
    }

}
