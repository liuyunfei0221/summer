package com.blue.lake.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.service.inter.LakeService;
import com.blue.lake.service.inter.OptEventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * lake service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class LakeServiceImpl implements LakeService {

    private static final Logger LOGGER = getLogger(LakeServiceImpl.class);

    private ExecutorService executorService;

    private OptEventService optEventService;

    public LakeServiceImpl(ExecutorService executorService, OptEventService optEventService) {
        this.executorService = executorService;
        this.optEventService = optEventService;
    }

    private final Function<DataEvent, Mono<Boolean>> EVENT_INSERTER = dataEvent ->
            ofNullable(dataEvent)
                    .map(event ->
                            fromFuture(supplyAsync(() -> optEventService.insertEvent(event), executorService))
                    ).orElseGet(() -> just(false));

    private final Function<List<DataEvent>, Mono<Boolean>> EVENTS_INSERTER = dataEvents ->
            ofNullable(dataEvents)
                    .filter(BlueChecker::isNotEmpty)
                    .map(events ->
                            fromFuture(supplyAsync(() -> optEventService.insertEvents(events), executorService))
                    ).orElseGet(() -> just(false));

    /**
     * insert event
     *
     * @param dataEvent
     */
    @Override
    public Mono<Boolean> insertEvent(DataEvent dataEvent) {
        LOGGER.info("dataEvent = {}", dataEvent);
        if (isNull(dataEvent))
            return error(() -> new BlueException(EMPTY_PARAM));

        return EVENT_INSERTER.apply(dataEvent);
    }

    /**
     * insert events
     *
     * @param dataEvents
     */
    @Override
    public Mono<Boolean> insertEvents(List<DataEvent> dataEvents) {
        LOGGER.info("dataEvents = {}", dataEvents);
        if (isEmpty(dataEvents))
            return error(() -> new BlueException(EMPTY_PARAM));

        return EVENTS_INSERTER.apply(dataEvents);
    }

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<OptEvent, Long>> selectEventScrollByScrollAndCursor(ScrollModelRequest<Void, Long> scrollModelRequest) {
        LOGGER.info("scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            return error(() -> new BlueException(EMPTY_PARAM));

        return optEventService.selectEventScrollByScrollAndCursor(scrollModelRequest);
    }

}