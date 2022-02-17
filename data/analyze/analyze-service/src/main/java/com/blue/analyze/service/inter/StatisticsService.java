package com.blue.analyze.service.inter;

import com.blue.analyze.model.ActiveSummary;
import com.blue.analyze.model.MergeSummaryParam;
import com.blue.analyze.model.SummaryParam;
import reactor.core.publisher.Mono;

/**
 * statistics service
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface StatisticsService {

    /**
     * select active
     *
     * @param summaryParam
     * @return
     */
    Mono<Long> selectActiveSimple(SummaryParam summaryParam);

    /**
     * select merge active
     *
     * @param mergeSummaryParam
     * @return
     */
    Mono<Long> selectActiveMerge(MergeSummaryParam mergeSummaryParam);

    /**
     * select summary active
     *
     * @return
     */
    Mono<ActiveSummary<Long>> selectActiveSummary();

}
