package com.blue.risk.service.inter;


import com.blue.basic.model.event.DataEvent;
import com.blue.risk.api.model.RiskAsserted;
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
    Mono<RiskAsserted> analyzeEvent(DataEvent dataEvent);

}
