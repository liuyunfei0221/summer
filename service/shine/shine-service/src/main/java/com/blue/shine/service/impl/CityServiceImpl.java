package com.blue.shine.service.impl;

import com.blue.base.api.model.CityRegion;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.shine.config.deploy.CaffeineDeploy;
import com.blue.shine.remote.consumer.RpcCityServiceConsumer;
import com.blue.shine.service.inter.CityService;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isValidIdentity;
import static com.blue.basic.constant.common.ResponseElement.DATA_NOT_EXIST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static reactor.core.publisher.Mono.*;

/**
 * city service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = Loggers.getLogger(CityServiceImpl.class);

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    public CityServiceImpl(RpcCityServiceConsumer rpcCityServiceConsumer, ExecutorService executorService, CaffeineDeploy caffeineDeploy) {
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;

        this.cityIdRegionCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private AsyncCache<Long, CityRegion> cityIdRegionCache;

    private final BiFunction<Long, Executor, CompletableFuture<CityRegion>> CITY_REGION_REMOTE_GETTER = (id, executor) -> {
        if (isValidIdentity(id))
            return rpcCityServiceConsumer.getCityRegionById(id)
                    .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                    .toFuture();

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<Long, CompletableFuture<CityRegion>> CITY_REGION_GETTER = id -> {
        if (isValidIdentity(id))
            return cityIdRegionCache.get(id, CITY_REGION_REMOTE_GETTER);

        throw new BlueException(INVALID_IDENTITY);
    };

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityRegion> getCityRegionById(Long id) {
        LOGGER.info("id = {}", id);
        return fromFuture(CITY_REGION_GETTER.apply(id));
    }

}
