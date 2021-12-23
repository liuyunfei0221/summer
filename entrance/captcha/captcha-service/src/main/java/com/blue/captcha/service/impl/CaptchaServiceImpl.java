package com.blue.captcha.service.impl;

import com.blue.base.constant.base.RandomType;
import com.blue.captcha.api.model.CaptchaPair;
import com.blue.captcha.service.inter.CaptchaService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;

import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.RandomType.ALPHANUMERIC;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.VALIDATION;
import static java.time.temporal.ChronoUnit.MINUTES;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class CaptchaServiceImpl implements CaptchaService {

    private static final Logger LOGGER = getLogger(CaptchaServiceImpl.class);

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public CaptchaServiceImpl(ReactiveStringRedisTemplate reactiveStringRedisTemplate) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
    }


    private static final String SCRIPT_STR = VALIDATION.str;

    /**
     * verify assert script
     */
    private static final RedisScript<Boolean> SCRIPT = generateScriptByScriptStr(SCRIPT_STR, Boolean.class);
    private static final Function<String, List<String>> LIST_GENERATOR = List::of;

    private static final RandomType KEY_TYPE = ALPHANUMERIC;
    private static final int KEY_LEN = 16;
    private static final Duration DEFAULT_EXPIRE = Duration.of(15L, MINUTES);


    private static final Function<Throwable, Flux<Long>> FALL_BACKER = e -> {
        LOGGER.error("e = {}", e);
        return Flux.just(1L);
    };
    //
    //private final Function<String, Mono<Boolean>> ALLOWED_GETTER = limitKey ->
    //        reactiveStringRedisTemplate.execute(SCRIPT, LIMIT_KEYS_GENERATOR.apply(limitKey),
    //                        SCRIPT_ARGS_SUP.get())
    //                .onErrorResume(FALL_BACKER)
    //                .elementAt(0)
    //                .flatMap(allowed ->
    //                        Mono.just(allowed == 1L));

    //private final Function<CaptchaPair, Mono<Boolean>> UN_REPEATABLE_VALIDATOR = pair -> {
    //
    //     reactiveStringRedisTemplate.execute(SCRIPT, LIST_GENERATOR.apply(pair.getKey()), LIST_GENERATOR.apply(pair.getVerify()))
    //             .elementAt(0,false)
    //
    //
    //};

    /**
     * generate pair
     *
     * @param type
     * @param length
     * @param expire
     * @return
     */
    @Override
    public Mono<CaptchaPair> generate(RandomType type, int length, Duration expire) {
        LOGGER.info("Mono<CaptchaPair> generate(RandomType type, int length, Duration expire), type = {}, length = {}, expire = {}", type, length, expire);

        String key = generateRandom(KEY_TYPE, KEY_LEN);
        String verify = generateRandom(type, length);

        LOGGER.info("Mono<CaptchaPair> generate(RandomType type, int length, Duration expire), key = {}, verify = {}", key, verify);

        return reactiveStringRedisTemplate.opsForValue()
                .set(key, verify, expire != null ? expire : DEFAULT_EXPIRE)
                .flatMap(s -> just(new CaptchaPair(key, verify)));
    }

    /**
     * validate pair
     *
     * @param captchaPair
     * @param repeatable
     * @return
     */
    @Override
    public Mono<Boolean> validate(CaptchaPair captchaPair, boolean repeatable) {


        return null;
    }

}
