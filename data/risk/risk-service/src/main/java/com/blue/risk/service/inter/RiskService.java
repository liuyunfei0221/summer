package com.blue.risk.service.inter;


import com.blue.base.model.base.DataEvent;
import reactor.core.publisher.Mono;

/**
 * risk analyse service
 *
 * @author DarkBlue
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
