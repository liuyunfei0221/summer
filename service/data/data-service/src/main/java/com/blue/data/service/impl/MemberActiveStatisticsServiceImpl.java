package com.blue.data.service.impl;

import com.blue.base.constant.data.StatisticsRange;
import com.blue.base.model.exps.BlueException;
import com.blue.data.common.marker.StatisticsMarker;
import com.blue.data.service.inter.MemberActiveStatisticsService;
import org.springframework.stereotype.Service;

import static com.blue.base.common.base.Asserter.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.data.StatisticsType.MEMBER_ACTIVE;

/**
 * member active statistics service impl
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Service
public class MemberActiveStatisticsServiceImpl implements MemberActiveStatisticsService {

    private final StatisticsMarker statisticsMarker;

    public MemberActiveStatisticsServiceImpl(StatisticsMarker statisticsMarker) {
        this.statisticsMarker = statisticsMarker;
    }

    @Override
    public Boolean markActive(Long memberId, StatisticsRange statisticsRange) {
        if (isValidIdentity(memberId))
            return statisticsMarker.mark(MEMBER_ACTIVE, statisticsRange, memberId.toString());

        throw new BlueException(INVALID_IDENTITY);
    }


    @Override
    public Long selectActive(Long memberId, StatisticsRange statisticsRange) {
        return statisticsMarker.count(MEMBER_ACTIVE, statisticsRange);
    }

}
