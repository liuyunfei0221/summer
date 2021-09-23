package com.blue.secure.component.verify;

import com.blue.base.constant.base.CacheKey;
import com.blue.base.constant.base.RandomType;
import com.blue.secure.component.auth.AuthInfoCacher;
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
 * 验证码工具
 *
 * @author liuyunfei
 * @date 2021/8/18
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "FieldCanBeLocal", "unused", "AliControlFlowStatementWithoutBraces"})
public final class VerificationCodeProcessor {

    private static final Logger LOGGER = getLogger(AuthInfoCacher.class);

    private final StringRedisTemplate stringRedisTemplate;

    /**
     * 默认类型
     */
    private final RandomType DEFAULT_TYPE;

    /**
     * 默认长度
     */
    private final Integer DEFAULT_LENGTH;

    /**
     * 验证码最大/默认过期时间
     */
    private final Duration DEFAULT_EXPIRE_DURATION;


    public VerificationCodeProcessor(StringRedisTemplate stringRedisTemplate, RandomType type, Integer length, Long expireMillis) {
        if (stringRedisTemplate == null)
            throw new RuntimeException("stringRedisTemplate不能为空");
        if (type == null)
            throw new RuntimeException("type不能为空");
        if (length == null || length < 1)
            throw new RuntimeException("length不能为空或小于1");
        if (expireMillis == null || expireMillis < 1L)
            throw new RuntimeException("expireMillis不能为空或小于1");

        this.stringRedisTemplate = stringRedisTemplate;
        this.DEFAULT_TYPE = type;
        this.DEFAULT_LENGTH = length;
        this.DEFAULT_EXPIRE_DURATION = Duration.of(expireMillis, MILLIS);
    }


    private static final String SCRIPT_STR = VALIDATION.str;

    /**
     * 判断验证码脚本
     */
    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(SCRIPT_STR, Boolean.class);

    private static final String VERIFY_KEY_PRE = CacheKey.VERIFY_KEY_PRE.key;

    private static final UnaryOperator<String> KEY_GENERATOR = key -> VERIFY_KEY_PRE + key;

    private static final Function<String, List<String>> KEYS_GENERATOR = key -> of(KEY_GENERATOR.apply(key));

    private static final Function<Long, Duration> MILLIS_DURATION_GENERATOR = millis -> Duration.of(millis, MILLIS);


    /**
     * 生成
     *
     * @param key
     * @return
     */
    public String generateCode(String key) {
        return generateCode(key, null, null, null);
    }

    /**
     * 生成
     *
     * @param key
     * @param type
     * @return
     */
    public String generateCode(String key, RandomType type) {
        return generateCode(key, type, null, null);
    }

    /**
     * 生成
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
     * 生成
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
        throw new RuntimeException("key不能为空或''");
    }

    /**
     * 验证
     *
     * @param key
     * @param code
     * @return
     */
    public boolean assertCode(String key, String code) {
        if (hasText(key) && hasText(code))
            return ofNullable(stringRedisTemplate.execute(SCRIPT, KEYS_GENERATOR.apply(key), code)).orElse(false);

        throw new RuntimeException("key及code不能为空或''");
    }

}
