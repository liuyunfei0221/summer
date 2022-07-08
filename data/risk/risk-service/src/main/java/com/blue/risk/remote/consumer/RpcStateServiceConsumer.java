package com.blue.risk.remote.consumer;

import com.blue.base.api.inter.RpcStateService;
import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.Map;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc state consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcStateServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getStateInfoById", async = true),
                    @Method(name = "selectStateInfoByCountryId", async = true),
                    @Method(name = "selectStateInfoByIds", async = true),
                    @Method(name = "getStateRegionById", async = true),
                    @Method(name = "selectStateRegionByIds", async = true)
            })
    private RpcStateService rpcStateService;

    private final Scheduler scheduler;

    public RpcStateServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get state info mono by id
     *
     * @param id
     * @return
     */
    public Mono<StateInfo> getStateInfoById(Long id) {
        return fromFuture(rpcStateService.getStateInfoById(id)).publishOn(scheduler);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    public Mono<List<StateInfo>> selectStateInfoByCountryId(Long countryId) {
        return fromFuture(rpcStateService.selectStateInfoByCountryId(countryId)).publishOn(scheduler);
    }

    /**
     * select state info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, StateInfo>> selectStateInfoByIds(List<Long> ids) {
        return fromFuture(rpcStateService.selectStateInfoByIds(ids)).publishOn(scheduler);
    }

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    public Mono<StateRegion> getStateRegionById(Long id) {
        return fromFuture(rpcStateService.getStateRegionById(id)).publishOn(scheduler);
    }

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, StateRegion>> selectStateRegionByIds(List<Long> ids) {
        return fromFuture(rpcStateService.selectStateRegionByIds(ids)).publishOn(scheduler);
    }

}
