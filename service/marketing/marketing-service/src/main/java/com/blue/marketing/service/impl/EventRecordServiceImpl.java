package com.blue.marketing.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.constant.common.SortType;
import com.blue.basic.model.common.*;
import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.constant.EventRecordSortAttribute;
import com.blue.marketing.model.EventRecordCondition;
import com.blue.marketing.model.EventRecordManagerCondition;
import com.blue.marketing.model.EventRecordManagerInfo;
import com.blue.marketing.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.marketing.repository.entity.EventRecord;
import com.blue.marketing.repository.template.EventRecordRepository;
import com.blue.marketing.service.inter.EventRecordService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_WRITE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.marketing.constant.EventRecordColumnName.CREATE_TIME;
import static com.blue.marketing.converter.MarketingModelConverters.EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER;
import static com.blue.marketing.converter.MarketingModelConverters.EVENT_RECORD_2_EVENT_RECORD_MANAGER_INFO_CONVERTER;
import static com.blue.mongo.common.MongoSearchAfterProcessor.packageSearchAfter;
import static com.blue.mongo.common.MongoSearchAfterProcessor.parseSearchAfter;
import static com.blue.mongo.common.MongoSortProcessor.process;
import static com.blue.mongo.constant.SortSchema.DESC;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.data.mongodb.core.query.Criteria.byExample;
import static org.springframework.data.mongodb.core.query.Criteria.where;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * event record service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class EventRecordServiceImpl implements EventRecordService {

    private static final Logger LOGGER = getLogger(EventRecordServiceImpl.class);

    private final RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    private final EventRecordRepository eventRecordRepository;

    public EventRecordServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, ReactiveMongoTemplate reactiveMongoTemplate,
                                  EventRecordRepository eventRecordRepository) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.reactiveMongoTemplate = reactiveMongoTemplate;
        this.eventRecordRepository = eventRecordRepository;
    }

    private static final BiFunction<ScrollModelRequest<EventRecordCondition, Long>, Long, Query> SCROLL_MODEL_REQUEST_PROCESSOR = (smr, mid) -> {
        if (isInvalidIdentity(mid))
            throw new BlueException(UNAUTHORIZED);
        if (isNull(smr))
            throw new BlueException(EMPTY_PARAM);

        EventRecordCondition c = smr.getCondition();

        Query query = new Query();

        EventRecord probe = new EventRecord();

        probe.setMemberId(mid);

        if (isNotNull(c)) {
            ofNullable(c.getType()).ifPresent(probe::setType);
            ofNullable(c.getStatus()).ifPresent(probe::setStatus);

            ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                    query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
            ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                    query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));
        }

        query.addCriteria(byExample(probe));

        packageSearchAfter(query, DESC.sortType.identity, EventRecordSortAttribute.ID.column, smr.getCursor());
        query.with(process(List.of(new SortElement(EventRecordSortAttribute.CREATE_TIME.column, DESC.sortType.identity),
                new SortElement(EventRecordSortAttribute.ID.column, DESC.sortType.identity))));

        query.limit(smr.getRows().intValue());

        return query;
    };

    private static final Map<String, String> MANAGER_SORT_ATTRIBUTE_MAPPING = Stream.of(EventRecordSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final Function<EventRecordManagerCondition, Sort> MANAGER_SORTER_CONVERTER = c -> {
        String sortAttribute = ofNullable(c).map(EventRecordManagerCondition::getSortAttribute)
                .map(MANAGER_SORT_ATTRIBUTE_MAPPING::get)
                .filter(BlueChecker::isNotBlank)
                .orElse(EventRecordSortAttribute.CREATE_TIME.column);

        String sortType = ofNullable(c).map(EventRecordManagerCondition::getSortType)
                .filter(BlueChecker::isNotBlank)
                .orElse(SortType.DESC.identity);

        return sortAttribute.equals(EventRecordSortAttribute.ID.column) ?
                process(singletonList(new SortElement(sortAttribute, sortType)))
                :
                process(Stream.of(sortAttribute, EventRecordSortAttribute.ID.column)
                        .map(attr -> new SortElement(attr, sortType)).collect(toList()));
    };

    private static final Function<EventRecordManagerCondition, Query> MANAGER_CONDITION_PROCESSOR = c -> {
        Query query = new Query();

        if (c == null) {
            query.with(MANAGER_SORTER_CONVERTER.apply(new EventRecordManagerCondition()));
            return query;
        }

        EventRecord probe = new EventRecord();

        ofNullable(c.getId()).ifPresent(probe::setId);
        ofNullable(c.getMemberId()).ifPresent(probe::setMemberId);
        ofNullable(c.getType()).ifPresent(probe::setType);
        ofNullable(c.getStatus()).ifPresent(probe::setStatus);

        ofNullable(c.getCreateTimeBegin()).ifPresent(createTimeBegin ->
                query.addCriteria(where(CREATE_TIME.name).gte(createTimeBegin)));
        ofNullable(c.getCreateTimeEnd()).ifPresent(createTimeEnd ->
                query.addCriteria(where(CREATE_TIME.name).lte(createTimeEnd)));
        query.addCriteria(byExample(probe));

        query.with(MANAGER_SORTER_CONVERTER.apply(c));

        return query;
    };

    /**
     * insert event record
     *
     * @param eventRecord
     * @return
     */
    @Override
    public Mono<EventRecord> insertEventRecord(EventRecord eventRecord) {
        LOGGER.info("Mono<EventRecord> insertEventRecord(EventRecord eventRecord), eventRecord = {}", eventRecord);
        if (eventRecord == null)
            throw new BlueException(EMPTY_PARAM);

        return eventRecordRepository.insert(eventRecord);
    }

    /**
     * insert event record batch
     *
     * @param eventRecords
     * @return
     */
    @Override
    public Mono<List<EventRecord>> insertEventRecords(List<EventRecord> eventRecords) {
        LOGGER.info("Mono<List<EventRecord>> insertEventRecords(List<EventRecord> eventRecords), eventRecords = {}", eventRecords);

        return isNotEmpty(eventRecords) ?
                fromIterable(allotByMax(eventRecords, (int) DB_WRITE.value, false))
                        .map(eventRecordRepository::saveAll)
                        .reduce(Flux::concat)
                        .flatMap(Flux::collectList)
                :
                just(emptyList());
    }

    /**
     * get event record mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<EventRecord> getEventRecord(Long id) {
        LOGGER.info(" Mono<EventRecord> getEventRecordMono(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return eventRecordRepository.findById(id);
    }

    /**
     * get event record info mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<EventRecordInfo> getEventRecordInfo(Long id) {
        return getEventRecord(id).map(EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER);
    }

    /**
     * select event record info by scroll and member id
     *
     * @param scrollModelRequest
     * @param memberId
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<EventRecordInfo, String>> selectEventRecordInfoScrollByScrollAndCursorBaseOnMemberId(ScrollModelRequest<EventRecordCondition, Long> scrollModelRequest, Long memberId) {
        LOGGER.info("Mono<ScrollModelResponse<EventRecordInfo, String>> selectEventRecordInfoScrollMonoByScrollAndCursorBaseOnMemberId(ScrollModelRequest<EventRecordCondition, Long> scrollModelRequest, Long memberId), " +
                "scrollModelRequest = {}, memberId = {}", scrollModelRequest, memberId);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(memberId))
            throw new BlueException(INVALID_IDENTITY);

        return reactiveMongoTemplate.find(SCROLL_MODEL_REQUEST_PROCESSOR.apply(scrollModelRequest, memberId), EventRecord.class)
                .collectList()
                .map(eventRecords ->
                        isNotEmpty(eventRecords) ?
                                new ScrollModelResponse<>(eventRecords.stream().map(EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER).collect(toList()),
                                        parseSearchAfter(eventRecords, attachment -> String.valueOf(attachment.getId())))
                                :
                                new ScrollModelResponse<>(emptyList(), ""));
    }

    /**
     * select event record by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    @Override
    public Mono<List<EventRecord>> selectEventRecordByLimitAndQuery(Long limit, Long rows, Query query) {
        LOGGER.info("Mono<List<Attachment>> selectEventRecordMonoByLimitAndQuery(Long limit, Long rows, Query query)," +
                " limit = {}, rows = {}, query = {}", limit, rows, query);

        if (isInvalidLimit(limit) || isInvalidRows(rows))
            throw new BlueException(INVALID_PARAM);

        Query listQuery = isNotNull(query) ? Query.of(query) : new Query();
        listQuery.skip(limit).limit(rows.intValue());

        return reactiveMongoTemplate.find(listQuery, EventRecord.class).collectList();
    }

    /**
     * count event record by condition
     *
     * @param query
     * @return
     */
    @Override
    public Mono<Long> countEventRecordByQuery(Query query) {
        LOGGER.info("Mono<Long> countEventRecordMonoByQuery(Query query), query = {}", query);
        return reactiveMongoTemplate.count(query, EventRecord.class);
    }

    /**
     * select event record manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<EventRecordManagerInfo>> selectEventRecordInfoPageByPageAndCondition(PageModelRequest<EventRecordManagerCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<EventRecordManagerInfo>> selectEventRecordInfoPageMonoByPageAndCondition(PageModelRequest<EventRecordCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        Query query = MANAGER_CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(selectEventRecordByLimitAndQuery(pageModelRequest.getLimit(), pageModelRequest.getRows(), query),
                countEventRecordByQuery(query))
                .flatMap(tuple2 -> {
                    List<EventRecord> eventRecords = tuple2.getT1();
                    Long count = tuple2.getT2();
                    return isNotEmpty(eventRecords) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(eventRecords.stream().map(EventRecord::getMemberId).collect(toList()))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.stream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(eventRecords.stream().map(er ->
                                                        EVENT_RECORD_2_EVENT_RECORD_MANAGER_INFO_CONVERTER.apply(er, ofNullable(idAndNameMapping.get(er.getMemberId()))
                                                                .orElse(EMPTY_VALUE.value)))
                                                .collect(toList()));
                                    }).flatMap(attachmentDetailInfos ->
                                            just(new PageModelResponse<>(attachmentDetailInfos, count)))
                            :
                            just(new PageModelResponse<>(emptyList(), count));
                });
    }

}
