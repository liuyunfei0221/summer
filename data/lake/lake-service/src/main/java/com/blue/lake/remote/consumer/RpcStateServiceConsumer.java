package com.blue.lake.remote.consumer;

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
import java.util.Optional;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc state consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcStateServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getStateInfoOptById", async = false),
                    @Method(name = "getStateInfoById", async = false),
                    @Method(name = "getStateInfoMonoById", async = true),
                    @Method(name = "selectStateInfoByCountryId", async = false),
                    @Method(name = "selectStateInfoMonoByCountryId", async = true),
                    @Method(name = "selectStateInfoByIds", async = false),
                    @Method(name = "selectStateInfoMonoByIds", async = true),
                    @Method(name = "getStateRegionById", async = true),
                    @Method(name = "getStateRegionMonoById", async = true),
                    @Method(name = "selectStateRegionByIds", async = true),
                    @Method(name = "selectStateRegionMonoByIds", async = true)
            })
    private RpcStateService rpcStateService;

    private final Scheduler scheduler;

    public RpcStateServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get state info by id
     *
     * @param id
     * @return
     */
    public Optional<StateInfo> getStateInfoOptById(Long id) {
        return rpcStateService.getStateInfoOptById(id);
    }

    /**
     * get state info by id with assert
     *
     * @param id
     * @return
     */
    public StateInfo getStateInfoById(Long id) {
        return rpcStateService.getStateInfoById(id);
    }

    /**
     * get state info mono by id
     *
     * @param id
     * @return
     */
    public Mono<StateInfo> getStateInfoMonoById(Long id) {
        return fromFuture(rpcStateService.getStateInfoMonoById(id)).subscribeOn(scheduler);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    public List<StateInfo> selectStateInfoByCountryId(Long countryId) {
        return rpcStateService.selectStateInfoByCountryId(countryId);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    public Mono<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId) {
        return fromFuture(rpcStateService.selectStateInfoMonoByCountryId(countryId)).subscribeOn(scheduler);
    }

    /**
     * select state info by ids
     *
     * @param ids
     * @return
     */
    public Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids) {
        return rpcStateService.selectStateInfoByIds(ids);
    }

    /**
     * select state info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcStateService.selectStateInfoMonoByIds(ids)).subscribeOn(scheduler);
    }

    /**
     * get state region by id
     *
     * @param id
     * @return
     */
    public StateRegion getStateRegionById(Long id) {
        return rpcStateService.getStateRegionById(id);
    }

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    public Mono<StateRegion> getStateRegionMonoById(Long id) {
        return fromFuture(rpcStateService.getStateRegionMonoById(id)).subscribeOn(scheduler);
    }

    /**
     * select state regions by ids
     *
     * @param ids
     * @return
     */
    public Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids) {
        return rpcStateService.selectStateRegionByIds(ids);
    }

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids) {
        return fromFuture(rpcStateService.selectStateRegionMonoByIds(ids)).subscribeOn(scheduler);
    }

}
