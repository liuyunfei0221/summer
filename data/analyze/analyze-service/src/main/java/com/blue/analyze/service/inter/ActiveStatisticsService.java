package com.blue.analyze.service.inter;

import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * member active statistics service
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface ActiveStatisticsService {

    /**
     * mark an id active
     *
     * @param id
     * @param statisticsType
     * @param statisticsRange
     * @return
     */
    Mono<Boolean> markActive(Long id, StatisticsType statisticsType, StatisticsRange statisticsRange);

    /**
     * select active
     *
     * @param statisticsType
     * @param statisticsRange
     * @return
     */
    Mono<Long> selectActiveSimple(StatisticsType statisticsType, StatisticsRange statisticsRange);

    /**
     * select multi active
     *
     * @param statisticsTypes
     * @param statisticsRanges
     * @return
     */
    Mono<Long> selectActiveMerge(List<StatisticsType> statisticsTypes, List<StatisticsRange> statisticsRanges);

}
