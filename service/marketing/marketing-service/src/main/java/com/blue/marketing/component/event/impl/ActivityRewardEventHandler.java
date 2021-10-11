package com.blue.marketing.component.event.impl;

import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.event.inter.EventHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static com.blue.base.constant.marketing.MarketingEventType.ACTIVITY_REWARD;
import static reactor.util.Loggers.getLogger;


/**
 * activity reward event handler impl
 *
 * @author DarkBlue
 */
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ActivityRewardEventHandler implements EventHandler {

    private static final Logger LOGGER = getLogger(ActivityRewardEventHandler.class);

    @Override
    public void handleEvent(MarketingEvent marketingEvent) {
        LOGGER.warn("handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);
        System.err.println(marketingEvent);
    }

    @Override
    public MarketingEventType targetType() {
        return ACTIVITY_REWARD;
    }

}
