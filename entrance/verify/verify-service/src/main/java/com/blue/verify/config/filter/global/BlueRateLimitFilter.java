package com.blue.verify.config.filter.global;

import com.blue.base.model.exps.BlueException;
import com.blue.verify.config.deploy.RateLimiterDeploy;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.common.base.CommonFunctions.LIMIT_KEYS_GENERATOR;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.REQUEST_IDENTITY_GETTER;
import static com.blue.base.constant.base.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.RATE_LIMITER;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_RATE_LIMIT;
import static java.lang.String.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static reactor.core.publisher.Mono.error;

/**
 * rate limiter
 *
 * @author DarkBlue
 */
@Component
public final class BlueRateLimitFilter implements WebFilter, Ordered {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public BlueRateLimitFilter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, RateLimiterDeploy rateLimiterDeploy) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;

        REPLENISH_RATE = valueOf(rateLimiterDeploy.getReplenishRate());
        BURST_CAPACITY = valueOf(rateLimiterDeploy.getBurstCapacity());
    }

    private static final RedisScript<Long> SCRIPT = generateScriptByScriptStr(RATE_LIMITER.str, Long.class);

    private static String REPLENISH_RATE, BURST_CAPACITY;

    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + "";

    private static final Supplier<List<String>> SCRIPT_ARGS_SUP = () ->
            asList(REPLENISH_RATE, BURST_CAPACITY, CURRENT_SEC_STAMP_SUP.get());

    private static final Function<Throwable, Flux<Long>> FALL_BACKER = e ->
            Flux.just(1L);

    private final Function<String, Mono<Boolean>> ALLOWED_GETTER = limitKey ->
            reactiveStringRedisTemplate.execute(SCRIPT, LIMIT_KEYS_GENERATOR.apply(limitKey),
                            SCRIPT_ARGS_SUP.get())
                    .onErrorResume(FALL_BACKER)
                    .elementAt(0)
                    .flatMap(allowed ->
                            Mono.just(allowed == 1L));

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return REQUEST_IDENTITY_GETTER.apply(exchange.getRequest())
                .flatMap(ALLOWED_GETTER)
                .flatMap(a ->
                        a ? chain.filter(exchange) : error(() -> new BlueException(TOO_MANY_REQUESTS))
                );
    }

    @Override
    public int getOrder() {
        return BLUE_RATE_LIMIT.order;
    }

}
