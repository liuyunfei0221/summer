package com.blue.redis.common;

import com.blue.base.model.exps.BlueException;
import org.springframework.data.redis.connection.BitFieldSubCommands;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.SET_BIT_WITH_EXPIRE;
import static java.nio.ByteBuffer.wrap;
import static java.util.Arrays.asList;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;

/**
 * bit marker
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueBitMarker {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public BlueBitMarker(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        assertParam(reactiveStringRedisTemplate);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
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
            reactiveStringRedisTemplate.opsForValue().getBit(key, (long) offset);

    private static final BiFunction<List<Long>, Integer, boolean[]> BITS_PARSER = (records, limit) -> {
        if (limit < MIN_LIMIT)
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

    private List<String> generateArgs(int offset, boolean bit, int expireSeconds) {
        return asList(String.valueOf(offset), String.valueOf(BIT_VALUE_MAPPING.get(bit)), String.valueOf(expireSeconds));
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
     * @param expireSeconds
     * @return
     */
    public Mono<Boolean> setBit(String key, int offset, boolean bit, int expireSeconds) {
        return reactiveStringRedisTemplate.execute(BIT_SET_SCRIPT, SCRIPT_KEYS_WRAPPER.apply(key),
                        generateArgs(offset, bit, expireSeconds))
                .elementAt(FLUX_ELEMENT_INDEX);
    }

    /**
     * delete key
     *
     * @param key
     * @return
     */
    public Mono<Boolean> delete(String key) {
        return isNotBlank(key) ?
                reactiveStringRedisTemplate.delete(key).flatMap(r -> just(r > 0))
                :
                error(() -> new BlueException(BAD_REQUEST));
    }

    /**
     * assert params
     *
     * @param reactiveStringRedisTemplate
     */
    private void assertParam(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        if (isNull(reactiveStringRedisTemplate))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "reactiveStringRedisTemplate can't be null");
    }

}
