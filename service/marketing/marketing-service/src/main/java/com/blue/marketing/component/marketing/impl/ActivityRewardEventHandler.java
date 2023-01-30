package com.blue.marketing.component.marketing.impl;

import com.blue.basic.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.inter.EventHandler;
import reactor.util.Logger;

import static com.blue.basic.constant.marketing.MarketingEventType.ACTIVITY_REWARD;
import static reactor.util.Loggers.getLogger;


/**
 * activity reward event handler impl
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class ActivityRewardEventHandler implements EventHandler {

    private static final Logger LOGGER = getLogger(ActivityRewardEventHandler.class);

    @Override
    public void handleEvent(MarketingEvent marketingEvent) {
        LOGGER.warn("marketingEvent = {}", marketingEvent);
        System.err.println(marketingEvent);
    }

    @Override
    public MarketingEventType targetType() {
        return ACTIVITY_REWARD;
    }

}
