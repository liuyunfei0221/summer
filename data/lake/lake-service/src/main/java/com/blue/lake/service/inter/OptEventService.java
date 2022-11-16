package com.blue.lake.service.inter;

import com.blue.basic.model.common.ConditionCountResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.lake.model.OptEventCondition;
import com.blue.lake.repository.entity.OptEvent;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * option event service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface OptEventService {

    /**
     * insert events
     *
     * @param optEvents
     * @return
     */
    Mono<Boolean> insertOptEvents(List<OptEvent> optEvents);

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    Mono<ScrollModelResponse<OptEvent, Long>> selectOptEventScrollByConditionAndCursor(ScrollModelRequest<OptEventCondition, Long> scrollModelRequest);

    /**
     * count by condition
     *
     * @param optEventCondition
     * @return
     */
    Mono<ConditionCountResponse<OptEventCondition>> countOptEventByCondition(OptEventCondition optEventCondition);

}
