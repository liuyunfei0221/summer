package com.blue.risk.service.inter;


import com.blue.base.model.common.DataEvent;
import reactor.core.publisher.Mono;

/**
 * risk analyse service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface RiskService {

    /**
     * analyze event
     *
     * @param dataEvent
     * @return
     */
    Mono<Boolean> analyzeEvent(DataEvent dataEvent);

}
