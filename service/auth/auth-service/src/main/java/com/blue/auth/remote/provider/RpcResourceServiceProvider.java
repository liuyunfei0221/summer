package com.blue.auth.remote.provider;

import com.blue.auth.converter.AuthModelConverters;
import com.blue.auth.service.inter.ResourceService;
import com.blue.auth.api.inter.RpcResourceService;
import com.blue.auth.api.model.ResourceInfo;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc role provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcResourceService.class, version = "1.0", methods = {
        @Method(name = "selectResourceInfo", async = true)
})
public class RpcResourceServiceProvider implements RpcResourceService {

    private static final Logger LOGGER = getLogger(RpcResourceServiceProvider.class);

    private final ResourceService resourceService;

    private final Scheduler scheduler;

    public RpcResourceServiceProvider(ResourceService resourceService, Scheduler scheduler) {
        this.resourceService = resourceService;
        this.scheduler = scheduler;
    }

    /**
     * select all resource infos
     *
     * @return
     */
    @Override
    public CompletableFuture<List<ResourceInfo>> selectResourceInfo() {
        LOGGER.info("CompletableFuture<RoleInfo> selectRoleInfo()");
        return just(true)
                .subscribeOn(scheduler)
                .flatMap(v -> resourceService.selectResource())
                .flatMap(resources -> just(resources.stream().map(AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList())))
                .toFuture();
    }

}
