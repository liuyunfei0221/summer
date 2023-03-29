package com.blue.analyze.component.statistics.impl;

import com.blue.analyze.component.statistics.inter.StatisticsCommand;
import com.blue.analyze.service.inter.ActiveStatisticsService;
import com.blue.basic.common.access.AccessProcessor;
import com.blue.basic.constant.analyze.StatisticsType;
import com.blue.basic.constant.common.BlueDataAttrKey;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.event.DataEvent;
import org.slf4j.Logger;

import java.util.Optional;

import static com.blue.basic.constant.analyze.StatisticsRange.D;
import static com.blue.basic.constant.analyze.StatisticsRange.M;
import static com.blue.basic.constant.analyze.StatisticsType.MA;
import static com.blue.basic.constant.common.BlueDataAttrKey.MEMBER_ID;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;

/**
 * active member statistics command impl
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public class ActiveMemberStatisticsCommand implements StatisticsCommand {

    private static final Logger LOGGER = getLogger(ActiveMemberStatisticsCommand.class);

    private final ActiveStatisticsService activeStatisticsService;

    public ActiveMemberStatisticsCommand(ActiveStatisticsService activeStatisticsService) {
        this.activeStatisticsService = activeStatisticsService;
    }

    private static final String MEMBER_ID_KEY = MEMBER_ID.key;

    @Override
    public void analyze(DataEvent dataEvent) {
        try {
            ofNullable(dataEvent)
                    .map(DataEvent::getEntries)
                    .map(es -> es.get(BlueDataAttrKey.ACCESS.key))
                    .map(AccessProcessor::jsonToAccess)
                    .map(Access::getId)
                    .filter(id -> id >= 1L)
                    .ifPresent(mid -> {
                        activeStatisticsService.markActive(mid, MA, D)
                                .onErrorResume(t -> {
                                    LOGGER.error("markActive failed, t = {}", t);
                                    return just(false);
                                }).toFuture().join();
                        activeStatisticsService.markActive(mid, MA, M)
                                .onErrorResume(t -> {
                                    LOGGER.error("markActive failed, t = {}", t);
                                    return just(false);
                                }).toFuture().join();

                        Optional.of(dataEvent)
                                .map(DataEvent::getEntries)
                                .ifPresent(es -> es.put(MEMBER_ID_KEY, String.valueOf(mid)));
                    });
        } catch (Exception e) {
            LOGGER.error("analyze, dataEvent = {}, e = {}", dataEvent, e);
        }
    }

    @Override
    public void summary(DataEvent dataEvent) {
        ofNullable(dataEvent)
                .map(DataEvent::getEntries)
                .map(es -> es.get(MEMBER_ID_KEY))
                .ifPresent(memberId -> {
                            LOGGER.info("dayActiveCount = " + activeStatisticsService.selectActiveSimple(MA, D)
                                    .onErrorResume(t -> {
                                        LOGGER.error("selectActiveSimple failed, t = {}", t);
                                        return empty();
                                    }).toFuture().join());
                            LOGGER.info("monthActiveCount = " + activeStatisticsService.selectActiveSimple(MA, M)
                                    .onErrorResume(t -> {
                                        LOGGER.error("selectActiveSimple failed, t = {}", t);
                                        return empty();
                                    }).toFuture().join());
                        }
                );
    }

    @Override
    public StatisticsType targetType() {
        return MA;
    }
}
