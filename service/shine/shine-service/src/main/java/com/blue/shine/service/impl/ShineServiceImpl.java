package com.blue.shine.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.*;
import com.blue.base.api.model.CityRegion;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.*;
import com.blue.basic.model.event.IdentityEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.config.deploy.DefaultPriorityDeploy;
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
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
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
import static com.blue.mongo.common.SortConverter.convert;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static com.blue.shine.constant.ColumnName.*;
import static com.blue.shine.converter.ShineModelConverters.SHINES_2_SHINES_INFO;
import static com.blue.shine.converter.ShineModelConverters.SHINE_2_SHINE_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
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

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final Scheduler scheduler;

    private final ElasticsearchAsyncClient elasticsearchAsyncClient;

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    private final ShineInsertProducer shineInsertProducer;

    private final ShineUpdateProducer shineUpdateProducer;

    private final ShineDeleteProducer shineDeleteProducer;

    private final ShineRepository shineRepository;

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler, ElasticsearchAsyncClient elasticsearchAsyncClient,
                            RpcCityServiceConsumer rpcCityServiceConsumer, ShineRepository shineRepository, ShineInsertProducer shineInsertProducer, ShineUpdateProducer shineUpdateProducer,
                            ShineDeleteProducer shineDeleteProducer, DefaultPriorityDeploy defaultPriorityDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.shineRepository = shineRepository;
        this.shineInsertProducer = shineInsertProducer;
        this.shineUpdateProducer = shineUpdateProducer;
        this.shineDeleteProducer = shineDeleteProducer;

        this.DEFAULT_PRIORITY = defaultPriorityDeploy.getPriority();
    }

    private static final String INDEX_NAME = "shine";

    private int DEFAULT_PRIORITY;

    private final BiConsumer<Shine, Long> SHINE_CITY_PACKAGER = (shine, cid) -> {
        if (isNull(shine) || isInvalidIdentity(cid))
            return;

        CityRegion cityRegion = rpcCityServiceConsumer.getCityRegionById(cid)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .toFuture().join();

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
        shine.setPriority(ofNullable(p.getPriority()).orElse(DEFAULT_PRIORITY));

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

    private static final Function<ShineCondition, Sort> SORTER_CONVERTER = c ->
            convert(c, SORT_ATTRIBUTE_MAPPING, ShineSortAttribute.ID.column);

    private static final Function<ShineCondition, Query> CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (isNull(c)) {
            query.with(SORTER_CONVERTER.apply(new ShineCondition()));
            return query;
        }

        Shine probe = new Shine();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getTitleLike()).filter(BlueChecker::isNotBlank).ifPresent(titleLike ->
                query.addCriteria(where(TITLE.name).regex(compile(PREFIX.element + titleLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getContentLike()).filter(BlueChecker::isNotBlank).ifPresent(contentLike ->
                query.addCriteria(where(CONTENT.name).regex(compile(PREFIX.element + contentLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(detailLike ->
                query.addCriteria(where(DETAIL.name).regex(compile(PREFIX.element + detailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getContactLike()).filter(BlueChecker::isNotBlank).ifPresent(contactLike ->
                query.addCriteria(where(CONTACT.name).regex(compile(PREFIX.element + contactLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getContactDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(contactDetailLike ->
                query.addCriteria(where(CONTACT_DETAIL.name).regex(compile(PREFIX.element + contactDetailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getCountryId()).ifPresent(probe::setCountryId);
        ofNullable(c.getStateId()).ifPresent(probe::setStateId);
        ofNullable(c.getCityId()).ifPresent(probe::setCityId);
        ofNullable(c.getContactDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(addressDetailLike ->
                query.addCriteria(where(ADDRESS_DETAIL.name).regex(compile(PREFIX.element + addressDetailLike + SUFFIX.element, CASE_INSENSITIVE))));
        ofNullable(c.getExtra()).filter(BlueChecker::isNotBlank).ifPresent(probe::setExtra);
        ofNullable(c.getPriority()).ifPresent(probe::setPriority);

        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));

        ofNullable(c.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                query.addCriteria(where(UPDATE_TIME.name).gte(updateTimeBegin)));
        ofNullable(c.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                query.addCriteria(where(UPDATE_TIME.name).lte(updateTimeEnd)));

        ofNullable(c.getCreator()).ifPresent(probe::setCreator);
        ofNullable(c.getUpdater()).ifPresent(probe::setUpdater);

        query.addCriteria(byExample(probe));

        query.with(SORTER_CONVERTER.apply(c));

        return query;
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
     * select shine by page and query
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    public Mono<List<Shine>> selectShineMonoByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<Shine>> selectShineMonoByLimitAndQuery(Long limit, Long rows, Query query), " +
                "limit = {}, rows = {}, query = {}", limit, rows, query);
        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, Shine.class).publishOn(scheduler).collectList();
    }

    /**
     * count shine by query
     *
     * @param query
     * @return
     */
    public Mono<Long> countShineMonoByQuery(Query query) {
        LOGGER.info("Mono<Long> countAddressMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, Shine.class).publishOn(scheduler);
    }

    /**
     * select shine info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    public Mono<PageModelResponse<ShineInfo>> selectShineInfoPageMonoByPageAndCondition(PageModelRequest<ShineCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<AddressInfo>> selectAddressInfoPageMonoByPageAndCondition(PageModelRequest<AddressCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectShineMonoByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countShineMonoByQuery(query)
        ).flatMap(tuple2 ->
                just(new PageModelResponse<>(SHINES_2_SHINES_INFO.apply(tuple2.getT1()), tuple2.getT2()))
        );
    }

    /**
     * select shine info scroll by cursor
     *
     * @param scrollModelRequest
     * @return
     */
    public Mono<ScrollModelResponse<ShineInfo, Pit>> selectShineInfoScrollMonoByScrollAndCursor(ScrollModelRequest<Pit> scrollModelRequest) {
        return null;
    }

}
