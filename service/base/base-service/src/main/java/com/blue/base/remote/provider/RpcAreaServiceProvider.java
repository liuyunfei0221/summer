package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcAreaService;
import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import com.blue.base.service.inter.AreaService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;

/**
 * rpc city provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcAreaService.class,
        version = "1.0",
        methods = {
                @Method(name = "getAreaInfoOptById", async = false),
                @Method(name = "getAreaInfoById", async = false),
                @Method(name = "getAreaInfoMonoById", async = true),
                @Method(name = "selectAreaInfoByCityId", async = false),
                @Method(name = "selectAreaInfoMonoByCityId", async = true),
                @Method(name = "selectAreaInfoByIds", async = false),
                @Method(name = "selectAreaInfoMonoByIds", async = true),
                @Method(name = "getAreaRegionById", async = false),
                @Method(name = "getAreaRegionMonoById", async = true),
                @Method(name = "selectAreaRegionByIds", async = false),
                @Method(name = "selectAreaRegionMonoByIds", async = true)
        })
public class RpcAreaServiceProvider implements RpcAreaService {

    private final AreaService areaService;

    private final Scheduler scheduler;

    public RpcAreaServiceProvider(AreaService areaService, Scheduler scheduler) {
        this.areaService = areaService;
        this.scheduler = scheduler;
    }

    /**
     * get area info opt by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AreaInfo> getAreaInfoOptById(Long id) {
        return areaService.getAreaInfoOptById(id);
    }

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public AreaInfo getAreaInfoById(Long id) {
        return areaService.getAreaInfoById(id);
    }

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<AreaInfo> getAreaInfoMonoById(Long id) {
        return just(id)
                .subscribeOn(scheduler)
                .flatMap(areaService::getAreaInfoMonoById)
                .toFuture();
    }

    /**
     * select area info by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public List<AreaInfo> selectAreaInfoByCityId(Long cityId) {
        return areaService.selectAreaInfoByCityId(cityId);
    }

    /**
     * select area info mono by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public CompletableFuture<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId) {
        return just(cityId)
                .subscribeOn(scheduler)
                .flatMap(areaService::selectAreaInfoMonoByCityId)
                .toFuture();
    }

    /**
     * select area info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids) {
        return areaService.selectAreaInfoByIds(ids);
    }

    /**
     * select area info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids) {
        return just(ids)
                .subscribeOn(scheduler)
                .flatMap(areaService::selectAreaInfoMonoByIds)
                .toFuture();
    }

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    @Override
    public AreaRegion getAreaRegionById(Long id) {
        return areaService.getAreaRegionById(id);
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<AreaRegion> getAreaRegionMonoById(Long id) {
        return just(id)
                .subscribeOn(scheduler)
                .flatMap(areaService::getAreaRegionMonoById)
                .toFuture();
    }

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, AreaRegion> selectAreaRegionByIds(List<Long> ids) {
        return areaService.selectAreaRegionByIds(ids);
    }

    /**
     * get regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids) {
        return just(ids)
                .subscribeOn(scheduler)
                .flatMap(areaService::selectAreaRegionMonoByIds)
                .toFuture();
    }

}
