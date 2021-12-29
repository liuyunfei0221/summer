package com.blue.risk.component.risk.inter;

import com.blue.base.model.base.DataEvent;

/**
 * risk handler interface
 *
 * @author liuyunfei
 * @date 2021/12/29
 * @apiNote
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
