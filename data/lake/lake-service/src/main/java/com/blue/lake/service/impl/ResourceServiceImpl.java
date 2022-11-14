package com.blue.lake.service.impl;

import com.blue.auth.api.model.ResourceInfo;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.lake.config.deploy.CaffeineDeploy;
import com.blue.lake.remote.consumer.RpcResourceServiceConsumer;
import com.blue.lake.service.inter.ResourceService;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Supplier;

import static com.blue.basic.common.base.CommonFunctions.INIT_RES_KEY_GENERATOR;
import static com.blue.basic.constant.common.SpecialIntegerElement.ONE;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.*;

/**
 * resource service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Service
public class ResourceServiceImpl implements ResourceService {

    private static final Logger LOGGER = Loggers.getLogger(ResourceServiceImpl.class);

    private RpcResourceServiceConsumer rpcResourceServiceConsumer;

    public ResourceServiceImpl(RpcResourceServiceConsumer rpcResourceServiceConsumer, ExecutorService executorService, CaffeineDeploy caffeineDeploy) {
        this.rpcResourceServiceConsumer = rpcResourceServiceConsumer;

        resourceInfoCache = generateCacheAsyncCache(new CaffeineConfParams(
                ONE.value, Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService));

        identityWithResourceInfoCache = generateCacheAsyncCache(new CaffeineConfParams(
                ONE.value, Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, executorService));
    }

    private static final String ALL_RESOURCES_CACHE_KEY = "ALL_RESOURCES";

    private AsyncCache<String, List<ResourceInfo>> resourceInfoCache;

    private AsyncCache<String, Map<String, ResourceInfo>> identityWithResourceInfoCache;

    private final BiFunction<String, Executor, CompletableFuture<List<ResourceInfo>>> RESOURCE_INFO_REMOTE_GETTER = (ig, executor) ->
            rpcResourceServiceConsumer.selectResourceInfo()
                    .switchIfEmpty(defer(() -> just(emptyList())))
                    .toFuture();

    private final BiFunction<String, Executor, CompletableFuture<Map<String, ResourceInfo>>> IDENTITY_WITH_RESOURCE_INFO_REMOTE_GETTER = (ig, executor) ->
            rpcResourceServiceConsumer.selectResourceInfo()
                    .map(ris -> ris.stream().collect(toMap(ri ->
                                    INIT_RES_KEY_GENERATOR.apply(ri.getRequestMethod().toUpperCase().intern(), ri.getAbsoluteUri().intern()).intern(),
                            identity(), (a, b) -> a)))
                    .switchIfEmpty(defer(() -> just(emptyMap())))
                    .toFuture();

    private final Supplier<CompletableFuture<List<ResourceInfo>>> RESOURCE_INFO_GETTER = () ->
            resourceInfoCache.get(ALL_RESOURCES_CACHE_KEY, RESOURCE_INFO_REMOTE_GETTER);

    private final Supplier<CompletableFuture<Map<String, ResourceInfo>>> IDENTITY_WITH_RESOURCE_INFO_GETTER = () ->
            identityWithResourceInfoCache.get(ALL_RESOURCES_CACHE_KEY, IDENTITY_WITH_RESOURCE_INFO_REMOTE_GETTER);

    /**
     * select all resource info
     *
     * @return
     */
    @Override
    public Mono<List<ResourceInfo>> selectResourceInfo() {
        return fromFuture(RESOURCE_INFO_GETTER.get());
    }

    /**
     * select all resource info
     *
     * @return
     */
    @Override
    public Mono<Map<String, ResourceInfo>> selectIdentityWithResourceInfo() {
        return fromFuture(IDENTITY_WITH_RESOURCE_INFO_GETTER.get());
    }

    /**
     * get resource info by key
     *
     * @param resourceKey
     * @return
     */
    @Override
    public Mono<ResourceInfo> getResourceInfoByResourceKey(String resourceKey) {
        LOGGER.info("resourceKey = {}", resourceKey);

        return this.selectIdentityWithResourceInfo()
                .map(m -> m.get(resourceKey));
    }

}
