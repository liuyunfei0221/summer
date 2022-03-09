package com.blue.marketing.service.impl;

import com.blue.marketing.api.model.EventHandleResult;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.MarketingEventProcessor;
import com.blue.marketing.service.inter.MarketingEventHandleService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * marketing event handle service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class MarketingEventHandleServiceImpl implements MarketingEventHandleService {

    private static final Logger LOGGER = getLogger(MarketingEventHandleServiceImpl.class);

    private final MarketingEventProcessor marketingEventProcessor;

    private final ExecutorService executorService;

    public MarketingEventHandleServiceImpl(MarketingEventProcessor marketingEventProcessor, ExecutorService executorService) {
        this.marketingEventProcessor = marketingEventProcessor;
        this.executorService = executorService;
    }

    /**
     * handle marketing event
     *
     * @param marketingEvent
     */
    @Override
    public Mono<EventHandleResult> handleEvent(MarketingEvent marketingEvent) {
        LOGGER.info("Mono<EventHandleResult> handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);

        return fromFuture(supplyAsync(() -> marketingEventProcessor.handleEvent(marketingEvent), executorService));
    }

}
