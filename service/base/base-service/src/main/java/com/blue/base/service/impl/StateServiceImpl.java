package com.blue.base.service.impl;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.StateRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
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

/**
 * state service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class StateServiceImpl implements StateService {

    private CountryService countryService;

    private StateRepository stateRepository;

    public StateServiceImpl(CountryService countryService, ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, StateRepository stateRepository) {
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
                    this.selectStateByCountryId(cid).stream().sorted(Comparator.comparing(State::getStateCode)).collect(toList()));

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

                            for (StateInfo si : stateInfos) {
                                countryIds.add(si.getCountryId());
                            }

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

        State state = new State();
        state.setCountryId(countryId);

        return stateRepository.findAll(Example.of(state), Sort.by("name"))
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
        return just(STATES_BY_COUNTRY_ID_GETTER.apply(countryId)).switchIfEmpty(just(emptyList()));
    }

    /**
     * select states infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, StateInfo> selectStateInfoByIds(List<Long> ids) {
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
     * invalid state infos
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
