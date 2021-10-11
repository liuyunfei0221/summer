package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.EventHandleResult;
import com.blue.marketing.api.model.MarketingEvent;

/**
 * marketing event handle service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MarketingEventHandleService {

    /**
     * handle marketing event
     *
     * @param marketingEvent
     * @return
     */
    EventHandleResult handleEvent(MarketingEvent marketingEvent);

}