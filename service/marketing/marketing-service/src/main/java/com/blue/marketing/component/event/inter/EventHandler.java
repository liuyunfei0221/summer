package com.blue.marketing.component.event.inter;

import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;

/**
 * 营销事件处理接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface EventHandler {

    /**
     * 消费营销事件
     *
     * @param marketingEvent
     */
    void handleEvent(MarketingEvent marketingEvent);

    /**
     * 处理的营销事件类型
     *
     * @return
     */
    MarketingEventType targetType();

}
