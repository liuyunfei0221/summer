package com.blue.analyze.common.marker;

import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;
import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.blue.base.common.base.Check.isNotBlank;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * statistics marker
 *
 * @author liuyunfei
 * @date 2021/9/6
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "unused"})
@Component
public final class StatisticsMarker {

    private static final Logger LOGGER = getLogger(StatisticsMarker.class);

    private StringRedisTemplate stringRedisTemplate;

    private ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public StatisticsMarker(StringRedisTemplate stringRedisTemplate, ExecutorService executorService) {
        this.stringRedisTemplate = stringRedisTemplate;
        this.executorService = executorService;

        for (StatisticsType statisticsType : StatisticsType.values())
            for (StatisticsRange statisticsRange : StatisticsRange.values())
                CURRENT_KEY_VALUE_HOLDER.put(HOLDER_KEY_GENERATOR.apply(statisticsType, statisticsRange), REDIS_KEY_GENERATOR.apply(statisticsType, statisticsRange));
    }

    private final Map<String, String> CURRENT_KEY_VALUE_HOLDER = new ConcurrentHashMap<>(StatisticsType.values().length * StatisticsRange.values().length, 1.0f);

    private static final BiConsumer<StatisticsType, StatisticsRange> PARAMS_ASSERTER = (type, range) -> {
        if (type == null || range == null)
            throw new BlueException(INVALID_IDENTITY);
    };

    private static final BiFunction<StatisticsType, StatisticsRange, String> HOLDER_KEY_GENERATOR = (type, range) ->
            type.identity.intern() + PAR_CONCATENATION.identity.intern() + range.identity.intern();

    private static final BiFunction<StatisticsType, StatisticsRange, String> REDIS_KEY_GENERATOR = (type, range) ->
            type.identity.intern() + PAR_CONCATENATION.identity.intern() + range.keyGenerator.get().intern();

    private final Consumer<String> REDIS_KEY_DELETER = key -> {
        try {
            stringRedisTemplate.opsForHyperLogLog().delete(key);
        } catch (Exception e) {
            LOGGER.error("REDIS_KEY_DELETER -> key delete failed, key = {}, e = {}", key, e);
        }
    };

    private final BiFunction<StatisticsType, StatisticsRange, String> STATISTICS_KEY_GENERATOR = (type, range) -> {
        PARAMS_ASSERTER.accept(type, range);

        String holdingKey = HOLDER_KEY_GENERATOR.apply(type, range).intern();
        String currentRedisKey = REDIS_KEY_GENERATOR.apply(type, range);

        if (!currentRedisKey.equals(CURRENT_KEY_VALUE_HOLDER.get(holdingKey)))
            synchronized (this) {
                String holdingRedisKey = CURRENT_KEY_VALUE_HOLDER.get(holdingKey);
                if (currentRedisKey.equals(holdingRedisKey)) {
                    CURRENT_KEY_VALUE_HOLDER.put(holdingKey, currentRedisKey);
                    executorService.execute(() -> REDIS_KEY_DELETER.accept(holdingRedisKey));
                }
            }

        return currentRedisKey;
    };

    private static final String UNION_PF_KEY_PREFIX = "union_pf_key_";

    private static final int RAN_KEY_STR_LEN = 6;

    public static final Supplier<String> UNION_PF_KEY_GETTER = () ->
            UNION_PF_KEY_PREFIX + randomAlphanumeric(RAN_KEY_STR_LEN);

    /**
     * mark
     *
     * @param statisticsType
     * @param statisticsRange
     * @param value
     * @return
     */
    public boolean mark(StatisticsType statisticsType, StatisticsRange statisticsRange, String value) {
        LOGGER.info("mark(StatisticsType statisticsType, StatisticsRange statisticsRange, String value), statisticsType = {}, statisticsRange = {}, value = {}",
                statisticsType, statisticsRange, value);

        if (isNotBlank(value))
            return stringRedisTemplate.opsForHyperLogLog()
                    .add(STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange), value) > 0L;

        throw new BlueException(BAD_REQUEST);
    }

    /**
     * count
     *
     * @param statisticsType
     * @param statisticsRange
     * @return
     */
    public long count(StatisticsType statisticsType, StatisticsRange statisticsRange) {
        LOGGER.info("count(StatisticsType statisticsType, StatisticsRange statisticsRange), statisticsType = {}, statisticsRange = {}", statisticsType, statisticsRange);
        return stringRedisTemplate.opsForHyperLogLog().size(STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange));
    }

    /**
     * merge count
     *
     * @param statisticsTypes
     * @param statisticsRanges
     * @return
     */
    public long mergeCount(List<StatisticsType> statisticsTypes, List<StatisticsRange> statisticsRanges) {
        LOGGER.info("mergeCount(List<StatisticsType> statisticsTypes, List<StatisticsRange> statisticsRanges), statisticsTypes = {}, statisticsRanges = {}", statisticsTypes, statisticsRanges);

        if (isEmpty(statisticsTypes) && isEmpty(statisticsRanges))
            return 0L;

        String[] keys = new String[statisticsTypes.size() * statisticsRanges.size()];
        int index = 0;
        for (StatisticsType statisticsType : statisticsTypes)
            for (StatisticsRange statisticsRange : statisticsRanges)
                keys[index++] = STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange);

        String tempKey = UNION_PF_KEY_GETTER.get();

        long count = 0L;
        try {
            count = stringRedisTemplate.opsForHyperLogLog().union(tempKey, keys);
        } catch (Exception e) {
            LOGGER.error("mergeCount(List<StatisticsType> statisticsTypes, List<StatisticsRange> statisticsRanges) failed, e = {0}", e);
        } finally {
            stringRedisTemplate.delete(tempKey);
        }

        return count;
    }


}
