package com.blue.shine.service.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkOperation;
import com.blue.base.api.model.CityRegion;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.*;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.shine.api.model.ShineInfo;
import com.blue.shine.constant.ShineSortAttribute;
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
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isInvalidIdentity;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.TIME_STAMP_GETTER;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.mongo.common.SortConverter.convert;
import static com.blue.mongo.constant.LikeElement.PREFIX;
import static com.blue.mongo.constant.LikeElement.SUFFIX;
import static com.blue.shine.constant.ColumnName.*;
import static java.util.Optional.ofNullable;
import static java.util.regex.Pattern.CASE_INSENSITIVE;
import static java.util.regex.Pattern.compile;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * shine service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class ShineServiceImpl implements ShineService {

    private static final Logger LOGGER = getLogger(ShineServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final Scheduler scheduler;

    private final ElasticsearchClient elasticsearchClient;

    private RpcCityServiceConsumer rpcCityServiceConsumer;

    private final ShineRepository shineRepository;

    public ShineServiceImpl(BlueIdentityProcessor blueIdentityProcessor, ReactiveMongoTemplate reactiveMongoTemplate, Scheduler scheduler,
                            ElasticsearchClient elasticsearchClient, RpcCityServiceConsumer rpcCityServiceConsumer, ShineRepository shineRepository) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.scheduler = scheduler;
        this.elasticsearchClient = elasticsearchClient;
        this.rpcCityServiceConsumer = rpcCityServiceConsumer;
        this.shineRepository = shineRepository;
    }

    private final int DEFAULT_ORDER = 0;

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
        shine.setOrder(ofNullable(p.getOrder()).orElse(DEFAULT_ORDER));

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
                .filter(BlueChecker::isNotBlank).ifPresent(t::setAddressDetail);
        ofNullable(p.getAddressDetail())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setTitle);
        ofNullable(p.getExtra())
                .filter(BlueChecker::isNotBlank).ifPresent(t::setExtra);
        ofNullable(p.getOrder())
                .ifPresent(t::setOrder);

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
        ofNullable(c.getOrder()).ifPresent(probe::setOrder);

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


    @PostConstruct
    private void init() {

        Long stamp = TIME_STAMP_GETTER.get();

        List<Shine> elements = new LinkedList<>();
        List<BulkOperation> bulkOperations = new LinkedList<>();

        for (int i = 0; i <= 1000; i++) {
            long id = blueIdentityProcessor.generate(Shine.class);
//            Shine shine = new Shine(id, "title-" + id, "content-" + id, 1, stamp, stamp, 1L, 1L);
            Shine shine = new Shine();

            elements.add(shine);
            bulkOperations.add(new BulkOperation.Builder().create(d -> d.document(shine)).build());

//            try {
//                IndexResponse indexResponse = elasticsearchClient.index(idx ->
//                        idx.index("shine")
//                                .id(String.valueOf(id))
//                                .document(shine));
//                System.err.println(indexResponse);
//            } catch (Exception e) {
//                LOGGER.error("index() failed, e = {0}", e);
//            }
        }

        try {
            BulkResponse bulkResponse = elasticsearchClient.bulk(e -> e.index("shine").operations(bulkOperations));
            System.err.println(bulkResponse);
        } catch (IOException e) {
            LOGGER.error("bulk() failed, e = {0}", e);
        }

        shineRepository.saveAll(elements).subscribe(System.err::println);
    }

    /**
     * insert shine
     *
     * @param shineInsertParam
     * @param memberId
     * @return
     */
    public Mono<ShineInfo> insertShine(ShineInsertParam shineInsertParam, Long memberId) {
        return null;
    }

    /**
     * update exist shine
     *
     * @param shineUpdateParam
     * @param memberId
     * @return
     */
    public Mono<ShineInfo> updateShine(ShineUpdateParam shineUpdateParam, Long memberId) {
        return null;
    }

    /**
     * delete shine
     *
     * @param id
     * @param memberId
     * @return
     */
    public Mono<ShineInfo> deleteShine(Long id, Long memberId) {
        return null;
    }

    /**
     * query shine mono by id
     *
     * @param id
     * @return
     */
    public Mono<Shine> getShineMono(Long id) {
        return null;
    }

    /**
     * query shine info mono by id with assert
     *
     * @param id
     * @return
     */
    public Mono<ShineInfo> getShineInfoMonoByPrimaryKeyWithAssert(Long id) {
        return null;
    }

    /**
     * select shine info by ids
     *
     * @param ids
     * @return
     */
    public Mono<List<ShineInfo>> selectShineInfoMonoByIds(List<Long> ids) {
        return null;
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
        return null;
    }

    /**
     * count shine by query
     *
     * @param query
     * @return
     */
    public Mono<Long> countShineMonoByQuery(Query query) {
        return null;
    }

    /**
     * select shine info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    public Mono<PageModelResponse<ShineInfo>> selectShineInfoPageMonoByPageAndCondition(PageModelRequest<ShineCondition> pageModelRequest) {
        return null;
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
