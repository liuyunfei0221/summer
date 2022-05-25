package com.blue.base.service.impl;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.StateInsertParam;
import com.blue.base.model.StateUpdateParam;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.StateRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.common.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
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
import static reactor.core.publisher.Mono.defer;
import static reactor.core.publisher.Mono.just;

/**
 * state service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class StateServiceImpl implements StateService {

    private static final Logger LOGGER = Loggers.getLogger(CityServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private CountryService countryService;

    private StateRepository stateRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StateServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CountryService countryService, ExecutorService executorService,
                            AreaCaffeineDeploy areaCaffeineDeploy, StateRepository stateRepository) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.countryService = countryService;
        this.stateRepository = stateRepository;

        countryIdStatesCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getStateMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idStateCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getStateMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idRegionCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getStateMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private Cache<Long, StateInfo> idStateCache;

    private Cache<Long, List<StateInfo>> countryIdStatesCache;

    private Cache<Long, StateRegion> idRegionCache;

    private final Function<Long, StateInfo> DB_STATE_GETTER = id ->
            this.getStateById(id).map(STATE_2_STATE_INFO_CONVERTER).orElse(null);

    private final Function<Long, StateInfo> DB_STATE_GETTER_WITH_ASSERT = id ->
            this.getStateById(id).map(STATE_2_STATE_INFO_CONVERTER)
                    .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

    private final Function<Long, List<StateInfo>> DB_STATES_BY_COUNTRY_ID_GETTER = cid ->
            STATES_2_STATE_INFOS_CONVERTER.apply(
                    this.selectStateByCountryId(cid).stream().sorted(Comparator.comparing(State::getName)).collect(toList()));

    private final Function<Long, Optional<StateInfo>> STATE_OPT_BY_ID_GETTER = id ->
            ofNullable(idStateCache.get(id, DB_STATE_GETTER));

    private final Function<Long, StateInfo> STATE_BY_ID_WITH_ASSERT_GETTER = id ->
            idStateCache.get(id, DB_STATE_GETTER_WITH_ASSERT);

    private final Function<Long, List<StateInfo>> STATES_BY_COUNTRY_ID_GETTER = cid ->
            countryIdStatesCache.get(cid, DB_STATES_BY_COUNTRY_ID_GETTER);

    private final Function<List<Long>, Map<Long, StateInfo>> CACHE_STATES_BY_IDS_GETTER = ids -> {
        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idStateCache.getAll(l, is -> stateRepository.findAllById(l)
                                        .flatMap(s -> just(STATE_2_STATE_INFO_CONVERTER.apply(s)))
                                        .collectList().toFuture().join()
                                        .parallelStream()
                                        .collect(toMap(StateInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    private final Function<Long, StateRegion> STATE_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            return idRegionCache.get(id, i ->
                    just(idStateCache.get(i, DB_STATE_GETTER_WITH_ASSERT))
                            .flatMap(stateInfo ->
                                    countryService.getCountryInfoMonoById(stateInfo.getCountryId())
                                            .flatMap(countryInfo -> just(new StateRegion(id, countryInfo, stateInfo)))
                            )
                            .toFuture().join()
            );
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<List<Long>, Map<Long, StateRegion>> STATE_REGIONS_GETTER = ids -> {
        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idRegionCache.getAll(l, is -> {
                            Collection<StateInfo> stateInfos = CACHE_STATES_BY_IDS_GETTER.apply(ids).values();
                            int size = stateInfos.size();
                            List<Long> countryIds = new ArrayList<>(size);

                            for (StateInfo si : stateInfos)
                                countryIds.add(si.getCountryId());

                            return
                                    countryService.selectCountryInfoMonoByIds(countryIds)
                                            .flatMap(countryInfos ->
                                                    just(stateInfos.parallelStream().map(si -> new StateRegion(si.getId(), countryInfos.get(si.getCountryId()), si))
                                                            .collect(toMap(StateRegion::getStateId, ar -> ar, (a, b) -> a)))
                                            ).toFuture().join();
                        }).entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    /**
     * invalid all chche
     */
    private void invalidCache() {
        idStateCache.invalidateAll();
        countryIdStatesCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

    /**
     * is a state exist?
     */
    private final Consumer<StateInsertParam> INSERT_STATE_VALIDATOR = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        State probe = new State();
        probe.setCountryId(probe.getCountryId());
        probe.setName(param.getName());

        Long count = ofNullable(stateRepository.count(Example.of(probe)).toFuture().join()).orElse(0L);

        if (count > 0L)
            throw new BlueException(STATE_ALREADY_EXIST);
    };

    /**
     * state insert param -> state
     */
    public final Function<StateInsertParam, State> STATE_INSERT_PARAM_2_AREA_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long countryId = param.getCountryId();

        if (countryService.getCountryById(countryId).isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        Long stamp = TIME_STAMP_GETTER.get();

        State state = new State();

        state.setCountryId(countryId);
        state.setName(param.getName());
        state.setFipsCode(param.getFipsCode());
        state.setStateCode(param.getStateCode());
        state.setStatus(VALID.status);
        state.setCreateTime(stamp);
        state.setUpdateTime(stamp);

        return state;
    };

    /**
     * is a state exist?
     */
    private final Function<StateUpdateParam, State> UPDATE_STATE_VALIDATOR_AND_ORIGIN_RETURNER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long id = param.getId();

        State probe = new State();
        probe.setCountryId(param.getCountryId());
        probe.setName(param.getName());

        List<State> states = ofNullable(stateRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList);

        if (states.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        State state = stateRepository.findById(id).toFuture().join();
        if (isNull(state))
            throw new BlueException(DATA_NOT_EXIST);

        return state;
    };

    /**
     * for state
     */
    public final BiFunction<StateUpdateParam, State, Boolean> UPDATE_STATE_VALIDATOR = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);

        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        Long countryId = p.getCountryId();
        if (countryId != null && !countryId.equals(t.getCountryId())) {
            if (countryService.getCountryById(countryId).isEmpty())
                throw new BlueException(DATA_NOT_EXIST);
            t.setCountryId(countryId);
            alteration = true;
        }

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }


        String fipsCode = p.getFipsCode();
        if (isNotBlank(fipsCode) && !fipsCode.equals(t.getFipsCode())) {
            t.setFipsCode(fipsCode);
            alteration = true;
        }

        String stateCode = p.getStateCode();
        if (isNotBlank(stateCode) && !stateCode.equals(t.getStateCode())) {
            t.setStateCode(stateCode);
            alteration = true;
        }

        return alteration;
    };

    /**
     * insert state
     *
     * @param stateInsertParam
     * @return
     */
    @Override
    public Mono<StateInfo> insertState(StateInsertParam stateInsertParam) {
        LOGGER.info("Mono<StateInfo> insertState(StateInsertParam stateInsertParam), stateInsertParam = {}", stateInsertParam);

        INSERT_STATE_VALIDATOR.accept(stateInsertParam);
        State state = STATE_INSERT_PARAM_2_AREA_CONVERTER.apply(stateInsertParam);

        state.setId(blueIdentityProcessor.generate(State.class));

        return stateRepository.insert(state)
                .map(STATE_2_STATE_INFO_CONVERTER)
                .doOnSuccess(si -> {
                    LOGGER.info("si = {}", si);
                    invalidCache();
                });
    }

    /**
     * update state
     *
     * @param stateUpdateParam
     * @return
     */
    @Override
    public Mono<StateInfo> updateState(StateUpdateParam stateUpdateParam) {
        LOGGER.info("Mono<StateInfo> updateState(StateUpdateParam stateUpdateParam), stateUpdateParam = {}", stateUpdateParam);

        State state = UPDATE_STATE_VALIDATOR_AND_ORIGIN_RETURNER.apply(stateUpdateParam);

        Boolean changed = UPDATE_STATE_VALIDATOR.apply(stateUpdateParam, state);
        if (changed != null && !changed)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        return stateRepository.save(state)
                .map(STATE_2_STATE_INFO_CONVERTER)
                .doOnSuccess(si -> {
                    LOGGER.info("si = {}", si);
                    invalidCache();
                });
    }

    /**
     * delete city
     *
     * @param id
     * @return
     */
    @Override
    public Mono<StateInfo> deleteCity(Long id) {
        LOGGER.info("Mono<StateInfo> deleteCity(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        State state = stateRepository.findById(id).toFuture().join();
        if (isNull(state))
            throw new BlueException(DATA_NOT_EXIST);

        return stateRepository.delete(state)
                .then(just(STATE_2_STATE_INFO_CONVERTER.apply(state)))
                .doOnSuccess(si -> {
                    LOGGER.info("si = {}", si);
                    invalidCache();
                });
    }

    /**
     * a state's countryId was changed
     *
     * @param countryId
     * @param stateId
     * @return
     */
    @Override
    public int updateCountryIdOfCityByStateId(Long countryId, Long stateId) {
        //TODO
        return 0;
    }

    /**
     * a state's countryId was changed
     *
     * @param countryId
     * @param stateId
     * @return
     */
    @Override
    public int updateCountryIdOfAreaByStateId(Long countryId, Long stateId) {
        //TODO
        return 0;
    }

    /**
     * get state by state id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<State> getStateById(Long id) {
        return ofNullable(stateRepository.findById(id).toFuture().join());
    }

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<State> selectStateByCountryId(Long countryId) {
        if (isInvalidIdentity(countryId))
            throw new BlueException(INVALID_IDENTITY);

        State probe = new State();
        probe.setCountryId(countryId);

        return stateRepository.findAll(Example.of(probe), Sort.by(Sort.Order.asc("name")))
                .collectList().toFuture().join();
    }

    /**
     * select states by ids
     *
     * @return
     */
    @Override
    public List<State> selectStateByIds(List<Long> ids) {
        if (isInvalidIdentities(ids) || ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream()
                .map(l -> stateRepository.findAllById(l)
                        .collectList().toFuture().join())
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * get state info by country id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<StateInfo> getStateInfoOptById(Long id) {
        return STATE_OPT_BY_ID_GETTER.apply(id);
    }

    /**
     * get state info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public StateInfo getStateInfoById(Long id) {
        return STATE_BY_ID_WITH_ASSERT_GETTER.apply(id);
    }

    /**
     * get state info mono by country id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<StateInfo> getStateInfoMonoById(Long id) {
        return just(STATE_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<StateInfo> selectStateInfoByCountryId(Long countryId) {
        return STATES_BY_COUNTRY_ID_GETTER.apply(countryId);
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public Mono<List<StateInfo>> selectStateInfoMonoByCountryId(Long countryId) {
        return just(STATES_BY_COUNTRY_ID_GETTER.apply(countryId)).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select states info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids) {
        return CACHE_STATES_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select state info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, StateInfo>> selectStateInfoMonoByIds(List<Long> ids) {
        return just(CACHE_STATES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * get state region by id
     *
     * @param id
     * @return
     */
    @Override
    public StateRegion getStateRegionById(Long id) {
        return STATE_REGION_GETTER.apply(id);
    }

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<StateRegion> getStateRegionMonoById(Long id) {
        return just(STATE_REGION_GETTER.apply(id));
    }

    /**
     * select state regions by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, StateRegion> selectStateRegionByIds(List<Long> ids) {
        return STATE_REGIONS_GETTER.apply(ids);
    }

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, StateRegion>> selectStateRegionMonoByIds(List<Long> ids) {
        return just(STATE_REGIONS_GETTER.apply(ids));
    }

    /**
     * invalid state info
     *
     * @return
     */
    @Override
    public void invalidStateInfosCache() {
        countryIdStatesCache.invalidateAll();
        idStateCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

}
