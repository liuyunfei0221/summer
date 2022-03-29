package com.blue.base.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.mapper.CityMapper;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
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
import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.BlueNumericalValue.MAX_SERVICE_SELECT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.converter.BaseModelConverters.CITIES_2_CITY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.CITY_2_CITY_INFO_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Collections.emptyMap;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * city service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CityServiceImpl implements CityService {

    private static final Logger LOGGER = getLogger(CityServiceImpl.class);

    private StateService stateService;

    private CountryService countryService;

    private CityMapper cityMapper;

//    private CityRepository cityRepository;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CityServiceImpl(StateService stateService, CountryService countryService, ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, CityMapper cityMapper) {
        this.stateService = stateService;
        this.countryService = countryService;
        this.cityMapper = cityMapper;

        stateIdCitiesCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idCityCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idRegionCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private final Cache<Long, List<CityInfo>> stateIdCitiesCache;

    private Cache<Long, CityInfo> idCityCache;

    private Cache<Long, CityRegion> idRegionCache;


    private final Function<Long, List<CityInfo>> DB_CITIES_GETTER = sid -> {
        LOGGER.info("DB_CITIES_GETTER, sid = {}", sid);
        return CITIES_2_CITY_INFOS_CONVERTER.apply(
                this.selectCityByStateId(sid).stream().sorted(Comparator.comparing(City::getName)).collect(toList()));
    };

    private final Function<Long, CityInfo> DB_CITY_GETTER = id -> {
        LOGGER.info("Function<Long, CityInfo> DB_CITY_GETTER, id = {}", id);
        return this.getCityById(id).map(CITY_2_CITY_INFO_CONVERTER).orElse(null);
    };

    private final Function<Long, CityInfo> DB_CITY_GETTER_WITH_ASSERT = id -> {
        LOGGER.info("Function<Long, CityInfo> DB_CITY_GETTER_WITH_ASSERT, id = {}", id);
        return this.getCityById(id).map(CITY_2_CITY_INFO_CONVERTER)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    };

    private final Function<List<Long>, Map<Long, CityInfo>> CACHE_CITIES_BY_IDS_GETTER = ids -> {
        LOGGER.info("Function<List<Long>, Map<Long, CityInfo>> CACHE_CITIES_BY_IDS_GETTER, ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idCityCache.getAll(l, is -> cityMapper.selectByIds(l)
                                        .parallelStream()
                                        .map(CITY_2_CITY_INFO_CONVERTER)
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
        LOGGER.info("Function<List<Long>, Map<Long, CityRegion>> CITY_REGIONS_GETTER, ids = {}", ids);

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
     * get city by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<City> getCityById(Long id) {
        LOGGER.info("Optional<City> getCityById(Long id), id = {}", id);

        return ofNullable(cityMapper.selectByPrimaryKey(id));
    }

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<City> selectCityByStateId(Long stateId) {
        LOGGER.info("List<City> selectCityByStateId(Long stateId), stateId = {}", stateId);

        if (isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        return cityMapper.selectByCountryId(stateId);
    }

    /**
     * select cities by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<City> selectCityByIds(List<Long> ids) {
        LOGGER.info("List<City> selectCityByIds(List<Long> ids), ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyList();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(cityMapper::selectByIds)
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
        return ofNullable(idCityCache.get(id, DB_CITY_GETTER));
    }

    /**
     * get city info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CityInfo getCityInfoById(Long id) {
        return idCityCache.get(id, DB_CITY_GETTER_WITH_ASSERT);
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityInfo> getCityInfoMonoById(Long id) {
        return just(idCityCache.get(id, DB_CITY_GETTER_WITH_ASSERT));
    }

    /**
     * select city infos by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<CityInfo> selectCityInfoByStateId(Long stateId) {
        return stateIdCitiesCache.get(stateId, DB_CITIES_GETTER);
    }

    /**
     * select city infos mono by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        return just(stateIdCitiesCache.get(stateId, DB_CITIES_GETTER)).switchIfEmpty(just(emptyList()));
    }

    /**
     * select city infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids) {
        LOGGER.info("Map<Long, CityInfo> selectStateInfoByIds(List<Long> ids), ids = {}", ids);

        return CACHE_CITIES_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select city infos mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, CityInfo>> selectCityInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<Map<Long, CityInfo>> selectStateInfoMonoByIds(List<Long> ids), ids = {}", ids);

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
        return idRegionCache.get(id, CITY_REGION_GETTER);
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityRegion> getCityRegionMonoById(Long id) {
        return just(idRegionCache.get(id, CITY_REGION_GETTER));
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
     * invalid city infos
     *
     * @return
     */
    @Override
    public void invalidCityInfosCache() {
        LOGGER.info("void invalidStateInfosCache()");

        stateIdCitiesCache.invalidateAll();
        idCityCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

}
