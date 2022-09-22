package com.blue.lake.service.inter;

import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.DataEvent;
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
     * insert event
     *
     * @param dataEvent
     * @return
     */
    boolean insertEvent(DataEvent dataEvent);

    /**
     * insert events
     *
     * @param dataEvents
     * @return
     */
    boolean insertEvents(List<DataEvent> dataEvents);

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    Mono<ScrollModelResponse<OptEvent, Long>> selectEventScrollMonoByScrollAndCursor(ScrollModelRequest<Void, Long> scrollModelRequest);

}
