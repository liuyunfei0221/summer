package com.blue.marketing.component.marketing.impl;

import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.inter.EventHandler;
import reactor.util.Logger;

import static com.blue.base.constant.marketing.MarketingEventType.SIGN_IN_REWARD;
import static reactor.util.Loggers.getLogger;

/**
 * sign in reward event handler impl
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class SignRewardHandler implements EventHandler {

    private static final Logger LOGGER = getLogger(SignRewardHandler.class);

    @Override
    public void handleEvent(MarketingEvent marketingEvent) {
        LOGGER.warn("handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);
        System.err.println(marketingEvent);
    }

    @Override
    public MarketingEventType targetType() {
        return SIGN_IN_REWARD;
    }

}
