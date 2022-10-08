package com.blue.lake.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.event.DataEvent;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.service.inter.LakeService;
import com.blue.lake.service.inter.OptEventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.core.publisher.Mono.just;
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
        LOGGER.info("Mono<Boolean> insertEvent(DataEvent dataEvent), dataEvent = {}", dataEvent);

        return EVENT_INSERTER.apply(dataEvent);
    }

    /**
     * insert events
     *
     * @param dataEvents
     */
    @Override
    public Mono<Boolean> insertEvents(List<DataEvent> dataEvents) {
        LOGGER.info("Mono<Boolean> insertEvents(List<DataEvent> dataEvents), dataEvents = {}", dataEvents);

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

        return optEventService.selectEventScrollMonoByScrollAndCursor(scrollModelRequest);
    }

}