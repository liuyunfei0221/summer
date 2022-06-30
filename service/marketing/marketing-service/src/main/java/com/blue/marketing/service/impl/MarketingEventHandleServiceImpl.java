package com.blue.marketing.service.impl;

import com.blue.base.constant.marketing.HandleStatus;
import com.blue.base.constant.marketing.MarketingEventType;
import com.blue.base.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.marketing.api.model.MarketingEvent;
import com.blue.marketing.component.marketing.MarketingEventProcessor;
import com.blue.marketing.repository.entity.EventRecord;
import com.blue.marketing.service.inter.EventRecordService;
import com.blue.marketing.service.inter.MarketingEventHandleService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static org.springframework.transaction.annotation.Propagation.REQUIRED;
import static reactor.util.Loggers.getLogger;

/**
 * marketing event handle service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MarketingEventHandleServiceImpl implements MarketingEventHandleService {

    private static final Logger LOGGER = getLogger(MarketingEventHandleServiceImpl.class);

    private final MarketingEventProcessor marketingEventProcessor;

    private BlueIdentityProcessor blueIdentityProcessor;

    private final EventRecordService eventRecordService;

    public MarketingEventHandleServiceImpl(MarketingEventProcessor marketingEventProcessor, BlueIdentityProcessor blueIdentityProcessor, EventRecordService eventRecordService) {
        this.marketingEventProcessor = marketingEventProcessor;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.eventRecordService = eventRecordService;
    }

    private final Function<MarketingEvent, EventRecord> EVENT_ENTITY_GEN = marketingEvent -> {
        if (isNull(marketingEvent))
            throw new BlueException(INVALID_IDENTITY);

        EventRecord eventRecord = new EventRecord();

        eventRecord.setId(blueIdentityProcessor.generate(EventRecord.class));
        eventRecord.setType(ofNullable(marketingEvent.getEventType()).map(type -> type.identity).orElse(MarketingEventType.UNKNOWN.identity));
        eventRecord.setData(marketingEvent.getEvent());
        eventRecord.setCreateTime(marketingEvent.getEventTime());
        eventRecord.setCreator(marketingEvent.getMemberId());

        return eventRecord;
    };

    /**
     * handle marketing event
     *
     * @param marketingEvent
     * @return
     */
    @Override
    @Transactional(propagation = REQUIRED, isolation = REPEATABLE_READ, rollbackFor = Exception.class, timeout = 30)
    public EventRecord handleEvent(MarketingEvent marketingEvent) {
        LOGGER.info("Mono<EventHandleResult> handleEvent(MarketingEvent marketingEvent), marketingEvent = {}", marketingEvent);

        EventRecord eventRecord = EVENT_ENTITY_GEN.apply(marketingEvent);

        try {
            marketingEventProcessor.handleEvent(marketingEvent);
            eventRecord.setStatus(HandleStatus.HANDLED.status);
            LOGGER.info("handleEvent(MarketingEvent marketingEvent) success, marketingEvent = {}", marketingEvent);
        } catch (Exception exception) {
            eventRecord.setStatus(HandleStatus.BROKEN.status);
            LOGGER.error("handleEvent(MarketingEvent marketingEvent) failed, marketingEvent = {}, e = {}", marketingEvent, exception);
        } finally {
            eventRecordService.insertEventRecord(eventRecord);
            LOGGER.info("eventMapper.insert(event) success, eventRecord = {}", eventRecord);
        }

        return eventRecord;
    }

}
