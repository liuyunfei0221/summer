package com.blue.verify.service.impl;

import com.blue.base.constant.base.RandomType;
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
import java.util.function.Function;

import static com.blue.base.common.base.Asserter.isNotBlank;
import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.RandomType.ALPHANUMERIC;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.VALIDATION;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.ofNullable;
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
    private final RandomType VERIFY_TYPE;
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

        RandomType type = verifyDeploy.getType();
        if (type == null)
            throw new RuntimeException("type can't be null");

        Integer expireMillis = verifyDeploy.getExpireMillis();
        if (expireMillis == null || expireMillis < 1)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        Boolean repeatable = verifyDeploy.getRepeatable();
        if (repeatable == null)
            throw new RuntimeException("repeatable can't be null");

        VERIFY_LEN = verifyLength;
        MIN_LEN = minLength;
        MAX_LEN = maxLength;
        VERIFY_TYPE = type;
        DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
        DEFAULT_REPEATABLE = repeatable;

        VALIDATORS.put(false, UN_REPEATABLE_VALIDATOR);
        VALIDATORS.put(true, REPEATABLE_VALIDATOR);
    }

    /**
     * verify assert script
     */
    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(VALIDATION.str, Boolean.class);
    private static final Function<String, List<String>> LIST_GENERATOR = List::of;

    private static final Function<Throwable, Flux<Boolean>> FALL_BACKER = e -> {
        LOGGER.error("e = {}", e);
        return Flux.just(false);
    };

    private final Function<VerifyPair, Mono<Boolean>> UN_REPEATABLE_VALIDATOR = pair -> {
        String key = pair.getKey();
        String verify = pair.getVerify();

        return isNotBlank(key) && isNotBlank(verify) ?
                reactiveStringRedisTemplate.execute(SCRIPT, LIST_GENERATOR.apply(key), LIST_GENERATOR.apply(verify))
                        .onErrorResume(FALL_BACKER)
                        .elementAt(0, false)
                :
                just(false);
    };

    private final Function<VerifyPair, Mono<Boolean>> REPEATABLE_VALIDATOR = pair -> {
        String key = pair.getKey();
        String verify = pair.getVerify();

        return isNotBlank(key) && isNotBlank(verify) ?
                reactiveStringRedisTemplate.opsForValue().get(key).switchIfEmpty(just(""))
                        .flatMap(v -> just(v.equals(verify)))
                :
                just(false);
    };

    private final Map<Boolean, Function<VerifyPair, Mono<Boolean>>> VALIDATORS = new HashMap<>(2, 1.0f);

    /**
     * generate pair
     *
     * @return
     */
    @Override
    public Mono<VerifyPair> generate() {
        return this.generate(null);
    }

    /**
     * generate pair
     *
     * @param type
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(RandomType type) {
        return this.generate(type, null);
    }

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(RandomType type, Integer length) {
        return this.generate(type, length, null);
    }

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    @Override
    public Mono<VerifyPair> generate(RandomType type, Integer length, Duration expire) {
        LOGGER.info("Mono<VerifyPair> generate(RandomType type, Integer length, Duration expire), type = {}, length = {}, expire = {}", type, length, expire);

        String key = generateRandom(KEY_TYPE, KEY_LEN);
        String verify = generateRandom(type != null ? type : VERIFY_TYPE, length != null && length >= MIN_LEN && length <= MAX_LEN ? length : VERIFY_LEN);

        LOGGER.info("Mono<VerifyPair> generate(RandomType type, int length, Duration expire), key = {}, verify = {}", key, verify);

        return reactiveStringRedisTemplate.opsForValue()
                .set(key, verify, expire != null ? expire : DEFAULT_DURATION)
                .flatMap(s -> just(new VerifyPair(key, verify)));
    }

    /**
     * validate pair
     *
     * @param verifyPair
     * @return
     */
    @Override
    public Mono<Boolean> validate(VerifyPair verifyPair) {
        return this.validate(verifyPair, null);
    }

    /**
     * validate pair
     *
     * @param verifyPair
     * @param repeatable
     * @return
     */
    @Override
    public Mono<Boolean> validate(VerifyPair verifyPair, Boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(VerifyPair verifyPair, Boolean repeatable), verifyPair = {}, repeatable = {}", verifyPair, repeatable);
        return ofNullable(verifyPair)
                .map(cp -> VALIDATORS.get(repeatable != null ? repeatable : DEFAULT_REPEATABLE).apply(verifyPair)).orElse(just(false));
    }

}
