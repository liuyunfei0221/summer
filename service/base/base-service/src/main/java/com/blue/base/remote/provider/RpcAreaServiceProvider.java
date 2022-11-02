package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcAreaService;
import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import com.blue.base.service.inter.AreaService;
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

    public RpcAreaServiceProvider(AreaService areaService) {
        this.areaService = areaService;
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
                .flatMap(areaService::getAreaInfoById)
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
                .flatMap(areaService::selectAreaInfoByCityId)
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
                .flatMap(areaService::selectAreaInfoByIds)
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
                .flatMap(areaService::getAreaRegionById)
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
                .flatMap(areaService::selectAreaRegionByIds)
                .toFuture();
    }

}
