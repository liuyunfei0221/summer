package com.blue.shine.remote.consumer;

import com.blue.base.api.inter.RpcCityService;
import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Map;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc city consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcCityServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getCityInfoById", async = true),
                    @Method(name = "selectCityInfoByStateId", async = true),
                    @Method(name = "selectCityInfoByIds", async = true),
                    @Method(name = "getCityRegionById", async = true),
                    @Method(name = "selectCityRegionByIds", async = true)
            })
    private RpcCityService rpcCityService;

    private final Scheduler scheduler;

    public RpcCityServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    public Mono<CityInfo> getCityInfoById(Long id) {
        return fromFuture(rpcCityService.getCityInfoById(id)).publishOn(scheduler);
    }

    /**
     * select city info mono by state id
     *
     * @param stateId
     * @return
     */
    public Mono<List<CityInfo>> selectCityInfoByStateId(Long stateId) {
        return fromFuture(rpcCityService.selectCityInfoByStateId(stateId)).publishOn(scheduler);
    }

    /**
     * select city info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, CityInfo>> selectCityInfoByIds(List<Long> ids) {
        return fromFuture(rpcCityService.selectCityInfoByIds(ids)).publishOn(scheduler);
    }

    /**
     * get city region mono by id
     *
     * @param id
     * @return
     */
    public Mono<CityRegion> getCityRegionById(Long id) {
        return fromFuture(rpcCityService.getCityRegionById(id)).publishOn(scheduler);
    }

    /**
     * get city regions mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, CityRegion>> selectCityRegionByIds(List<Long> ids) {
        return fromFuture(rpcCityService.selectCityRegionByIds(ids)).publishOn(scheduler);
    }

}
