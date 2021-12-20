package com.blue.marketing.service.impl;

import com.blue.base.constant.marketing.HandleStatus;
import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.marketing.api.model.EventHandleResult;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.event.inter.EventHandler;
import com.blue.marketing.repository.entity.Event;
import com.blue.marketing.repository.mapper.EventMapper;
import com.blue.marketing.service.inter.MarketingEventHandleService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import reactor.util.Logger;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.*;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * marketing event handle service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MarketingEventHandleServiceImpl implements MarketingEventHandleService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(MarketingEventHandleServiceImpl.class);

    private final EventMapper eventMapper;

    private final BlueIdentityProcessor blueIdentityProcessor;

    /**
     * event type -> event handler
     */
    private Map<MarketingEventType, EventHandler> marketingEventHandlers;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MarketingEventHandleServiceImpl(EventMapper eventMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.eventMapper = eventMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, EventHandler> beansOfType = applicationContext.getBeansOfType(EventHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "marketingEventHandlers is empty");

        marketingEventHandlers = beansOfType.values().stream()
                .collect(toMap(EventHandler::targetType, eh -> eh, (a, b) -> a));
    }

    private final Consumer<MarketingEvent> eventHandler = marketingEvent -> {
        MarketingEventType marketingEventType = marketingEvent.getEventType();
        if (marketingEventType == null)
            throw new BlueException(INVALID_IDENTITY);

        EventHandler eventHandler = marketingEventHandlers.get(marketingEventType);
        if (eventHandler == null)
            throw new BlueException(BAD_REQUEST);

        eventHandler.handleEvent(marketingEvent);
    };

    private static final Function<MarketingEvent, Event> EVENT_ENTITY_GEN = marketingEvent -> {
        if (marketingEvent == null)
            throw new BlueException(INVALID_IDENTITY);

        Event event = new Event();

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
    @Override
    public EventHandleResult handleEvent(MarketingEvent marketingEvent) {
        LOGGER.info("handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);

        EventHandleResult handleResult = new EventHandleResult();

        Event event = EVENT_ENTITY_GEN.apply(marketingEvent);
        event.setId(blueIdentityProcessor.generate(Event.class));

        try {
            eventHandler.accept(marketingEvent);

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
            try {
                eventMapper.insert(event);
                LOGGER.info("eventMapper.insert(event) success, event = {}", event);
            } catch (Exception exception) {
                LOGGER.error("eventMapper.insert(event) failed, event = {}, e = {}", event, exception);
            }
        }

        return handleResult;
    }

}
