package com.blue.base.service.impl;

import com.blue.base.api.model.StateInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.mapper.StateMapper;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.common.base.BlueChecker.isValidIdentities;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.DATA_NOT_EXIST;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.converter.BaseModelConverters.STATES_2_STATE_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.STATE_2_STATE_INFO_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * state service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class StateServiceImpl implements StateService {

    private static final Logger LOGGER = getLogger(StateServiceImpl.class);

    private StateMapper stateMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StateServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, StateMapper stateMapper) {
        this.stateMapper = stateMapper;

        COUNTRY_ID_STATES_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getStateMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        ID_STATE_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCountryMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static Cache<Long, List<StateInfo>> COUNTRY_ID_STATES_CACHE;

    private static Cache<Long, StateInfo> ID_STATE_CACHE;


    private final Function<Long, List<StateInfo>> DB_STATES_GETTER = cid -> {
        LOGGER.info("Function<Long, List<StateInfo>> DB_STATES_GETTER, cid = {}", cid);
        return STATES_2_STATE_INFOS_CONVERTER.apply(
                this.selectStateByCountryId(cid).stream().sorted(Comparator.comparing(State::getStateCode)).collect(toList()));
    };

    private final Function<Long, StateInfo> DB_STATE_GETTER = id -> {
        LOGGER.info("Function<Long, StateInfo> DB_STATE_GETTER, id = {}", id);
        return this.getStateById(id).map(STATE_2_STATE_INFO_CONVERTER).orElse(null);
    };

    private final Function<Long, StateInfo> DB_STATE_GETTER_WITH_ASSERT = id -> {
        LOGGER.info("Function<Long, StateInfo> DB_STATE_GETTER_WITH_ASSERT, id = {}", id);
        return this.getStateById(id).map(STATE_2_STATE_INFO_CONVERTER)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    };

    private final Function<List<Long>, Map<Long, StateInfo>> CACHE_STATES_BY_IDS_GETTER = ids -> {
        LOGGER.info("Function<List<Long>, Map<Long, StateInfo>> CACHE_STATES_BY_IDS_GETTER, ids = {}", ids);

        return isValidIdentities(ids) ? allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        ID_STATE_CACHE.getAll(l, is -> stateMapper.selectByIds(l)
                                        .parallelStream()
                                        .map(STATE_2_STATE_INFO_CONVERTER)
                                        .collect(toMap(StateInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a))
                :
                emptyMap();
    };

    /**
     * get state by state id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<State> getStateById(Long id) {
        LOGGER.info("Optional<State> getStateById(Long id), id = {}", id);

        return ofNullable(stateMapper.selectByPrimaryKey(id));
    }

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<State> selectStateByCountryId(Long countryId) {
        LOGGER.info("List<State> selectStateByCountryId(Long countryId), countryId = {}", countryId);

        if (isInvalidIdentity(countryId))
            throw new BlueException(INVALID_IDENTITY);

        return stateMapper.selectByCountryId(countryId);
    }

    /**
     * select states by ids
     *
     * @return
     */
    @Override
    public List<State> selectStateByIds(List<Long> ids) {
        LOGGER.info("List<State> selectStateByIds(List<Long> ids), ids = {}", ids);

        return isValidIdentities(ids) ? allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(stateMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList())
                :
                emptyList();
    }

    /**
     * get state info by country id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<StateInfo> getStateInfoOptById(Long id) {
        return ofNullable(ID_STATE_CACHE.get(id, DB_STATE_GETTER));
    }

    /**
     * get state info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public StateInfo getStateInfoById(Long id) {
        return ID_STATE_CACHE.get(id, DB_STATE_GETTER_WITH_ASSERT);
    }

    /**
     * get state info mono by country id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<StateInfo> getStateInfoMonoById(Long id) {
        return just(ID_STATE_CACHE.get(id, DB_STATE_GETTER_WITH_ASSERT));
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<StateInfo> selectStateInfoByCountryId(Long countryId) {
        return COUNTRY_ID_STATES_CACHE.get(countryId, DB_STATES_GETTER);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public Mono<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId) {
        return just(COUNTRY_ID_STATES_CACHE.get(countryId, DB_STATES_GETTER)).switchIfEmpty(just(emptyList()));
    }

    /**
     * select states infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids) {
        LOGGER.info("Map<Long,StateInfo> selectStateInfoByIds(List<Long> ids), ids = {}", ids);

        return CACHE_STATES_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select state infos mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids), ids = {}", ids);

        return just(CACHE_STATES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * invalid state infos
     *
     * @return
     */
    @Override
    public void invalidStateInfosCache() {
        LOGGER.info("void invalidStateInfosCache()");

        COUNTRY_ID_STATES_CACHE.invalidateAll();
        ID_STATE_CACHE.invalidateAll();
    }

}
