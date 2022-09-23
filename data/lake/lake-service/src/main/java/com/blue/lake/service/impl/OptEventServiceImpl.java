package com.blue.lake.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.repository.mapper.OptEventMapper;
import com.blue.lake.service.inter.OptEventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.database.common.SearchAfterProcessor.parseSearchAfter;
import static com.blue.lake.converter.LakeModelConverters.DATA_EVENT_2_OPT_EVENT;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * option event service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "SpringJavaInjectionPointsAutowiringInspection"})
@Service
public class OptEventServiceImpl implements OptEventService {

    private static final Logger LOGGER = getLogger(OptEventServiceImpl.class);

    private BlueIdentityProcessor blueIdentityProcessor;

    private OptEventMapper optEventMapper;

    public OptEventServiceImpl(BlueIdentityProcessor blueIdentityProcessor, OptEventMapper optEventMapper) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.optEventMapper = optEventMapper;
    }

    private final Function<DataEvent, Boolean> EVENT_INSERTER = dataEvent ->
            ofNullable(dataEvent)
                    .map(event -> {
                        OptEvent optEvent = DATA_EVENT_2_OPT_EVENT.apply(event);
                        optEvent.setId(blueIdentityProcessor.generate(OptEvent.class));

                        try {
                            optEventMapper.insert(optEvent);
                            return true;
                        } catch (Exception e) {
                            LOGGER.info("optEventMapper.insert() failed, dataEvent = {}, e = {}", dataEvent, e);
                            return false;
                        }
                    }).orElse(false);

    private final Function<List<DataEvent>, Boolean> EVENTS_INSERTER = dataEvents ->
            ofNullable(dataEvents)
                    .filter(BlueChecker::isNotEmpty)
                    .map(events -> {
                        List<OptEvent> optEvents = dataEvents.stream()
                                .map(event -> {
                                    OptEvent optEvent = DATA_EVENT_2_OPT_EVENT.apply(event);
                                    optEvent.setId(blueIdentityProcessor.generate(OptEvent.class));
                                    return optEvent;
                                }).collect(toList());

                        try {
                            optEventMapper.insertBatch(optEvents);
                            return true;
                        } catch (Exception e) {
                            LOGGER.info("optEventMapper.insertBatch() failed, dataEvents = {}, e = {}", dataEvents, e);
                            return false;
                        }
                    }).orElse(false);

    /**
     * insert event
     *
     * @param dataEvent
     * @return
     */
    @Override
    public boolean insertEvent(DataEvent dataEvent) {
        LOGGER.info("boolean insertEvent(DataEvent dataEvent), dataEvent = {}", dataEvent);
        if (isNull(dataEvent))
            throw new BlueException(EMPTY_PARAM);

        return EVENT_INSERTER.apply(dataEvent);
    }

    /**
     * insert events
     *
     * @param dataEvents
     * @return
     */
    @Override
    public boolean insertEvents(List<DataEvent> dataEvents) {
        LOGGER.info("boolean insertEvents(List<DataEvent> dataEvents), dataEvents = {}", dataEvents);
        if (isEmpty(dataEvents))
            throw new BlueException(EMPTY_PARAM);

        return EVENTS_INSERTER.apply(dataEvents);
    }

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<OptEvent, Long>> selectEventScrollMonoByScrollAndCursor(ScrollModelRequest<Void, Long> scrollModelRequest) {
        LOGGER.info("Mono<ScrollModelResponse<OptEvent, Long>> selectEventScrollMonoByScrollAndCursor(ScrollModelRequest<Void, Long> scrollModelRequest), scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        List<OptEvent> optEvents = optEventMapper.selectByRowsAndSearchAfter(scrollModelRequest.getRows(), scrollModelRequest.getCursor());

        return isNotEmpty(optEvents) ?
                just(new ScrollModelResponse<>(optEvents, parseSearchAfter(optEvents, OptEvent::getId)))
                :
                just(new ScrollModelResponse<>(emptyList(), null));
    }

}
