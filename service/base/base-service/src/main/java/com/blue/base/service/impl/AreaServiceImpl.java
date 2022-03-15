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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.base.common.base.BlueChecker.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.converter.BaseModelConverters.AREAS_2_AREA_INFOS_CONVERTER;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.*;
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

    private final AreaMapper areaMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public AreaServiceImpl(ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, AreaMapper areaMapper) {
        this.areaMapper = areaMapper;

        CITY_ID_AREA_CACHE = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private static Cache<Long, List<AreaInfo>> CITY_ID_AREA_CACHE;

    private final Function<Long, List<AreaInfo>> DB_AREA_GETTER = cid -> {
        List<Area> areas = this.selectAreaByCityId(cid);

        LOGGER.info("DB_AREA_GETTER, cid = {}, areas = {}", cid, areas);
        return AREAS_2_AREA_INFOS_CONVERTER.apply(
                areas.stream().sorted(Comparator.comparing(Area::getName)).collect(toList()));
    };

    /**
     * get city by state id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Area> getAreaById(Long id) {
        LOGGER.info("Optional<Area> getAreaById(Long id), id = {}", id);

        return ofNullable(areaMapper.selectByPrimaryKey(id));
    }

    /**
     * select area by country id
     *
     * @param countryId
     * @return
     */
    @Override
    public List<Area> selectAreaByCountryId(Long countryId) {
        LOGGER.info("List<Area> selectAreaByCountryId(Long countryId), countryId = {}", countryId);

        if (isInvalidIdentity(countryId))
            throw new BlueException(INVALID_IDENTITY);

        return areaMapper.selectByCountryId(countryId);
    }

    /**
     * select area by state id
     *
     * @param stateId
     * @return
     */
    @Override
    public List<Area> selectAreaByStateId(Long stateId) {
        LOGGER.info("List<Area> selectAreaByStateId(Long stateId), stateId = {}", stateId);

        if (isInvalidIdentity(stateId))
            throw new BlueException(INVALID_IDENTITY);

        return areaMapper.selectByStateId(stateId);
    }

    /**
     * select area by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public List<Area> selectAreaByCityId(Long cityId) {
        LOGGER.info("List<Area> selectAreaByCityId(Long cityId), cityId = {}", cityId);

        if (isInvalidIdentity(cityId))
            throw new BlueException(INVALID_IDENTITY);

        return areaMapper.selectByCityId(cityId);
    }

    /**
     * select all areas
     *
     * @return
     */
    @Override
    public List<Area> selectArea() {
        LOGGER.info("List<Area> selectArea()");

        return areaMapper.select();
    }

    /**
     * select areas by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public Mono<List<AreaInfo>> selectAreaInfoByCityId(Long cityId) {
        LOGGER.info("Mono<List<AreaInfo>> selectAreaInfoByCityId(Long cityId), cityId = {}", cityId);

        return isValidIdentity(cityId)
                ?
                justOrEmpty(CITY_ID_AREA_CACHE.get(cityId, DB_AREA_GETTER)).switchIfEmpty(just(emptyList()))
                :
                error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * invalid area infos
     *
     * @return
     */
    @Override
    public void invalidAreaInfosCache() {
        LOGGER.info("void invalidAreaInfosCache()");

        CITY_ID_AREA_CACHE.invalidateAll();
    }

}
