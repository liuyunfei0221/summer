package com.blue.marketing.component.marketing.inter;

import com.blue.basic.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;

/**
 * event handler interface
 *
 * @author liuyunfei
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
