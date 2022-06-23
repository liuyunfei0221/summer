package com.blue.marketing.component.marketing;

import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.base.model.exps.BlueException;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.inter.EventHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.Map;
import java.util.function.Consumer;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.*;
import static java.util.stream.Collectors.toMap;
import static reactor.util.Loggers.getLogger;

/**
 * marketing event handle service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class MarketingEventProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(MarketingEventProcessor.class);

    /**
     * event type -> event handler
     */
    private Map<MarketingEventType, EventHandler> eventHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, EventHandler> beansOfType = applicationContext.getBeansOfType(EventHandler.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("marketingEventHandlers is empty");

        eventHandlers = beansOfType.values().stream()
                .collect(toMap(EventHandler::targetType, eh -> eh, (a, b) -> a));
    }

    private final Consumer<MarketingEvent> EVENT_HANDLER = marketingEvent -> {
        MarketingEventType marketingEventType = marketingEvent.getEventType();
        if (isNull(marketingEventType))
            throw new BlueException(INVALID_IDENTITY);

        EventHandler eventHandler = eventHandlers.get(marketingEventType);
        if (isNull(eventHandler))
            throw new BlueException(BAD_REQUEST);

        eventHandler.handleEvent(marketingEvent);
    };

    /**
     * handle marketing event
     *
     * @param marketingEvent
     */
    public void handleEvent(MarketingEvent marketingEvent) {
        LOGGER.info("EventHandleResult handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);
        EVENT_HANDLER.accept(marketingEvent);
    }

}
