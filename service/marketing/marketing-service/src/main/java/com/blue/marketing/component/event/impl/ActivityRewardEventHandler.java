package com.blue.marketing.component.event.impl;

import com.blue.base.constant.marketing.EventType;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.event.inter.EventHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static com.blue.base.constant.marketing.EventType.ACTIVITY_REWARD;
import static reactor.util.Loggers.getLogger;


/**
 * 活动奖励业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ActivityRewardEventHandler implements EventHandler {

    private static final Logger LOGGER = getLogger(ActivityRewardEventHandler.class);

    /**
     * 处理活动奖励事件
     *
     * @param marketingEvent
     */
    @Override
    public void handleEvent(MarketingEvent marketingEvent) {
        LOGGER.warn("handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);
        System.err.println(marketingEvent);
    }

    @Override
    public EventType targetType() {
        return ACTIVITY_REWARD;
    }

}
