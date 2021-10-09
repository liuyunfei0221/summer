package com.blue.data.common.statistics.impl;

import com.blue.base.common.auth.AuthProcessor;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.constant.data.StatisticsRange;
import com.blue.base.model.base.Access;
import com.blue.data.common.statistics.inter.StatisticsCommand;
import com.blue.data.service.inter.ActiveMemberStatisticsService;
import reactor.util.Logger;

import java.util.Map;
import java.util.Optional;

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

    private final ActiveMemberStatisticsService activeMemberStatisticsService;

    public ActiveMemberStatisticsCommand(ActiveMemberStatisticsService activeMemberStatisticsService) {
        this.activeMemberStatisticsService = activeMemberStatisticsService;
    }

    @Override
    public int getPrecedence() {
        return 0;
    }

    @Override
    public void analyzeAndPackage(Map<String, String> data) {
        try {
            Access access = AuthProcessor.jsonToAccess(data.get(BlueDataAttrKey.ACCESS.key));
            long memberId = access.getId();

            activeMemberStatisticsService.markActive(memberId, StatisticsRange.DAY);
            activeMemberStatisticsService.markActive(memberId, StatisticsRange.MONTH);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void summary(Map<String, String> data) {
        Optional.ofNullable(data)
                .map(d -> d.get("memberId"))
                .ifPresent(mid -> {
                            LOGGER.info("dayActiveCount = " + activeMemberStatisticsService.selectActive(Long.parseLong(mid), StatisticsRange.DAY));
                            LOGGER.info("monthActiveCount = " + activeMemberStatisticsService.selectActive(Long.parseLong(mid), StatisticsRange.MONTH));
                        }
                );
    }
}
