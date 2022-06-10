package com.blue.risk.remote.consumer;

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
import java.util.Optional;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc city consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public class RpcCityServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getCityInfoOptById", async = false),
                    @Method(name = "getCityInfoById", async = false),
                    @Method(name = "getCityInfoMonoById", async = true),
                    @Method(name = "selectCityInfoByStateId", async = false),
                    @Method(name = "selectCityInfoMonoByStateId", async = true),
                    @Method(name = "selectCityInfoByIds", async = false),
                    @Method(name = "selectCityInfoMonoByIds", async = true),
                    @Method(name = "getCityRegionById", async = true),
                    @Method(name = "getCityRegionMonoById", async = true),
                    @Method(name = "selectCityRegionByIds", async = true),
                    @Method(name = "selectCityRegionMonoByIds", async = true)
            })
    private RpcCityService rpcCityService;

    private final Scheduler scheduler;

    public RpcCityServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get city info by id
     *
     * @param id
     * @return
     */
    public Optional<CityInfo> getCityInfoOptById(Long id) {
        return rpcCityService.getCityInfoOptById(id);
    }

    /**
     * get city info by id with assert
     *
     * @param id
     * @return
     */
    public CityInfo getCityInfoById(Long id) {
        return rpcCityService.getCityInfoById(id);
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    public Mono<CityInfo> getCityInfoMonoById(Long id) {
        return fromFuture(rpcCityService.getCityInfoMonoById(id)).subscribeOn(scheduler);
    }

    /**
     * select city info by state id
     *
     * @param stateId
     * @return
     */
    public List<CityInfo> selectCityInfoByStateId(Long stateId) {
        return rpcCityService.selectCityInfoByStateId(stateId);
    }

    /**
     * select city info mono by state id
     *
     * @param stateId
     * @return
     */
    public Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        return fromFuture(rpcCityService.selectCityInfoMonoByStateId(stateId)).subscribeOn(scheduler);
    }

    /**
     * select city info by ids
     *
     * @param ids
     * @return
     */
    public Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids) {
        return rpcCityService.selectCityInfoByIds(ids);
    }

    /**
     * select city info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcCityService.selectCityInfoMonoByIds(ids)).subscribeOn(scheduler);
    }

    /**
     * get city region by id
     *
     * @param id
     * @return
     */
    public CityRegion getCityRegionById(Long id) {
        return rpcCityService.getCityRegionById(id);
    }

    /**
     * get city region mono by id
     *
     * @param id
     * @return
     */
    public Mono<CityRegion> getCityRegionMonoById(Long id) {
        return fromFuture(rpcCityService.getCityRegionMonoById(id)).subscribeOn(scheduler);
    }

    /**
     * get city regions by id
     *
     * @param ids
     * @return
     */
    public Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids) {
        return rpcCityService.selectCityRegionByIds(ids);
    }

    /**
     * get city regions mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids) {
        return fromFuture(rpcCityService.selectCityRegionMonoByIds(ids)).subscribeOn(scheduler);
    }

}
