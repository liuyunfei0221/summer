package com.blue.shine.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch._types.SortOptions;
import co.elastic.clients.elasticsearch._types.Time;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.elasticsearch.core.OpenPointInTimeRequest;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.HitsMetadata;
import co.elastic.clients.json.JsonData;
import com.blue.base.api.model.CityRegion;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.*;
import com.blue.basic.model.event.IdentityEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.es.model.PitCursor;
import com.blue.es.model.QueryAndHighlightColumns;
import com.blue.es.model.SearchAfterCursor;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.config.deploy.DefaultPriorityDeploy;
import com.blue.shine.config.deploy.FuzzinessDeploy;
import com.blue.shine.config.deploy.HighlightDeploy;
import com.blue.shine.config.deploy.PitDeploy;
import com.blue.shine.constant.ShineSortAttribute;
import com.blue.shine.event.producer.ShineDeleteProducer;
import com.blue.shine.event.producer.ShineInsertProducer;
import com.blue.shine.event.producer.ShineUpdateProducer;
import com.blue.shine.model.ShineCondition;
import com.blue.shine.model.ShineInsertParam;
import com.blue.shine.model.ShineUpdateParam;
import com.blue.shine.repository.entity.Shine;
import com.blue.shine.repository.template.ShineRepository;
import com.blue.shine.service.inter.CityService;
import com.blue.shine.service.inter.ShineService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.*;
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
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.es.common.EsPitProcessor.packagePit;
import static com.blue.es.common.EsPitProcessor.parsePit;
import static com.blue.es.common.EsSearchAfterProcessor.packageSearchAfter;
import static com.blue.es.common.EsSearchAfterProcessor.parseSearchAfter;
import static com.blue.es.common.EsSortProcessor.process;
import static com.blue.es.common.HighlightProcessor.packageHighlight;
import static com.blue.es.common.HighlightProcessor.parseHighlight;
import static com.blue.shine.constant.ShineColumnName.*;
import static com.blue.shine.constant.ShineTableName.SHINE;
import static com.blue.shine.converter.ShineModelConverters.SHINE_2_SHINE_INFO;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
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

    private ElasticsearchAsyncClient elasticsearchAsyncClient;

    private CityService cityService;

    private final ShineInsertProducer shineInsertProducer;

    private final ShineUpdateProducer shineUpdateProducer;

    private final ShineDeleteProducer shineDeleteProducer;

    private final ShineRepository shineRepository;

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ElasticsearchAsyncClient elasticsearchAsyncClient,
                            CityService cityService, ShineRepository shineRepository, ShineInsertProducer shineInsertProducer, ShineUpdateProducer shineUpdateProducer,
                            ShineDeleteProducer shineDeleteProducer, PitDeploy pitDeploy, FuzzinessDeploy fuzzinessDeploy, HighlightDeploy highlightDeploy,
                            DefaultPriorityDeploy defaultPriorityDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.elasticsearchAsyncClient = elasticsearchAsyncClient;
        this.cityService = cityService;
        this.shineRepository = shineRepository;
        this.shineInsertProducer = shineInsertProducer;
        this.shineUpdateProducer = shineUpdateProducer;
        this.shineDeleteProducer = shineDeleteProducer;

        this.pitTime = Time.of(builder -> builder.time(pitDeploy.getTime()));
        this.fuzziness = ofNullable(fuzzinessDeploy.getFuzziness()).filter(f -> f >= 0 && f <= 2).map(String::valueOf).orElseThrow(() -> new RuntimeException("Invalid Fuzziness"));
        this.preTags = ofNullable(highlightDeploy.getPreTags()).orElseGet(() -> singletonList(EMPTY_VALUE.value));
        this.postTags = ofNullable(highlightDeploy.getPostTags()).orElseGet(() -> singletonList(EMPTY_VALUE.value));

        this.defaultPriority = defaultPriorityDeploy.getPriority();
    }

    private Time pitTime;

    private String fuzziness;

    private List<String> preTags;

    private List<String> postTags;

    private int defaultPriority;

    private static final String INDEX_NAME = SHINE.name;

    private final BiConsumer<Shine, Long> SHINE_CITY_PACKAGER = (shine, cid) -> {
        if (isNull(shine) || isInvalidIdentity(cid))
            return;

        CityRegion cityRegion = cityService.getCityRegionById(cid);

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

    private static final Function<ShineCondition, SortOptions> SORT_PROCESSOR = c -> {
        String sortAttribute = ofNullable(c).map(ShineCondition::getSortAttribute)
                .map(SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(ShineSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(ShineCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return sortAttribute.equals(ShineSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, ShineSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Map<String, BiConsumer<List<String>, Shine>> HIGH_LIGHT_PROCESSORS;

    static {
        HIGH_LIGHT_PROCESSORS = new HashMap<>();

        HIGH_LIGHT_PROCESSORS.put(TITLE.name, (highlights, entity) -> {
            if (isNotEmpty(highlights) && isNotNull(entity))
                entity.setTitle(highlights.get(0));
        });

        HIGH_LIGHT_PROCESSORS.put(CONTENT.name, (highlights, entity) -> {
            if (isNotEmpty(highlights) && isNotNull(entity))
                entity.setContent(highlights.get(0));
        });

        HIGH_LIGHT_PROCESSORS.put(DETAIL.name, (highlights, entity) -> {
            if (isNotEmpty(highlights) && isNotNull(entity))
                entity.setDetail(highlights.get(0));
        });

        HIGH_LIGHT_PROCESSORS.put(CONTACT.name, (highlights, entity) -> {
            if (isNotEmpty(highlights) && isNotNull(entity))
                entity.setContact(highlights.get(0));
        });

        HIGH_LIGHT_PROCESSORS.put(CONTACT_DETAIL.name, (highlights, entity) -> {
            if (isNotEmpty(highlights) && isNotNull(entity))
                entity.setContactDetail(highlights.get(0));
        });
    }

    private final Function<ShineCondition, QueryAndHighlightColumns> CONDITION_PROCESSOR = c -> {
        if (isNull(c))
            return new QueryAndHighlightColumns(Query.of(builder -> builder.matchAll(MatchAllQuery.of(b -> b.boost(1.0f)))), emptyList());

        BoolQuery.Builder builder = new BoolQuery.Builder();
        List<String> highlightColumns = new LinkedList<>();

        ofNullable(c.getId()).filter(BlueChecker::isValidIdentity).ifPresent(id ->
                builder.must(TermQuery.of(b -> b.field(ID.name).value(id))._toQuery()));

        ofNullable(c.getTitleLike()).filter(BlueChecker::isNotBlank).ifPresent(titleLike -> {
            builder.must(FuzzyQuery.of(b -> b.field(TITLE.name).value(titleLike).fuzziness(fuzziness))._toQuery());
            highlightColumns.add(TITLE.name);
        });

        ofNullable(c.getContentLike()).filter(BlueChecker::isNotBlank).ifPresent(contentLike -> {
            builder.must(FuzzyQuery.of(b -> b.field(CONTENT.name).value(contentLike).fuzziness(fuzziness))._toQuery());
            highlightColumns.add(CONTENT.name);
        });

        ofNullable(c.getDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(detailLike -> {
            builder.must(FuzzyQuery.of(b -> b.field(DETAIL.name).value(detailLike).fuzziness(fuzziness))._toQuery());
            highlightColumns.add(DETAIL.name);
        });

        ofNullable(c.getContactLike()).filter(BlueChecker::isNotBlank).ifPresent(contactLike -> {
            builder.must(FuzzyQuery.of(b -> b.field(CONTACT.name).value(contactLike).fuzziness(fuzziness))._toQuery());
            highlightColumns.add(CONTACT.name);
        });

        ofNullable(c.getContactDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(contactDetailLike -> {
            builder.must(FuzzyQuery.of(b -> b.field(CONTACT_DETAIL.name).value(contactDetailLike).fuzziness(fuzziness))._toQuery());
            highlightColumns.add(CONTACT_DETAIL.name);
        });

        ofNullable(c.getCountryId()).ifPresent(countryId ->
                builder.must(TermQuery.of(b -> b.field(COUNTRY_ID.name).value(countryId))._toQuery()));

        ofNullable(c.getStateId()).ifPresent(stateId ->
                builder.must(TermQuery.of(b -> b.field(STATE_ID.name).value(stateId))._toQuery()));

        ofNullable(c.getCityId()).ifPresent(cityId ->
                builder.must(TermQuery.of(b -> b.field(CITY_ID.name).value(cityId))._toQuery()));

        ofNullable(c.getAddressDetailLike()).filter(BlueChecker::isNotBlank).ifPresent(addressDetailLike ->
                builder.must(MatchQuery.of(b -> b.field(ADDRESS_DETAIL.name).query(addressDetailLike))._toQuery()));

        ofNullable(c.getExtra()).filter(BlueChecker::isNotBlank).ifPresent(extra ->
                builder.must(TermQuery.of(b -> b.field(EXTRA.name).value(extra))._toQuery()));

        ofNullable(c.getPriority()).ifPresent(priority ->
                builder.must(TermQuery.of(b -> b.field(PRIORITY.name).value(priority))._toQuery()));

        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                builder.must(RangeQuery.of(b -> b.field(CREATE_TIME.name).gte(JsonData.of(createTimeBegin)))._toQuery()));

        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                builder.must(RangeQuery.of(b -> b.field(CREATE_TIME.name).lte(JsonData.of(createTimeEnd)))._toQuery()));

        ofNullable(c.getUpdateTimeBegin()).ifPresent(updateTimeBegin ->
                builder.must(RangeQuery.of(b -> b.field(UPDATE_TIME.name).gte(JsonData.of(updateTimeBegin)))._toQuery()));

        ofNullable(c.getUpdateTimeEnd()).ifPresent(updateTimeEnd ->
                builder.must(RangeQuery.of(b -> b.field(UPDATE_TIME.name).lte(JsonData.of(updateTimeEnd)))._toQuery()));

        ofNullable(c.getCreator()).ifPresent(creator ->
                builder.must(TermQuery.of(b -> b.field(CREATOR.name).value(creator))._toQuery()));

        ofNullable(c.getUpdater()).ifPresent(updater ->
                builder.must(TermQuery.of(b -> b.field(UPDATER.name).value(updater))._toQuery()));

        return new QueryAndHighlightColumns(Query.of(b -> b.bool(builder.build())), highlightColumns);
    };

    private final Function<ScrollModelRequest<ShineCondition, SearchAfterCursor>, Mono<SearchRequest>> SEARCH_REQUEST_PARSER_FOR_SCROLL = scrollModelRequest -> {
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        ShineCondition condition = scrollModelRequest.getCondition();
        QueryAndHighlightColumns queryAndHighlightColumns = CONDITION_PROCESSOR.apply(condition);

        return just(SearchRequest.of(builder -> {
            builder
                    .index(INDEX_NAME)
                    .query(queryAndHighlightColumns.getQuery())
                    .sort(SORT_PROCESSOR.apply(condition))
                    .from(scrollModelRequest.getFrom().intValue())
                    .size(scrollModelRequest.getRows().intValue());

            packageHighlight(builder, queryAndHighlightColumns.getColumns(), preTags, postTags);
            packageSearchAfter(builder, ofNullable(scrollModelRequest.getCursor())
                    .map(SearchAfterCursor::getSearchAfter)
                    .filter(BlueChecker::isNotEmpty).orElseGet(Collections::emptyList));

            return builder;
        }));
    };

    private final Function<ScrollModelRequest<ShineCondition, PitCursor>, Mono<SearchRequest>> SEARCH_REQUEST_PARSER_FOR_SCROLL_WITH_PIT = scrollModelRequest -> {
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        PitCursor cursor = scrollModelRequest.getCursor();

        ShineCondition condition = scrollModelRequest.getCondition();
        QueryAndHighlightColumns queryAndHighlightColumns = CONDITION_PROCESSOR.apply(condition);

        return justOrEmpty(cursor).map(PitCursor::getId).filter(BlueChecker::isNotBlank)
                .switchIfEmpty(defer(() ->
                        fromFuture(elasticsearchAsyncClient.openPointInTime(OpenPointInTimeRequest.of(builder -> builder.index(INDEX_NAME).keepAlive(pitTime))))
                                .flatMap(response -> just(response.id()))
                                .filter(BlueChecker::isNotBlank)
                                .switchIfEmpty(defer(() -> just(EMPTY_VALUE.value)))
                )).map(id ->
                        SearchRequest.of(builder -> {
                            builder
                                    .query(queryAndHighlightColumns.getQuery())
                                    .sort(SORT_PROCESSOR.apply(condition))
                                    .from(scrollModelRequest.getFrom().intValue())
                                    .size(scrollModelRequest.getRows().intValue());

                            packageHighlight(builder, queryAndHighlightColumns.getColumns(), preTags, postTags);
                            packageSearchAfter(builder, ofNullable(cursor).map(PitCursor::getSearchAfter).filter(BlueChecker::isNotEmpty).orElseGet(Collections::emptyList));
                            packagePit(builder, id, pitTime);

                            return builder;
                        })
                );
    };

    private final Function<PageModelRequest<ShineCondition>, Mono<SearchRequest>> SEARCH_REQUEST_PARSER_FOR_PAGE = pageModelRequest -> {
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        ShineCondition condition = pageModelRequest.getCondition();

        QueryAndHighlightColumns queryAndHighlightColumns = CONDITION_PROCESSOR.apply(condition);

        return just(SearchRequest.of(builder -> {
            builder
                    .index(INDEX_NAME)
                    .query(queryAndHighlightColumns.getQuery())
                    .sort(SORT_PROCESSOR.apply(condition))
                    .from(pageModelRequest.getLimit().intValue())
                    .size(pageModelRequest.getRows().intValue());

            packageHighlight(builder, queryAndHighlightColumns.getColumns(), preTags, postTags);

            return builder;
        }));
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

                .flatMap(shine -> {
                    try {
                        shineInsertProducer.send(shine);
                    } catch (Exception e) {
                        LOGGER.error("shineInsertProducer.send(shine) failed, shine = {}, e = {}", shine, e);
                    }
                    return just(shine);
                })
                .flatMap(shine -> just(SHINE_2_SHINE_INFO.apply(shine)));
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

        return fromFuture(elasticsearchAsyncClient.index(request ->
                request.index(INDEX_NAME)
                        .id(String.valueOf(shine.getId()))
                        .document(shine)))
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

                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(shine ->
                        SHINE_UPDATE_PARAM_2_SHINE.apply(shineUpdateParam, shine)
                )
                .flatMap(shineRepository::save)

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

        return fromFuture(elasticsearchAsyncClient.update(request ->
                request.index(INDEX_NAME)
                        .id(String.valueOf(shine.getId()))
                        .doc(shine), Shine.class))
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

                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .flatMap(s ->
                        shineRepository.delete(s).then(just(s))
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

        return fromFuture(elasticsearchAsyncClient.delete(request ->
                request.index(INDEX_NAME)
                        .id(String.valueOf(id))))
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

        return fromFuture(elasticsearchAsyncClient.get(request ->
                request.index(INDEX_NAME).id(String.valueOf(id)), Shine.class))
                .flatMap(getResponse ->
                        getResponse.found() ?
                                just(getResponse.source())
                                :
                                shineRepository.findById(id)
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
    public Mono<ScrollModelResponse<ShineInfo, SearchAfterCursor>> selectShineInfoScrollMonoByScrollAndCursor(ScrollModelRequest<ShineCondition, SearchAfterCursor> scrollModelRequest) {
        LOGGER.info("Mono<ScrollModelResponse<ShineInfo, SearchAfterCursor>> selectShineInfoScrollMonoByScrollAndCursor(ScrollModelRequest<ShineCondition, SearchAfterCursor> scrollModelRequest), " +
                "scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        return SEARCH_REQUEST_PARSER_FOR_SCROLL.apply(scrollModelRequest)
                .flatMap(searchRequest -> fromFuture(elasticsearchAsyncClient.search(searchRequest, Shine.class)))
                .flatMap(searchResponse ->
                        ofNullable(searchResponse.hits())
                                .map(HitsMetadata::hits)
                                .filter(BlueChecker::isNotEmpty)
                                .map(hits ->
                                        just(new ScrollModelResponse<>(parseHighlight(hits, HIGH_LIGHT_PROCESSORS).stream().map(SHINE_2_SHINE_INFO).collect(toList()),
                                                new SearchAfterCursor(parseSearchAfter(hits))))
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

        return SEARCH_REQUEST_PARSER_FOR_SCROLL_WITH_PIT.apply(scrollModelRequest)
                .flatMap(searchRequest -> fromFuture(elasticsearchAsyncClient.search(searchRequest, Shine.class)))
                .flatMap(searchResponse ->
                        ofNullable(searchResponse.hits())
                                .map(HitsMetadata::hits)
                                .filter(BlueChecker::isNotEmpty)
                                .map(hits ->
                                        just(new ScrollModelResponse<>(parseHighlight(hits, HIGH_LIGHT_PROCESSORS).stream().map(SHINE_2_SHINE_INFO).collect(toList()),
                                                new PitCursor(parsePit(searchResponse), parseSearchAfter(hits))))
                                ).orElseGet(() -> just(new ScrollModelResponse<>(emptyList(), new PitCursor())))
                )
                .switchIfEmpty(defer(() -> just(new ScrollModelResponse<>(emptyList()))));
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

        return SEARCH_REQUEST_PARSER_FOR_PAGE.apply(pageModelRequest)
                .flatMap(searchRequest -> fromFuture(elasticsearchAsyncClient.search(searchRequest, Shine.class)))
                .flatMap(searchResponse -> {
                    HitsMetadata<Shine> hits = searchResponse.hits();
                    return just(new PageModelResponse<>(parseHighlight(hits.hits(), HIGH_LIGHT_PROCESSORS).stream().map(SHINE_2_SHINE_INFO).collect(toList()),
                            hits.total().value()));
                })
                .switchIfEmpty(defer(() -> just(new PageModelResponse<>(emptyList(), 0L))));
    }

}
