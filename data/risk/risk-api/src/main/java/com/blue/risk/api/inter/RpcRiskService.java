package com.blue.risk.api.inter;

import com.blue.basic.model.event.DataEvent;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;

import java.util.concurrent.CompletableFuture;

/**
 * rpc risk interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcRiskService {

    /**
     * handle event
     *
     * @param riskEvent
     * @return
     */
    CompletableFuture<RiskAsserted> handleRiskEvent(RiskEvent riskEvent);

    /**
     * handle event
     *
     * @param dataEvent
     * @return
     */
    CompletableFuture<RiskAsserted> handleDataEvent(DataEvent dataEvent);

    /**
     * validate event
     *
     * @param riskEvent
     * @return
     */
    CompletableFuture<RiskAsserted> validateRiskEvent(RiskEvent riskEvent);

    /**
     * validate event
     *
     * @param dataEvent
     * @return
     */
    CompletableFuture<RiskAsserted> validateDataEvent(DataEvent dataEvent);

}
