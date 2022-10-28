package com.blue.base.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
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
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.base.constant.BaseColumnName.*;
import static com.blue.base.converter.BaseModelConverters.CITIES_2_CITY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.CITY_2_CITY_INFO_CONVERTER;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
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
 * city service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = Loggers.getLogger(CityServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private StateService stateService;

    private CountryService countryService;

    private CityRepository cityRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public CityServiceImpl(BlueIdentityProcessor blueIdentityProcessor, StateService stateService, CountryService countryService,
                           CityRepository cityRepository, ReactiveMongoTemplate reactiveMongoTemplate,
                           ExecutorService executorService, CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.stateService = stateService;
        this.countryService = countryService;
        this.cityRepository = cityRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;

        idCityCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));

        stateIdCitiesCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));

        idRegionCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private Cache<Long, CityInfo> idCityCache;

    private Cache<Long, List<CityInfo>> stateIdCitiesCache;

    private Cache<Long, CityRegion> idRegionCache;

    private final Function<Long, CityInfo> DB_CITY_GETTER = id ->
            this.getCityById(id).map(CITY_2_CITY_INFO_CONVERTER).orElse(null);

    private final Function<Long, CityInfo> DB_CITY_GETTER_WITH_ASSERT = id ->
            this.getCityById(id).map(CITY_2_CITY_INFO_CONVERTER)
                    .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

    private final Function<Long, List<CityInfo>> DB_CITIES_BY_STATE_ID_GETTER = sid ->
            CITIES_2_CITY_INFOS_CONVERTER.apply(this.selectCityByStateId(sid));

    private final Function<Long, Optional<CityInfo>> CITY_OPT_BY_ID_GETTER = id ->
            ofNullable(idCityCache.get(id, DB_CITY_GETTER));

    private final Function<Long, CityInfo> CITY_BY_ID_WITH_ASSERT_GETTER = id ->
            idCityCache.get(id, DB_CITY_GETTER_WITH_ASSERT);

    private final Function<Long, List<CityInfo>> CITIES_BY_STATE_ID_GETTER = sid ->
            stateIdCitiesCache.get(sid, DB_CITIES_BY_STATE_ID_GETTER);

    private final Function<List<Long>, Map<Long, CityInfo>> CACHE_CITIES_BY_IDS_GETTER = ids -> {
        if (isEmpty(ids))
            return emptyMap();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idCityCache.getAll(l, is -> cityRepository.findAllById(l)
                                        .flatMap(c -> just(CITY_2_CITY_INFO_CONVERTER.apply(c)))
                                        .collectList().toFuture().join()
                                        .parallelStream()
                                        .collect(toMap(CityInfo::getId, i -> i, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    private final Function<Long, CityRegion> CITY_REGION_GETTER = id -> {
        if (isValidIdentity(id))
            return idRegionCache.get(id, i ->
                    just(idCityCache.get(i, DB_CITY_GETTER_WITH_ASSERT))
                            .flatMap(cityInfo ->
                                    zip(
                                            countryService.getCountryInfoMonoById(cityInfo.getCountryId()),
                                            stateService.getStateInfoMonoById(cityInfo.getStateId())
                                    ).flatMap(tuple2 ->
                                            just(new CityRegion(id, tuple2.getT1(), tuple2.getT2(), cityInfo))))
                            .toFuture().join()
            );

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<List<Long>, Map<Long, CityRegion>> CITY_REGIONS_GETTER = ids -> {
        if (isEmpty(ids))
            return emptyMap();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idRegionCache.getAll(l, is -> {
                            Collection<CityInfo> cityInfos = CACHE_CITIES_BY_IDS_GETTER.apply(ids).values();
                            int size = cityInfos.size();
                            List<Long> countryIds = new ArrayList<>(size);
                            List<Long> stateIds = new ArrayList<>(size);

                            for (CityInfo ci : cityInfos) {
                                countryIds.add(ci.getCountryId());
                                stateIds.add(ci.getStateId());
                            }

                            return zip(
                                    stateService.selectStateInfoMonoByIds(stateIds),
                                    countryService.selectCountryInfoMonoByIds(countryIds)
                            ).flatMap(tuple2 ->
                                    just(cityInfos.parallelStream().map(ai -> new CityRegion(ai.getId(), tuple2.getT2().get(ai.getCountryId()),
                                                    tuple2.getT1().get(ai.getStateId()), ai))
                                            .collect(toMap(CityRegion::getCityId, ar -> ar, (a, b) -> a)))
                            ).toFuture().join();
                        }).entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
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

        State state = stateService.getStateById(stateId)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
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

        City probe = new City();
        probe.setStateId(p.getStateId());
        probe.setName(p.getName());

        List<City> cities = ofNullable(cityRepository.findAll(Example.of(probe))
                .collectList().toFuture().join())
                .orElseGet(Collections::emptyList);

        if (cities.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        City city = cityRepository.findById(id).toFuture().join();
        if (isNull(city))
            throw new BlueException(DATA_NOT_EXIST);

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
            State state = stateService.getStateById(stateId)
                    .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

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

        if (c == null)
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
        LOGGER.info("Mono<CityInfo> insertCity(CityInsertParam cityInsertParam), cityInsertParam = {}", cityInsertParam);

        INSERT_ITEM_VALIDATOR.accept(cityInsertParam);
        City city = CITY_INSERT_PARAM_2_CITY_CONVERTER.apply(cityInsertParam);

        return cityRepository.insert(city)

                .map(CITY_2_CITY_INFO_CONVERTER)
                .doOnSuccess(ci -> {
                    LOGGER.info("ci = {}", ci);
                    invalidCache();
                });
    }

    /**
     * update city
     *
     * @param cityUpdateParam
     * @return
     */
    @Override
    public Mono<CityInfo> updateCity(CityUpdateParam cityUpdateParam) {
        LOGGER.info("Mono<CityInfo> updateCity(CityUpdateParam cityUpdateParam), cityUpdateParam = {}", cityUpdateParam);

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
    public Mono<CityInfo> deleteCity(Long id) {
        LOGGER.info("Mono<CityInfo> deleteCity(Long id), id = {}", id);
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
                            .then(just(CITY_2_CITY_INFO_CONVERTER.apply(city)))
                            .doOnSuccess(ci -> {
                                LOGGER.info("ci = {}", ci);
                                invalidCache();
                            });
                });
    }

    /**
     * invalid cache
     */
    @Override
    public void invalidCache() {
        idCityCache.invalidateAll();
        stateIdCitiesCache.invalidateAll();
        idRegionCache.invalidateAll();
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
        LOGGER.info("Mono<Long> updateCountryIdAndStateIdOfAreaByCityId(Long countryId, Long stateId, Long cityId), countryId = {}, stateId = {}, cityId = {}",
                countryId, stateId, cityId);
        if (isInvalidIdentity(countryId) || isInvalidIdentity(stateId) || isInvalidIdentity(cityId))
            throw new BlueException(INVALID_IDENTITY);

        Area probe = new Area();
        probe.setCityId(cityId);

        return reactiveMongoTemplate.updateMulti(query(byExample(probe)), new Update()
                        .set(COUNTRY_ID.name, countryId).set(STATE_ID.name, stateId), Area.class)
                .flatMap(updateResult -> {
                    long modifiedCount = updateResult.getModifiedCount();

                    LOGGER.info("Mono<Long> updateCountryIdAndStateIdOfAreaByCityId(Long countryId, Long stateId, Long cityId), matchedCount = {}, modifiedCount = {}, wasAcknowledged = {}",
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
    public Optional<City> getCityById(Long id) {
        return ofNullable(cityRepository.findById(id).toFuture().join());
    }

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<City> selectCityByStateId(Long stateId) {
        if (isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        City probe = new City();
        probe.setStateId(stateId);

        return cityRepository.findAll(Example.of(probe), by(Sort.Order.asc(NAME.name)))
                .collectList().toFuture().join();
    }

    /**
     * select cities by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<City> selectCityByIds(List<Long> ids) {
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream()
                .map(l -> cityRepository.findAllById(l)
                        .collectList().toFuture().join())
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * get city info opt by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<CityInfo> getCityInfoOptById(Long id) {
        return CITY_OPT_BY_ID_GETTER.apply(id);
    }

    /**
     * get city info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CityInfo getCityInfoById(Long id) {
        return CITY_BY_ID_WITH_ASSERT_GETTER.apply(id);
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityInfo> getCityInfoMonoById(Long id) {
        return just(CITY_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select city info by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<CityInfo> selectCityInfoByStateId(Long stateId) {
        return CITIES_BY_STATE_ID_GETTER.apply(stateId);
    }

    /**
     * select city info mono by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        return just(CITIES_BY_STATE_ID_GETTER.apply(stateId)).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select city info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids) {
        return CACHE_CITIES_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select city info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids) {
        return just(CACHE_CITIES_BY_IDS_GETTER.apply(ids));
    }

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    @Override
    public CityRegion getCityRegionById(Long id) {
        return CITY_REGION_GETTER.apply(id);
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityRegion> getCityRegionMonoById(Long id) {
        return just(CITY_REGION_GETTER.apply(id));
    }

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, CityRegion> selectCityRegionByIds(List<Long> ids) {
        return CITY_REGIONS_GETTER.apply(ids);
    }

    /**
     * get regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, CityRegion>> selectCityRegionMonoByIds(List<Long> ids) {
        return just(CITY_REGIONS_GETTER.apply(ids));
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
    public Mono<List<City>> selectCityMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<City>> selectCityMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
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
    public Mono<Long> countCityMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countCityMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, City.class);
    }

    /**
     * select city info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<CityInfo>> selectCityPageMonoByPageAndCondition(PageModelRequest<CityCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<CityInfo>> selectCityPageMonoByPageAndCondition(PageModelRequest<CityCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(
                selectCityMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countCityMonoByQuery(query)
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
