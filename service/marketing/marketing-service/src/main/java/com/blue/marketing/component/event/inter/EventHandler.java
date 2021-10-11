package com.blue.marketing.component.event.inter;

import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;

/**
 * event handler interface
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface EventHandler {

    /**
     * handle event
     *
     * @param marketingEvent
     */
    void handleEvent(MarketingEvent marketingEvent);

    /**
     * target event type to process
     *
     * @return
     */
    MarketingEventType targetType();

}
