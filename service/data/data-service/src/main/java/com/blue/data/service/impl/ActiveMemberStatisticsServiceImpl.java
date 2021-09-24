package com.blue.data.service.impl;

import com.blue.base.constant.data.StatisticsRange;
import com.blue.base.model.exps.BlueException;
import com.blue.data.common.marker.StatisticsMarker;
import com.blue.data.service.inter.ActiveMemberStatisticsService;
import org.springframework.stereotype.Service;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.INVALID_IDENTITY;
import static com.blue.base.constant.data.StatisticsType.MEMBER_ACTIVE;

/**
 * 统计业务实现
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Service
public class ActiveMemberStatisticsServiceImpl implements ActiveMemberStatisticsService {

    private final StatisticsMarker statisticsMarker;

    public ActiveMemberStatisticsServiceImpl(StatisticsMarker statisticsMarker) {
        this.statisticsMarker = statisticsMarker;
    }

    @Override
    public Boolean markActive(Long memberId, StatisticsRange statisticsRange) {
        if (memberId == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, INVALID_IDENTITY.message);

        return statisticsMarker.mark(MEMBER_ACTIVE, statisticsRange, memberId.toString());
    }


    @Override
    public Long selectActive(Long memberId, StatisticsRange statisticsRange) {
        return statisticsMarker.count(MEMBER_ACTIVE, statisticsRange);
    }

}
