package com.blue.base.remote.provider;

import com.blue.base.api.inter.RpcStateService;
import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.service.inter.StateService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc state provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcStateService.class,
        version = "1.0",
        methods = {
                @Method(name = "getStateInfoOptById", async = false),
                @Method(name = "getStateInfoById", async = false),
                @Method(name = "getStateInfoMonoById", async = true),
                @Method(name = "selectStateInfoByCountryId", async = false),
                @Method(name = "selectStateInfoMonoByCountryId", async = true),
                @Method(name = "selectStateInfoByIds", async = false),
                @Method(name = "selectStateInfoMonoByIds", async = true),
                @Method(name = "getStateRegionById", async = false),
                @Method(name = "getStateRegionMonoById", async = true),
                @Method(name = "selectStateRegionByIds", async = false),
                @Method(name = "selectStateRegionMonoByIds", async = true)
        })
public class RpcStateServiceProvider implements RpcStateService {

    private static final Logger LOGGER = getLogger(RpcStateServiceProvider.class);

    private final StateService stateService;

    private final Scheduler scheduler;

    public RpcStateServiceProvider(StateService stateService, Scheduler scheduler) {
        this.stateService = stateService;
        this.scheduler = scheduler;
    }

    /**
     * get state info by State id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<StateInfo> getStateInfoOptById(Long id) {
        LOGGER.info("Optional<StateInfo> getStateInfoOptById(Long id), id = {}", id);
        return stateService.getStateInfoOptById(id);
    }

    /**
     * get state info by State id with assert
     *
     * @param id
     * @return
     */
    @Override
    public StateInfo getStateInfoById(Long id) {
        LOGGER.info("StateInfo getStateInfoById(Long id), id = {}", id);
        return stateService.getStateInfoById(id);
    }

    /**
     * get state info mono by State id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<StateInfo> getStateInfoMonoById(Long id) {
        LOGGER.info("CompletableFuture<StateInfo> getStateInfoMonoById(Long id), id = {}", id);
        return just(id)
                .subscribeOn(scheduler)
                .flatMap(stateService::getStateInfoMonoById)
                .toFuture();
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<StateInfo> selectStateInfoByCountryId(Long countryId) {
        LOGGER.info("List<StateInfo> selectStateInfoByCountryId(Long countryId), countryId = {}", countryId);
        return stateService.selectStateInfoByCountryId(countryId);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public CompletableFuture<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId) {
        LOGGER.info("CompletableFuture<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId), countryId = {}", countryId);
        return just(countryId)
                .subscribeOn(scheduler)
                .flatMap(stateService::selectStateInfoMonoByCountryId)
                .toFuture();
    }

    /**
     * select state infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids) {
        LOGGER.info("Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids), ids = {}", ids);
        return stateService.selectStateInfoByIds(ids);
    }

    /**
     * select state infos mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids) {
        LOGGER.info("CompletableFuture<Map<Long,StateInfo>> selectStateInfoMonoByIds(List<Long> ids), ids = {}", ids);
        return just(ids)
                .subscribeOn(scheduler)
                .flatMap(stateService::selectStateInfoMonoByIds)
                .toFuture();
    }

    /**
     * get state region by id
     *
     * @param id
     * @return
     */
    @Override
    public StateRegion getStateRegionById(Long id) {
        LOGGER.info("StateRegion getStateRegionById(Long id), id = {}", id);
        return stateService.getStateRegionById(id);
    }

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<StateRegion> getStateRegionMonoById(Long id) {
        LOGGER.info("CompletableFuture<StateRegion> getStateRegionMonoById(Long id), id = {}", id);
        return just(id)
                .subscribeOn(scheduler)
                .flatMap(stateService::getStateRegionMonoById)
                .toFuture();
    }

    /**
     * select state regions by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids) {
        LOGGER.info("Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids), ids = {}", ids);
        return stateService.selectStateRegionByIds(ids);
    }

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids) {
        LOGGER.info("CompletableFuture<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids), ids = {}", ids);
        return just(ids)
                .subscribeOn(scheduler)
                .flatMap(stateService::selectStateRegionMonoByIds)
                .toFuture();
    }

}
