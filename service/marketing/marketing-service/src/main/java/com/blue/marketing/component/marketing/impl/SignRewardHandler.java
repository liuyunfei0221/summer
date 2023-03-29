package com.blue.marketing.component.marketing.impl;

import com.blue.basic.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.inter.EventHandler;
import org.slf4j.Logger;

import static com.blue.basic.constant.marketing.MarketingEventType.SIGN_IN_REWARD;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * sign in reward event handler impl
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class SignRewardHandler implements EventHandler {

    private static final Logger LOGGER = getLogger(SignRewardHandler.class);

    @Override
    public void handleEvent(MarketingEvent marketingEvent) {
        LOGGER.warn("marketingEvent = {}", marketingEvent);
        System.err.println(marketingEvent);
    }

    @Override
    public MarketingEventType targetType() {
        return SIGN_IN_REWARD;
    }

}
