package com.blue.risk.component.risk.inter;

import com.blue.basic.constant.risk.RiskType;
import com.blue.basic.model.event.DataEvent;
import com.blue.risk.api.model.RiskAsserted;

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
     * @param dataEvent
     * @param riskAsserted
     * @return
     */
    RiskAsserted handleEvent(DataEvent dataEvent, RiskAsserted riskAsserted);

    /**
     * handler type
     *
     * @return
     */
    RiskType targetType();

}
