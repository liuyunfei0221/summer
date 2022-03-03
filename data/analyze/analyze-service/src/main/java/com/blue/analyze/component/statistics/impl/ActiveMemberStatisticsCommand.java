package com.blue.analyze.component.statistics.impl;

import com.blue.analyze.component.statistics.inter.StatisticsCommand;
import com.blue.analyze.service.inter.ActiveStatisticsService;
import com.blue.base.common.auth.AuthProcessor;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.base.Access;
import reactor.util.Logger;

import java.util.Map;

import static com.blue.base.constant.analyze.StatisticsRange.D;
import static com.blue.base.constant.analyze.StatisticsRange.M;
import static com.blue.base.constant.analyze.StatisticsType.MA;
import static com.blue.base.constant.base.BlueDataAttrKey.MEMBER_ID;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * active member statistics command impl
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
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
                    .map(AuthProcessor::jsonToAccess)
                    .map(Access::getId)
                    .filter(id -> id >= 1L)
                    .ifPresent(mid -> {
                        activeStatisticsService.markActive(mid, MA, D).subscribe();
                        activeStatisticsService.markActive(mid, MA, M).subscribe();
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
                            LOGGER.info("dayActiveCount = " + activeStatisticsService.selectActiveSimple(MA, D).subscribe());
                            LOGGER.info("monthActiveCount = " + activeStatisticsService.selectActiveSimple(MA, M).subscribe());
                        }
                );
    }
}