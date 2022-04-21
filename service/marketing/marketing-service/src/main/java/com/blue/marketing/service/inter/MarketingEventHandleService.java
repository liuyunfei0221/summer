package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.repository.entity.EventRecord;

/**
 * marketing event handle service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MarketingEventHandleService {

    /**
     * handle marketing event
     *
     * @param marketingEvent
     * @return
     */
    EventRecord handleEvent(MarketingEvent marketingEvent);

}