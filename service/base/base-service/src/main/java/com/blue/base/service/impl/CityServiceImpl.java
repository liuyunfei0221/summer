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
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.ArrayAllocator.allotByMax;
import static com.blue.base.common.base.BlueChecker.isValidIdentities;
import static com.blue.base.constant.base.BlueNumericalValue.DB_SELECT;
import static com.blue.base.constant.base.ResponseElement.DATA_NOT_EXIST;
import static com.blue.base.converter.BaseModelConverters.CITIES_2_CITY_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.CITY_2_CITY_INFO_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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

    private final Function<List<Long>, List<CityInfo>> CACHE_CITIES_BY_IDS_GETTER = ids -> {
        LOGGER.info("Function<List<Long>, List<CityInfo>> CACHE_CITIES_BY_IDS_GETTER, ids = {}", ids);

        return isValidIdentities(ids) ? allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        ID_CITY_CACHE.getAll(l, is -> cityMapper.selectByIds(l)
                                        .parallelStream()
                                        .map(CITY_2_CITY_INFO_CONVERTER)
                                        .collect(toMap(CityInfo::getId, ci -> ci, (a, b) -> a)))
                                .values()
                )
                .flatMap(Collection::stream)
                .collect(toList())
                :
                emptyList();
    };

    /**
     * get city by state id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<City> getCityById(Long id) {
        return Optional.empty();
    }

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<City> selectCityByStateId(Long stateId) {
        return null;
    }

    /**
     * select cities by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<City> selectCityByIds(List<Long> ids) {
        return null;
    }

    /**
     * get city info opt by country id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<CityInfo> getCityInfoOptById(Long id) {
        return Optional.empty();
    }

    /**
     * get city info by country id with assert
     *
     * @param id
     * @return
     */
    @Override
    public CityInfo getCityInfoById(Long id) {
        return null;
    }

    /**
     * get city info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<CityInfo> getCityInfoMonoById(Long id) {
        return null;
    }

    /**
     * select states mono by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<CityInfo> selectCityInfoByStateId(Long stateId) {
        return null;
    }

    /**
     * select cities by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoMonoByStateId(Long stateId) {
        return null;
    }

    /**
     * select state infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<CityInfo> selectCityInfoByIds(List<Long> ids) {
        return null;
    }

    /**
     * select state infos mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<CityInfo>> selectCityInfoMonoByIds(List<Long> ids) {
        return null;
    }

    /**
     * invalid city infos
     *
     * @return
     */
    @Override
    public void invalidCityInfosCache() {

    }

}
