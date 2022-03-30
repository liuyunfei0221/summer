package com.blue.base.service.impl;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Area;
import com.blue.base.repository.template.AreaRepository;
import com.blue.base.service.inter.AreaService;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
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
import static com.blue.base.converter.BaseModelConverters.*;
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
 * area service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class AreaServiceImpl implements AreaService {

    private static final Logger LOGGER = getLogger(CityServiceImpl.class);

    private CityService cityService;

    private StateService stateService;

    private CountryService countryService;

    private AreaRepository areaRepository;

    public AreaServiceImpl(CityService cityService, StateService stateService, CountryService countryService,
                           ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy, AreaRepository areaRepository) {
        this.cityService = cityService;
        this.stateService = stateService;
        this.countryService = countryService;
        this.areaRepository = areaRepository;

        cityIdAreasCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idAreaCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idRegionCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private final Cache<Long, List<AreaInfo>> cityIdAreasCache;

    private Cache<Long, AreaInfo> idAreaCache;

    private Cache<Long, AreaRegion> idRegionCache;


    private final Function<Long, List<AreaInfo>> DB_AREAS_GETTER = cid -> {
        LOGGER.info("Function<Long, List<AreaInfo>> DB_AREAS_GETTER, cid = {}", cid);

        return AREAS_2_AREA_INFOS_CONVERTER.apply(this.selectAreaByCityId(cid));
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
                        idAreaCache.getAll(l, is -> areaRepository.findAllById(l)
                                        .flatMap(a -> just(AREA_2_AREA_INFO_CONVERTER.apply(a)))
                                        .collectList().toFuture().join()
                                        .parallelStream()
                                        .collect(toMap(AreaInfo::getId, ci -> ci, (a, b) -> a)))
                                .entrySet()
                )
                .flatMap(Collection::stream)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue, (a, b) -> a));
    };

    private final Function<Long, AreaRegion> AREA_REGION_GETTER = id -> {
        if (isValidIdentity(id)) {
            return idRegionCache.get(id, i ->
                    just(idAreaCache.get(i, DB_AREA_GETTER_WITH_ASSERT))
                            .flatMap(areaInfo ->
                                    zip(
                                            countryService.getCountryInfoMonoById(areaInfo.getCountryId()),
                                            stateService.getStateInfoMonoById(areaInfo.getStateId()),
                                            cityService.getCityInfoMonoById(areaInfo.getCityId())
                                    ).flatMap(tuple3 ->
                                            just(new AreaRegion(id, tuple3.getT1(), tuple3.getT2(), tuple3.getT3(), areaInfo))))
                            .toFuture().join()
            );
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<List<Long>, Map<Long, AreaRegion>> AREA_REGIONS_GETTER = ids -> {
        LOGGER.info("Function<List<Long>, Map<Long, AreaRegion>> AREA_REGIONS_GETTER, ids = {}", ids);

        if (isInvalidIdentities(ids))
            return emptyMap();

        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(l ->
                        idRegionCache.getAll(l, is -> {
                            Collection<AreaInfo> areaInfos = CACHE_AREAS_BY_IDS_GETTER.apply(ids).values();
                            int size = areaInfos.size();
                            List<Long> countryIds = new ArrayList<>(size);
                            List<Long> stateIds = new ArrayList<>(size);
                            List<Long> cityIds = new ArrayList<>(size);

                            for (AreaInfo ai : areaInfos) {
                                countryIds.add(ai.getCountryId());
                                stateIds.add(ai.getStateId());
                                cityIds.add(ai.getCityId());
                            }

                            return zip(
                                    cityService.selectCityInfoMonoByIds(cityIds),
                                    stateService.selectStateInfoMonoByIds(stateIds),
                                    countryService.selectCountryInfoMonoByIds(countryIds)
                            ).flatMap(tuple3 ->
                                    just(areaInfos.parallelStream().map(ai -> new AreaRegion(ai.getId(), tuple3.getT3().get(ai.getCountryId()),
                                                    tuple3.getT2().get(ai.getStateId()), tuple3.getT1().get(ai.getCityId()), ai))
                                            .collect(toMap(AreaRegion::getAreaId, ar -> ar, (a, b) -> a)))
                            ).toFuture().join();
                        }).entrySet()
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

        return ofNullable(areaRepository.findById(id).toFuture().join());
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

        Area area = new Area();
        area.setCityId(cityId);

        return areaRepository.findAll(Example.of(area), Sort.by("name"))
                .collectList().toFuture().join();
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

        if (isInvalidIdentities(ids) || ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(INVALID_PARAM);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream()
                .map(l -> areaRepository.findAllById(l)
                        .collectList().toFuture().join())
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
        return ofNullable(idAreaCache.get(id, DB_AREA_GETTER));
    }

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public AreaInfo getAreaInfoById(Long id) {
        return idAreaCache.get(id, DB_AREA_GETTER_WITH_ASSERT);
    }

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaInfo> getAreaInfoMonoById(Long id) {
        return just(idAreaCache.get(id, DB_AREA_GETTER_WITH_ASSERT));
    }

    /**
     * select area infos by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public List<AreaInfo> selectAreaInfoByCityId(Long cityId) {
        return cityIdAreasCache.get(cityId, DB_AREAS_GETTER);
    }

    /**
     * select area infos mono by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public Mono<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId) {
        return just(cityIdAreasCache.get(cityId, DB_AREAS_GETTER)).switchIfEmpty(just(emptyList()));
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
     * get region by id
     *
     * @param id
     * @return
     */
    @Override
    public AreaRegion getAreaRegionById(Long id) {
        return idRegionCache.get(id, AREA_REGION_GETTER);
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaRegion> getAreaRegionMonoById(Long id) {
        return just(idRegionCache.get(id, AREA_REGION_GETTER));
    }

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, AreaRegion> selectAreaRegionByIds(List<Long> ids) {
        return AREA_REGIONS_GETTER.apply(ids);
    }

    /**
     * get regions mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, AreaRegion>> selectAreaRegionMonoByIds(List<Long> ids) {
        return just(AREA_REGIONS_GETTER.apply(ids));
    }

    /**
     * invalid area infos
     *
     * @return
     */
    @Override
    public void invalidAreaInfosCache() {
        LOGGER.info("void invalidAreaInfosCache()");

        cityIdAreasCache.invalidateAll();
        idAreaCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

}
