package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcCityService;
import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.service.inter.CityService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

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

    public RpcCityServiceProvider(CityService cityService) {
        this.cityService = cityService;
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
                .flatMap(cityService::getCityInfoById)
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
                .flatMap(cityService::selectCityInfoByStateId)
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
                .flatMap(cityService::selectCityInfoByIds)
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
                .flatMap(cityService::getCityRegionById)
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
                .flatMap(cityService::selectCityRegionByIds)
                .toFuture();
    }

}
