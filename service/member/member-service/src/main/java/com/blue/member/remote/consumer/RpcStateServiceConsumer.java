package com.blue.member.remote.consumer;

import com.blue.base.api.inter.RpcStateService;
import com.blue.base.api.model.StateInfo;
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
 * rpc state consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcStateServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcStateServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-base"}, methods = {
            @Method(name = "getStateInfoOptById", async = false),
            @Method(name = "getStateInfoById", async = false),
            @Method(name = "getStateInfoMonoById", async = true),
            @Method(name = "selectStateInfoByCountryId", async = false),
            @Method(name = "selectStateInfoMonoByCountryId", async = true),
            @Method(name = "selectStateInfoByIds", async = false),
            @Method(name = "selectStateInfoMonoByIds", async = true)
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
    Optional<StateInfo> getStateInfoOptById(Long id) {
        return rpcStateService.getStateInfoOptById(id);
    }

    /**
     * get state info by id with assert
     *
     * @param id
     * @return
     */
    StateInfo getStateInfoById(Long id) {
        return rpcStateService.getStateInfoById(id);
    }

    /**
     * get state info mono by id
     *
     * @param id
     * @return
     */
    Mono<StateInfo> getStateInfoMonoById(Long id) {
        return fromFuture(rpcStateService.getStateInfoMonoById(id)).publishOn(scheduler);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    List<StateInfo> selectStateInfoByCountryId(Long countryId) {
        return rpcStateService.selectStateInfoByCountryId(countryId);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    Mono<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId) {
        return fromFuture(rpcStateService.selectStateInfoMonoByCountryId(countryId)).publishOn(scheduler);
    }

    /**
     * select state infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids) {
        return rpcStateService.selectStateInfoByIds(ids);
    }

    /**
     * select state infos mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcStateService.selectStateInfoMonoByIds(ids)).publishOn(scheduler);
    }

}
