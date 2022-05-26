package com.blue.base.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.CityInsertParam;
import com.blue.base.model.CityUpdateParam;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.entity.State;
import com.blue.base.repository.template.CityRepository;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.mongo.component.CollectionGetter;
import com.github.benmanes.caffeine.cache.Cache;
import com.mongodb.client.model.UpdateOptions;
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
import static com.blue.base.converter.BaseModelConverters.CITIES_2_CITY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.CITY_2_CITY_INFO_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Updates.combine;
import static com.mongodb.client.model.Updates.set;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.*;

/**
 * city service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = Loggers.getLogger(CityServiceImpl.class);

    private final BlueIdentityProcessor blueIdentityProcessor;

    private StateService stateService;

    private CountryService countryService;

    private CityRepository cityRepository;

    private final CollectionGetter collectionGetter;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CityServiceImpl(BlueIdentityProcessor blueIdentityProcessor, StateService stateService, CountryService countryService,
                           ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, CityRepository cityRepository, CollectionGetter collectionGetter) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.stateService = stateService;
        this.countryService = countryService;
        this.cityRepository = cityRepository;
        this.collectionGetter = collectionGetter;

        idCityCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        stateIdCitiesCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idRegionCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
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
            CITIES_2_CITY_INFOS_CONVERTER.apply(
                    this.selectCityByStateId(sid).stream().sorted(Comparator.comparing(City::getName)).collect(toList()));

    private final Function<Long, Optional<CityInfo>> CITY_OPT_BY_ID_GETTER = id ->
            ofNullable(idCityCache.get(id, DB_CITY_GETTER));

    private final Function<Long, CityInfo> CITY_BY_ID_WITH_ASSERT_GETTER = id ->
            idCityCache.get(id, DB_CITY_GETTER_WITH_ASSERT);

    private final Function<Long, List<CityInfo>> CITIES_BY_STATE_ID_GETTER = sid ->
            stateIdCitiesCache.get(sid, DB_CITIES_BY_STATE_ID_GETTER);

    private final Function<List<Long>, Map<Long, CityInfo>> CACHE_CITIES_BY_IDS_GETTER = ids -> {
        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idCityCache.getAll(l, is -> cityRepository.findAllById(l)
                                        .flatMap(c -> just(CITY_2_CITY_INFO_CONVERTER.apply(c)))
                                        .collectList().toFuture().join()
                                        .parallelStream()
                                        .collect(toMap(CityInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    private final Function<Long, CityRegion> CITY_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
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
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<List<Long>, Map<Long, CityRegion>> CITY_REGIONS_GETTER = ids -> {
        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

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

    /**
     * invalid all chche
     */
    private void invalidCache() {
        idCityCache.invalidateAll();
        stateIdCitiesCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

    /**
     * is a city exist?
     */
    private final Consumer<CityInsertParam> INSERT_CITY_VALIDATOR = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        City probe = new City();
        probe.setStateId(param.getStateId());
        probe.setName(param.getName());

        Long count = ofNullable(cityRepository.count(Example.of(probe)).toFuture().join()).orElse(0L);

        if (count > 0L)
            throw new BlueException(CITY_ALREADY_EXIST);
    };

    /**
     * city insert param -> city
     */
    public final Function<CityInsertParam, City> CITY_INSERT_PARAM_2_AREA_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long stateId = param.getStateId();

        State state = stateService.getStateById(stateId)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
        Long stamp = TIME_STAMP_GETTER.get();

        City city = new City();

        city.setCountryId(state.getCountryId());
        city.setStateId(stateId);
        city.setName(param.getName());
        city.setStatus(VALID.status);
        city.setCreateTime(stamp);
        city.setUpdateTime(stamp);

        return city;
    };

    /**
     * is a city exist?
     */
    private final Function<CityUpdateParam, City> UPDATE_CITY_VALIDATOR_AND_ORIGIN_RETURNER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long id = param.getId();

        City probe = new City();
        probe.setStateId(param.getStateId());
        probe.setName(param.getName());

        List<City> cities = ofNullable(cityRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList);

        if (cities.stream().anyMatch(c -> !id.equals(c.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        City city = cityRepository.findById(id).toFuture().join();
        if (isNull(city))
            throw new BlueException(DATA_NOT_EXIST);

        return city;
    };

    /**
     * for city
     */
    public final BiFunction<CityUpdateParam, City, Boolean> UPDATE_CITY_VALIDATOR = (p, t) -> {
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

        return alteration;
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

        INSERT_CITY_VALIDATOR.accept(cityInsertParam);
        City city = CITY_INSERT_PARAM_2_AREA_CONVERTER.apply(cityInsertParam);

        city.setId(blueIdentityProcessor.generate(City.class));

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

        City city = UPDATE_CITY_VALIDATOR_AND_ORIGIN_RETURNER.apply(cityUpdateParam);

        Boolean changed = UPDATE_CITY_VALIDATOR.apply(cityUpdateParam, city);
        if (changed != null && !changed)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        return cityRepository.save(city)
                .map(CITY_2_CITY_INFO_CONVERTER)
                .doOnSuccess(ci -> {
                    LOGGER.info("ci = {}", ci);
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

        City city = cityRepository.findById(id).toFuture().join();
        if (isNull(city))
            throw new BlueException(DATA_NOT_EXIST);

        return cityRepository.delete(city)
                .then(just(CITY_2_CITY_INFO_CONVERTER.apply(city)))
                .doOnSuccess(ci -> {
                    LOGGER.info("ci = {}", ci);
                    invalidCache();
                });
    }

    private static final String COUNTRY_ID_COLUMN_NAME = "countryId";
    private static final String STATE_ID_COLUMN_NAME = "stateId";
    private static final String CITY_ID_COLUMN_NAME = "cityId";

    private static final UpdateOptions OPTIONS = new UpdateOptions().upsert(true);

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

        return collectionGetter.getCollectionReact(City.class)
                .flatMap(collection ->
                        from(collection.updateMany(eq(CITY_ID_COLUMN_NAME, cityId),
                                combine(set(COUNTRY_ID_COLUMN_NAME, countryId),
                                        set(STATE_ID_COLUMN_NAME, stateId)), OPTIONS)))
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

        return cityRepository.findAll(Example.of(probe), Sort.by(Sort.Order.asc("name")))
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
        if (isInvalidIdentities(ids) || ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

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
     * invalid city info
     *
     * @return
     */
    @Override
    public void invalidCityInfosCache() {
        stateIdCitiesCache.invalidateAll();
        idCityCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

}
