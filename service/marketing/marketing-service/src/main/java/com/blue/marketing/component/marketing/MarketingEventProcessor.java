package com.blue.marketing.component.marketing;

import com.blue.basic.model.exps.BlueException;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.inter.EventHandler;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.function.Consumer;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * marketing event handle service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(HIGHEST_PRECEDENCE)
public class MarketingEventProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(MarketingEventProcessor.class);

    /**
     * event type -> event handler
     */
    private Map<Integer, EventHandler> eventHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, EventHandler> beansOfType = applicationContext.getBeansOfType(EventHandler.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("marketingEventHandlers is empty");

        eventHandlers = beansOfType.values().stream()
                .collect(toMap(eh -> eh.targetType().identity, eh -> eh, (a, b) -> a));
    }

    private final Consumer<MarketingEvent> EVENT_HANDLER = marketingEvent -> {
        Integer marketingEventType = marketingEvent.getEventType();
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
        LOGGER.info("marketingEvent = {}", marketingEvent);
        EVENT_HANDLER.accept(marketingEvent);
    }

}
