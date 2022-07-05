package com.blue.analyze.remote.consumer;

import com.blue.base.api.inter.RpcAreaService;
import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc area consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcAreaServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getAreaInfoOptById", async = false),
                    @Method(name = "getAreaInfoById", async = false),
                    @Method(name = "getAreaInfoMonoById", async = true),
                    @Method(name = "selectAreaInfoByCityId", async = false),
                    @Method(name = "selectAreaInfoMonoByCityId", async = true),
                    @Method(name = "selectAreaInfoByIds", async = false),
                    @Method(name = "selectAreaInfoMonoByIds", async = true),
                    @Method(name = "getAreaRegionById", async = true),
                    @Method(name = "getAreaRegionMonoById", async = true),
                    @Method(name = "selectAreaRegionByIds", async = true),
                    @Method(name = "selectAreaRegionMonoByIds", async = true)
            })
    private RpcAreaService rpcAreaService;

    private final Scheduler scheduler;

    public RpcAreaServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get area info opt by id
     *
     * @param id
     * @return
     */
    public Optional<AreaInfo> getAreaInfoOptById(Long id) {
        return rpcAreaService.getAreaInfoOptById(id);
    }

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    public AreaInfo getAreaInfoById(Long id) {
        return rpcAreaService.getAreaInfoById(id);
    }

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    public Mono<AreaInfo> getAreaInfoMonoById(Long id) {
        return fromFuture(rpcAreaService.getAreaInfoMonoById(id)).publishOn(scheduler);
    }

    /**
     * select area info by city id
     *
     * @param cityId
     * @return
     */
    public List<AreaInfo> selectAreaInfoByCityId(Long cityId) {
        return rpcAreaService.selectAreaInfoByCityId(cityId);
    }

    /**
     * select area info mono by city id
     *
     * @param cityId
     * @return
     */
    public Mono<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId) {
        return fromFuture(rpcAreaService.selectAreaInfoMonoByCityId(cityId)).publishOn(scheduler);
    }

    /**
     * select area info by ids
     *
     * @param ids
     * @return
     */
    public Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids) {
        return rpcAreaService.selectAreaInfoByIds(ids);
    }

    /**
     * select area info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcAreaService.selectAreaInfoMonoByIds(ids)).publishOn(scheduler);
    }

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    public AreaRegion getAreaRegionById(Long id) {
        return rpcAreaService.getAreaRegionById(id);
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    public Mono<AreaRegion> getAreaRegionMonoById(Long id) {
        return fromFuture(rpcAreaService.getAreaRegionMonoById(id)).publishOn(scheduler);
    }

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    public Map<Long, AreaRegion> selectAreaRegionByIds(List<Long> ids) {
        return rpcAreaService.selectAreaRegionByIds(ids);
    }

    /**
     * get regions mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids) {
        return fromFuture(rpcAreaService.selectAreaRegionMonoByIds(ids)).publishOn(scheduler);
    }

}
