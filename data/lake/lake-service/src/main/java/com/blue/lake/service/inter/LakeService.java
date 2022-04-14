package com.blue.lake.service.inter;

import com.blue.base.model.base.DataEvent;
import com.blue.base.model.base.LimitModelRequest;
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
    void insertEvent(DataEvent dataEvent);

    /**
     * select by limit
     *
     * @param limitModelRequest
     * @return
     */
    Mono<List<OptEvent>> selectByLimitAndRows(LimitModelRequest<Void> limitModelRequest);

}
