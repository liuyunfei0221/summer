package com.blue.risk.service.inter;


import com.blue.base.model.base.DataEvent;

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
     */
    void analyzeEvent(DataEvent dataEvent);

}
