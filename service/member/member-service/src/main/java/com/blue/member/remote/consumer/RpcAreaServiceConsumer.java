package com.blue.member.remote.consumer;

import com.blue.base.api.inter.RpcAreaService;
import com.blue.base.api.model.AreaInfo;
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
 * rpc area consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcAreaServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcAreaServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-base"}, methods = {
            @Method(name = "getAreaInfoOptById", async = false),
            @Method(name = "getAreaInfoById", async = false),
            @Method(name = "getAreaInfoMonoById", async = true),
            @Method(name = "selectAreaInfoByCityId", async = false),
            @Method(name = "selectAreaInfoMonoByCityId", async = true),
            @Method(name = "selectAreaInfoByIds", async = false),
            @Method(name = "selectAreaInfoMonoByIds", async = true)
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
    Optional<AreaInfo> getAreaInfoOptById(Long id) {
        return rpcAreaService.getAreaInfoOptById(id);
    }

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    AreaInfo getAreaInfoById(Long id) {
        return rpcAreaService.getAreaInfoById(id);
    }

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    Mono<AreaInfo> getAreaInfoMonoById(Long id) {
        return fromFuture(rpcAreaService.getAreaInfoMonoById(id)).publishOn(scheduler);
    }

    /**
     * select area infos by city id
     *
     * @param cityId
     * @return
     */
    List<AreaInfo> selectAreaInfoByCityId(Long cityId) {
        return rpcAreaService.selectAreaInfoByCityId(cityId);
    }

    /**
     * select area infos mono by city id
     *
     * @param cityId
     * @return
     */
    Mono<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId) {
        return fromFuture(rpcAreaService.selectAreaInfoMonoByCityId(cityId)).publishOn(scheduler);
    }

    /**
     * select area infos by ids
     *
     * @param ids
     * @return
     */
    Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids) {
        return rpcAreaService.selectAreaInfoByIds(ids);
    }

    /**
     * select area infos mono by ids
     *
     * @param ids
     * @return
     */
    Mono<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids) {
        return fromFuture(rpcAreaService.selectAreaInfoMonoByIds(ids)).publishOn(scheduler);
    }

}
