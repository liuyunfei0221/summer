package com.blue.base.service.impl;

import com.blue.base.api.model.*;
import com.blue.base.config.deploy.CaffeineDeploy;
import com.blue.base.model.AreaCondition;
import com.blue.base.model.AreaInsertParam;
import com.blue.base.model.AreaUpdateParam;
import com.blue.base.repository.entity.Area;
import com.blue.base.repository.entity.City;
import com.blue.base.repository.template.AreaRepository;
import com.blue.base.service.inter.AreaService;
import com.blue.base.service.inter.CityService;
import com.blue.base.service.inter.CountryService;
import com.blue.base.service.inter.StateService;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.identity.component.BlueIdentityProcessor;
import com.github.benmanes.caffeine.cache.AsyncCache;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.base.constant.BaseColumnName.NAME;
import static com.blue.base.converter.BaseModelConverters.AREAS_2_AREA_INFOS_CONVERTER;
import static com.blue.base.converter.BaseModelConverters.AREA_2_AREA_INFO_CONVERTER;
import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_WRITE;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Status.VALID;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.function.Function.identity;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.domain.Sort.by;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;

/**
 * area service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
@Service
public class AreaServiceImpl implements AreaService {

    private static final Logger LOGGER = Loggers.getLogger(AreaServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private ExecutorService executorService;

    private CityService cityService;

    private StateService stateService;

    private CountryService countryService;

    private AreaRepository areaRepository;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    public AreaServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ExecutorService executorService, CityService cityService, StateService stateService, CountryService countryService,
                           AreaRepository areaRepository, ReactiveMongoTemplate reactiveMongoTemplate, CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.executorService = executorService;
        this.cityService = cityService;
        this.stateService = stateService;
        this.countryService = countryService;
        this.areaRepository = areaRepository;
        this.reactiveMongoTemplate = reactiveMongoTemplate;

        idAreaCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getAreaMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, this.executorService));

        cityIdAreasCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, this.executorService));

        idRegionCache = generateCacheAsyncCache(new CaffeineConfParams(
                caffeineDeploy.getAreaMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, this.executorService));
    }

    private AsyncCache<Long, AreaInfo> idAreaCache;

    private AsyncCache<Long, List<AreaInfo>> cityIdAreasCache;

    private AsyncCache<Long, AreaRegion> idRegionCache;

    private final BiFunction<Long, Executor, CompletableFuture<AreaInfo>> DB_AREA_WITH_ASSERT_GETTER = (id, executor) -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return this.getAreaById(id).map(AREA_2_AREA_INFO_CONVERTER)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture();
    };

    private final BiFunction<Long, Executor, CompletableFuture<List<AreaInfo>>> DB_AREAS_BY_CITY_ID_GETTER = (cid, executor) -> {
        if (isInvalidIdentity(cid))
            throw new BlueException(INVALID_IDENTITY);

        return this.selectAreaByCityId(cid).map(AREAS_2_AREA_INFOS_CONVERTER)
                .switchIfEmpty(defer(() -> just(emptyList())))
                .toFuture();
    };

    private final Function<Long, CompletableFuture<AreaInfo>> AREA_BY_ID_WITH_ASSERT_GETTER = id -> {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return idAreaCache.get(id, DB_AREA_WITH_ASSERT_GETTER);
    };

    private final Function<Long, CompletableFuture<List<AreaInfo>>> AREAS_BY_CITY_ID_GETTER = cid -> {
        if (isInvalidIdentity(cid))
            throw new BlueException(INVALID_IDENTITY);

        return cityIdAreasCache.get(cid, DB_AREAS_BY_CITY_ID_GETTER);
    };

    private final Function<List<Long>, CompletableFuture<Map<Long, AreaInfo>>> CACHE_AREAS_BY_IDS_GETTER = ids -> {
        if (isEmpty(ids))
            return supplyAsync(Collections::emptyMap, this.executorService);
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return idAreaCache.getAll(ids, (is, executor) -> fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(l -> areaRepository.findAllById(ids)
                        .map(AREA_2_AREA_INFO_CONVERTER))
                .reduce(Flux::concat)
                .flatMap(Flux::collectList)
                .map(l -> l.stream().collect(toMap(AreaInfo::getId, identity(), (a, b) -> a)))
                .toFuture());
    };

    private final Function<Long, Mono<AreaRegion>> AREA_REGION_GETTER = id ->
            isValidIdentity(id) ?
                    fromFuture(AREA_BY_ID_WITH_ASSERT_GETTER.apply(id))
                            .flatMap(areaInfo ->
                                    zip(
                                            countryService.getCountryInfoById(areaInfo.getCountryId()),
                                            stateService.getStateInfoById(areaInfo.getStateId()),
                                            cityService.getCityInfoById(areaInfo.getStateId())
                                    ).map(tuple3 ->
                                            new AreaRegion(id, tuple3.getT1(), tuple3.getT2(), tuple3.getT3(), areaInfo)))
                    :
                    error(() -> new BlueException(INVALID_IDENTITY));

    private final Function<List<Long>, CompletableFuture<Map<Long, AreaRegion>>> AREA_REGIONS_GETTER = ids -> {
        if (isEmpty(ids))
            return supplyAsync(Collections::emptyMap, this.executorService);
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return idRegionCache.getAll(ids, (is, executor) ->
                fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                        .flatMap(l ->
                                fromFuture(CACHE_AREAS_BY_IDS_GETTER.apply(l))
                                        .flatMap(areaMap ->
                                                zip(cityService.selectCityInfoByIds(areaMap.values().stream().map(AreaInfo::getCityId).distinct().collect(toList())),
                                                        stateService.selectStateInfoByIds(areaMap.values().stream().map(AreaInfo::getStateId).distinct().collect(toList())),
                                                        countryService.selectCountryInfoByIds(areaMap.values().stream().map(AreaInfo::getCountryId).distinct().collect(toList()))
                                                ).map(tuple3 -> {
                                                    Map<Long, CityInfo> cityMap = tuple3.getT1();
                                                    Map<Long, StateInfo> stateMap = tuple3.getT2();
                                                    Map<Long, CountryInfo> countryMap = tuple3.getT3();

                                                    return areaMap.values().stream().map(ai -> new AreaRegion(ai.getId(), countryMap.get(ai.getCountryId()),
                                                                    stateMap.get(ai.getStateId()), cityMap.get(ai.getCityId()), ai))
                                                            .collect(toMap(AreaRegion::getAreaId, identity(), (a, b) -> a));
                                                })))
                        .map(m -> fromIterable(m.values()))
                        .reduce(Flux::concat)
                        .flatMap(f -> f.collectMap(AreaRegion::getAreaId, identity()))
                        .toFuture());
    };

    private final Consumer<AreaInsertParam> INSERT_ITEM_VALIDATOR = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Area probe = new Area();
        probe.setCityId(p.getCityId());
        probe.setName(p.getName());

        if (ofNullable(areaRepository.count(Example.of(probe)).toFuture().join()).orElse(0L) > 0L)
            throw new BlueException(AREA_ALREADY_EXIST);
    };

    private final Function<AreaInsertParam, Area> AREA_INSERT_PARAM_2_AREA_CONVERTER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long cityId = p.getCityId();

        City city = cityService.getCityById(cityId).toFuture().join();
        if (isNull(city))
            throw new BlueException(DATA_NOT_EXIST);

        Long stamp = TIME_STAMP_GETTER.get();

        Area area = new Area();

        area.setId(blueIdentityProcessor.generate(Area.class));
        area.setCountryId(city.getCountryId());
        area.setStateId(city.getStateId());
        area.setCityId(cityId);
        area.setName(p.getName());
        area.setStatus(VALID.status);
        area.setCreateTime(stamp);
        area.setUpdateTime(stamp);

        return area;
    };

    private final Function<AreaUpdateParam, Area> UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER = p -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        Long id = p.getId();

        Area probe = new Area();
        probe.setCityId(p.getCityId());
        probe.setName(p.getName());

        List<Area> areas = ofNullable(areaRepository.findAll(Example.of(probe))
                .collectList().toFuture().join())
                .orElseGet(Collections::emptyList);

        if (areas.stream().anyMatch(a -> !id.equals(a.getId())))
            throw new BlueException(DATA_ALREADY_EXIST);

        Area area = areaRepository.findById(id).toFuture().join();
        if (isNull(area))
            throw new BlueException(DATA_NOT_EXIST);

        return area;
    };

    public final BiConsumer<AreaUpdateParam, Area> UPDATE_ITEM_WITH_ASSERT_PACKAGER = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(BAD_REQUEST);
        if (!p.getId().equals(t.getId()))
            throw new BlueException(BAD_REQUEST);

        boolean alteration = false;

        Long cityId = p.getCityId();
        if (cityId != null && !cityId.equals(t.getCityId())) {
            City city = cityService.getCityById(cityId).toFuture().join();
            if (isNull(city))
                throw new BlueException(DATA_NOT_EXIST);

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

        if (!alteration)
            throw new BlueException(DATA_HAS_NOT_CHANGED);

        t.setUpdateTime(TIME_STAMP_GETTER.get());
    };

    private static final Function<AreaCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null)
            return query;

        Area probe = new Area();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(c.getStateId()).ifPresent(probe::setStateId);
        ofNullable(c.getCityId()).ifPresent(probe::setCityId);
        ofNullable(c.getStatus()).ifPresent(probe::setStatus);

        query.addCriteria(byExample(probe));

        ofNullable(c.getNameLike()).ifPresent(nameLike ->
                query.addCriteria(where(NAME.name).regex(compile(PREFIX.element + nameLike + SUFFIX.element, CASE_INSENSITIVE))));

        query.with(by(Sort.Order.asc(NAME.name)));

        return query;
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

        INSERT_ITEM_VALIDATOR.accept(areaInsertParam);
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

        Area area = UPDATE_ITEM_VALIDATOR_AND_ORIGIN_RETURNER.apply(areaUpdateParam);

        UPDATE_ITEM_WITH_ASSERT_PACKAGER.accept(areaUpdateParam, area);

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

        return areaRepository.findById(id)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(area -> areaRepository.delete(area)
                        .then(just(AREA_2_AREA_INFO_CONVERTER.apply(area)))
                        .doOnSuccess(ai -> {
                            LOGGER.info("ai = {}", ai);
                            invalidCache();
                        }));
    }

    /**
     * invalid cache
     */
    @Override
    public void invalidCache() {
        idAreaCache.synchronous().invalidateAll();
        cityIdAreasCache.synchronous().invalidateAll();
        idRegionCache.synchronous().invalidateAll();
    }

    /**
     * get area by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Area> getAreaById(Long id) {
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return areaRepository.findById(id);
    }

    /**
     * select area by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public Mono<List<Area>> selectAreaByCityId(Long cityId) {
        if (isInvalidIdentity(cityId))
            throw new BlueException(INVALID_IDENTITY);

        Area probe = new Area();
        probe.setCityId(cityId);

        return areaRepository.findAll(Example.of(probe), by(Sort.Order.asc(NAME.name)))
                .collectList();
    }

    /**
     * select area by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<Area>> selectAreaByIds(List<Long> ids) {
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return fromIterable(allotByMax(ids, (int) DB_WRITE.value, false))
                .map(areaRepository::findAllById)
                .reduce(Flux::concat)
                .flatMap(Flux::collectList);
    }

    /**
     * get area info by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaInfo> getAreaInfoById(Long id) {
        return fromFuture(AREA_BY_ID_WITH_ASSERT_GETTER.apply(id));
    }

    /**
     * select area info by city id
     *
     * @param cityId
     * @return
     */
    @Override
    public Mono<List<AreaInfo>> selectAreaInfoByCityId(Long cityId) {
        return fromFuture(AREAS_BY_CITY_ID_GETTER.apply(cityId));
    }

    /**
     * select area info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, AreaInfo>> selectAreaInfoByIds(List<Long> ids) {
        return fromFuture(CACHE_AREAS_BY_IDS_GETTER.apply(ids));
    }

    /**
     * get region by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<AreaRegion> getAreaRegionById(Long id) {
        return AREA_REGION_GETTER.apply(id);
    }

    /**
     * get regions by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<Map<Long, AreaRegion>> selectAreaRegionByIds(List<Long> ids) {
        return fromFuture(AREA_REGIONS_GETTER.apply(ids));
    }

    /**
     * select area by limit and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<Area>> selectAreaByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<Area>> selectAreaMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
        if (limit == null || limit < 0 || rows == null || rows == 0)
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Area.class).collectList();
    }

    /**
     * count area by query
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countAreaByQuery(Query query) {
        LOGGER.info("Mono<Long> countAreaMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, Area.class);
    }

    /**
     * select area info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<AreaInfo>> selectAreaPageByPageAndCondition(PageModelRequest<AreaCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<AreaInfo>> selectAreaPageMonoByPageAndCondition(PageModelRequest<AreaCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(
                selectAreaByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countAreaByQuery(query)
        ).flatMap(tuple2 -> {
            List<Area> areas = tuple2.getT1();
            Long count = tuple2.getT2();
            return isNotEmpty(areas) ?
                    just(new PageModelResponse<>(AREAS_2_AREA_INFOS_CONVERTER.apply(areas), count))
                    :
                    just(new PageModelResponse<>(emptyList(), count));
        });
    }

}
