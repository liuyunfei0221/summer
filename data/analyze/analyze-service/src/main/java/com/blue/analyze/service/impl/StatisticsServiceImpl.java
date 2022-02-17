package com.blue.analyze.service.impl;

import com.blue.analyze.model.ActiveSummary;
import com.blue.analyze.model.MergeSummaryParam;
import com.blue.analyze.model.SummaryParam;
import com.blue.analyze.service.inter.ActiveStatisticsService;
import com.blue.analyze.service.inter.StatisticsService;
import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueCheck.isNotEmpty;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * statistics service impl
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class StatisticsServiceImpl implements StatisticsService {

    private static final Logger LOGGER = getLogger(StatisticsServiceImpl.class);

    private ActiveStatisticsService activeStatisticsService;

    public StatisticsServiceImpl(ActiveStatisticsService activeStatisticsService) {
        this.activeStatisticsService = activeStatisticsService;
    }

    private static final List<StatisticsType> STATISTICS_TYPE_LIST = Stream.of(StatisticsType.values()).collect(toList());
    private static final List<StatisticsRange> STATISTICS_RANGE_LIST = Stream.of(StatisticsRange.values()).collect(toList());

    private static final int SUMMARY_SIZE = STATISTICS_TYPE_LIST.size() * STATISTICS_RANGE_LIST.size();

    private static final BiFunction<StatisticsType, StatisticsRange, String> SUMMARY_KEY_GENERATOR = (type, range) ->
            type.identity + PAR_CONCATENATION.identity + range.identity;

    /**
     * select active
     *
     * @param summaryParam
     * @return
     */
    @Override
    public Mono<Long> selectActiveSimple(SummaryParam summaryParam) {
        LOGGER.info("Long selectActive(SummaryParam summaryParam), summaryParam = {}", summaryParam);

        StatisticsType statisticsType = summaryParam.getStatisticsType();
        StatisticsRange statisticsRange = summaryParam.getStatisticsRange();

        return statisticsType != null && statisticsRange != null ? just(activeStatisticsService.selectActiveSimple(statisticsType, statisticsRange)) : just(0L);
    }

    /**
     * select merge active
     *
     * @param mergeSummaryParam
     * @return
     */
    @Override
    public Mono<Long> selectActiveMerge(MergeSummaryParam mergeSummaryParam) {
        LOGGER.info("Long selectMergeActive(MergeSummaryParam mergeSummaryParam), mergeSummaryParam = {}", mergeSummaryParam);

        List<StatisticsType> statisticsTypes = mergeSummaryParam.getStatisticsTypes();
        List<StatisticsRange> statisticsRanges = mergeSummaryParam.getStatisticsRanges();

        return isNotEmpty(statisticsTypes) && isNotEmpty(statisticsRanges) ? just(activeStatisticsService.selectActiveMerge(statisticsTypes, statisticsRanges)) : just(0L);
    }

    private final Supplier<Map<String, Long>> ACTIVE_SUMMARY_SUP = () -> {
        Map<String, Long> summary = new HashMap<>(SUMMARY_SIZE, 1.0f);

        for (StatisticsType type : STATISTICS_TYPE_LIST)
            for (StatisticsRange range : STATISTICS_RANGE_LIST)
                summary.put(SUMMARY_KEY_GENERATOR.apply(type, range), activeStatisticsService.selectActiveSimple(type, range));

        return summary;
    };

    /**
     * select summary active
     *
     * @return
     */
    @Override
    public Mono<ActiveSummary<Long>> selectActiveSummary() {

        return just(ACTIVE_SUMMARY_SUP.get())
                .flatMap(summary -> {
                    LOGGER.info("ActiveSummary<Long> selectSummaryActive(), summary = {}", summary);
                    return just(new ActiveSummary<>(summary, "active summary"));
                });
    }

}
