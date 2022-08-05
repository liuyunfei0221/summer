package com.blue.marketing.service.impl;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.constant.EventRecordSortAttribute;
import com.blue.marketing.model.EventRecordCondition;
import com.blue.marketing.remote.consumer.RpcMemberBasicServiceConsumer;
import com.blue.marketing.repository.entity.EventRecord;
import com.blue.marketing.repository.mapper.EventRecordMapper;
import com.blue.marketing.service.inter.EventRecordService;
import com.blue.member.api.model.MemberBasicInfo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ArrayAllocator.allotByMax;
import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.ConditionSortProcessor.process;
import static com.blue.basic.constant.common.BlueCommonThreshold.DB_SELECT;
import static com.blue.basic.constant.common.BlueCommonThreshold.MAX_SERVICE_SELECT;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.marketing.converter.MarketingModelConverters.EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.core.publisher.Mono.*;
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

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final EventRecordMapper eventRecordMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public EventRecordServiceImpl(RpcMemberBasicServiceConsumer rpcMemberBasicServiceConsumer, BlueIdentityProcessor blueIdentityProcessor, EventRecordMapper eventRecordMapper) {
        this.rpcMemberBasicServiceConsumer = rpcMemberBasicServiceConsumer;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.eventRecordMapper = eventRecordMapper;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(EventRecordSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<EventRecordCondition> CONDITION_PROCESSOR = c -> {
        if (isNull(c))
            return new EventRecordCondition();

        process(c, SORT_ATTRIBUTE_MAPPING, EventRecordSortAttribute.ID.column);

        return c;
    };

    /**
     * insert event record
     *
     * @param eventRecord
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public int insertEventRecord(EventRecord eventRecord) {
        LOGGER.info("int insertEvent(EventRecord eventRecord), eventRecord = {}", eventRecord);
        if (isNull(eventRecord))
            throw new BlueException(EMPTY_PARAM);

        if (isInvalidIdentity(eventRecord.getId()))
            eventRecord.setId(blueIdentityProcessor.generate(EventRecord.class));

        return eventRecordMapper.insert(eventRecord);
    }

    /**
     * insert event record batch
     *
     * @param eventRecords
     * @return
     */
    @Override
    public int insertEventRecords(List<EventRecord> eventRecords) {
        LOGGER.info("int insertBatch(List<EventRecord> eventRecords), eventRecords = {}", eventRecords);
        return ofNullable(eventRecords)
                .filter(as -> as.size() > 0)
                .map(eventRecordMapper::insertBatch)
                .orElse(0);
    }

    /**
     * get event record by id
     *
     * @param id
     * @return
     */
    @Override
    public Optional<EventRecord> getEventRecord(Long id) {
        LOGGER.info("Optional<EventRecord> getEventRecord(Long id), id = {}", id);
        if (isInvalidIdentity(id))
            throw new BlueException(INVALID_IDENTITY);

        return ofNullable(eventRecordMapper.selectByPrimaryKey(id));
    }

    /**
     * get event record mono by id
     *
     * @param id
     * @return
     */
    @Override
    public Mono<EventRecord> getEventRecordMono(Long id) {
        return justOrEmpty(eventRecordMapper.selectByPrimaryKey(id));
    }

    /**
     * select event records by ids
     *
     * @param ids
     * @return
     */
    @Override
    public List<EventRecord> selectEventRecordByIds(List<Long> ids) {
        LOGGER.info("List<EventRecord> selectEventRecordByIds(List<Long> ids), ids = {}", ids);
        if (isEmpty(ids))
            return emptyList();
        if (ids.size() > (int) MAX_SERVICE_SELECT.value)
            throw new BlueException(PAYLOAD_TOO_LARGE);

        return allotByMax(ids, (int) DB_SELECT.value, false)
                .stream().map(eventRecordMapper::selectByIds)
                .flatMap(List::stream)
                .collect(toList());
    }

    /**
     * select event records mono by ids
     *
     * @param ids
     * @return
     */
    @Override
    public Mono<List<EventRecord>> selectEventRecordMonoByIds(List<Long> ids) {
        return just(this.selectEventRecordByIds(ids));
    }

    /**
     * select event records by page and creator id
     *
     * @param limit
     * @param rows
     * @param creator
     * @return
     */
    @Override
    public Mono<List<EventRecord>> selectEventRecordMonoByLimitAndCreator(Long limit, Long rows, Long creator) {
        LOGGER.info("Mono<List<EventRecord>> selectEventRecordMonoByLimitAndCreator(Long limit, Long rows, Long creator), " +
                "limit = {}, rows = {}, creator = {}", limit, rows, creator);
        return just(eventRecordMapper.selectByLimitAndCreator(limit, rows, creator));
    }

    /**
     * count event records mono by creator id
     *
     * @param creator
     * @return
     */
    @Override
    public Mono<Long> countEventRecordMonoByCreator(Long creator) {
        LOGGER.info("Mono<Long> countEventRecordMonoByCreator(Long creator), creator = {}", creator);
        return just(ofNullable(eventRecordMapper.countByCreator(creator)).orElse(0L));
    }

    /**
     * select event record info by page and creator id
     *
     * @param pageModelRequest
     * @param creator
     * @return
     */
    @Override
    public Mono<PageModelResponse<EventRecordInfo>> selectEventRecordInfoByPageAndCreator(PageModelRequest<Void> pageModelRequest, Long creator) {
        LOGGER.info("Mono<PageModelResponse<EventRecordInfo>> selectEventRecordInfoByPageAndCreator(PageModelRequest<Void> pageModelRequest, Long creator), pageModelRequest = {}, creator = {}",
                pageModelRequest, creator);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);
        if (isInvalidIdentity(creator))
            throw new BlueException(INVALID_IDENTITY);

        return zip(
                selectEventRecordMonoByLimitAndCreator(pageModelRequest.getLimit(), pageModelRequest.getRows(), creator),
                countEventRecordMonoByCreator(creator),
                rpcMemberBasicServiceConsumer.getMemberBasicInfoByPrimaryKey(creator)
        ).flatMap(tuple3 -> {
            List<EventRecord> eventRecords = tuple3.getT1();
            String creatorName = tuple3.getT3().getName();

            return isNotEmpty(eventRecords) ?
                    just(eventRecords.stream().map(e -> EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER.apply(e, creatorName)).collect(toList()))
                            .flatMap(eventRecordInfos ->
                                    just(new PageModelResponse<>(eventRecordInfos, tuple3.getT2())))
                    :
                    just(new PageModelResponse<>(emptyList(), tuple3.getT2()));
        });
    }

    /**
     * select event record by page and condition
     *
     * @param limit
     * @param rows
     * @param eventRecordCondition
     * @return
     */
    @Override
    public Mono<List<EventRecord>> selectEventRecordMonoByLimitAndCondition(Long limit, Long rows, EventRecordCondition eventRecordCondition) {
        LOGGER.info("Mono<List<EventRecord>> selectEventRecordMonoByLimitAndCondition(Long limit, Long rows, EventRecordCondition eventRecordCondition), " +
                "limit = {}, rows = {}, eventRecordCondition = {}", limit, rows, eventRecordCondition);
        return just(eventRecordMapper.selectByLimitAndCondition(limit, rows, eventRecordCondition));
    }

    /**
     * count event record by condition
     *
     * @param eventRecordCondition
     * @return
     */
    @Override
    public Mono<Long> countEventRecordMonoByCondition(EventRecordCondition eventRecordCondition) {
        LOGGER.info("Mono<Long> countEventRecordMonoByCondition(EventRecordCondition eventRecordCondition), eventRecordCondition = {}", eventRecordCondition);
        return just(ofNullable(eventRecordMapper.countByCondition(eventRecordCondition)).orElse(0L));
    }

    /**
     * select event record info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<EventRecordInfo>> selectEventRecordInfoPageMonoByPageAndCondition(PageModelRequest<EventRecordCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<EventRecordInfo>> selectEventRecordInfoPageMonoByPageAndCondition(PageModelRequest<EventRecordCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        EventRecordCondition eventRecordCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(selectEventRecordMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), eventRecordCondition), countEventRecordMonoByCondition(eventRecordCondition))
                .flatMap(tuple2 -> {
                    List<EventRecord> eventRecords = tuple2.getT1();
                    return isNotEmpty(eventRecords) ?
                            rpcMemberBasicServiceConsumer.selectMemberBasicInfoByIds(eventRecords.parallelStream().map(EventRecord::getCreator).collect(toList()))
                                    .flatMap(memberBasicInfos -> {
                                        Map<Long, String> idAndNameMapping = memberBasicInfos.parallelStream().collect(toMap(MemberBasicInfo::getId, MemberBasicInfo::getName, (a, b) -> a));
                                        return just(eventRecords.stream().map(e ->
                                                        EVENT_RECORD_2_EVENT_RECORD_INFO_CONVERTER.apply(e, ofNullable(idAndNameMapping.get(e.getCreator())).orElse(EMPTY_DATA.value)))
                                                .collect(toList()));
                                    }).flatMap(eventRecordInfos ->
                                            just(new PageModelResponse<>(eventRecordInfos, tuple2.getT2())))
                            :
                            just(new PageModelResponse<>(emptyList(), tuple2.getT2()));
                });
    }

}
