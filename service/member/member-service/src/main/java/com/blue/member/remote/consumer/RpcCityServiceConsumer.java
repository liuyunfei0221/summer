package com.blue.member.remote.consumer;

import com.blue.base.api.inter.RpcCityService;
import com.blue.base.api.model.CityInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc city consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcCityServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcCityServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-base"}, methods = {
            @Method(name = "getCityInfoOptById", async = false),
            @Method(name = "getCityInfoById", async = false),
            @Method(name = "getCityInfoMonoById", async = true),
            @Method(name = "selectCityInfoByStateId", async = false),
            @Method(name = "selectCityInfoMonoByStateId", async = true),
            @Method(name = "selectCityInfoByIds", async = false),
            @Method(name = "selectCityInfoMonoByIds", async = true)
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
    Optional<CityInfo> getCityInfoOptById(Long id) {
        return rpcCityService.getCityInfoOptById(id);
    }

    /**
     * get city info by id with assert
     *
     * @param id
     * @return
     */
    CityInfo getCityInfoById(Long id) {
        return rpcCityService.getCityInfoById(id);
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    Mono<CityInfo> getCityInfoMonoById(Long id) {
        return fromFuture(rpcCityService.getCityInfoMonoById(id)).publishOn(scheduler);
    }

    /**
     * select city infos by state id
     *
     * @param stateId
     * @return
     */
    List<CityInfo> selectCityInfoByStateId(Long stateId) {
        return rpcCityService.selectCityInfoByStateId(stateId);
    }

    /**
     * select city infos mono by state id
     *
     * @param stateId
     * @return
     */
    Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        return fromFuture(rpcCityService.selectCityInfoMonoByStateId(stateId)).publishOn(scheduler);
    }

    /**
     * select city infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids) {
        return rpcCityService.selectCityInfoByIds(ids);
    }

    /**
     * select city infos mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcCityService.selectCityInfoMonoByIds(ids)).publishOn(scheduler);
    }

}
