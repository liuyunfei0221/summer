package com.blue.lake.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.event.DataEvent;
import com.blue.basic.model.exps.BlueException;
import com.blue.lake.service.inter.LakeService;
import com.blue.lake.service.inter.OptEventService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;
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

    private OptEventService optEventService;

    public LakeServiceImpl(OptEventService optEventService) {
        this.optEventService = optEventService;
    }

    private final Function<List<DataEvent>, Mono<Boolean>> EVENTS_INSERTER = dataEvents ->
            ofNullable(dataEvents)
                    .filter(BlueChecker::isNotEmpty)
                    .map(events -> optEventService.insertOptEvents(events)
                    ).orElseGet(() -> just(false));

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

}