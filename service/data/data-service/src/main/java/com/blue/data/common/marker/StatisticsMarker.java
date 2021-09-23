package com.blue.data.common.marker;

import com.blue.base.constant.data.StatisticsRange;
import com.blue.base.constant.data.StatisticsType;
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

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
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

    /**
     * 统计key与当前redis key维护
     */
    private final Map<String, String> CURRENT_KEY_VALUE_HOLDER = new ConcurrentHashMap<>(StatisticsType.values().length * StatisticsRange.values().length, 1.0f);

    /**
     * 参数校验
     */
    private static final BiConsumer<StatisticsType, StatisticsRange> PARAMS_ASSERTER = (type, range) -> {
        if (type == null || range == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "type不能为空,range不能为空");
    };

    /**
     * key构建规则
     */
    private static final BiFunction<StatisticsType, StatisticsRange, String> HOLDER_KEY_GENERATOR = (type, range) ->
            type.identity.intern() + PAR_CONCATENATION.identity.intern() + range.identity.intern();

    /**
     * value构建规则
     */
    private static final BiFunction<StatisticsType, StatisticsRange, String> REDIS_KEY_GENERATOR = (type, range) ->
            type.identity.intern() + PAR_CONCATENATION.identity.intern() + range.keyGenerator.get().intern();

    /**
     * redis key删除
     */
    private final Consumer<String> REDIS_KEY_DELETER = key -> {
        try {
            stringRedisTemplate.opsForHyperLogLog().delete(key);
        } catch (Exception e) {
            LOGGER.error("REDIS_KEY_DELETER -> key删除失败, key = {}", key);
        }
    };

    /**
     * 获取统计key
     */
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

    /**
     * merge key前缀
     */
    private static final String UNION_PF_KEY_PRE = "union_pf_key_";

    /**
     * 随机key字符串长度
     */
    private static final int RAN_KEY_STR_LEN = 6;

    /**
     * 随机字符串生成器
     */
    public static final Supplier<String> UNION_PF_KEY_GETTER = () ->
            UNION_PF_KEY_PRE + randomAlphanumeric(RAN_KEY_STR_LEN);

    /**
     * 标记
     *
     * @param statisticsType
     * @param statisticsRange
     * @param value
     * @return
     */
    public boolean mark(StatisticsType statisticsType, StatisticsRange statisticsRange, String value) {
        LOGGER.info("mark(StatisticsType statisticsType, StatisticsRange statisticsRange, String value), statisticsType = {}, statisticsRange = {}, value = {}",
                statisticsType, statisticsRange, value);

        if (isBlank(value))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "value不能为空或''");

        return stringRedisTemplate.opsForHyperLogLog()
                .add(STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange), value) > 0L;
    }

    /**
     * 统计
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
     * 合并统计
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
