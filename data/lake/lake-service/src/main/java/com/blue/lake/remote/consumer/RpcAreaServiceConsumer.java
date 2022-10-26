package com.blue.lake.remote.consumer;

import com.blue.base.api.inter.RpcAreaService;
import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static reactor.core.publisher.Mono.fromFuture;

/**
 * rpc area consumer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused"})
@Component
public class RpcAreaServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-base"},
            methods = {
                    @Method(name = "getAreaInfoById", async = true),
                    @Method(name = "selectAreaInfoByCityId", async = true),
                    @Method(name = "selectAreaInfoByIds", async = true),
                    @Method(name = "getAreaRegionById", async = true),
                    @Method(name = "selectAreaRegionByIds", async = true)
            })
    private RpcAreaService rpcAreaService;

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    public Mono<AreaInfo> getAreaInfoById(Long id) {
        return fromFuture(rpcAreaService.getAreaInfoById(id));
    }

    /**
     * select area info mono by city id
     *
     * @param cityId
     * @return
     */
    public Mono<List<AreaInfo>> selectAreaInfoByCityId(Long cityId) {
        return fromFuture(rpcAreaService.selectAreaInfoByCityId(cityId));
    }

    /**
     * select area info mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, AreaInfo>> selectAreaInfoByIds(List<Long> ids) {
        return fromFuture(rpcAreaService.selectAreaInfoByIds(ids));
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    public Mono<AreaRegion> getAreaRegionById(Long id) {
        return fromFuture(rpcAreaService.getAreaRegionById(id));
    }

    /**
     * get regions mono by ids
     *
     * @param ids
     * @return
     */
    public Mono<Map<Long, AreaRegion>> selectAreaRegionByIds(List<Long> ids) {
        return fromFuture(rpcAreaService.selectAreaRegionByIds(ids));
    }

}
