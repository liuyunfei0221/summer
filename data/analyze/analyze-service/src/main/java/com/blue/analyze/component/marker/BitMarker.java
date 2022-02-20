package com.blue.analyze.component.marker;

import com.blue.base.constant.analyze.StatisticsRange;
import com.blue.base.constant.analyze.StatisticsType;
import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.blue.base.common.base.BlueCheck.isNotBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * statistics marker
 *
 * @author liuyunfei
 * @date 2021/9/6
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
@Component
public final class BitMarker {

    private static final Logger LOGGER = getLogger(BitMarker.class);

    private StringRedisTemplate stringRedisTemplate;

    private ExecutorService executorService;

    public BitMarker(StringRedisTemplate stringRedisTemplate, ExecutorService executorService) {
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
            stringRedisTemplate.delete(key);
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
    public Mono<Boolean> mark(StatisticsType statisticsType, StatisticsRange statisticsRange, String value) {
        LOGGER.info("mark(StatisticsType statisticsType, StatisticsRange statisticsRange, String value), statisticsType = {}, statisticsRange = {}, value = {}",
                statisticsType, statisticsRange, value);

        return statisticsType != null && statisticsRange != null && isNotBlank(value) ?
                just(stringRedisTemplate.opsForHyperLogLog()
                        .add(STATISTICS_KEY_GENERATOR.apply(statisticsType, statisticsRange), value) > 0L)
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * count
     *
     * @param key
     * @return
     */
    public Mono<Long> count(String key) {
        LOGGER.info("Mono<Long> count(String key), key = {}", key);
        return isNotBlank(key) ? just(stringRedisTemplate.opsForHyperLogLog().size(key))
                :
                Mono.error(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * merge count
     *
     * @param keys
     * @return
     */
    public Mono<Long> mergeCount(List<String> keys) {
        LOGGER.info("Mono<Long> mergeCount(List<String> keys), keys = {}", keys);
        return ofNullable(keys)
                .filter(ks -> ks.size() > 0)
                .map(ks -> {
                    String tempKey = UNION_PF_KEY_GETTER.get();
                    return just(stringRedisTemplate.opsForHyperLogLog().union(tempKey,
                            ks.toArray(String[]::new)))
                            .doFinally(signalType -> stringRedisTemplate.delete(tempKey));
                }).orElseGet(() -> just(0L));
    }

}
