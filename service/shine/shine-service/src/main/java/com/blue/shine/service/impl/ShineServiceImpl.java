package com.blue.shine.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.blue.base.api.model.CityRegion;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.IdentityEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.es.common.EsPitSearchAfterProcessor;
import com.blue.es.common.EsSearchAfterProcessor;
import com.blue.es.model.PitCursor;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.config.deploy.CaffeineDeploy;
import com.blue.shine.config.deploy.DefaultPriorityDeploy;
import com.blue.shine.config.deploy.PitDeploy;
import com.blue.shine.constant.ShineSortAttribute;
import com.blue.shine.event.producer.ShineDeleteProducer;
import com.blue.shine.event.producer.ShineInsertProducer;
import com.blue.shine.event.producer.ShineUpdateProducer;
import com.blue.shine.model.ShineCondition;
import com.blue.shine.model.ShineInsertParam;
import com.blue.shine.model.ShineUpdateParam;
import com.blue.shine.remote.consumer.RpcCityServiceConsumer;
import com.blue.shine.repository.entity.Shine;
import com.blue.shine.repository.template.ShineRepository;
import com.blue.shine.service.inter.ShineService;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static com.blue.es.common.EsPitProcessor.packagePit;
import static com.blue.es.common.EsPitProcessor.parsePit;
import static com.blue.es.common.EsPitSearchAfterProcessor.packagePitSearchAfter;
import static com.blue.es.common.EsSearchAfterProcessor.packageSearchAfter;
import static com.blue.es.common.EsSortProcessor.process;
import static com.blue.shine.constant.ShineColumnName.*;
import static com.blue.shine.constant.ShineTableName.SHINE;
import static com.blue.shine.converter.ShineModelConverters.SHINE_2_SHINE_INFO;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.lang3.math.NumberUtils.isDigits;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * shine service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "SpringJavaInjectionPointsAutowiringInspection", "ConstantConditions"})
@Service
public class ShineServiceImpl implements ShineService {

    private static final Logger LOGGER = getLogger(ShineServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private final Scheduler scheduler;

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    private final ShineInsertProducer shineInsertProducer;

    private final ShineUpdateProducer shineUpdateProducer;

    private final ShineDeleteProducer shineDeleteProducer;

    private final ShineRepository shineRepository;

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, Scheduler scheduler, ElasticsearchAsyncClient elasticsearchAsyncClient,
                            RpcCityServiceConsumer rpcCityServiceConsumer, ShineRepository shineRepository, ShineInsertProducer shineInsertProducer, ShineUpdateProducer shineUpdateProducer,
                            ShineDeleteProducer shineDeleteProducer, ExecutorService executorService, PitDeploy pitDeploy, DefaultPriorityDeploy defaultPriorityDeploy,
                            CaffeineDeploy caffeineDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.scheduler = scheduler;
        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.shineRepository = shineRepository;
        this.shineInsertProducer = shineInsertProducer;
        this.shineUpdateProducer = shineUpdateProducer;
        this.shineDeleteProducer = shineDeleteProducer;

        this.PIT_TIME = Time.of(builder -> builder.time(pitDeploy.getTime()));
        this.defaultPriority = defaultPriorityDeploy.getPriority();
        this.idRegionCache = generateCache(new CaffeineConfParams(
                caffeineDeploy.getCityMaximumSize(), Duration.of(caffeineDeploy.getExpiresSecond(), SECONDS),
                AFTER_ACCESS, executorService));
    }

    private final Time PIT_TIME;

    private int defaultPriority;

    private static final String INDEX_NAME = SHINE.name;

    private Cache<Long, CityRegion> idRegionCache;

    private final Function<Long, CityRegion> CITY_REGION_REMOTE_GETTER = id -> {
        if (isValidIdentity(id))
            return ofNullable(rpcCityServiceConsumer.getCityRegionById(id)
                    .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                    .toFuture().join()).orElseThrow(() -> new BlueException(DATA_NOT_EXIST));

        throw new BlueException(INVALID_IDENTITY);
    };

    private final Function<Long, CityRegion> CITY_REGION_GETTER = id -> {
        if (isValidIdentity(id))
            return idRegionCache.get(id, CITY_REGION_REMOTE_GETTER);

        throw new BlueException(INVALID_IDENTITY);
    };

