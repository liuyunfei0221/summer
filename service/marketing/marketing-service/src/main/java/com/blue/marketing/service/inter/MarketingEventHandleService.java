package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.EventHandleResult;
import com.blue.marketing.api.model.MarketingEvent;

/**
 * 营销事件处理接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MarketingEventHandleService {

    /**
     * 消费营销事件
     *
     * @param marketingEvent
     * @return
     */
    EventHandleResult handleEvent(MarketingEvent marketingEvent);

}