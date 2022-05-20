package com.blue.risk.remote.consumer;

import com.blue.auth.api.inter.RpcResourceService;
import com.blue.auth.api.model.ResourceInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc resource reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public class RpcResourceServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcResourceServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "selectResourceInfo", async = true, retries = 2)
            })
    private RpcResourceService rpcResourceService;

    private final Scheduler scheduler;

    public RpcResourceServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * select all resource info
     *
     * @return
     */
    public Mono<List<ResourceInfo>> selectResourceInfo() {
        LOGGER.info("Mono<List<ResourceInfo>> selectResourceInfo()");
        return fromFuture(rpcResourceService.selectResourceInfo()).subscribeOn(scheduler);
    }

}
