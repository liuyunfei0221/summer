package com.blue.lake.service.impl;

import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.ConditionCountResponse;
import com.blue.basic.model.common.ScrollModelRequest;
import com.blue.basic.model.common.ScrollModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.lake.config.deploy.NestingResponseDeploy;
import com.blue.lake.constant.OptEventSortAttribute;
import com.blue.lake.model.OptEventCondition;
import com.blue.lake.repository.entity.OptEvent;
import com.blue.lake.repository.mapper.OptEventMapper;
import com.blue.lake.service.inter.OptEventService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.SortType.DESC;
import static com.blue.database.common.ConditionSortProcessor.process;
import static com.blue.database.common.SearchAfterProcessor.parseSearchAfter;
import static com.blue.database.common.SearchAfterProcessor.parseSearchAfterComparison;
import static com.blue.lake.constant.OptEventSortAttribute.CURSOR;
import static com.blue.lake.constant.OptEventSortAttribute.STAMP;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.*;

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

    public OptEventServiceImpl(BlueIdentityProcessor blueIdentityProcessor, OptEventMapper optEventMapper, NestingResponseDeploy nestingResponseDeploy) {
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.optEventMapper = optEventMapper;
    }

    private final Function<List<OptEvent>, Mono<Boolean>> EVENTS_INSERTER = events ->
            justOrEmpty(events)
                    .filter(BlueChecker::isNotEmpty)
                    .map(oes -> {
                        optEventMapper.insertBatch(
                                oes.stream().filter(BlueChecker::isNotNull)
                                        .peek(event -> {
                                            if (isInvalidIdentity(event.getId()))
                                                event.setId(blueIdentityProcessor.generate(OptEvent.class));
                                        }).collect(toList()));
                        return true;
                    })
                    .onErrorResume(t -> {
                        LOGGER.error("EVENTS_INSERTER failed, t = {}", t.getMessage());
                        return just(false);
                    })
                    .switchIfEmpty(defer(() -> just(false)));

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(OptEventSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<OptEventCondition> CONDITION_PROCESSOR = c -> {
        OptEventCondition oec = isNotNull(c) ? c : new OptEventCondition();

        process(oec, SORT_ATTRIBUTE_MAPPING, STAMP.column);

        return oec;
    };

    /**
     * insert events
     *
     * @param optEvents
     * @return
     */
    @Override
    public Mono<Boolean> insertOptEvents(List<OptEvent> optEvents) {
        LOGGER.info("optEvents = {}", optEvents);
        if (isEmpty(optEvents))
            throw new BlueException(EMPTY_PARAM);

        return EVENTS_INSERTER.apply(optEvents);
    }

    /**
     * select by search after
     *
     * @param scrollModelRequest
     * @return
     */
    @Override
    public Mono<ScrollModelResponse<OptEvent, Long>> selectOptEventScrollByConditionAndCursor(ScrollModelRequest<OptEventCondition, Long> scrollModelRequest) {
        LOGGER.info("scrollModelRequest = {}", scrollModelRequest);
        if (isNull(scrollModelRequest))
            throw new BlueException(EMPTY_PARAM);

        OptEventCondition optEventCondition = CONDITION_PROCESSOR.apply(scrollModelRequest.getCondition());

        List<OptEvent> optEvents = optEventMapper.selectBySearchAfterAndCondition(scrollModelRequest.getRows(), optEventCondition, CURSOR.column,
                parseSearchAfterComparison(optEventCondition.getSortType()), scrollModelRequest.getCursor());

        return isNotEmpty(optEvents) ?
                just(new ScrollModelResponse<>(optEvents,
                        parseSearchAfter(optEvents, ofNullable(optEventCondition.getSortType()).orElse(DESC.identity), OptEvent::getCursor)))
                :
                just(new ScrollModelResponse<>(emptyList()));
    }

    /**
     * count by condition
     *
     * @param optEventCondition
     * @return
     */
    @Override
    public Mono<ConditionCountResponse<OptEventCondition>> countOptEventByCondition(OptEventCondition optEventCondition) {
        LOGGER.info("optEventCondition = {}", optEventCondition);
        if (isNull(optEventCondition))
            throw new BlueException(EMPTY_PARAM);

        OptEventCondition countCondition = CONDITION_PROCESSOR.apply(optEventCondition);

        return justOrEmpty(optEventMapper.countByCondition(countCondition))
                .switchIfEmpty(defer(() -> just(0L)))
                .map(count -> new ConditionCountResponse<>(optEventCondition, count));
    }

}
