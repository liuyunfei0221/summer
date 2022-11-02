package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcStateService;
import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.service.inter.StateService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;

/**
 * rpc state provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcStateService.class,
        version = "1.0",
        methods = {
                @Method(name = "getStateInfoById", async = true),
                @Method(name = "selectStateInfoByCountryId", async = true),
                @Method(name = "selectStateInfoByIds", async = true),
                @Method(name = "getStateRegionById", async = true),
                @Method(name = "selectStateRegionByIds", async = true)
        })
public class RpcStateServiceProvider implements RpcStateService {

    private final StateService stateService;

    public RpcStateServiceProvider(StateService stateService) {
        this.stateService = stateService;
    }

    /**
     * get state info mono by State id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<StateInfo> getStateInfoById(Long id) {
        return just(id)
                .flatMap(stateService::getStateInfoById)
                .toFuture();
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public CompletableFuture<List<StateInfo>> selectStateInfoByCountryId(Long countryId) {
        return just(countryId)
                .flatMap(stateService::selectStateInfoByCountryId)
                .toFuture();
    }

    /**
     * select state info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, StateInfo>> selectStateInfoByIds(List<Long> ids) {
        return just(ids)
                .flatMap(stateService::selectStateInfoByIds)
                .toFuture();
    }

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<StateRegion> getStateRegionById(Long id) {
        return just(id)
                .flatMap(stateService::getStateRegionById)
                .toFuture();
    }

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, StateRegion>> selectStateRegionByIds(List<Long> ids) {
        return just(ids)
                .flatMap(stateService::selectStateRegionByIds)
                .toFuture();
    }

}
