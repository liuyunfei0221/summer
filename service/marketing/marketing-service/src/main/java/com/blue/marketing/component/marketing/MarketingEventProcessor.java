package com.blue.marketing.component.marketing;

import com.blue.base.constant.marketing.HandleStatus;
import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.marketing.api.model.EventHandleResult;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.inter.EventHandler;
import com.blue.marketing.repository.entity.Event;
import com.blue.marketing.repository.mapper.EventMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
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

    private BlueIdentityProcessor blueIdentityProcessor;

    private final EventMapper eventMapper;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MarketingEventProcessor(BlueIdentityProcessor blueIdentityProcessor, EventMapper eventMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.eventMapper = eventMapper;
    }

    /**
     * event type -> event handler
     */
    private Map<MarketingEventType, EventHandler> eventHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, EventHandler> beansOfType = applicationContext.getBeansOfType(EventHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "marketingEventHandlers is empty");

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

    private final Function<MarketingEvent, Event> EVENT_ENTITY_GEN = marketingEvent -> {
        if (isNull(marketingEvent))
            throw new BlueException(INVALID_IDENTITY);

        Event event = new Event();

        event.setId(blueIdentityProcessor.generate(Event.class));

        event.setType(ofNullable(marketingEvent.getEventType()).map(type -> type.identity).orElse(MarketingEventType.UNKNOWN.identity));
        event.setData(marketingEvent.getEvent());
        event.setCreateTime(marketingEvent.getEventTime());
        event.setCreator(marketingEvent.getMemberId());

        return event;
    };

    /**
     * handle marketing event
     *
     * @param marketingEvent
     */
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 15)
    public EventHandleResult handleEvent(MarketingEvent marketingEvent) {
        LOGGER.info("EventHandleResult handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);

        EventHandleResult handleResult = new EventHandleResult();

        Event event = EVENT_ENTITY_GEN.apply(marketingEvent);

        try {
            EVENT_HANDLER.accept(marketingEvent);

            event.setStatus(HandleStatus.HANDLED.status);
            LOGGER.info("handleEvent(MarketingEvent marketingEvent) success, marketingEvent = {}", marketingEvent);

            handleResult.setSuccess(true);
            handleResult.setMessage("handle success");
        } catch (Exception exception) {
            event.setStatus(HandleStatus.BROKEN.status);
            LOGGER.error("handleEvent(MarketingEvent marketingEvent) failed, marketingEvent = {}, e = {}", marketingEvent, exception);

            handleResult.setSuccess(false);
            handleResult.setMessage("handle failed");
            handleResult.setThrowable(exception);
        } finally {
            eventMapper.insert(event);
            LOGGER.info("eventMapper.insert(event) success, event = {}", event);
        }

        return handleResult;
    }

}
