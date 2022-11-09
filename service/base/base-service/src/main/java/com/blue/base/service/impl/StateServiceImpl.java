package com.blue.base.service.impl;

import com.blue.base.api.model.StateInfo;
import com.blue.base.api.model.StateRegion;
import com.blue.base.config.deploy.CaffeineDeploy;
import com.blue.base.model.StateCondition;
import com.blue.base.model.StateInsertParam;
import com.blue.base.model.StateUpdateParam;
import com.blue.base.repository.entity.Area;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.StateRepository;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.base.constant.BaseColumnName.COUNTRY_ID;
import static com.blue.base.constant.BaseColumnName.NAME;
import static com.blue.base.converter.BaseModelConverters.STATES_2_STATE_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.STATE_2_STATE_INFO_CONVERTER;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_WRITE;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;

/**
 * state service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class StateServiceImpl implements StateService {

    private static final Logger LOGGER = Loggers.getLogger(CityServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private ExecutorService executorService;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private CountryService countryService;

    private StateRepository stateRepository;

    public StateServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ExecutorService executorService, ReactiveMongoTemplate reactiveMongoTemplate,
                            CountryService countryService, StateRepository stateRepository, CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.executorService = executorService;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.countryService = countryService;
        this.stateRepository = stateRepository;

        idStateCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getStateMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));

        countryIdStatesCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getCountryMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));

        idRegionCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getStateMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));
    }

    private AsyncCache<Long, StateInfo> idStateCache;

    private AsyncCache<Long, List<StateInfo>> countryIdStatesCache;

    private AsyncCache<Long, StateRegion> idRegionCache;

    private final BiFunction<Long, Executor, CompletableFuture<StateInfo>> DB_STATE_WITH_ASSERT_GETTER = (id, executor) -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return this.getStateById(id).map(STATE_2_STATE_INFO_CONVERTER)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture();
    };

    private final BiFunction<Long, Executor, CompletableFuture<List<StateInfo>>> DB_STATES_BY_COUNTRY_ID_GETTER = (cid, executor) -> {
        if (isInvalidIdentity(cid))
            throw new BlueException(INVALID_IDENTITY);

        return this.selectStateByCountryId(cid).map(STATES_2_STATE_INFOS_CONVERTER)
                .switchIfEmpty(defer(() -> just(emptyList())))
                .toFuture();
    };

    private final Function<Long, CompletableFuture<StateInfo>> STATE_BY_ID_WITH_ASSERT_GETTER = id -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return idStateCache.get(id, DB_STATE_WITH_ASSERT_GETTER);
    };

    private final Function<Long, CompletableFuture<List<StateInfo>>> STATES_BY_COUNTRY_ID_GETTER = cid -> {
        if (isInvalidIdentity(cid))
            throw new BlueException(INVALID_IDENTITY);

        return countryIdStatesCache.get(cid, DB_STATES_BY_COUNTRY_ID_GETTER);
    };

    private final Function<List<Long>, CompletableFuture<Map<Long, StateInfo>>> CACHE_STATES_BY_IDS_GETTER = ids -> {
        if (isEmpty(ids))
            return supplyAsync(Collections::emptyMap, this.executorService);
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return idStateCache.getAll(ids, (is, executor) -> fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(l -> stateRepository.findAllById(ids)
                        .map(STATE_2_STATE_INFO_CONVERTER))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList)
                .map(l -> l.stream().collect(toMap(StateInfo::getId, identity(), (a, b) -> a)))
                .toFuture());
    };

    private final Function<Long, Mono<StateRegion>> STATE_REGION_GETTER = id ->
            isValidIdentity(id) ?
                    fromFuture(STATE_BY_ID_WITH_ASSERT_GETTER.apply(id))
                            .flatMap(stateInfo ->
                                    countryService.getCountryInfoById(stateInfo.getCountryId())
                                            .map(countryInfo -> new StateRegion(id, countryInfo, stateInfo)))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));

    private final Function<List<Long>, CompletableFuture<Map<Long, StateRegion>>> STATE_REGIONS_GETTER = ids -> {
        if (isEmpty(ids))
            return supplyAsync(Collections::emptyMap, this.executorService);
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return idRegionCache.getAll(ids, (is, executor) ->
                fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                        .flatMap(l ->
                                fromFuture(CACHE_STATES_BY_IDS_GETTER.apply(l))
                                        .flatMap(stateMap ->
                                                countryService.selectCountryInfoByIds(
                                                                stateMap.values().stream().map(StateInfo::getCountryId).distinct().collect(toList()))
                                                        .map(countryMap ->
                                                                stateMap.values().stream()
                                                                        .map(si ->
                                                                                new StateRegion(si.getId(), countryMap.get(si.getCountryId()), si))
                                                                        .collect(toMap(StateRegion::getStateId, ar -> ar, (a, b) -> a))
                                                        )))
                        .map(m -> fromIterable(m.values()))
                        .reduce(Flux::concat)
                        .flatMap(f -> f.collectMap(StateRegion::getStateId, identity()))
                        .toFuture());
    };

    private final Consumer<StateInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        State probe = new State();
        probe.setCountryId(p.getCountryId());
        probe.setName(p.getName());

        if (ofNullable(stateRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(STATE_ALREADY_EXIST);
    };

    public final Function<StateInsertParam, State> STATE_INSERT_PARAM_2_STATE_CONVERTER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long countryId = p.getCountryId();

        if (isNull(countryService.getCountryById(countryId).toFuture().join()))
            throw new BlueException(DATA_NOT_EXIST);

        Long stamp = TIME_STAMP_GETTER.get();

        State state = new State();

        state.setId(blueIdentityProcessor.generate(State.class));
        state.setCountryId(countryId);
        state.setName(p.getName());
        state.setFipsCode(p.getFipsCode());
        state.setStateCode(p.getStateCode());
        state.setStatus(VALID.status);
        state.setCreateTime(stamp);
        state.setUpdateTime(stamp);

        return state;
    };

    private final Function<StateUpdateParam, State> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        State probe = new State();
        probe.setCountryId(p.getCountryId());
        probe.setName(p.getName());

        List<State> states = ofNullable(stateRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList);

        if (states.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        State state = stateRepository.findById(id).toFuture().join();
        if (isNull(state))
            throw new BlueException(DATA_NOT_EXIST);

        return state;
    };

    public final BiConsumer<StateUpdateParam, State> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);
        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        Long countryId = p.getCountryId();
        if (countryId != null && !countryId.equals(t.getCountryId())) {
            if (isNull(countryService.getCountryById(countryId).toFuture().join()))
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

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Function<StateCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null)
            return query;

        State probe = new State();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(c.getStatus()).ifPresent(probe::setStatus);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));

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
        LOGGER.info("stateInsertParam = {}", stateInsertParam);

        INSERT_ITEM_VALIDATOR.accept(stateInsertParam);
        State state = STATE_INSERT_PARAM_2_STATE_CONVERTER.apply(stateInsertParam);

        return stateRepository.insert(state)
                .map(STATE_2_STATE_INFO_CONVERTER);
    }

    /**
     * update state
     *
     * @param stateUpdateParam
     * @return
     */
    @Override
    public Mono<StateInfo> updateState(StateUpdateParam stateUpdateParam) {
        LOGGER.info("stateUpdateParam = {}", stateUpdateParam);

        State state = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(stateUpdateParam);

        Long originalCountryId = state.getCountryId();

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(stateUpdateParam, state);

        return stateRepository.save(state)
                .map(STATE_2_STATE_INFO_CONVERTER)
                .doOnSuccess(si -> {
                    LOGGER.info("si = {}", si);

                    Long destCountryId = stateUpdateParam.getCountryId();
                    if (!originalCountryId.equals(destCountryId)) {
                        Long stateId = state.getId();

                        Long cityModifiedCount = updateCountryIdOfCityByStateId(destCountryId, stateId).toFuture().join();
                        Long areaModifiedCount = updateCountryIdOfAreaByStateId(destCountryId, stateId).toFuture().join();

                        LOGGER.info("cityModifiedCount = {}, areaModifiedCount = {}", cityModifiedCount, areaModifiedCount);
                    }
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
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return stateRepository.findById(id)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(state -> {
                    City probe = new City();
                    probe.setStateId(id);

                    Query query = new Query();
                    query.addCriteria(byExample(probe));

                    return reactiveMongoTemplate.count(query, City.class)
                            .flatMap(cityCount ->
                                    cityCount <= 0L ?
                                            stateRepository.delete(state)
                                            :
                                            error(new BlueException(REGION_DATA_STILL_USED))
                            )
                            .then(just(STATE_2_STATE_INFO_CONVERTER.apply(state)));
                });
    }

    /**
     * invalid cache
     */
    @Override
    public void invalidCache() {
        idStateCache.synchronous().invalidateAll();
        countryIdStatesCache.synchronous().invalidateAll();
        idRegionCache.synchronous().invalidateAll();
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
        LOGGER.info("countryId = {}, stateId = {}", countryId, stateId);
        if (isInvalidIdentity(countryId) || isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        City probe = new City();
        probe.setStateId(stateId);

        return reactiveMongoTemplate.updateMulti(query(byExample(probe)), new Update()
                        .set(COUNTRY_ID.name, countryId), City.class)
                .flatMap(updateResult -> {
                    long modifiedCount = updateResult.getModifiedCount();

                    LOGGER.info("matchedCount = {}, modifiedCount = {}, wasAcknowledged = {}",
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
        LOGGER.info("countryId = {}, stateId = {}", countryId, stateId);
        if (isInvalidIdentity(countryId) || isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        Area probe = new Area();
        probe.setStateId(stateId);

        return reactiveMongoTemplate.updateMulti(query(byExample(probe)), new Update()
                        .set(COUNTRY_ID.name, countryId), Area.class)
                .flatMap(updateResult -> {
                    long modifiedCount = updateResult.getModifiedCount();

                    LOGGER.info("matchedCount = {}, modifiedCount = {}, wasAcknowledged = {}",
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
    public Mono<State> getStateById(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return stateRepository.findById(id);
    }

    /**
     * select states by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public Mono<List<State>> selectStateByCountryId(Long countryId) {
        if (isInvalidIdentity(countryId))
            return error(() -> new BlueException(INVALID_IDENTITY));

        State probe = new State();
        probe.setCountryId(countryId);

        return stateRepository.findAll(Example.of(probe), by(Sort.Order.asc(NAME.name)))
                .collectList();
    }

    /**
     * select states by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<State>> selectStateByIds(List<Long> ids) {
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(stateRepository::findAllById)
                .reduce(Flux::concat)
                .flatMap(Flux::collectList);
    }

    /**
     * get state info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<StateInfo> getStateInfoById(Long id) {
        return fromFuture(STATE_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select states mono by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public Mono<List<StateInfo>> selectStateInfoByCountryId(Long countryId) {
        return fromFuture(STATES_BY_COUNTRY_ID_GETTER.apply(countryId));
    }

    /**
     * select state info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, StateInfo>> selectStateInfoByIds(List<Long> ids) {
        return fromFuture(CACHE_STATES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * get state region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<StateRegion> getStateRegionById(Long id) {
        return STATE_REGION_GETTER.apply(id);
    }

    /**
     * select state regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, StateRegion>> selectStateRegionByIds(List<Long> ids) {
        return fromFuture(STATE_REGIONS_GETTER.apply(ids));
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
    public Mono<List<State>> selectStateByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
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
    public Mono<Long> countStateByQuery(Query query) {
        LOGGER.info("query = {}", query);
        return reactiveMongoTemplate.count(query, State.class);
    }

    /**
     * select state info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<StateInfo>> selectStatePageByPageAndCondition(PageModelRequest<StateCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(
                selectStateByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countStateByQuery(query)
        ).flatMap(tuple2 -> {
            List<State> states = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(states) ?
                    just(new PageModelResponse<>(STATES_2_STATE_INFOS_CONVERTER.apply(states), count))
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

}