    private final BiConsumer<Shine, Long> SHINE_CITY_PACKAGER = (shine, cid) -> {
        if (isNull(shine) || isInvalidIdentity(cid))
            return;

        CityRegion cityRegion = CITY_REGION_GETTER.apply(cid);

        ofNullable(cityRegion.getCountry())
                .ifPresent(countryInfo -> {
                    shine.setCountryId(countryInfo.getId());
                    shine.setCountry(countryInfo.getName());
                });

        ofNullable(cityRegion.getState())
                .ifPresent(stateInfo -> {
                    shine.setStateId(stateInfo.getId());
                    shine.setState(stateInfo.getName());
                });

        ofNullable(cityRegion.getCity())
                .ifPresent(cityInfo -> {
                    shine.setCityId(cityInfo.getId());
                    shine.setCity(cityInfo.getName());
                });
    };

    private final BiFunction<ShineInsertParam, Long, Mono<Shine>> SHINE_INSERT_PARAM_2_SHINE = (p, mid) -> {
        if (isNull(p))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();
        if (isInvalidIdentity(mid))
            throw new BlueException(UNAUTHORIZED);

        Shine shine = new Shine();

        SHINE_CITY_PACKAGER.accept(shine, p.getCityId());

        shine.setId(blueIdentityProcessor.generate(Shine.class));

        shine.setTitle(p.getTitle());
        shine.setContent(p.getContent());
        shine.setDetail(p.getDetail());
        shine.setContact(p.getContact());
        shine.setContactDetail(p.getContactDetail());
        shine.setAddressDetail(p.getAddressDetail());
        shine.setExtra(p.getExtra());
        shine.setPriority(ofNullable(p.getPriority()).orElse(defaultPriority));

        Long stamp = TIME_STAMP_GETTER.get();
        shine.setCreateTime(stamp);
        shine.setUpdateTime(stamp);

        shine.setCreator(mid);
        shine.setUpdater(mid);

        return just(shine);
    };

