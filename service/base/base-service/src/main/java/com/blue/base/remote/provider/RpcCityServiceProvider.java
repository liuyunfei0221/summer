package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcCityService;
import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.service.inter.CityService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;

/**
 * rpc city provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcCityService.class,
        version = "1.0",
        methods = {
                @Method(name = "getCityInfoById", async = true),
                @Method(name = "selectCityInfoByStateId", async = true),
                @Method(name = "selectCityInfoByIds", async = true),
                @Method(name = "getCityRegionById", async = true),
                @Method(name = "selectCityRegionByIds", async = true)
        })
public class RpcCityServiceProvider implements RpcCityService {

    private final CityService cityService;

    private final Scheduler scheduler;

    public RpcCityServiceProvider(CityService cityService, Scheduler scheduler) {
        this.cityService = cityService;
        this.scheduler = scheduler;
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<CityInfo> getCityInfoById(Long id) {
        return just(id)
                .publishOn(scheduler)
                .flatMap(cityService::getCityInfoMonoById)
                .toFuture();
    }

    /**
     * select city info mono by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public CompletableFuture<List<CityInfo>> selectCityInfoByStateId(Long stateId) {
        return just(stateId)
                .publishOn(scheduler)
                .flatMap(cityService::selectCityInfoMonoByStateId)
                .toFuture();
    }

    /**
     * select city info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, CityInfo>> selectCityInfoByIds(List<Long> ids) {
        return just(ids)
                .publishOn(scheduler)
                .flatMap(cityService::selectCityInfoMonoByIds)
                .toFuture();
    }

    /**
     * get city region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<CityRegion> getCityRegionById(Long id) {
        return just(id)
                .publishOn(scheduler)
                .flatMap(cityService::getCityRegionMonoById)
                .toFuture();
    }

    /**
     * get city regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, CityRegion>> selectCityRegionByIds(List<Long> ids) {
        return just(ids)
                .publishOn(scheduler)
                .flatMap(cityService::selectCityRegionMonoByIds)
                .toFuture();
    }

}
