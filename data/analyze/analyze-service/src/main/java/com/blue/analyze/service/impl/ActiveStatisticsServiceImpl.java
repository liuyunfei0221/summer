package com.blue.analyze.service.impl;

import com.blue.analyze.service.inter.ActiveStatisticsService;
import com.blue.basic.constant.analyze.StatisticsRange;
import com.blue.basic.constant.analyze.StatisticsType;
import com.blue.basic.model.exps.BlueException;
import com.blue.redis.component.BlueValueMarker;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.BlueChecker.isValidIdentity;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * member active statistics service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
@Service
public class ActiveStatisticsServiceImpl implements ActiveStatisticsService {

    private static final Logger LOGGER = getLogger(ActiveStatisticsServiceImpl.class);

    private BlueValueMarker blueValueMarker;

    private ExecutorService executorService;

    public ActiveStatisticsServiceImpl(BlueValueMarker blueValueMarker, ExecutorService executorService) {
        this.blueValueMarker = blueValueMarker;
        this.executorService = executorService;

        for (StatisticsType statisticsType : StatisticsType.values())
            for (StatisticsRange statisticsRange : StatisticsRange.values())
                CURRENT_KEY_VALUE_HOLDER.put(HOLDER_KEY_GENERATOR.apply(statisticsType, statisticsRange), REDIS_KEY_GENERATOR.apply(statisticsType, statisticsRange));
    }

    private final Map<String, String> CURRENT_KEY_VALUE_HOLDER = new ConcurrentHashMap<>(StatisticsType.values().length * StatisticsRange.values().length, 1.0f);

    private static final BiConsumer<StatisticsType, StatisticsRange> PARAMS_ASSERTER = (type, range) -> {
        if (isNull(type) || isNull(range))
            throw new BlueException(INVALID_IDENTITY);
    };

    private static final BiFunction<StatisticsType, StatisticsRange, String> HOLDER_KEY_GENERATOR = (type, range) ->
            type.identity.intern() + PAR_CONCATENATION.identity.intern() + range.identity.intern();

    private static final BiFunction<StatisticsType, StatisticsRange, String> REDIS_KEY_GENERATOR = (type, range) ->
            type.identity.intern() + PAR_CONCATENATION.identity.intern() + range.keyGenerator.get().intern();

    private final BiFunction<StatisticsType, StatisticsRange, String> STATISTICS_KEY_GENERATOR = (type, range) -> {
        PARAMS_ASSERTER.accept(type, range);

        String holdingKey = HOLDER_KEY_GENERATOR.apply(type, range).intern();
        String currentRedisKey = REDIS_KEY_GENERATOR.apply(type, range);

        if (!currentRedisKey.equals(CURRENT_KEY_VALUE_HOLDER.get(holdingKey)))
            synchronized (this) {
                String holdingRedisKey = CURRENT_KEY_VALUE_HOLDER.get(holdingKey);
                if (currentRedisKey.equals(holdingRedisKey)) {
                    CURRENT_KEY_VALUE_HOLDER.put(holdingKey, currentRedisKey);
                    executorService.execute(() -> blueValueMarker.delete(holdingRedisKey).subscribe(b -> {
                        if (b) {
                            LOGGER.info("delete key, key = {}", holdingRedisKey);
                        } else {
                            LOGGER.error("delete key failed, key = {}", holdingRedisKey);
                        }
                    }));
                }
            }

        return currentRedisKey;
    };

    private final BiFunction<List<StatisticsType>, List<StatisticsRange>, List<String>> KEYS_GENERATOR = (types, ranges) -> {
        if (isEmpty(types) || isEmpty(ranges))
            throw new BlueException(BAD_REQUEST);

        List<String> keys = new ArrayList<>(types.size() * ranges.size());

        for (StatisticsType statisticsType : types)
            for (StatisticsRange statisticsRange : ranges)
                keys.add(STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange));

        return keys;
    };

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
        LOGGER.info("id = {}, statisticsType = {}, statisticsRange = {}", id, statisticsType, statisticsRange);

        return isValidIdentity(id) && statisticsType != null && statisticsRange != null ?
                blueValueMarker.mark(STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange), id.toString())
                :
                error(() -> new BlueException(INVALID_IDENTITY));
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
        LOGGER.info("statisticsType = {}, statisticsRange = {}", statisticsType, statisticsRange);

        return blueValueMarker.count(STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange));
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
        LOGGER.info("statisticsTypes = {}, statisticsRanges = {}", statisticsTypes, statisticsRanges);

        return just(KEYS_GENERATOR.apply(statisticsTypes, statisticsRanges))
                .flatMap(blueValueMarker::mergeCount);
    }

}
