package com.blue.analyze.component.statistics.impl;

import com.blue.analyze.component.statistics.inter.StatisticsCommand;
import com.blue.analyze.service.inter.ActiveStatisticsService;
import com.blue.base.common.access.AccessProcessor;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.base.Access;
import reactor.util.Logger;

import java.util.Map;

import static com.blue.base.constant.analyze.StatisticsRange.D;
import static com.blue.base.constant.analyze.StatisticsRange.M;
import static com.blue.base.constant.analyze.StatisticsType.MA;
import static com.blue.base.constant.base.BlueDataAttrKey.MEMBER_ID;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.empty;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

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
    public int getPrecedence() {
        return 0;
    }

    @Override
    public void analyzeAndPackage(Map<String, String> data) {
        try {
            ofNullable(data.get(BlueDataAttrKey.ACCESS.key))
                    .map(AccessProcessor::jsonToAccess)
                    .map(Access::getId)
                    .filter(id -> id >= 1L)
                    .ifPresent(mid -> {
                        activeStatisticsService.markActive(mid, MA, D)
                                .onErrorResume(throwable -> {
                                    LOGGER.error("activeStatisticsService.markActive(mid, MA, D) failed, throwable = {}", throwable);
                                    return just(false);
                                }).toFuture().join();
                        activeStatisticsService.markActive(mid, MA, M)
                                .onErrorResume(throwable -> {
                                    LOGGER.error("activeStatisticsService.markActive(mid, MA, M) failed, throwable = {}", throwable);
                                    return just(false);
                                }).toFuture().join();
                        data.put(MEMBER_ID_KEY, String.valueOf(mid));
                    });
        } catch (Exception e) {
            LOGGER.error("analyzeAndPackage(Map<String, String> data), data = {}, e = {}", data, e);
        }
    }

    @Override
    public void summary(Map<String, String> data) {
        ofNullable(data)
                .map(d -> d.get(MEMBER_ID_KEY))
                .ifPresent(memberId -> {
                            LOGGER.info("dayActiveCount = " + activeStatisticsService.selectActiveSimple(MA, D)
                                    .onErrorResume(throwable -> {
                                        LOGGER.error("activeStatisticsService.selectActiveSimple(MA, D) failed, throwable = {}", throwable);
                                        return empty();
                                    }).toFuture().join());
                            LOGGER.info("monthActiveCount = " + activeStatisticsService.selectActiveSimple(MA, M)
                                    .onErrorResume(throwable -> {
                                        LOGGER.error("activeStatisticsService.selectActiveSimple(MA, M) failed, throwable = {}", throwable);
                                        return empty();
                                    }).toFuture().join());
                        }
                );
    }

}
