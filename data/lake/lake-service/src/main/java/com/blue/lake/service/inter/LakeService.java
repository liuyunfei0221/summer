package com.blue.lake.service.inter;

import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.DataEvent;
import com.blue.lake.repository.entity.OptEvent;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * lake service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface LakeService {

    /**
     * insert events
     *
     * @param dataEvents
     */
    Mono<Boolean> insertEvents(List<DataEvent> dataEvents);

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    Mono<ScrollModelResponse<OptEvent, Long>> selectEventScrollByScrollAndCursor(ScrollModelRequest<Void, Long> scrollModelRequest);

}
