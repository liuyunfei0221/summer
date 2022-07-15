package com.blue.analyze.service.impl;

import com.blue.analyze.model.ActiveSummary;
import com.blue.analyze.model.MergeSummaryParam;
import com.blue.analyze.model.SummaryParam;
import com.blue.analyze.service.inter.ActiveStatisticsService;
import com.blue.analyze.service.inter.StatisticsService;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.common.base.ConstantProcessor;
import com.blue.basic.constant.analyze.StatisticsRange;
import com.blue.basic.constant.analyze.StatisticsType;
import com.blue.basic.model.exps.BlueException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static com.blue.basic.common.base.ConstantProcessor.getStatisticsRangeByIdentity;
import static com.blue.basic.common.base.ConstantProcessor.getStatisticsTypeByIdentity;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;


/**
 * statistics service impl
 *
 * @author liuyunfei
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

    private static final BiFunction<StatisticsType, StatisticsRange, String> SUMMARY_KEY_GENERATOR = (type, range) ->
            type.identity + PAR_CONCATENATION.identity + range.identity;

    private static final List<SummaryElement> ELEMENTS = new ArrayList<>(STATISTICS_TYPE_LIST.size() * STATISTICS_RANGE_LIST.size());

    static {
        for (StatisticsType type : STATISTICS_TYPE_LIST)
            for (StatisticsRange range : STATISTICS_RANGE_LIST)
                ELEMENTS.add(new SummaryElement(type, range, SUMMARY_KEY_GENERATOR.apply(type, range)));
    }

    private final Supplier<Mono<Map<String, Long>>> ACTIVE_SUMMARY_SUP = () ->
            Flux.fromStream(ELEMENTS.parallelStream()).flatMap(ele ->
                            activeStatisticsService.selectActiveSimple(ele.type, ele.range)
                                    .flatMap(count -> just(new Summary(ele.key, count)))
                    ).collectList()
                    .flatMap(sl -> just(sl.parallelStream()
                            .collect(toMap(s -> s.key, s -> s.count, (a, b) -> a))));

    /**
     * select active
     *
     * @param summaryParam
     * @return
     */
    @Override
    public Mono<Long> selectActiveSimple(SummaryParam summaryParam) {
        LOGGER.info("Long selectActive(SummaryParam summaryParam), summaryParam = {}", summaryParam);

        return activeStatisticsService.selectActiveSimple(
                getStatisticsTypeByIdentity(summaryParam.getStatisticsType()),
                getStatisticsRangeByIdentity(summaryParam.getStatisticsRange()));
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

        return activeStatisticsService.selectActiveMerge(
                ofNullable(mergeSummaryParam.getStatisticsTypes())
                        .map(types -> types.stream().map(ConstantProcessor::getStatisticsTypeByIdentity).collect(toList()))
                        .filter(BlueChecker::isNotEmpty).orElseThrow(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "statisticsTypes can't be empty")),
                ofNullable(mergeSummaryParam.getStatisticsRanges())
                        .map(types -> types.stream().map(ConstantProcessor::getStatisticsRangeByIdentity).collect(toList()))
                        .filter(BlueChecker::isNotEmpty).orElseThrow(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "statisticsRanges can't be empty")));
    }

    /**
     * select summary active
     *
     * @return
     */
    @Override
    public Mono<ActiveSummary<Long>> selectActiveSummary() {
        return ACTIVE_SUMMARY_SUP.get()
                .flatMap(summary -> {
                    LOGGER.info("ActiveSummary<Long> selectSummaryActive(), summary = {}", summary);
                    return just(new ActiveSummary<>(summary, "active summary"));
                });
    }

    /**
     * summary elements
     */
    static final class SummaryElement {

        StatisticsType type;

        StatisticsRange range;

        String key;

        public SummaryElement(StatisticsType type, StatisticsRange range, String key) {
            this.type = type;
            this.range = range;
            this.key = key;
        }
    }

    /**
     * summary
     */
    static final class Summary {

        String key;

        Long count;

        public Summary(String key, Long count) {
            this.key = key;
            this.count = count;
        }
    }

}
