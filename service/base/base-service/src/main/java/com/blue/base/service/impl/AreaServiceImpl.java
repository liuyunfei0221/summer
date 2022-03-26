package com.blue.base.service.impl;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Area;
import com.blue.base.repository.mapper.AreaMapper;
import com.blue.base.service.inter.AreaService;
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
import static com.blue.base.converter.BaseModelConverters.AREAS_2_AREA_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.AREA_2_AREA_INFO_CONVERTER;
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
 * area service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class AreaServiceImpl implements AreaService {

    private static final Logger LOGGER = getLogger(CityServiceImpl.class);

    private AreaMapper areaMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AreaServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, AreaMapper areaMapper) {
        this.areaMapper = areaMapper;

        CITY_ID_AREAS_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        ID_AREA_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getCityMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static Cache<Long, List<AreaInfo>> CITY_ID_AREAS_CACHE;

    private static Cache<Long, AreaInfo> ID_AREA_CACHE;


    private final Function<Long, List<AreaInfo>> DB_AREAS_GETTER = cid -> {
        LOGGER.info("Function<Long, List<AreaInfo>> DB_AREAS_GETTER, cid = {}", cid);
        return AREAS_2_AREA_INFOS_CONVERTER.apply(
                this.selectAreaByCityId(cid).stream().sorted(Comparator.comparing(Area::getName)).collect(toList()));
    };

    private final Function<Long, AreaInfo> DB_AREA_GETTER = id -> {
        LOGGER.info("Function<Long, AreaInfo> DB_AREA_GETTER, id = {}", id);
        return this.getAreaById(id).map(AREA_2_AREA_INFO_CONVERTER).orElse(null);
    };

    private final Function<Long, AreaInfo> DB_AREA_GETTER_WITH_ASSERT = id -> {
        LOGGER.info("Function<Long, AreaInfo> DB_AREA_GETTER_WITH_ASSERT, id = {}", id);
        return this.getAreaById(id).map(AREA_2_AREA_INFO_CONVERTER)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
    };

    private final Function<List<Long>, Map<Long, AreaInfo>> CACHE_AREAS_BY_IDS_GETTER = ids -> {
        LOGGER.info("Function<List<Long>, Map<Long, AreaInfo>> CACHE_AREAS_BY_IDS_GETTER, ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        ID_AREA_CACHE.getAll(l, is -> areaMapper.selectByIds(l)
                                        .parallelStream()
                                        .map(AREA_2_AREA_INFO_CONVERTER)
                                        .collect(toMap(AreaInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    /**
     * get area by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Area> getAreaById(Long id) {
        LOGGER.info("Optional<City> getCityById(Long id), id = {}", id);

        return ofNullable(areaMapper.selectByPrimaryKey(id));
    }

    /**
     * select area by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public List<Area> selectAreaByCityId(Long cityId) {
        LOGGER.info("List<City> selectCityByStateId(Long stateId), cityId = {}", cityId);

        if (isInvalidIdentity(cityId))
            throw new BlueException(INVALID_IDENTITY);

        return areaMapper.selectByCountryId(cityId);
    }

    /**
     * select area by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<Area> selectAreaByIds(List<Long> ids) {
        LOGGER.info("List<Area> selectAreaByIds(List<Long> ids), ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyList();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(areaMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * get area info opt by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<AreaInfo> getAreaInfoOptById(Long id) {
        return ofNullable(ID_AREA_CACHE.get(id, DB_AREA_GETTER));
    }

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public AreaInfo getAreaInfoById(Long id) {
        return ID_AREA_CACHE.get(id, DB_AREA_GETTER_WITH_ASSERT);
    }

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaInfo> getAreaInfoMonoById(Long id) {
        return just(ID_AREA_CACHE.get(id, DB_AREA_GETTER_WITH_ASSERT));
    }

    /**
     * select area infos by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public List<AreaInfo> selectAreaInfoByCityId(Long cityId) {
        return CITY_ID_AREAS_CACHE.get(cityId, DB_AREAS_GETTER);
    }

    /**
     * select area infos mono by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public Mono<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId) {
        return just(CITY_ID_AREAS_CACHE.get(cityId, DB_AREAS_GETTER)).switchIfEmpty(just(emptyList()));
    }

    /**
     * select area infos by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids) {
        LOGGER.info("Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids), ids = {}", ids);

        return CACHE_AREAS_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select area infos mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids)), ids = {}", ids);


        return just(CACHE_AREAS_BY_IDS_GETTER.apply(ids));
    }

    /**
     * invalid area infos
     *
     * @return
     */
    @Override
    public void invalidAreaInfosCache() {
        LOGGER.info("void invalidAreaInfosCache()");

        CITY_ID_AREAS_CACHE.invalidateAll();
        ID_AREA_CACHE.invalidateAll();
    }

}
