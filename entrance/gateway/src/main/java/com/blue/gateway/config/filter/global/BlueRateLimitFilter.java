package com.blue.gateway.config.filter.global;

import com.blue.base.common.reactive.ReactiveCommonFunctions;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.gateway.common.GatewayCommonFactory;
import com.blue.gateway.config.deploy.RateLimiterDeploy;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.base.constant.base.CommonException.TOO_MANY_REQUESTS_EXP;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_RATE_LIMIT;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.RATE_LIMITER;
import static java.lang.String.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;

/**
 * rate limiter
 *
 * @author DarkBlue
 */
@SuppressWarnings("AliControlFlowStatementWithoutBraces")
@Component
public final class BlueRateLimitFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = Loggers.getLogger(BlueRateLimitFilter.class);

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private final RateLimiterDeploy rateLimiterDeploy;

    public BlueRateLimitFilter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, RateLimiterDeploy rateLimiterDeploy) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.rateLimiterDeploy = rateLimiterDeploy;
    }

    private static final String SCRIPT_STR = RATE_LIMITER.str;

    private static final Function<ServerHttpRequest, String> REQUEST_IDENTITY_GETTER = ReactiveCommonFunctions.REQUEST_IDENTITY_GETTER;

    private static final RedisScript<Long> SCRIPT = generateScriptByScriptStr(SCRIPT_STR, Long.class);

    private static final Function<String, List<String>> LIMIT_KEYS_GENERATOR = GatewayCommonFactory.LIMIT_KEYS_GENERATOR;

    private static String REPLENISH_RATE, BURST_CAPACITY;

    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + "";

    private static final Supplier<List<String>> SCRIPT_ARGS_SUP = () ->
            asList(REPLENISH_RATE, BURST_CAPACITY, CURRENT_SEC_STAMP_SUP.get());

    private static final Function<Throwable, Flux<Long>> FALL_BACKER = e -> {
        LOGGER.error("e = {}", e);
        return Flux.just(1L);
    };

    private final Function<String, Mono<Boolean>> ALLOWED_GETTER = limitKey ->
            reactiveStringRedisTemplate.execute(SCRIPT, LIMIT_KEYS_GENERATOR.apply(limitKey),
                            SCRIPT_ARGS_SUP.get())
                    .onErrorResume(FALL_BACKER)
                    .elementAt(0)
                    .flatMap(allowed ->
                            Mono.just(allowed == 1L));

    @PostConstruct
    private void init() {
        REPLENISH_RATE = valueOf(rateLimiterDeploy.getReplenishRate());
        BURST_CAPACITY = valueOf(rateLimiterDeploy.getBurstCapacity());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = ofNullable(exchange.getAttribute(BlueDataAttrKey.REQUEST_ID.key)).map(String::valueOf).orElse("");

        String limitKey = REQUEST_IDENTITY_GETTER.apply(exchange.getRequest());
        return ALLOWED_GETTER.apply(limitKey)
                .flatMap(a -> {
                    if (a)
                        return chain.filter(exchange);
                    LOGGER.error("has been limited -> requestId = {}, limitKey = {}", requestId, limitKey);
                    return error(TOO_MANY_REQUESTS_EXP.exp);
                });
    }

    @Override
    public int getOrder() {
        return BLUE_RATE_LIMIT.order;
    }

}
