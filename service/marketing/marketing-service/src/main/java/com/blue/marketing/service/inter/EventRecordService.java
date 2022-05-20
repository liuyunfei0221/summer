package com.blue.marketing.service.inter;

import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.model.EventRecordCondition;
import com.blue.marketing.repository.entity.EventRecord;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * event record service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "unused"})
public interface EventRecordService {

    /**
     * insert event record
     *
     * @param eventRecord
     * @return
     */
    int insertEventRecord(EventRecord eventRecord);

    /**
     * insert event record batch
     *
     * @param eventRecords
     * @return
     */
    int insertEventRecords(List<EventRecord> eventRecords);

    /**
     * get event record by id
     *
     * @param id
     * @return
     */
    Optional<EventRecord> getEventRecord(Long id);

    /**
     * get event record mono by id
     *
     * @param id
     * @return
     */
    Mono<Optional<EventRecord>> getEventRecordMono(Long id);

    /**
     * select event records by ids
     *
     * @param ids
     * @return
     */
    List<EventRecord> selectEventRecordByIds(List<Long> ids);

    /**
     * select event records mono by ids
     *
     * @param ids
     * @return
     */
    Mono<List<EventRecord>> selectEventRecordMonoByIds(List<Long> ids);

    /**
     * select event records by page and creator id
     *
     * @param limit
     * @param rows
     * @param creator
     * @return
     */
    Mono<List<EventRecord>> selectEventRecordMonoByLimitAndCreator(Long limit, Long rows, Long creator);

    /**
     * count event records mono by creator id
     *
     * @param creator
     * @return
     */
    Mono<Long> countEventRecordMonoByCreator(Long creator);

    /**
     * select event record info by page and creator id
     *
     * @param pageModelRequest
     * @param creator
     * @return
     */
    Mono<PageModelResponse<EventRecordInfo>> selectEventRecordInfoByPageAndCreator(PageModelRequest<Void> pageModelRequest, Long creator);

    /**
     * select event record by page and condition
     *
     * @param limit
     * @param rows
     * @param eventRecordCondition
     * @return
     */
    Mono<List<EventRecord>> selectEventRecordMonoByLimitAndCondition(Long limit, Long rows, EventRecordCondition eventRecordCondition);

    /**
     * count event record by condition
     *
     * @param eventRecordCondition
     * @return
     */
    Mono<Long> countEventRecordMonoByCondition(EventRecordCondition eventRecordCondition);

    /**
     * select event record info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<EventRecordInfo>> selectEventRecordInfoPageMonoByPageAndCondition(PageModelRequest<EventRecordCondition> pageModelRequest);

}
