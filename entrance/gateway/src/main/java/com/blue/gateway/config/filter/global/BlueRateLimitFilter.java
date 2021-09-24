package com.blue.gateway.config.filter.global;

import com.blue.base.common.reactive.ReactiveCommonFunctions;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.exps.BlueException;
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

import static com.blue.base.constant.base.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_RATE_LIMIT;
import static com.blue.redis.api.generator.BlueRedisScriptGenerator.generateScriptByScriptStr;
import static com.blue.redis.constant.RedisScripts.RATE_LIMITER;
import static java.lang.String.valueOf;
import static java.time.Instant.now;
import static java.util.Arrays.asList;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;

/**
 * 限流过滤器
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

    /**
     * 限流特征获取器
     */
    private static final Function<ServerHttpRequest, String> REQUEST_IDENTITY_GETTER = ReactiveCommonFunctions.REQUEST_IDENTITY_GETTER;

    /**
     * redis限流脚本
     */
    private static final RedisScript<Long> SCRIPT = generateScriptByScriptStr(SCRIPT_STR, Long.class);

    /**
     * 限流脚本keys构建器
     */
    private static final Function<String, List<String>> LIMIT_KEYS_GENERATOR = GatewayCommonFactory.LIMIT_KEYS_GENERATOR;

    /**
     * 填充速率，突发容量
     */
    private static String REPLENISH_RATE, BURST_CAPACITY;

    /**
     * 当前秒级时间戳字符串获取器
     */
    private static final Supplier<String> CURRENT_SEC_STAMP_SUP = () -> now().getEpochSecond() + "";

    /**
     * 限流脚本参数构建器
     */
    private static final Supplier<List<String>> SCRIPT_ARGS_SUP = () ->
            asList(REPLENISH_RATE, BURST_CAPACITY, CURRENT_SEC_STAMP_SUP.get());

    /**
     * redis异常处理
     */
    private static final Function<Throwable, Flux<Long>> FALL_BACKER = e -> {
        LOGGER.error("e = {}", e);
        return Flux.just(1L);
    };

    /**
     * 限流处理器
     */
    private final Function<String, Mono<Boolean>> ALLOWED_GETTER = limitKey ->
            reactiveStringRedisTemplate.execute(SCRIPT, LIMIT_KEYS_GENERATOR.apply(limitKey),
                            SCRIPT_ARGS_SUP.get())
                    .onErrorResume(FALL_BACKER)
                    .elementAt(0)
                    .flatMap(allowed ->
                            Mono.just(allowed == 1L));

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        REPLENISH_RATE = valueOf(rateLimiterDeploy.getReplenishRate());
        BURST_CAPACITY = valueOf(rateLimiterDeploy.getBurstCapacity());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String requestId = ofNullable(exchange.getAttribute(BlueDataAttrKey.REQUEST_ID.key)).map(String::valueOf).orElse("");
        LOGGER.info("blueRateLimitFilter -> requestId = {}", requestId);

        String limitKey = REQUEST_IDENTITY_GETTER.apply(exchange.getRequest());
        return ALLOWED_GETTER.apply(limitKey)
                .flatMap(a -> {
                    if (a)
                        return chain.filter(exchange);
                    LOGGER.error("触发限流 -> requestId = {}, limitKey = {}", requestId, limitKey);
                    return error(new BlueException(TOO_MANY_REQUESTS.status, TOO_MANY_REQUESTS.code, TOO_MANY_REQUESTS.message));
                });
    }

    @Override
    public int getOrder() {
        return BLUE_RATE_LIMIT.order;
    }

}
