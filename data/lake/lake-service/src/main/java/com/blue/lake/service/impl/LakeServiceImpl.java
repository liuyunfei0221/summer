package com.blue.lake.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.LimitModelRequest;
import com.blue.basic.model.event.DataEvent;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.repository.mapper.OptEventMapper;
import com.blue.lake.service.inter.LakeService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.basic.constant.common.BlueCommonThreshold.LIMIT;
import static com.blue.basic.constant.common.BlueCommonThreshold.ROWS;
import static com.blue.lake.converter.LakeModelConverters.DATA_EVENT_2_OPT_EVENT;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
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

    private OptEventMapper optEventMapper;

    private BlueIdentityProcessor blueIdentityProcessor;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public LakeServiceImpl(ExecutorService executorService, OptEventMapper optEventMapper, BlueIdentityProcessor blueIdentityProcessor) {
        this.executorService = executorService;
        this.optEventMapper = optEventMapper;
        this.blueIdentityProcessor = blueIdentityProcessor;
    }

    private final Function<DataEvent, Mono<Boolean>> EVENT_INSERTER = dataEvent ->
            ofNullable(dataEvent)
                    .map(event -> {
                        OptEvent optEvent = DATA_EVENT_2_OPT_EVENT.apply(event);
                        optEvent.setId(blueIdentityProcessor.generate(OptEvent.class));

                        return fromFuture(supplyAsync(() -> {
                            optEventMapper.insert(optEvent);
                            return true;
                        }, executorService));
                    }).orElseGet(() -> just(false));

    private final Function<List<DataEvent>, Mono<Boolean>> EVENTS_INSERTER = dataEvents ->
            ofNullable(dataEvents)
                    .filter(BlueChecker::isNotEmpty)
                    .map(events ->
                            fromFuture(supplyAsync(() -> {
                                optEventMapper.insertBatch(dataEvents.stream()
                                        .map(event -> {
                                            OptEvent optEvent = DATA_EVENT_2_OPT_EVENT.apply(event);
                                            optEvent.setId(blueIdentityProcessor.generate(OptEvent.class));
                                            return optEvent;
                                        }).collect(toList()));
                                return true;
                            }, executorService))
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
     * select by limit
     *
     * @param limitModelRequest
     * @return
     */
    @Override
    public Mono<List<OptEvent>> selectByLimitAndRows(LimitModelRequest<Void> limitModelRequest) {
        return limitModelRequest != null ?
                just(optEventMapper.selectByLimitAndRows(limitModelRequest.getLimit(), limitModelRequest.getRows()))
                :
                just(optEventMapper.selectByLimitAndRows(LIMIT.value, ROWS.value));
    }

}