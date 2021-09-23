package com.blue.data.common.statistics.impl;

import com.blue.base.constant.data.StatisticsRange;
import com.blue.data.common.statistics.inter.StatisticsCommand;
import com.blue.data.service.inter.ActiveMemberStatisticsService;
import reactor.util.Logger;

import java.util.Map;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.util.Loggers.getLogger;

/**
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
    public void packageAnalyzeData(Map<String, String> data) {
        String memberId = data.get("memberId");

        if (!isBlank(memberId)) {
            activeMemberStatisticsService.markActive(Long.parseLong(memberId), StatisticsRange.DAY);
            activeMemberStatisticsService.markActive(Long.parseLong(memberId), StatisticsRange.MONTH);
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
