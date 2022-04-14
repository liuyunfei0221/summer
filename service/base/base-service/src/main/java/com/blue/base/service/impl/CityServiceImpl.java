package com.blue.base.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.api.model.CityRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.template.CityRepository;
import com.blue.base.service.inter.CityService;
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

/**
 * city service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class CityServiceImpl implements CityService {

    private StateService stateService;

    private CountryService countryService;

    private CityRepository cityRepository;

    public CityServiceImpl(StateService stateService, CountryService countryService, ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, CityRepository cityRepository) {
        this.stateService = stateService;
        this.countryService = countryService;
        this.cityRepository = cityRepository;

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

        City city = new City();
        city.setStateId(stateId);

        return cityRepository.findAll(Example.of(city), Sort.by("name"))
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
     * select city infos by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<CityInfo> selectCityInfoByStateId(Long stateId) {
        return CITIES_BY_STATE_ID_GETTER.apply(stateId);
    }

    /**
     * select city infos mono by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        return just(CITIES_BY_STATE_ID_GETTER.apply(stateId)).switchIfEmpty(just(emptyList()));
    }

    /**
     * select city infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, CityInfo> selectCityInfoByIds(List<Long> ids) {
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
     * invalid city infos
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
