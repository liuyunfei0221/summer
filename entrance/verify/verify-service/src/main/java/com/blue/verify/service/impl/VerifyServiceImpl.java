package com.blue.verify.service.impl;

import com.blue.base.constant.base.RandomType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.redis.api.generator.BlueValidatorGenerator;
import com.blue.redis.common.BlueValidator;
import com.blue.verify.config.deploy.VerifyDeploy;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.ResponseElement.ILLEGAL_REQUEST;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static java.time.temporal.ChronoUnit.MILLIS;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "FieldCanBeLocal", "AlibabaAvoidComplexCondition"})
@Service
public class VerifyServiceImpl implements VerifyService {

    private static final Logger LOGGER = getLogger(VerifyServiceImpl.class);

    private BlueValidator blueValidator;

    private final int KEY_LEN;
    private final RandomType RANDOM_TYPE;
    private final int VERIFY_LEN;
    private final int MIN_LEN;
    private final int MAX_LEN;
    private final Duration DEFAULT_DURATION;
    private final boolean DEFAULT_REPEATABLE;

    public VerifyServiceImpl(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, VerifyDeploy verifyDeploy) {
        this.blueValidator = BlueValidatorGenerator.generateValidator(reactiveStringRedisTemplate, scheduler);

        if (isNull(verifyDeploy))
            throw new RuntimeException("verifyDeploy can't be null");

        Integer keyLength = verifyDeploy.getKeyLength();
        if (isNull(keyLength) || keyLength < 1)
            throw new RuntimeException("keyLength can't be null or less than 1");

        RandomType randomType = verifyDeploy.getRandomType();
        if (isNull(randomType))
            throw new RuntimeException("randomType can't be null");

        Integer verifyLength = verifyDeploy.getVerifyLength();
        if (isNull(verifyLength) || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Integer minLength = verifyDeploy.getMinLength();
        if (isNull(minLength) || minLength < 1)
            throw new RuntimeException("minLength can't be null or less than 1");

        Integer maxLength = verifyDeploy.getMaxLength();
        if (isNull(maxLength) || maxLength < minLength)
            throw new RuntimeException("maxLength can't be null or less than minLength");

        Integer expireMillis = verifyDeploy.getExpireMillis();
        if (isNull(expireMillis) || expireMillis < 1)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        Boolean repeatable = verifyDeploy.getRepeatable();
        if (isNull(repeatable))
            throw new RuntimeException("repeatable can't be null");

        this.KEY_LEN = keyLength;
        this.RANDOM_TYPE = randomType;
        this.VERIFY_LEN = verifyLength;
        this.MIN_LEN = minLength;
        this.MAX_LEN = maxLength;
        this.DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
        this.DEFAULT_REPEATABLE = repeatable;

        this.VALIDATORS.put(false, UN_REPEATABLE_VALIDATOR);
        this.VALIDATORS.put(true, REPEATABLE_VALIDATOR);
    }

    private static final int MAX_KEY_LEN = 512;

    private final BiFunction<String, String, Mono<Boolean>> UN_REPEATABLE_VALIDATOR = (k, v) ->
            isNotBlank(k) && isNotBlank(v) ?
                    blueValidator.unrepeatableValidate(k, v)
                    :
                    just(false);

    private final BiFunction<String, String, Mono<Boolean>> REPEATABLE_VALIDATOR = (k, v) ->
            isNotBlank(k) && isNotBlank(v) ?
                    blueValidator.repeatableValidateUntilSuccessOrTimeout(k, v)
                    :
                    just(false);

    private final Map<Boolean, BiFunction<String, String, Mono<Boolean>>> VALIDATORS = new HashMap<>(4, 1.0f);

    private static final BiFunction<VerifyType, String, String> KEY_WRAPPER = (type, k) -> {
        if (isNotNull(type) && isNotBlank(k) && k.length() <= MAX_KEY_LEN)
            return type.identity + PAR_CONCATENATION.identity + k;

        throw new BlueException(ILLEGAL_REQUEST.status, ILLEGAL_REQUEST.code, "type,key can't be null and key len can't be greater than " + MAX_KEY_LEN);
    };

    /**
     * generate verify
     *
     * @param type
     * @return
     */
    @Override
    public Mono<String> generate(VerifyType type) {
        return this.generate(type, null, null, null);
    }

    /**
     * generate verify
     *
     * @param type
     * @param key
     * @return
     */
    @Override
    public Mono<String> generate(VerifyType type, String key) {
        return this.generate(type, key, null, null);
    }

    /**
     * generate verify
     *
     * @param type
     * @param key
     * @param length
     * @return
     */
    @Override
    public Mono<String> generate(VerifyType type, String key, Integer length) {
        return this.generate(type, key, length, null);
    }

    /**
     * generate verify
     *
     * @param type
     * @param key
     * @param length
     * @param expire
     * @return
     */
    @Override
    public Mono<String> generate(VerifyType type, String key, Integer length, Duration expire) {
        LOGGER.info("Mono<VerifyPair> generate(VerifyType type, String key, Integer length,  Duration expire) , type = {}, key = {}, length = {}, expire = {}", type, key, length, expire);

        if (isNull(type))
            return error(() -> new BlueException(ILLEGAL_REQUEST.status, ILLEGAL_REQUEST.code, "type can't be null"));


        String v = generateRandom(type.randomType, isNotNull(length) && length >= MIN_LEN && length <= MAX_LEN ? length : VERIFY_LEN);
        LOGGER.info("Mono<VerifyPair> generate(RandomType type, int length, Duration expire), key = {}, v = {}", key, v);

        return blueValidator.setKeyValueWithExpire(KEY_WRAPPER.apply(type, isNotBlank(key) ? key : generateRandom(RANDOM_TYPE, KEY_LEN)), v, isNotNull(expire) ? expire : DEFAULT_DURATION)
                .flatMap(ignore -> just(v));
    }

    /**
     * validate verify
     *
     * @param type
     * @param key
     * @param verify
     * @return
     */
    @Override
    public Mono<Boolean> validate(VerifyType type, String key, String verify) {
        return this.validate(type, key, verify, null);
    }

    /**
     * validate verify
     *
     * @param type
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    @Override
    public Mono<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable) {
        LOGGER.info("Mono<Boolean> validate(VerifyType type, String key, String verify, Boolean repeatable), type = {}, key = {}, verify = {}, repeatable = {}",
                type, key, verify, repeatable);

        return isNotNull(type) && isNotBlank(key) && isNotBlank(verify) ?
                VALIDATORS.get(isNotNull(repeatable) ? repeatable : DEFAULT_REPEATABLE)
                        .apply(KEY_WRAPPER.apply(type, key), verify)
                :
                error(() -> new BlueException(ILLEGAL_REQUEST.status, ILLEGAL_REQUEST.code, "type or verifyPair can't be null"));
    }

}
