package com.blue.message.remote.consumer;

import com.blue.auth.api.inter.RpcAuthService;
import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

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
                    @Method(name = "assertAccess", async = true, retries = 2)
            })
    private RpcAuthService rpcAuthService;

    private final Scheduler scheduler;

    public RpcAuthServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * authentication and authorization
     *
     * @param accessAssert
     * @return
     */
    public Mono<AccessAsserted> assertAccess(AccessAssert accessAssert) {
        return fromFuture(rpcAuthService.assertAccess(accessAssert)).publishOn(scheduler);
    }

}
