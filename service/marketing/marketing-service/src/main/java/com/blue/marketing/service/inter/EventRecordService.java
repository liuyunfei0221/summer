package com.blue.marketing.service.inter;

import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.marketing.api.model.EventRecordInfo;
import com.blue.marketing.model.EventRecordCondition;
import com.blue.marketing.model.EventRecordManagerCondition;
import com.blue.marketing.model.EventRecordManagerInfo;
import com.blue.marketing.repository.entity.EventRecord;
import org.springframework.data.mongodb.core.query.Query;
import reactor.core.publisher.Mono;

import java.util.List;

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
    Mono<EventRecord> insertEventRecord(EventRecord eventRecord);

    /**
     * insert event record batch
     *
     * @param eventRecords
     * @return
     */
    Mono<List<EventRecord>> insertEventRecords(List<EventRecord> eventRecords);

    /**
     * get event record mono by id
     *
     * @param id
     * @return
     */
    Mono<EventRecord> getEventRecordMono(Long id);

    /**
     * get event record info mono by id
     *
     * @param id
     * @return
     */
    Mono<EventRecordInfo> getEventRecordInfoMono(Long id);

    /**
     * select event record info by scroll and member id
     *
     * @param scrollModelRequest
     * @param memberId
     * @return
     */
    Mono<ScrollModelResponse<EventRecordInfo, String>> selectEventRecordInfoScrollMonoByScrollAndCursorBaseOnMemberId(ScrollModelRequest<EventRecordCondition, Long> scrollModelRequest, Long memberId);

    /**
     * select event record by page and condition
     *
     * @param limit
     * @param rows
     * @param query
     * @return
     */
    Mono<List<EventRecord>> selectEventRecordMonoByLimitAndQuery(Long limit, Long rows, Query query);

    /**
     * count event record by condition
     *
     * @param query
     * @return
     */
    Mono<Long> countEventRecordMonoByQuery(Query query);

    /**
     * select event record manager info page by condition
     *
     * @param pageModelRequest
     * @return
     */
    Mono<PageModelResponse<EventRecordManagerInfo>> selectEventRecordInfoPageMonoByPageAndCondition(PageModelRequest<EventRecordManagerCondition> pageModelRequest);

}
