package com.blue.analyze.common.statistics.impl;

import com.blue.base.common.auth.AuthProcessor;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.model.base.Access;
import com.blue.analyze.common.statistics.inter.StatisticsCommand;
import com.blue.analyze.service.inter.MemberActiveStatisticsService;
import reactor.util.Logger;

import java.util.Map;

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

    private final MemberActiveStatisticsService memberActiveStatisticsService;

    public ActiveMemberStatisticsCommand(MemberActiveStatisticsService memberActiveStatisticsService) {
        this.memberActiveStatisticsService = memberActiveStatisticsService;
    }

    private static final String MEMBER_ID_KEY = "memberId";

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
                    .ifPresent(memberId -> {
                        memberActiveStatisticsService.markActive(memberId, StatisticsRange.DAY);
                        memberActiveStatisticsService.markActive(memberId, StatisticsRange.MONTH);
                        data.put(MEMBER_ID_KEY, String.valueOf(memberId));
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
                            LOGGER.info("dayActiveCount = " + memberActiveStatisticsService.selectActive(Long.parseLong(memberId), StatisticsRange.DAY));
                            LOGGER.info("monthActiveCount = " + memberActiveStatisticsService.selectActive(Long.parseLong(memberId), StatisticsRange.MONTH));
                        }
                );
    }
}
