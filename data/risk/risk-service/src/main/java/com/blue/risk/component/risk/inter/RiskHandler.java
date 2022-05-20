package com.blue.risk.component.risk.inter;

import com.blue.base.model.common.DataEvent;

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
     */
    void handleEvent(DataEvent dataEvent);

    /**
     * handler precedence
     *
     * @return
     */
    int precedence();

}
