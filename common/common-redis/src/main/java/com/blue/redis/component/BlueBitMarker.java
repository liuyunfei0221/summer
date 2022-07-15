package com.blue.redis.component;

import com.blue.basic.model.exps.BlueException;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.SET_BIT_WITH_EXPIRE;
import static java.nio.ByteBuffer.wrap;
import static java.util.Arrays.asList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * bit marker
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueBitMarker {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private Scheduler scheduler;

    public BlueBitMarker(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        if (isNull(reactiveStringRedisTemplate))
            throw new RuntimeException("reactiveStringRedisTemplate can't be null");

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = isNotNull(scheduler) ? scheduler : boundedElastic();
    }

    private static final int
            BIT_TRUE = 1, BIT_FALSE = 0,
            MIN_OFFSET = 0, MIN_LIMIT = 1, MAX_LIMIT = 64,
            FLUX_ELEMENT_INDEX = 0;
    private static final long MARK_BIT = 1L;

    private static final Map<Boolean, Integer> BIT_VALUE_MAPPING = new HashMap<>(4, 1.0f);

    static {
        BIT_VALUE_MAPPING.put(true, BIT_TRUE);
        BIT_VALUE_MAPPING.put(false, BIT_FALSE);
    }

    private static final RedisScript<Boolean> BIT_SET_SCRIPT = generateScriptByScriptStr(SET_BIT_WITH_EXPIRE.str, Boolean.class);

    private static final Function<String, List<String>> SCRIPT_KEYS_WRAPPER = Arrays::asList;

    private final BiFunction<String, Integer, Mono<Boolean>> BITMAP_BIT_GETTER = (key, offset) ->
            reactiveStringRedisTemplate.opsForValue().getBit(key, (long) offset).publishOn(scheduler);

    private static final BiFunction<List<Long>, Integer, boolean[]> BITS_PARSER = (records, limit) -> {
        if (limit < MIN_LIMIT || limit > MAX_LIMIT)
            throw new BlueException(INVALID_PARAM);

        boolean[] bits = new boolean[limit];

        long trueBit = 1L;
        for (long record : records) {
            for (int offset = limit; offset >= MIN_LIMIT; offset--) {
                bits[offset - 1] = (record & trueBit) != 0L;
                trueBit <<= 1;
            }
        }

        return bits;
    };

    private List<String> generateArgs(int offset, boolean bit, int expiresSecond) {
        return asList(String.valueOf(offset), String.valueOf(BIT_VALUE_MAPPING.get(bit)), String.valueOf(expiresSecond));
    }

    private Mono<List<Long>> getByLimitUpTo64(String key, int limit) {
        if (isBlank(key) || limit < MIN_LIMIT || limit > MAX_LIMIT)
            throw new BlueException(INVALID_PARAM);

        return reactiveStringRedisTemplate.execute(con ->
                        con.stringCommands()
                                .bitField(wrap(key.getBytes()),
                                        BitFieldSubCommands.create()
                                                .get(BitFieldSubCommands.BitFieldType.signed(limit))
                                                .valueAt(MARK_BIT)))
                .publishOn(scheduler)
                .elementAt(FLUX_ELEMENT_INDEX);
    }

    /**
     * get bit
     *
     * @param key
     * @param offset
     * @return
     */
    public Mono<Boolean> getBit(String key, int offset) {
        return BITMAP_BIT_GETTER.apply(key, offset);
    }

    /**
     * get bit limit value
     *
     * @param key
     * @param limit
     * @return
     */
    public Mono<List<Long>> getBitsValueByLimitUpTo64(String key, int limit) {
        return getByLimitUpTo64(key, limit);
    }

    /**
     * get bit limit list
     *
     * @param key
     * @param limit
     * @return
     */
    public Mono<boolean[]> getBitsArrByLimitUpTo64(String key, int limit) {
        return getByLimitUpTo64(key, limit)
                .flatMap(record -> just(BITS_PARSER.apply(record, limit)));
    }

    /**
     * set bit
     *
     * @param key
     * @param offset
     * @param expiresSecond
     * @return
     */
    public Mono<Boolean> setBit(String key, int offset, boolean bit, int expiresSecond) {
        return isNotBlank(key) ?
                reactiveStringRedisTemplate.execute(BIT_SET_SCRIPT, SCRIPT_KEYS_WRAPPER.apply(key),
                                generateArgs(offset, bit, expiresSecond))
                        .publishOn(scheduler)
                        .elementAt(FLUX_ELEMENT_INDEX)
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * delete key
     *
     * @param key
     * @return
     */
    public Mono<Boolean> delete(String key) {
        return isNotBlank(key) ?
                reactiveStringRedisTemplate.delete(key)
                        .publishOn(scheduler).flatMap(r -> just(r > 0))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

}
