package com.blue.base.service.impl;

import com.blue.base.api.model.CityInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.mapper.CityMapper;
import com.blue.base.service.inter.CityService;
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

    private CityMapper cityMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public CityServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, CityMapper cityMapper) {
        this.cityMapper = cityMapper;

        STATE_ID_CITIES_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        ID_CITY_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static Cache<Long, List<CityInfo>> STATE_ID_CITIES_CACHE;

    private static Cache<Long, CityInfo> ID_CITY_CACHE;


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
        LOGGER.info("Function<List<Long>, List<CityInfo>> CACHE_CITIES_BY_IDS_GETTER, ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        ID_CITY_CACHE.getAll(l, is -> cityMapper.selectByIds(l)
                                        .parallelStream()
                                        .map(CITY_2_CITY_INFO_CONVERTER)
                                        .collect(toMap(CityInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
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
        return ofNullable(ID_CITY_CACHE.get(id, DB_CITY_GETTER));
    }

    /**
     * get city info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CityInfo getCityInfoById(Long id) {
        return ID_CITY_CACHE.get(id, DB_CITY_GETTER_WITH_ASSERT);
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityInfo> getCityInfoMonoById(Long id) {
        return just(ID_CITY_CACHE.get(id, DB_CITY_GETTER_WITH_ASSERT));
    }

    /**
     * select city infos by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<CityInfo> selectCityInfoByStateId(Long stateId) {
        return STATE_ID_CITIES_CACHE.get(stateId, DB_CITIES_GETTER);
    }

    /**
     * select city infos mono by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        return just(STATE_ID_CITIES_CACHE.get(stateId, DB_CITIES_GETTER)).switchIfEmpty(just(emptyList()));
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
     * invalid city infos
     *
     * @return
     */
    @Override
    public void invalidCityInfosCache() {
        LOGGER.info("void invalidStateInfosCache()");

        STATE_ID_CITIES_CACHE.invalidateAll();
        ID_CITY_CACHE.invalidateAll();
    }

}