    private final BiFunction<ShineUpdateParam, Shine, Mono<Shine>> SHINE_UPDATE_PARAM_2_SHINE = (p, t) -> {
        if (isNull(p) || isNull(t))
            throw new BlueException(EMPTY_PARAM);
        p.asserts();

        SHINE_CITY_PACKAGER.accept(t, p.getCityId());

        ofNullable(p.getTitle())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setTitle);
        ofNullable(p.getContent())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setContent);
        ofNullable(p.getDetail())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setDetail);
        ofNullable(p.getContact())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setContact);
        ofNullable(p.getContactDetail())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setContactDetail);
        ofNullable(p.getAddressDetail())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setAddressDetail);
        ofNullable(p.getExtra())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setExtra);
        ofNullable(p.getPriority())
                .ifPresent(t::setPriority);

        t.setUpdateTime(TIME_STAMP_GETTER.get());

        return just(t);
    };

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(ShineSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<ShineCondition, SortOptions> SORT_PROCESSOR = c ->
            process(c, SORT_ATTRIBUTE_MAPPING, ShineSortAttribute.ID.column);

    private static final Function<ShineCondition, Query> CONDITION_PROCESSOR = c -> {
        if (isNotNull(c)) {
            BoolQuery.Builder builder = new BoolQuery.Builder();

            ofNullable(c.getId()).filter(BlueChecker::isValidIdentity).ifPresent(id ->
                    builder.must(TermQuery.of(q -> q.field(ID.name).value(id))._toQuery()));

            ofNullable(c.getTitleLike()).filter(BlueChecker::isNotBlank).ifPresent(titleLike ->
                    builder.must(MatchQuery.of(q -> q.field(TITLE.name).query(titleLike))._toQuery()));

            ofNullable(c.getContentLike()).filter(BlueChecker::isNotBlank).ifPresent(contentLike ->
                    builder.must(MatchQuery.of(q -> q.field(CONTENT.name).query(contentLike))._toQuery()));

            ofNullable(c.getDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(detailLike ->
                    builder.must(MatchQuery.of(q -> q.field(DETAIL.name).query(detailLike))._toQuery()));

            ofNullable(c.getContactLike()).filter(BlueChecker::isNotBlank).ifPresent(contactLike ->
                    builder.must(MatchQuery.of(q -> q.field(CONTACT.name).query(contactLike))._toQuery()));

            ofNullable(c.getContactDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(contactDetailLike ->
                    builder.must(MatchQuery.of(q -> q.field(CONTACT_DETAIL.name).query(contactDetailLike))._toQuery()));

            ofNullable(c.getCountryId()).ifPresent(countryId ->
                    builder.must(TermQuery.of(q -> q.field(COUNTRY_ID.name).value(countryId))._toQuery()));

            ofNullable(c.getStateId()).ifPresent(stateId ->
                    builder.must(TermQuery.of(q -> q.field(STATE_ID.name).value(stateId))._toQuery()));

            ofNullable(c.getCityId()).ifPresent(cityId ->
                    builder.must(TermQuery.of(q -> q.field(CITY_ID.name).value(cityId))._toQuery()));

            ofNullable(c.getAddressDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(addressDetailLike ->
                    builder.must(MatchQuery.of(q -> q.field(ADDRESS_DETAIL.name).query(addressDetailLike))._toQuery()));

            ofNullable(c.getExtra()).filter(BlueChecker::isNotBlank).ifPresent(extra ->
                    builder.must(TermQuery.of(q -> q.field(EXTRA.name).value(extra))._toQuery()));

            ofNullable(c.getPriority()).ifPresent(priority ->
                    builder.must(TermQuery.of(q -> q.field(PRIORITY.name).value(priority))._toQuery()));

            ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                    builder.must(RangeQuery.of(q -> q.field(CREATE_TIME.name).gte(JsonData.of(createTimeBegin)))._toQuery()));

            ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                    builder.must(RangeQuery.of(q -> q.field(CREATE_TIME.name).lte(JsonData.of(createTimeEnd)))._toQuery()));

            ofNullable(c.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                    builder.must(RangeQuery.of(q -> q.field(UPDATE_TIME.name).gte(JsonData.of(updateTimeBegin)))._toQuery()));

            ofNullable(c.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                    builder.must(RangeQuery.of(q -> q.field(UPDATE_TIME.name).lte(JsonData.of(updateTimeEnd)))._toQuery()));

            ofNullable(c.getCreator()).ifPresent(creator ->
                    builder.must(TermQuery.of(q -> q.field(CREATOR.name).value(creator))._toQuery()));

            ofNullable(c.getUpdater()).ifPresent(updater ->
                    builder.must(TermQuery.of(q -> q.field(UPDATER.name).value(updater))._toQuery()));

            return Query.of(b -> b.bool(builder.build()));
        }

        //TODO error
        return Query.of(builder -> builder.matchAll(MatchAllQuery.of(b -> b.boost(1.0f))));
    };

    /**
     * insert shine
     *
     * @param shineInsertParam
     * @param memberId
     * @return
     */
    public Mono<ShineInfo> insertShine(ShineInsertParam shineInsertParam, Long memberId) {
        LOGGER.info("Mono<ShineInfo> insertShine(ShineInsertParam shineInsertParam, Long memberId), shineInsertParam = {}, memberId = {}",
                shineInsertParam, memberId);
        if (shineInsertParam == null)
            throw new BlueException(EMPTY_PARAM);
        shineInsertParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return SHINE_INSERT_PARAM_2_SHINE.apply(shineInsertParam, memberId)
                .flatMap(shineRepository::insert)
                .publishOn(scheduler)
                .flatMap(shine -> {
                    try {
                        shineInsertProducer.send(shine);
                    } catch (Exception e) {
                        LOGGER.error("shineInsertProducer.send(shine) failed, shine = {}, e = {}", shine, e);
                    }
                    return just(shine);
                })
                .flatMap(s -> just(SHINE_2_SHINE_INFO.apply(s)));
    }

    /**
     * insert shine event
     *
     * @param shine
     * @return
     */
    @Override
    public Mono<Boolean> insertShineEvent(Shine shine) {
        LOGGER.info("Mono<Boolean> insertShineEvent(Shine shine), shine = {}", shine);
        if (shine == null)
            throw new BlueException(EMPTY_PARAM);

        CompletableFuture<IndexResponse> responseFuture = elasticsearchAsyncClient.index(request ->
                request.index(INDEX_NAME)
                        .id(String.valueOf(shine.getId()))
                        .document(shine));

        return fromFuture(responseFuture)
                .flatMap(indexResponse -> {
                    LOGGER.info("indexResponse = {}", indexResponse);
                    return just(true);
                })
                .onErrorResume(throwable -> {
                    LOGGER.info("Mono<Boolean> insertShineEvent(Shine shine) failed, shine = {}, throwable = {}", shine, throwable);
                    shineInsertProducer.send(shine);

                    return just(false);
                });
    }

    /**
     * update exist shine
     *
     * @param shineUpdateParam
     * @param memberId
     * @return
     */
    public Mono<ShineInfo> updateShine(ShineUpdateParam shineUpdateParam, Long memberId) {
        LOGGER.info("Mono<ShineInfo> updateShine(ShineUpdateParam shineUpdateParam, Long memberId), shineUpdateParam = {}, memberId = {}", shineUpdateParam, memberId);
        if (isNull(shineUpdateParam))
            throw new BlueException(EMPTY_PARAM);
        shineUpdateParam.asserts();
        if (isInvalidIdentity(memberId))
            throw new BlueException(UNAUTHORIZED);

        return shineRepository.findById(shineUpdateParam.getId())
                .publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(shine ->
                        SHINE_UPDATE_PARAM_2_SHINE.apply(shineUpdateParam, shine)
                )
                .flatMap(shineRepository::save)
                .publishOn(scheduler)
                .flatMap(shine -> {
                    try {
                        shineUpdateProducer.send(shine);
                    } catch (Exception e) {
                        LOGGER.error("shineUpdateProducer.send(shine) failed, shine = {}, e = {}", shine, e);
                    }
                    return just(shine);
                })
                .flatMap(s -> just(SHINE_2_SHINE_INFO.apply(s)));
    }

    /**
     * update shine event
     *
     * @param shine
     * @return
     */
    @Override
    public Mono<Boolean> updateShineEvent(Shine shine) {
        LOGGER.info("Mono<Boolean> updateShineEvent(Shine shine), shine = {}", shine);
        if (shine == null)
            throw new BlueException(EMPTY_PARAM);

        CompletableFuture<UpdateResponse<Shine>> responseFuture = elasticsearchAsyncClient.update(request ->
                request.index(INDEX_NAME)
                        .id(String.valueOf(shine.getId()))
                        .doc(shine), Shine.class);

        return fromFuture(responseFuture)
                .flatMap(updateResponse -> {
                    LOGGER.info("updateResponse = {}", updateResponse);
                    return just(true);
                })
                .onErrorResume(throwable -> {
                    LOGGER.info("Mono<Boolean> updateShineEvent(Shine shine) failed, shine = {}, throwable = {}", shine, throwable);
                    shineUpdateProducer.send(shine);

                    return just(false);
                });
    }

    /**
     * delete shine
     *
     * @param id
     * @return
     */
    @Override
    public Mono<ShineInfo> deleteShine(Long id) {
        LOGGER.info("Mono<ShineInfo> deleteShine(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return shineRepository.findById(id)
                .publishOn(scheduler)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(s ->
                        shineRepository.delete(s).publishOn(scheduler).then(just(s))
                )
                .flatMap(shine -> {
                    try {
                        shineDeleteProducer.send(new IdentityEvent(id));
                    } catch (Exception e) {
                        LOGGER.error("shineDeleteProducer.send(identityEvent) failed, id = {}, e = {}", id, e);
                    }
                    return just(shine);
                })
                .flatMap(a -> just(SHINE_2_SHINE_INFO.apply(a)));
    }

    /**
     * delete shine event
     *
     * @param identityEvent
     * @return
     */
    @Override
    public Mono<Boolean> deleteShineEvent(IdentityEvent identityEvent) {
        LOGGER.info("Mono<Boolean> deleteShineEvent(IdentityEvent identityEvent), identityEvent = {}", identityEvent);
        if (isNull(identityEvent))
            throw new BlueException(EMPTY_PARAM);

        Long id = identityEvent.getId();
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        CompletableFuture<DeleteResponse> responseFuture = elasticsearchAsyncClient.delete(request ->
                request.index(INDEX_NAME)
                        .id(String.valueOf(id)));

        return fromFuture(responseFuture)
                .flatMap(deleteResponse -> {
                    LOGGER.info("deleteResponse = {}", deleteResponse);
                    return just(true);
                })
                .onErrorResume(throwable -> {
                    LOGGER.info("Mono<Boolean> deleteShineEvent(IdentityEvent identityEvent) failed, identityEvent = {}, throwable = {}", identityEvent, throwable);
                    shineDeleteProducer.send(identityEvent);

                    return just(false);
                });
    }

    /**
     * query shine mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<Shine> getShineMono(Long id) {
        LOGGER.info("Mono<Shine> getShineMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        CompletableFuture<GetResponse<Shine>> responseFuture = elasticsearchAsyncClient.get(request ->
                request.index(INDEX_NAME).id(String.valueOf(id)), Shine.class);

        return fromFuture(responseFuture)
                .flatMap(getResponse ->
                        getResponse.found() ?
                                just(getResponse.source())
                                :
                                shineRepository.findById(id).publishOn(scheduler)
                );
    }

    /**
     * query shine info mono by id with assert
     *
     * @param id
     * @return
     */
    @Override
    public Mono<ShineInfo> getShineInfoMonoWithAssert(Long id) {
        LOGGER.info("Mono<ShineInfo> getShineInfoMonoWithAssert(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return just(id)
                .flatMap(this::getShineMono)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(s ->
                        just(SHINE_2_SHINE_INFO.apply(s))
                );
    }

    /**
     * select shine info by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<ShineInfo>> selectShineInfoMonoByIds(List<Long> ids) {
        LOGGER.info("Mono<List<AddressInfo>> selectAddressInfoMonoByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return just(emptyList());
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            return error(() -> new BlueException(PAYLOAD_TOO_LARGE));

        return fromFuture(elasticsearchAsyncClient.search(request ->
                request.index(INDEX_NAME)
                        .query(query ->
                                query.ids(q ->
                                        q.values(ids.stream().map(String::valueOf).collect(toList())))
                        ), Shine.class))
                .map(searchResponse ->
                        searchResponse.hits().hits().stream().map(hit ->
                                        SHINE_2_SHINE_INFO.apply(hit.source()))
                                .collect(toList()))
                .filter(BlueChecker::isNotEmpty)
                .switchIfEmpty(defer(() -> fromIterable(allotByMax(ids, (int) DB_SELECT.value, false))
                        .map(shardIds -> shineRepository.findAllById(shardIds)
                                .publishOn(scheduler)
                                .map(SHINE_2_SHINE_INFO))
                        .reduce(Flux::concat)
                        .flatMap(Flux::collectList)));
    }

    /**
     * select shine info scroll by cursor
     *
     * @param scrollModelRequest
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<ShineInfo, String>> selectShineInfoScrollMonoByScrollAndCursor(ScrollModelRequest<ShineCondition, String> scrollModelRequest) {
        LOGGER.info("Mono<ScrollModelResponse<ShineInfo, String>> selectShineInfoScrollMonoByScrollAndCursor(ScrollModelRequest<ShineCondition, String> scrollModelRequest), " +
                "scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        ofNullable(scrollModelRequest.getCursor())
                .filter(BlueChecker::isNotBlank)
                .ifPresent(cursor -> {
                    if (!isDigits(cursor))
                        throw new BlueException(INVALID_IDENTITY);
                });

        String searchAfter = ofNullable(scrollModelRequest.getCursor()).filter(BlueChecker::isNotBlank).orElse(EMPTY_DATA.value);
        if (isNotBlank(searchAfter) && !isDigits(searchAfter))
            throw new BlueException(INVALID_IDENTITY);

        ShineCondition condition = scrollModelRequest.getCondition();

        return fromFuture(elasticsearchAsyncClient.search(
                SearchRequest.of(builder -> {
                    builder
                            .index(INDEX_NAME)
                            .query(CONDITION_PROCESSOR.apply(condition))
                            .sort(SORT_PROCESSOR.apply(condition))
                            .from(scrollModelRequest.getFrom().intValue())
                            .size(scrollModelRequest.getRows().intValue());

                    packageSearchAfter(builder, searchAfter);

                    return builder;
                }), Shine.class)
        )
                .filter(BlueChecker::isNotNull)
                .flatMap(searchResponse ->
                        ofNullable(searchResponse.hits())
                                .map(HitsMetadata::hits)
                                .filter(BlueChecker::isNotEmpty)
                                .map(EsSearchAfterProcessor::parseSearchAfter)
                                .map(shineStringDataAndSearchAfter ->
                                        just(new ScrollModelResponse<>(shineStringDataAndSearchAfter.getData().stream().map(SHINE_2_SHINE_INFO).collect(toList()),
                                                shineStringDataAndSearchAfter.getSearchAfter()))
                                ).orElseGet(() -> just(new ScrollModelResponse<>(emptyList())))
                )
                .switchIfEmpty(defer(() -> just(new ScrollModelResponse<>(emptyList()))));
    }

    /**
     * select shine info scroll by cursor with pit
     *
     * @param scrollModelRequest
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<ShineInfo, PitCursor>> selectShineInfoScrollMonoByScrollAndCursorBaseOnSnapShot(ScrollModelRequest<ShineCondition, PitCursor> scrollModelRequest) {
        LOGGER.info("Mono<ScrollModelResponse<ShineInfo, PitCursor>> selectShineInfoScrollMonoByScrollAndCursorBaseOnSnapShot(ScrollModelRequest<ShineCondition, PitCursor> scrollModelRequest), " +
                "scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        PitCursor cursor = scrollModelRequest.getCursor();

        ShineCondition condition = scrollModelRequest.getCondition();

        return just(ofNullable(cursor).map(PitCursor::getId).filter(BlueChecker::isNotBlank).orElse(EMPTY_DATA.value))
                .filter(BlueChecker::isNotBlank)
                .switchIfEmpty(defer(() ->
                        fromFuture(elasticsearchAsyncClient.openPointInTime(OpenPointInTimeRequest.of(builder -> builder.index(INDEX_NAME).keepAlive(PIT_TIME))))
                                .flatMap(response -> just(response.id()))
                                .filter(BlueChecker::isNotBlank)
                                .switchIfEmpty(defer(() -> just(EMPTY_DATA.value)))
                )).flatMap(id ->
                        fromFuture(elasticsearchAsyncClient.search(
                                SearchRequest.of(builder -> {
                                    builder
                                            .query(CONDITION_PROCESSOR.apply(condition))
                                            .sort(SORT_PROCESSOR.apply(condition))
                                            .from(scrollModelRequest.getFrom().intValue())
                                            .size(scrollModelRequest.getRows().intValue());

                                    packagePitSearchAfter(builder, ofNullable(cursor).map(PitCursor::getSearchAfters).filter(BlueChecker::isNotEmpty).orElseGet(Collections::emptyList));
                                    packagePit(builder, id, PIT_TIME);

                                    return builder;
                                }), Shine.class)
                        ).filter(BlueChecker::isNotNull)
                                .flatMap(searchResponse ->
                                        ofNullable(searchResponse.hits())
                                                .map(HitsMetadata::hits)
                                                .filter(BlueChecker::isNotEmpty)
                                                .map(EsPitSearchAfterProcessor::parsePitSearchAfter)
                                                .map(dataAndSearchAfter ->
                                                        just(new ScrollModelResponse<>(dataAndSearchAfter.getData().stream().map(SHINE_2_SHINE_INFO).collect(toList()),
                                                                new PitCursor(parsePit(searchResponse), dataAndSearchAfter.getSearchAfters())))
                                                ).orElseGet(() -> just(new ScrollModelResponse<>(emptyList(), new PitCursor())))
                                )
                                .switchIfEmpty(defer(() -> just(new ScrollModelResponse<>(emptyList(), new PitCursor())))));
    }

    /**
     * select shine info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<ShineInfo>> selectShineInfoPageMonoByPageAndCondition(PageModelRequest<ShineCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<AddressInfo>> selectAddressInfoPageMonoByPageAndCondition(PageModelRequest<AddressCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        ShineCondition condition = pageModelRequest.getCondition();

        return fromFuture(elasticsearchAsyncClient.search(
                SearchRequest.of(builder -> builder
                        .index(INDEX_NAME)
                        .query(CONDITION_PROCESSOR.apply(condition))
                        .sort(SORT_PROCESSOR.apply(condition))
                        .from(pageModelRequest.getLimit().intValue())
                        .size(pageModelRequest.getRows().intValue())
                ), Shine.class)
        )
                .filter(BlueChecker::isNotNull)
                .flatMap(searchResponse -> {
                    HitsMetadata<Shine> hits = searchResponse.hits();
                    return just(new PageModelResponse<>(hits.hits().stream().map(Hit::source).map(SHINE_2_SHINE_INFO).collect(toList()),
                            hits.total().value()));
                })
                .switchIfEmpty(defer(() -> just(new PageModelResponse<>(emptyList(), 0L))));
    }

}
