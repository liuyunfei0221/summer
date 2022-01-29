package com.blue.secure.remote.provider;

import com.blue.secure.api.inter.RpcResourceService;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.service.inter.ResourceService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.secure.converter.SecureModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc role provider
 *
 * @author DarkBlue
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
                .publishOn(scheduler)
                .flatMap(v -> resourceService.selectResource())
                .flatMap(resources -> just(resources.stream().map(RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList())))
                .toFuture();
    }

}
