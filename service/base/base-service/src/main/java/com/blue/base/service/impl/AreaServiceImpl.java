package com.blue.base.service.impl;

import com.blue.base.api.model.AreaInfo;
import com.blue.base.api.model.AreaRegion;
import com.blue.base.config.deploy.AreaCaffeineDeploy;
import com.blue.base.model.AreaCondition;
import com.blue.base.model.AreaInsertParam;
import com.blue.base.model.AreaUpdateParam;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.base.repository.entity.Area;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.template.AreaRepository;
import com.blue.base.service.inter.AreaService;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.Cache;
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
import static reactor.core.publisher.Mono.*;

/**
 * area service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class AreaServiceImpl implements AreaService {

    private static final Logger LOGGER = Loggers.getLogger(AreaServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private CityService cityService;

    private StateService stateService;

    private CountryService countryService;

    private AreaRepository areaRepository;

    public AreaServiceImpl(BlueIdentityProcessor blueIdentityProcessor, CityService cityService, StateService stateService, CountryService countryService,
                           AreaRepository areaRepository, ExecutorService executorService, AreaCaffeineDeploy areaCaffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.cityService = cityService;
        this.stateService = stateService;
        this.countryService = countryService;
        this.areaRepository = areaRepository;

        idAreaCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        cityIdAreasCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));

        idRegionCache = generateCache(new CaffeineConfParams(
                areaCaffeineDeploy.getAreaMaximumSize(), Duration.of(areaCaffeineDeploy.getExpireSeconds(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private Cache<Long, AreaInfo> idAreaCache;

    private Cache<Long, List<AreaInfo>> cityIdAreasCache;

    private Cache<Long, AreaRegion> idRegionCache;

    private final Function<Long, AreaInfo> DB_AREA_GETTER = id ->
            this.getAreaById(id).map(AREA_2_AREA_INFO_CONVERTER).orElse(null);

    private final Function<Long, AreaInfo> DB_AREA_GETTER_WITH_ASSERT = id ->
            this.getAreaById(id).map(AREA_2_AREA_INFO_CONVERTER)
                    .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

    private final Function<Long, List<AreaInfo>> DB_AREAS_BY_CITY_ID_GETTER = cid ->
            AREAS_2_AREA_INFOS_CONVERTER.apply(
                    this.selectAreaByCityId(cid).stream().sorted(Comparator.comparing(Area::getName)).collect(toList()));

    private final Function<Long, Optional<AreaInfo>> AREA_OPT_BY_ID_GETTER = id ->
            ofNullable(idAreaCache.get(id, DB_AREA_GETTER));

    private final Function<Long, AreaInfo> AREA_BY_ID_WITH_ASSERT_GETTER = id ->
            idAreaCache.get(id, DB_AREA_GETTER_WITH_ASSERT);

    private final Function<Long, List<AreaInfo>> AREAS_BY_CITY_ID_GETTER = cid ->
            cityIdAreasCache.get(cid, DB_AREAS_BY_CITY_ID_GETTER);

    private final Function<List<Long>, Map<Long, AreaInfo>> CACHE_AREAS_BY_IDS_GETTER = ids -> {
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
                            .toFuture().join());
        }

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<List<Long>, Map<Long, AreaRegion>> AREA_REGIONS_GETTER = ids -> {
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
     * is an area exist?
     */
    private final Consumer<AreaInsertParam> INSERT_AREA_VALIDATOR = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Area probe = new Area();
        probe.setCityId(param.getCityId());
        probe.setName(param.getName());

        Long count = ofNullable(areaRepository.count(Example.of(probe)).toFuture().join()).orElse(0L);

        if (count > 0L)
            throw new BlueException(AREA_ALREADY_EXIST);
    };

    /**
     * area insert param -> area
     */
    public final Function<AreaInsertParam, Area> AREA_INSERT_PARAM_2_AREA_CONVERTER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long cityId = param.getCityId();

        City city = cityService.getCityById(cityId)
                .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));
        Long stamp = TIME_STAMP_GETTER.get();

        Area area = new Area();

        area.setId(blueIdentityProcessor.generate(Area.class));
        area.setCountryId(city.getCountryId());
        area.setStateId(city.getStateId());
        area.setCityId(cityId);
        area.setName(param.getName());
        area.setStatus(VALID.status);
        area.setCreateTime(stamp);
        area.setUpdateTime(stamp);

        return area;
    };

    /**
     * is an area exist?
     */
    private final Function<AreaUpdateParam, Area> UPDATE_AREA_VALIDATOR_AND_ORIGIN_RETURNER = param -> {
        if (isNull(param))
            throw new BlueException(EMPTY_PARAM);
        param.asserts();

        Long id = param.getId();

        Area probe = new Area();
        probe.setCityId(param.getCityId());
        probe.setName(param.getName());

        List<Area> areas = ofNullable(areaRepository.findAll(Example.of(probe)).collectList().toFuture().join())
                .orElseGet(Collections::emptyList);

        if (areas.stream().anyMatch(a -> !id.equals(a.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        Area area = areaRepository.findById(id).toFuture().join();
        if (isNull(area))
            throw new BlueException(DATA_NOT_EXIST);

        return area;
    };

    /**
     * for area
     */
    public final BiFunction<AreaUpdateParam, Area, Boolean> UPDATE_AREA_VALIDATOR = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);

        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        Long cityId = p.getCityId();
        if (cityId != null && !cityId.equals(t.getCityId())) {
            City city = cityService.getCityById(cityId)
                    .orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

            t.setCountryId(city.getCountryId());
            t.setStateId(city.getStateId());
            t.setCityId(cityId);

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
     * insert area
     *
     * @param areaInsertParam
     * @return
     */
    @Override
    public Mono<AreaInfo> insertArea(AreaInsertParam areaInsertParam) {
        LOGGER.info("Mono<AreaInfo> insertArea(AreaInsertParam areaInsertParam), areaInsertParam = {}", areaInsertParam);

        INSERT_AREA_VALIDATOR.accept(areaInsertParam);
        Area area = AREA_INSERT_PARAM_2_AREA_CONVERTER.apply(areaInsertParam);

        return areaRepository.insert(area)
                .map(AREA_2_AREA_INFO_CONVERTER)
                .doOnSuccess(ai -> {
                    LOGGER.info("ai = {}", ai);
                    invalidCache();
                });
    }

    /**
     * update area
     *
     * @param areaUpdateParam
     * @return
     */
    @Override
    public Mono<AreaInfo> updateArea(AreaUpdateParam areaUpdateParam) {
        LOGGER.info("Mono<AreaInfo> updateArea(AreaUpdateParam areaUpdateParam), areaUpdateParam = {}", areaUpdateParam);

        Area area = UPDATE_AREA_VALIDATOR_AND_ORIGIN_RETURNER.apply(areaUpdateParam);

        Boolean changed = UPDATE_AREA_VALIDATOR.apply(areaUpdateParam, area);
        if (changed != null && !changed)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        return areaRepository.save(area)
                .map(AREA_2_AREA_INFO_CONVERTER)
                .doOnSuccess(ai -> {
                    LOGGER.info("ai = {}", ai);
                    invalidCache();
                });
    }

    /**
     * delete area
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaInfo> deleteArea(Long id) {
        LOGGER.info("Mono<AreaInfo> deleteArea(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        Area area = areaRepository.findById(id).toFuture().join();
        if (isNull(area))
            throw new BlueException(DATA_NOT_EXIST);

        return areaRepository.delete(area)
                .then(just(AREA_2_AREA_INFO_CONVERTER.apply(area)))
                .doOnSuccess(ai -> {
                    LOGGER.info("ai = {}", ai);
                    invalidCache();
                });
    }

    /**
     * invalid chche
     */
    @Override
    public void invalidCache() {
        idAreaCache.invalidateAll();
        cityIdAreasCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

    /**
     * get area by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<Area> getAreaById(Long id) {
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
        if (isInvalidIdentity(cityId))
            throw new BlueException(INVALID_IDENTITY);

        Area probe = new Area();
        probe.setCityId(cityId);

        return areaRepository.findAll(Example.of(probe), Sort.by(Sort.Order.asc("name")))
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
        return AREA_OPT_BY_ID_GETTER.apply(id);
    }

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public AreaInfo getAreaInfoById(Long id) {
        return AREA_BY_ID_WITH_ASSERT_GETTER.apply(id);
    }

    /**
     * get area info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaInfo> getAreaInfoMonoById(Long id) {
        return just(AREA_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select area info by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public List<AreaInfo> selectAreaInfoByCityId(Long cityId) {
        return AREAS_BY_CITY_ID_GETTER.apply(cityId);
    }

    /**
     * select area info mono by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public Mono<List<AreaInfo>> selectAreaInfoMonoByCityId(Long cityId) {
        return just(AREAS_BY_CITY_ID_GETTER.apply(cityId)).switchIfEmpty(defer(() -> just(emptyList())));
    }

    /**
     * select area info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Map<Long, AreaInfo> selectAreaInfoByIds(List<Long> ids) {
        return CACHE_AREAS_BY_IDS_GETTER.apply(ids);
    }

    /**
     * select area info mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, AreaInfo>> selectAreaInfoMonoByIds(List<Long> ids) {
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
        return AREA_REGION_GETTER.apply(id);
    }

    /**
     * get region mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaRegion> getAreaRegionMonoById(Long id) {
        return just(AREA_REGION_GETTER.apply(id));
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
     * invalid area info
     *
     * @return
     */
    @Override
    public void invalidAreaInfosCache() {
        cityIdAreasCache.invalidateAll();
        idAreaCache.invalidateAll();
        idRegionCache.invalidateAll();
    }

    /**
     * select area by limit and condition
     *
     * @param limit
     * @param rows
     * @param areaCondition
     * @return
     */
    @Override
    public Mono<List<Area>> selectAreaMonoByLimitAndCondition(Long limit, Long rows, AreaCondition areaCondition) {



        return null;
    }

    /**
     * count area by condition
     *
     * @param areaCondition
     * @return
     */
    @Override
    public Mono<Long> countAreaMonoByCondition(AreaCondition areaCondition) {
        return null;
    }

    /**
     * select area info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AreaInfo>> selectAreaPageMonoByPageAndCondition(PageModelRequest<AreaCondition> pageModelRequest) {
        return null;
    }

}
