package com.blue.shine.service.impl;

import com.blue.base.api.model.CityRegion;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.shine.config.deploy.CaffeineDeploy;
import com.blue.shine.remote.consumer.RpcCityServiceConsumer;
import com.blue.shine.service.inter.CityService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isValidIdentity;
import static com.blue.basic.constant.common.ResponseElement.DATA_NOT_EXIST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.error;

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

        this.idRegionCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private Cache<Long, CityRegion> idRegionCache;

    private final Function<Long, CityRegion> CITY_REGION_REMOTE_GETTER = id -> {
        if (isValidIdentity(id))
            return ofNullable(rpcCityServiceConsumer.getCityRegionById(id)
                    .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                    .toFuture().join()).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<Long, CityRegion> CITY_REGION_GETTER = id -> {
        if (isValidIdentity(id))
            return idRegionCache.get(id, CITY_REGION_REMOTE_GETTER);

        throw new BlueException(INVALID_IDENTITY);
    };

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    @Override
    public CityRegion getCityRegionById(Long id) {
        LOGGER.info("CityRegion getCityRegionById(Long id), id = {}", id);
        return CITY_REGION_GETTER.apply(id);
    }

}
