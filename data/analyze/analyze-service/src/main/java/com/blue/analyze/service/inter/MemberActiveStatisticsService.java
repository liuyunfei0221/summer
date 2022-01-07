package com.blue.analyze.service.inter;

import com.blue.base.constant.analyze.StatisticsRange;

/**
 * member active statistics service
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface MemberActiveStatisticsService {


    /**
     * mark a member active
     *
     * @param memberId
     * @param statisticsRange
     * @return
     */
    Boolean markActive(Long memberId, StatisticsRange statisticsRange);

    /**
     * select active
     *
     * @param memberId
     * @param statisticsRange
     * @return
     */
    Long selectActive(Long memberId, StatisticsRange statisticsRange);
}
