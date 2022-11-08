package com.blue.risk.component.risk.inter;

import com.blue.basic.constant.risk.RiskType;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import org.springframework.lang.Nullable;

/**
 * risk handler interface
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface RiskHandler {

    /**
     * handle event
     *
     * @param riskEvent
     * @param riskAsserted
     * @return
     */
    RiskAsserted handle(RiskEvent riskEvent, @Nullable RiskAsserted riskAsserted);

    /**
     * valid event
     *
     * @param riskEvent
     * @param riskAsserted
     * @return
     */
    RiskAsserted validate(RiskEvent riskEvent, @Nullable RiskAsserted riskAsserted);

    /**
     * handler type
     *
     * @return
     */
    RiskType targetType();

}
