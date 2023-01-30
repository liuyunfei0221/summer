package com.blue.risk.service.inter;


import com.blue.basic.model.event.DataEvent;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import reactor.core.publisher.Mono;

/**
 * risk analyse service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RiskAnalyzeService {

    /**
     * handle event
     *
     * @param riskEvent
     * @return
     */
    Mono<RiskAsserted> handleRiskEvent(RiskEvent riskEvent);

    /**
     * handle event
     *
     * @param dataEvent
     * @return
     */
    Mono<RiskAsserted> handleDataEvent(DataEvent dataEvent);

    /**
     * validate event
     *
     * @param riskEvent
     * @return
     */
    Mono<RiskAsserted> validateRiskEvent(RiskEvent riskEvent);

    /**
     * validate event
     *
     * @param dataEvent
     * @return
     */
    Mono<RiskAsserted> validateDataEvent(DataEvent dataEvent);

}