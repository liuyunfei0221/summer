package com.blue.base.service.impl;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.StateCondition;
import com.blue.base.model.StateInsertParam;
import com.blue.base.model.StateUpdateParam;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Area;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.StateRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
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
import static com.blue.base.constant.ColumnName.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Status.VALID;
import static com.blue.base.converter.BaseModelConverters.STATES_2_STATE_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.STATE_2_STATE_INFO_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static reactor.core.publisher.Mono.*;

/**
 * state service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection", "DuplicatedCode"})
@Service
public class StateServiceImpl implements StateService {

    private static final Logger LOGGER = Loggers.getLogger(CityServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private CountryService countryService;

    private StateRepository stateRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public StateServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CountryService countryService, StateRepository stateRepository,
                            ExecutorService executorService, ReactiveMongoTemplate reactiveMongoTemplate, AreaCaffeineDeploy areaCaffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.countryService = countryService;
        this.stateRepository = stateRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;

        idStateCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getStateMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        countryIdStatesCache = generateCache(new CaffeineConfParams(
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
            STATES_2_STATE_INFOS_CONVERTER.apply(this.selectStateByCountryId(cid));

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
     * is a state exist?
     */
    private final Consumer<StateInsertParam> INSERT_STATE_VALIDATOR = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        State probe = new State();
        probe.setCountryId(param.getCountryId());
        probe.setName(param.getName());

        Long count = ofNullable(stateRepository.count(Example.of(probe)).toFuture().join()).orElse(0L);

        if (count > 0L)
            throw new BlueException(STATE_ALREADY_EXIST);
    };

    /**
     * state insert param -> state
     */
    public final Function<StateInsertParam, State> STATE_INSERT_PARAM_2_STATE_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long countryId = param.getCountryId();

        if (countryService.getCountryById(countryId).isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        Long stamp = TIME_STAMP_GETTER.get();

        State state = new State();

        state.setId(blueIdentityProcessor.generate(State.class));
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

    private static final String NAME_COLUMN_NAME = "name";

    private static final Function<StateCondition, Query> CONDITION_PROCESSOR = condition -> {
        Query query = new Query();

        if (condition == null)
            return query;

        State probe = new State();

        ofNullable(condition.getId()).ifPresent(probe::setId);
        ofNullable(condition.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(condition.getStatus()).ifPresent(probe::setStatus);

        query.addCriteria(byExample(probe));

        ofNullable(condition.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME_COLUMN_NAME).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));

        query.with(by(Sort.Order.asc(NAME.name)));

        return query;
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
        State state = STATE_INSERT_PARAM_2_STATE_CONVERTER.apply(stateInsertParam);

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

        Long originalCountryId = state.getCountryId();

        Boolean changed = UPDATE_STATE_VALIDATOR.apply(stateUpdateParam, state);
        if (changed != null && !changed)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        return stateRepository.save(state)
                .map(STATE_2_STATE_INFO_CONVERTER)
                .doOnSuccess(si -> {
                    LOGGER.info("si = {}", si);

                    Long destCountryId = stateUpdateParam.getCountryId();
                    if (!originalCountryId.equals(destCountryId)) {
                        Long stateId = state.getId();

                        Long cityModifiedCount = updateCountryIdOfCityByStateId(destCountryId, stateId).toFuture().join();
                        Long areayModifiedCount = updateCountryIdOfAreaByStateId(destCountryId, stateId).toFuture().join();

                        LOGGER.info("cityModifiedCount = {}, areayModifiedCount = {}", cityModifiedCount, areayModifiedCount);
                    }

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
    public Mono<StateInfo> deleteState(Long id) {
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
     * invalid chche
     */
    @Override
    public void invalidCache() {
        idStateCache.invalidateAll();
        countryIdStatesCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

    /**
     * a state's countryId was changed
     *
     * @param countryId
     * @param stateId
     * @return
     */
    @Override
    public Mono<Long> updateCountryIdOfCityByStateId(Long countryId, Long stateId) {
        LOGGER.info("Mono<Long> updateCountryIdOfCityByStateId(Long countryId, Long stateId), countryId = {}, stateId = {}", countryId, stateId);
        if (isInvalidIdentity(countryId) || isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        City probe = new City();
        probe.setStateId(stateId);

        return reactiveMongoTemplate.updateMulti(query(byExample(probe)), new Update()
                        .set(COUNTRY_ID.name, countryId), City.class)
                .flatMap(updateResult -> {
                    long modifiedCount = updateResult.getModifiedCount();

                    LOGGER.info("Mono<Long> updateCountryIdOfCityByStateId(Long countryId, Long stateId), matchedCount = {}, modifiedCount = {}, wasAcknowledged = {}",
                            countryId, stateId, updateResult.getMatchedCount(), modifiedCount, updateResult.wasAcknowledged());

                    return just(modifiedCount);
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
    public Mono<Long> updateCountryIdOfAreaByStateId(Long countryId, Long stateId) {
        LOGGER.info("Mono<Long> updateCountryIdOfAreaByStateId(Long countryId, Long stateId), countryId = {}, stateId = {}", countryId, stateId);
        if (isInvalidIdentity(countryId) || isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        Area probe = new Area();
        probe.setStateId(stateId);

        return reactiveMongoTemplate.updateMulti(query(byExample(probe)), new Update()
                        .set(COUNTRY_ID.name, countryId), Area.class)
                .flatMap(updateResult -> {
                    long modifiedCount = updateResult.getModifiedCount();

                    LOGGER.info("Mono<Long> updateCountryIdOfAreaByStateId(Long countryId, Long stateId), matchedCount = {}, modifiedCount = {}, wasAcknowledged = {}",
                            countryId, stateId, updateResult.getMatchedCount(), modifiedCount, updateResult.wasAcknowledged());

                    return just(modifiedCount);
                });
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

        return stateRepository.findAll(Example.of(probe), by(Sort.Order.asc(NAME.name)))
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

    /**
     * select state by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<State>> selectStateMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<State>> selectStateMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);

        if (limit == null || limit < 0 || rows == null || rows == 0)
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, State.class).collectList();
    }

    /**
     * count state by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countStateMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countStateMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, State.class);
    }

    /**
     * select state info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<StateInfo>> selectStatePageMonoByPageAndCondition(PageModelRequest<StateCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<StateInfo>> selectStatePageMonoByPageAndCondition(PageModelRequest<StateCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(
                selectStateMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countStateMonoByQuery(query)
        ).flatMap(tuple2 -> {
            List<State> states = tuple2.getT1();

            return isNotEmpty(states) ?
                    just(new PageModelResponse<>(STATES_2_STATE_INFOS_CONVERTER.apply(states), tuple2.getT2()))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
        });
    }

}
