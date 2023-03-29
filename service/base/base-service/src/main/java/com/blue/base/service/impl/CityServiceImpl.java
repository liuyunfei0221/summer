package com.blue.base.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.api.model.CountryInfo;
import com.blue.base.api.model.StateInfo;
import com.blue.base.config.deploy.CaffeineDeploy;
import com.blue.base.model.CityCondition;
import com.blue.base.model.CityInsertParam;
import com.blue.base.model.CityUpdateParam;
import com.blue.base.repository.entity.Area;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.CityRepository;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.slf4j.Logger;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

import static com.blue.base.constant.BaseColumnName.*;
import static com.blue.base.converter.BaseModelConverters.CITIES_2_CITY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.CITY_2_CITY_INFO_CONVERTER;
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
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;

/**
 * city service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = getLogger(CityServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private ExecutorService executorService;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private StateService stateService;

    private CountryService countryService;

    private CityRepository cityRepository;

    public CityServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ExecutorService executorService, ReactiveMongoTemplate reactiveMongoTemplate,
                           StateService stateService, CountryService countryService, CityRepository cityRepository, CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.executorService = executorService;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.stateService = stateService;
        this.countryService = countryService;
        this.cityRepository = cityRepository;

        idCityCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));

        stateIdCitiesCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getStateMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));

        idRegionCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_WRITE, this.executorService));
    }

    private AsyncCache<Long, CityInfo> idCityCache;

    private AsyncCache<Long, List<CityInfo>> stateIdCitiesCache;

    private AsyncCache<Long, CityRegion> idRegionCache;

    private final BiFunction<Long, Executor, CompletableFuture<CityInfo>> DB_CITY_WITH_ASSERT_GETTER = (id, executor) -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return this.getCityById(id).map(CITY_2_CITY_INFO_CONVERTER)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture();
    };

    private final BiFunction<Long, Executor, CompletableFuture<List<CityInfo>>> DB_CITIES_BY_STATE_ID_GETTER = (sid, executor) -> {
        if (isInvalidIdentity(sid))
            throw new BlueException(INVALID_IDENTITY);

        return this.selectCityByStateId(sid).map(CITIES_2_CITY_INFOS_CONVERTER)
                .switchIfEmpty(defer(() -> just(emptyList()))).toFuture();
    };

    private final Function<Long, CompletableFuture<CityInfo>> CITY_BY_ID_WITH_ASSERT_GETTER = id -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return idCityCache.get(id, DB_CITY_WITH_ASSERT_GETTER);
    };

    private final Function<Long, CompletableFuture<List<CityInfo>>> CITIES_BY_STATE_ID_GETTER = sid -> {
        if (isInvalidIdentity(sid))
            throw new BlueException(INVALID_IDENTITY);

        return stateIdCitiesCache.get(sid, DB_CITIES_BY_STATE_ID_GETTER);
    };

    private final Function<List<Long>, CompletableFuture<Map<Long, CityInfo>>> CACHE_CITIES_BY_IDS_GETTER = ids -> {
        if (isEmpty(ids))
            return supplyAsync(Collections::emptyMap, this.executorService);
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return idCityCache.getAll(ids, (is, executor) -> fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(l -> cityRepository.findAllById(ids)
                        .map(CITY_2_CITY_INFO_CONVERTER))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList)
                .map(l -> l.stream().collect(toMap(CityInfo::getId, identity(), (a, b) -> a)))
                .toFuture());
    };

    private final Function<Long, Mono<CityRegion>> CITY_REGION_GETTER = id ->
            isValidIdentity(id) ?
                    fromFuture(CITY_BY_ID_WITH_ASSERT_GETTER.apply(id))
                            .flatMap(cityInfo ->
                                    zip(
                                            countryService.getCountryInfoById(cityInfo.getCountryId()),
                                            stateService.getStateInfoById(cityInfo.getStateId())
                                    ).map(tuple2 ->
                                            new CityRegion(id, tuple2.getT1(), tuple2.getT2(), cityInfo)))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));

    private final Function<List<Long>, CompletableFuture<Map<Long, CityRegion>>> CITY_REGIONS_GETTER = ids -> {
        if (isEmpty(ids))
            return supplyAsync(Collections::emptyMap, this.executorService);
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return idRegionCache.getAll(ids, (is, executor) ->
                fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                        .flatMap(l ->
                                fromFuture(CACHE_CITIES_BY_IDS_GETTER.apply(l))
                                        .flatMap(cityMap ->
                                                zip(stateService.selectStateInfoByIds(cityMap.values().stream().map(CityInfo::getStateId).distinct().collect(toList())),
                                                        countryService.selectCountryInfoByIds(cityMap.values().stream().map(CityInfo::getCountryId).distinct().collect(toList()))
                                                ).map(tuple2 -> {
                                                    Map<Long, StateInfo> stateMap = tuple2.getT1();
                                                    Map<Long, CountryInfo> countryMap = tuple2.getT2();

                                                    return cityMap.values().stream().map(ci -> new CityRegion(ci.getId(), countryMap.get(ci.getCountryId()),
                                                                    stateMap.get(ci.getStateId()), ci))
                                                            .collect(toMap(CityRegion::getCityId, identity(), (a, b) -> a));
                                                })))
                        .map(m -> fromIterable(m.values()))
                        .reduce(Flux::concat)
                        .flatMap(f -> f.collectMap(CityRegion::getCityId, identity()))
                        .toFuture());
    };

    private final Consumer<CityInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        City probe = new City();
        probe.setStateId(p.getStateId());
        probe.setName(p.getName());

        if (ofNullable(cityRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(CITY_ALREADY_EXIST);
    };

    public final Function<CityInsertParam, City> CITY_INSERT_PARAM_2_CITY_CONVERTER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long stateId = p.getStateId();

        State state = stateService.getStateById(stateId).toFuture().join();
        if (isNull(state))
            throw new BlueException(DATA_NOT_EXIST);

        Long stamp = TIME_STAMP_GETTER.get();

        City city = new City();

        city.setId(blueIdentityProcessor.generate(City.class));
        city.setCountryId(state.getCountryId());
        city.setStateId(stateId);
        city.setName(p.getName());
        city.setStatus(VALID.status);
        city.setCreateTime(stamp);
        city.setUpdateTime(stamp);

        return city;
    };

    private final Function<CityUpdateParam, City> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        City city = cityRepository.findById(id).toFuture().join();
        if (isNull(city))
            throw new BlueException(DATA_NOT_EXIST);

        City probe = new City();
        probe.setStateId(p.getStateId());
        probe.setName(p.getName());

        List<City> cities = ofNullable(cityRepository.findAll(Example.of(probe))
                .collectList().toFuture().join())
                .orElseGet(Collections::emptyList);

        if (cities.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        return city;
    };

    public final BiConsumer<CityUpdateParam, City> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);
        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        Long stateId = p.getStateId();
        if (stateId != null && !stateId.equals(t.getStateId())) {
            State state = stateService.getStateById(stateId).toFuture().join();
            if (isNull(state))
                throw new BlueException(DATA_NOT_EXIST);

            t.setCountryId(state.getCountryId());
            t.setStateId(stateId);

            alteration = true;
        }

        String name = p.getName();
        if (isNotBlank(name) && !name.equals(t.getName())) {
            t.setName(name);
            alteration = true;
        }

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Function<CityCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (isNull(c))
            return query;

        City probe = new City();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(c.getStateId()).ifPresent(probe::setStateId);
        ofNullable(c.getStatus()).ifPresent(probe::setStatus);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));

        query.with(by(Sort.Order.asc(NAME.name)));

        return query;
    };

    /**
     * insert city
     *
     * @param cityInsertParam
     * @return
     */
    @Override
    public Mono<CityInfo> insertCity(CityInsertParam cityInsertParam) {
        LOGGER.info("cityInsertParam = {}", cityInsertParam);

        INSERT_ITEM_VALIDATOR.accept(cityInsertParam);
        City city = CITY_INSERT_PARAM_2_CITY_CONVERTER.apply(cityInsertParam);

        return cityRepository.insert(city)
                .map(CITY_2_CITY_INFO_CONVERTER);
    }

    /**
     * update city
     *
     * @param cityUpdateParam
     * @return
     */
    @Override
    public Mono<CityInfo> updateCity(CityUpdateParam cityUpdateParam) {
        LOGGER.info("cityUpdateParam = {}", cityUpdateParam);

        City city = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(cityUpdateParam);

        Long originalCountryId = city.getCountryId();
        Long originalStateId = city.getStateId();

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(cityUpdateParam, city);

        return cityRepository.save(city)
                .map(CITY_2_CITY_INFO_CONVERTER)
                .doOnSuccess(ci -> {
                    LOGGER.info("ci = {}", ci);

                    Long destCountryId = city.getCountryId();
                    Long destStateId = city.getStateId();

                    if (!originalCountryId.equals(destCountryId) || !originalStateId.equals(destStateId)) {
                        Long modifiedCount = updateCountryIdAndStateIdOfAreaByCityId(destCountryId, destStateId, city.getId()).toFuture().join();
                        LOGGER.info("modifiedCount = {}", modifiedCount);
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
    public Mono<CityInfo> deleteCity(Long id) {
        LOGGER.info("id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return cityRepository.findById(id)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(city -> {
                    Area probe = new Area();
                    probe.setCityId(id);

                    Query query = new Query();
                    query.addCriteria(byExample(probe));

                    return reactiveMongoTemplate.count(query, Area.class)
                            .flatMap(areaCount ->
                                    areaCount <= 0L ?
                                            cityRepository.delete(city)
                                            :
                                            error(new BlueException(REGION_DATA_STILL_USED))
                            )
                            .then(just(CITY_2_CITY_INFO_CONVERTER.apply(city)));
                });
    }

    /**
     * invalid cache
     */
    @Override
    public void invalidCache() {
        idCityCache.synchronous().invalidateAll();
        stateIdCitiesCache.synchronous().invalidateAll();
        idRegionCache.synchronous().invalidateAll();
    }

    /**
     * a city's stateId was changed
     *
     * @param countryId
     * @param stateId
     * @param cityId
     * @return
     */
    @Override
    public Mono<Long> updateCountryIdAndStateIdOfAreaByCityId(Long countryId, Long stateId, Long cityId) {
        LOGGER.info("countryId = {}, stateId = {}, cityId = {}",
                countryId, stateId, cityId);
        if (isInvalidIdentity(countryId) || isInvalidIdentity(stateId) || isInvalidIdentity(cityId))
            throw new BlueException(INVALID_IDENTITY);

        Area probe = new Area();
        probe.setCityId(cityId);

        return reactiveMongoTemplate.updateMulti(query(byExample(probe)), new Update()
                        .set(COUNTRY_ID.name, countryId).set(STATE_ID.name, stateId), Area.class)
                .flatMap(updateResult -> {
                    long modifiedCount = updateResult.getModifiedCount();

                    LOGGER.info("matchedCount = {}, modifiedCount = {}, wasAcknowledged = {}",
                            countryId, stateId, updateResult.getMatchedCount(), modifiedCount, updateResult.wasAcknowledged());

                    return just(modifiedCount);
                });
    }

    /**
     * get city by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<City> getCityById(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return cityRepository.findById(id);
    }

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<City>> selectCityByStateId(Long stateId) {
        if (isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        City probe = new City();
        probe.setStateId(stateId);

        return cityRepository.findAll(Example.of(probe), by(Sort.Order.asc(NAME.name)))
                .collectList();
    }

    /**
     * select cities by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<City>> selectCityByIds(List<Long> ids) {
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(l -> cityRepository.findAllById(l))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList);
    }

    /**
     * get city info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityInfo> getCityInfoById(Long id) {
        return fromFuture(CITY_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select city info by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoByStateId(Long stateId) {
        return fromFuture(CITIES_BY_STATE_ID_GETTER.apply(stateId));
    }

    /**
     * select city info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, CityInfo>> selectCityInfoByIds(List<Long> ids) {
        return fromFuture(CACHE_CITIES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityRegion> getCityRegionById(Long id) {
        return CITY_REGION_GETTER.apply(id);
    }

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, CityRegion>> selectCityRegionByIds(List<Long> ids) {
        return fromFuture(CITY_REGIONS_GETTER.apply(ids));
    }

    /**
     * select city by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<City>> selectCityByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("limit = {}, rows = {}, query = {}", limit, rows, query);
        if (limit == null || limit < 0 || rows == null || rows == 0)
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, City.class).collectList();
    }

    /**
     * count city by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countCityByQuery(Query query) {
        LOGGER.info("query = {}", query);
        return reactiveMongoTemplate.count(query, City.class);
    }

    /**
     * select city info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<CityInfo>> selectCityPageByPageAndCondition(PageModelRequest<CityCondition> pageModelRequest) {
        LOGGER.info("pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(
                selectCityByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countCityByQuery(query)
        ).flatMap(tuple2 -> {
            List<City> cities = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(cities) ?
                    just(new PageModelResponse<>(CITIES_2_CITY_INFOS_CONVERTER.apply(cities), count))
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

}
