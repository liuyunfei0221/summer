package com.blue.lake.service.inter;

import com.blue.basic.model.common.LimitModelRequest;
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
     * insert event
     *
     * @param dataEvent
     */
    Mono<Boolean> insertEvent(DataEvent dataEvent);

    /**
     * select by limit
     *
     * @param limitModelRequest
     * @return
     */
    Mono<List<OptEvent>> selectByLimitAndRows(LimitModelRequest<Void> limitModelRequest);

}
