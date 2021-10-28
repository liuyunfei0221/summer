package com.blue.secure.component.verify;

import com.blue.base.constant.base.CacheKey;
import com.blue.base.constant.base.RandomType;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import reactor.util.Logger;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.VALIDATION;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.springframework.util.StringUtils.hasText;
import static reactor.util.Loggers.getLogger;

/**
 * verify processor
 *
 * @author liuyunfei
 * @date 2021/8/18
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "FieldCanBeLocal", "unused", "AliControlFlowStatementWithoutBraces"})
public final class VerificationCodeProcessor {

    private static final Logger LOGGER = getLogger(VerificationCodeProcessor.class);

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * default type
     */
    private final RandomType DEFAULT_TYPE;

    /**
     * default length
     */
    private final Integer DEFAULT_LENGTH;

    /**
     * default expire duration
     */
    private final Duration DEFAULT_EXPIRE_DURATION;


    public VerificationCodeProcessor(StringRedisTemplate stringRedisTemplate, RandomType type, Integer length, Long expireMillis) {
        if (stringRedisTemplate == null)
            throw new RuntimeException("stringRedisTemplate can't be null");
        if (type == null)
            throw new RuntimeException("type can't be null");
        if (length == null || length < 1)
            throw new RuntimeException("length can't be null or less than 1");
        if (expireMillis == null || expireMillis < 1L)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        this.stringRedisTemplate = stringRedisTemplate;
        this.DEFAULT_TYPE = type;
        this.DEFAULT_LENGTH = length;
        this.DEFAULT_EXPIRE_DURATION = Duration.of(expireMillis, MILLIS);
    }


    private static final String SCRIPT_STR = VALIDATION.str;

    /**
     * verify assert script
     */
    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(SCRIPT_STR, Boolean.class);

    private static final String VERIFY_KEY_PRE = CacheKey.VERIFY_KEY_PRE.key;

    private static final UnaryOperator<String> KEY_GENERATOR = key -> VERIFY_KEY_PRE + key;

    private static final Function<String, List<String>> KEYS_GENERATOR = key -> of(KEY_GENERATOR.apply(key));

    private static final Function<Long, Duration> MILLIS_DURATION_GENERATOR = millis -> Duration.of(millis, MILLIS);


    /**
     * generate code
     *
     * @param key
     * @return
     */
    public String generateCode(String key) {
        return generateCode(key, null, null, null);
    }

    /**
     * generate code
     *
     * @param key
     * @param type
     * @return
     */
    public String generateCode(String key, RandomType type) {
        return generateCode(key, type, null, null);
    }

    /**
     * generate code
     *
     * @param key
     * @param type
     * @param length
     * @return
     */
    public String generateCode(String key, RandomType type, Integer length) {
        return generateCode(key, type, length, null);
    }

    /**
     * generate code
     *
     * @param key
     * @param type
     * @param length
     * @param expireMillis
     * @return
     */
    public String generateCode(String key, RandomType type, Integer length, Long expireMillis) {
        if (hasText(key)) {
            String code = generateRandom(ofNullable(type).orElse(DEFAULT_TYPE), ofNullable(length).orElse(DEFAULT_LENGTH));
            stringRedisTemplate.opsForValue()
                    .set(KEY_GENERATOR.apply(key), code,
                            ofNullable(expireMillis).map(MILLIS_DURATION_GENERATOR).orElse(DEFAULT_EXPIRE_DURATION));
            return code;
        }
        throw new RuntimeException("key can't be blank");
    }

    /**
     * verify
     *
     * @param key
     * @param code
     * @return
     */
    public boolean assertCode(String key, String code) {
        if (hasText(key) && hasText(code))
            return ofNullable(stringRedisTemplate.execute(SCRIPT, KEYS_GENERATOR.apply(key), code)).orElse(false);

        throw new RuntimeException("key or code can't be blank");
    }

}
