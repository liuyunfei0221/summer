package com.blue.data.service.inter;

import com.blue.base.constant.data.StatisticsRange;

/**
 * 统计业务接口
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface ActiveMemberStatisticsService {


    /**
     * 标记活跃
     *
     * @param memberId
     * @param statisticsRange
     * @return
     */
    Boolean markActive(Long memberId, StatisticsRange statisticsRange);

    /**
     * 查询活跃
     *
     * @param memberId
     * @param statisticsRange
     * @return
     */
    Long selectActive(Long memberId, StatisticsRange statisticsRange);
}
