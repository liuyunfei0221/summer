package com.blue.risk.component.risk.inter;

import com.blue.basic.constant.risk.RiskType;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import com.blue.risk.api.model.RiskStrategyInfo;
import org.springframework.lang.Nullable;

/**
 * risk handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface RiskHandler {

    /**
     * handle event / with treatment measures
     *
     * @param riskEvent
     * @param riskAsserted
     * @return
     */
    RiskAsserted handle(RiskEvent riskEvent, @Nullable RiskAsserted riskAsserted);

    /**
     * valid event / without treatment measures
     *
     * @param riskEvent
     * @param riskAsserted
     * @return
     */
    RiskAsserted validate(RiskEvent riskEvent, @Nullable RiskAsserted riskAsserted);

    /**
     * is enable
     *
     * @return
     */
    boolean isEnable();

    /**
     * assert strategy
     *
     * @param riskStrategyInfo
     */
    void assertStrategy(RiskStrategyInfo riskStrategyInfo);

    /**
     * update strategy
     *
     * @param riskStrategyInfo
     * @return
     */
    boolean updateStrategy(RiskStrategyInfo riskStrategyInfo);

    /**
     * handler type
     *
     * @return
     */
    RiskType targetType();

}
