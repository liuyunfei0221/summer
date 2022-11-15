package com.blue.lake.service.inter;

import com.blue.basic.model.event.DataEvent;
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
     * handle events
     *
     * @param dataEvents
     */
    Mono<Boolean> handleEvents(List<DataEvent> dataEvents);

}
