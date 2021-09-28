package com.blue.marketing.component.event.impl;

import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.event.inter.EventHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static com.blue.base.constant.marketing.MarketingEventType.SIGN_IN_REWARD;
import static reactor.util.Loggers.getLogger;

/**
 * 用户业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class SignRewardHandleServiceImpl implements EventHandler {

    private static final Logger LOGGER = getLogger(SignRewardHandleServiceImpl.class);

    /**
     * 处理签到奖励事件
     *
     * @param marketingEvent
     */
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
