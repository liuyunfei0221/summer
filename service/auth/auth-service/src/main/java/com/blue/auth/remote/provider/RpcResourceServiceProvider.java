package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcResourceService;
import com.blue.auth.api.model.ResourceInfo;
import com.blue.auth.converter.AuthModelConverters;
import com.blue.auth.service.inter.ResourceService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;

/**
 * rpc resource provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcResourceService.class,
        version = "1.0",
        methods = {
                @Method(name = "selectResourceInfo", async = true)
        })
public class RpcResourceServiceProvider implements RpcResourceService {

    private final ResourceService resourceService;

    private final Scheduler scheduler;

    public RpcResourceServiceProvider(ResourceService resourceService, Scheduler scheduler) {
        this.resourceService = resourceService;
        this.scheduler = scheduler;
    }

    /**
     * select all resource info
     *
     * @return
     */
    @Override
    public CompletableFuture<List<ResourceInfo>> selectResourceInfo() {
        return just(true)
                .subscribeOn(scheduler)
                .flatMap(v -> resourceService.selectResource())
                .flatMap(resources -> just(resources.stream().map(AuthModelConverters.RESOURCE_2_RESOURCE_INFO_CONVERTER).collect(toList())))
                .toFuture();
    }

}
