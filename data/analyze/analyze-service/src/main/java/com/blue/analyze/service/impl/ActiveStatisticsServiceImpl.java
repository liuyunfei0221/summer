package com.blue.analyze.service.impl;

import com.blue.analyze.component.marker.StatisticsMarker;
import com.blue.analyze.service.inter.ActiveStatisticsService;
import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;
import com.blue.base.model.exps.BlueException;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;

import static com.blue.base.common.base.BlueCheck.isValidIdentity;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static reactor.util.Loggers.getLogger;

/**
 * member active statistics service impl
 *
 * @author liuyunfei
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
@Service
public class ActiveStatisticsServiceImpl implements ActiveStatisticsService {

    private static final Logger LOGGER = getLogger(ActiveStatisticsServiceImpl.class);

    private final StatisticsMarker statisticsMarker;

    public ActiveStatisticsServiceImpl(StatisticsMarker statisticsMarker) {
        this.statisticsMarker = statisticsMarker;
    }

    /**
     * mark an id active
     *
     * @param id
     * @param statisticsType
     * @param statisticsRange
     * @return
     */
    @Override
    public Mono<Boolean> markActive(Long id, StatisticsType statisticsType, StatisticsRange statisticsRange) {
        LOGGER.info("Boolean markActive(Long id, StatisticsType statisticsType, StatisticsRange statisticsRange), id = {}, statisticsType = {}, statisticsRange = {}", id, statisticsType, statisticsRange);

        if (isValidIdentity(id))
            return statisticsMarker.mark(statisticsType, statisticsRange, id.toString());

        throw new BlueException(INVALID_IDENTITY);
    }

    /**
     * select active
     *
     * @param statisticsType
     * @param statisticsRange
     * @return
     */
    @Override
    public Mono<Long> selectActiveSimple(StatisticsType statisticsType, StatisticsRange statisticsRange) {
        LOGGER.info("Long selectActive(StatisticsType statisticsType, StatisticsRange statisticsRange), statisticsType = {}, statisticsRange = {}", statisticsType, statisticsRange);

        return statisticsMarker.count(statisticsType, statisticsRange);
    }

    /**
     * select multi active
     *
     * @param statisticsTypes
     * @param statisticsRanges
     * @return
     */
    @Override
    public Mono<Long> selectActiveMerge(List<StatisticsType> statisticsTypes, List<StatisticsRange> statisticsRanges) {
        LOGGER.info("Long selectMultiActive(List<StatisticsType> statisticsTypes, List<StatisticsRange> statisticsRanges), statisticsTypes = {}, statisticsRanges = {}", statisticsTypes, statisticsRanges);

        return statisticsMarker.mergeCount(statisticsTypes, statisticsRanges);
    }

}
