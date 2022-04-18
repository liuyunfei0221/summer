package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcCityService;
import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.service.inter.CityService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc city provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcCityService.class, version = "1.0", methods = {
        @Method(name = "getCityInfoOptById", async = false),
        @Method(name = "getCityInfoById", async = false),
        @Method(name = "getCityInfoMonoById", async = true),
        @Method(name = "selectCityInfoByStateId", async = false),
        @Method(name = "selectCityInfoMonoByStateId", async = true),
        @Method(name = "selectCityInfoByIds", async = false),
        @Method(name = "selectCityInfoMonoByIds", async = true),
        @Method(name = "getCityRegionById", async = false),
        @Method(name = "getCityRegionMonoById", async = true),
        @Method(name = "selectCityRegionByIds", async = false),
        @Method(name = "selectCityRegionMonoByIds", async = true)
})
public class RpcCityServiceProvider implements RpcCityService {

    private static final Logger LOGGER = getLogger(RpcCityServiceProvider.class);

    private final CityService cityService;

    private final Scheduler scheduler;

    public RpcCityServiceProvider(CityService cityService, Scheduler scheduler) {
        this.cityService = cityService;
        this.scheduler = scheduler;
    }

    /**
     * get city info by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<CityInfo> getCityInfoOptById(Long id) {
        LOGGER.info("Optional<CityInfo> getCityInfoOptById(Long id), id = {}", id);
        return cityService.getCityInfoOptById(id);
    }

    /**
     * get city info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CityInfo getCityInfoById(Long id) {
        LOGGER.info("CityInfo getCityInfoById(Long id), id = {}", id);
        return cityService.getCityInfoById(id);
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<CityInfo> getCityInfoMonoById(Long id) {
        LOGGER.info("CompletableFuture<CityInfo> getCityInfoMonoById(Long id), id = {}", id);
        return just(id)
                .subscribeOn(scheduler)
                .flatMap(cityService::getCityInfoMonoById)
                .toFuture();
    }

    /**
     * select city infos by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<CityInfo> selectCityInfoByStateId(Long stateId) {
        LOGGER.info("List<CityInfo> selectCityInfoByStateId(Long stateId), countryId = {}", stateId);
        return cityService.selectCityInfoByStateId(stateId);
    }

    /**
     * select city infos mono by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public CompletableFuture<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        LOGGER.info("CompletableFuture<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId), countryId = {}", stateId);
        return just(stateId)
                .subscribeOn(scheduler)
                .flatMap(cityService::selectCityInfoMonoByStateId)
                .toFuture();
    }

    /**
     * select city infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids) {
        LOGGER.info("Map<Long, CityInfo> selectStateInfoByIds(List<Long> ids), ids = {}", ids);
        return cityService.selectCityInfoByIds(ids);
    }

    /**
     * select city infos mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids) {
        LOGGER.info("CompletableFuture<Map<Long, CityInfo>> selectStateInfoMonoByIds(List<Long> ids), ids = {}", ids);
        return just(ids)
                .subscribeOn(scheduler)
                .flatMap(cityService::selectCityInfoMonoByIds)
                .toFuture();
    }

    /**
     * get city region by id
     *
     * @param id
     * @return
     */
    @Override
    public CityRegion getCityRegionById(Long id) {
        LOGGER.info("CityRegion getCityRegionById(Long id), id = {}", id);
        return cityService.getCityRegionById(id);
    }

    /**
     * get city region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<CityRegion> getCityRegionMonoById(Long id) {
        LOGGER.info("CompletableFuture<CityRegion> getCityRegionMonoById(Long id), id = {}", id);
        return just(id)
                .subscribeOn(scheduler)
                .flatMap(cityService::getCityRegionMonoById)
                .toFuture();
    }

    /**
     * get city regions by id
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids) {
        LOGGER.info("Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids), ids = {}", ids);
        return cityService.selectCityRegionByIds(ids);
    }

    /**
     * get city regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids) {
        LOGGER.info("CompletableFuture<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids), ids = {}", ids);
        return just(ids)
                .subscribeOn(scheduler)
                .flatMap(cityService::selectCityRegionMonoByIds)
                .toFuture();
    }

}
