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
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;

/**
 * rpc city provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcAreaService.class,
        version = "1.0",
        methods = {
                @Method(name = "getAreaInfoById", async = true),
                @Method(name = "selectAreaInfoByCityId", async = true),
                @Method(name = "selectAreaInfoByIds", async = true),
                @Method(name = "getAreaRegionById", async = true),
                @Method(name = "selectAreaRegionByIds", async = true)
        })
public class RpcAreaServiceProvider implements RpcAreaService {

    private final AreaService areaService;

    private final Scheduler scheduler;

    public RpcAreaServiceProvider(AreaService areaService, Scheduler scheduler) {
        this.areaService = areaService;
        this.scheduler = scheduler;
    }

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<AreaInfo> getAreaInfoById(Long id) {
        return just(id)
                .publishOn(scheduler)
                .flatMap(areaService::getAreaInfoMonoById)
                .toFuture();
    }

    /**
     * select area info mono by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public CompletableFuture<List<AreaInfo>> selectAreaInfoByCityId(Long cityId) {
        return just(cityId)
                .publishOn(scheduler)
                .flatMap(areaService::selectAreaInfoMonoByCityId)
                .toFuture();
    }

    /**
     * select area info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, AreaInfo>> selectAreaInfoByIds(List<Long> ids) {
        return just(ids)
                .publishOn(scheduler)
                .flatMap(areaService::selectAreaInfoMonoByIds)
                .toFuture();
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<AreaRegion> getAreaRegionById(Long id) {
        return just(id)
                .publishOn(scheduler)
                .flatMap(areaService::getAreaRegionMonoById)
                .toFuture();
    }

    /**
     * get regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, AreaRegion>> selectAreaRegionByIds(List<Long> ids) {
        return just(ids)
                .publishOn(scheduler)
                .flatMap(areaService::selectAreaRegionMonoByIds)
                .toFuture();
    }

}
