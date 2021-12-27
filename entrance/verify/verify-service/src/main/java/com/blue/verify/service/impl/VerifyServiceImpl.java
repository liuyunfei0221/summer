package com.blue.verify.service.impl;

import com.blue.base.constant.base.RandomType;
import com.blue.base.constant.base.ResponseElement;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.verify.api.model.VerifyPair;
import com.blue.verify.config.deploy.VerifyDeploy;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.Asserter.isNotBlank;
import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.RandomType.ALPHANUMERIC;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.VALIDATION;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.of;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "FieldCanBeLocal"})
@Service
public class VerifyServiceImpl implements VerifyService {

    private static final Logger LOGGER = getLogger(VerifyServiceImpl.class);

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private static final int KEY_LEN = 16;
    private static final RandomType KEY_TYPE = ALPHANUMERIC;

    private final int VERIFY_LEN;
    private final int MIN_LEN;
    private final int MAX_LEN;
    private final Duration DEFAULT_DURATION;
    private final boolean DEFAULT_REPEATABLE;


    public VerifyServiceImpl(ReactiveStringRedisTemplate reactiveStringRedisTemplate, VerifyDeploy verifyDeploy) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        if (verifyDeploy == null)
            throw new RuntimeException("verifyDeploy can't be null");

        Integer verifyLength = verifyDeploy.getVerifyLength();
        if (verifyLength == null || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Integer minLength = verifyDeploy.getMinLength();
        if (minLength == null || minLength < 1)
            throw new RuntimeException("minLength can't be null or less than 1");

        Integer maxLength = verifyDeploy.getMaxLength();
        if (maxLength == null || maxLength < minLength)
            throw new RuntimeException("maxLength can't be null or less than minLength");

        Integer expireMillis = verifyDeploy.getExpireMillis();
        if (expireMillis == null || expireMillis < 1)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        Boolean repeatable = verifyDeploy.getRepeatable();
        if (repeatable == null)
            throw new RuntimeException("repeatable can't be null");

        VERIFY_LEN = verifyLength;
        MIN_LEN = minLength;
        MAX_LEN = maxLength;
        DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
        DEFAULT_REPEATABLE = repeatable;

        VALIDATORS.put(false, UN_REPEATABLE_VALIDATOR);
        VALIDATORS.put(true, REPEATABLE_VALIDATOR);
    }

    private static final RandomType NULL_ELE_TYPE = ALPHANUMERIC;
    private static final int NULL_ELE_LEN = 6;

    /**
     * verify assert script
     */
    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(VALIDATION.str, Boolean.class);
    private static final Function<String, List<String>> LIST_GENERATOR = List::of;

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e -> {
        LOGGER.error("e = {}", e);
        return Flux.just(false);
    };

    private final BiFunction<String, String, Mono<Boolean>> UN_REPEATABLE_VALIDATOR = (k, v) ->
            isNotBlank(k) && isNotBlank(v) ?
                    reactiveStringRedisTemplate.execute(SCRIPT, LIST_GENERATOR.apply(k), LIST_GENERATOR.apply(v))
                            .onErrorResume(FALL_BACKER)
                            .elementAt(0, false)
                    :
                    just(false);

    private final BiFunction<String, String, Mono<Boolean>> REPEATABLE_VALIDATOR = (k, v) ->
            isNotBlank(k) && isNotBlank(v) ?
                    reactiveStringRedisTemplate.opsForValue().get(k).switchIfEmpty(just(""))
                            .flatMap(val -> just(v.equals(val)))
                    :
                    just(false);

    private final Map<Boolean, BiFunction<String, String, Mono<Boolean>>> VALIDATORS = new HashMap<>(4, 1.0f);

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(VerifyType type, String key) {
        return this.generate(type, key, null);
    }

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(VerifyType type, String key, Integer length) {
        return this.generate(type, key, length, null, null);
    }

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @param toUpperCase
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(VerifyType type, String key, Integer length, Boolean toUpperCase) {
        return this.generate(type, key, length, toUpperCase, null);
    }

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @param expire
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(VerifyType type, String key, Integer length, Duration expire) {
        return this.generate(type, key, length, null, expire);
    }

    /**
     * generate pair
     *
     * @param type
     * @param key
     * @param length
     * @param toUpperCase
     * @param expire
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(VerifyType type, String key, Integer length, Boolean toUpperCase, Duration expire) {
        LOGGER.info("Mono<VerifyPair> generate(RandomType type, Integer length, Duration expire), type = {}, length = {}, expire = {}", type, length, expire);

        if (type != null) {
            String k = isNotBlank(key) ? key : generateRandom(KEY_TYPE, KEY_LEN);
            String v = of(generateRandom(type.randomType, length != null && length >= MIN_LEN && length <= MAX_LEN ? length : VERIFY_LEN))
                    .map(val -> toUpperCase != null && toUpperCase ? val.toUpperCase() : val).get();

            LOGGER.info("Mono<VerifyPair> generate(RandomType type, int length, Duration expire), k = {}, v = {}", k, v);

            return reactiveStringRedisTemplate.opsForValue()
                    .set(k, v, expire != null ? expire : DEFAULT_DURATION)
                    .flatMap(s -> just(new VerifyPair(key, v)));
        }

        return error(() -> new BlueException(ResponseElement.ILLEGAL_REQUEST));
    }

    /**
     * validate pair
     *
     * @param type
     * @param verifyPair
     * @return
     */
    @Override
    public Mono<Boolean> validate(VerifyType type, VerifyPair verifyPair) {
        return this.validate(type, verifyPair, null);
    }

    /**
     * validate pair
     *
     * @param type
     * @param verifyPair
     * @param repeatable
     * @return
     */
    @Override
    public Mono<Boolean> validate(VerifyType type, VerifyPair verifyPair, Boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(VerifyType type, VerifyPair verifyPair, Boolean repeatable), type = {}, verifyPair = {}, repeatable = {}",
                type, verifyPair, repeatable);

        return type != null && verifyPair != null ?
                VALIDATORS.get(repeatable != null ? repeatable : DEFAULT_REPEATABLE)
                        .apply(type.identity + ofNullable(verifyPair.getKey()).orElseGet(() -> generateRandom(NULL_ELE_TYPE, NULL_ELE_LEN)),
                                ofNullable(verifyPair.getVerify()).orElseGet(() -> generateRandom(NULL_ELE_TYPE, NULL_ELE_LEN)))
                :
                just(false);
    }

}
