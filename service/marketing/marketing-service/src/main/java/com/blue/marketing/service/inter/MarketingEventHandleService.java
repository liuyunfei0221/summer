package com.blue.marketing.service.inter;

import com.blue.marketing.api.model.EventHandleResult;
import com.blue.marketing.api.model.MarketingEvent;
import reactor.core.publisher.Mono;

/**
 * marketing event handle service
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
public interface MarketingEventHandleService {

    /**
     * handle marketing event
     *
     * @param marketingEvent
     * @return
     */
    Mono<EventHandleResult> handleEvent(MarketingEvent marketingEvent);

}