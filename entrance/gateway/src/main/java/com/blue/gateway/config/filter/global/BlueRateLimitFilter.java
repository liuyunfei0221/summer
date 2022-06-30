package com.blue.gateway.config.filter.global;

import com.blue.base.model.exps.BlueException;
import com.blue.gateway.config.deploy.RateLimiterDeploy;
import com.blue.redis.api.generator.BlueRateLimiterGenerator;
import com.blue.redis.common.BlueFixedTokenBucketRateLimiter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.SERVER_HTTP_REQUEST_IDENTITY_SYNC_KEY_GETTER;
import static com.blue.base.constant.common.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_RATE_LIMIT;
import static reactor.core.publisher.Mono.error;

/**
 * rate limiter
 *
 * @author liuyunfei
 */
@Component
public final class BlueRateLimitFilter implements GlobalFilter, Ordered {

    private final BlueFixedTokenBucketRateLimiter blueFixedTokenBucketRateLimiter;

    public BlueRateLimitFilter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, RateLimiterDeploy rateLimiterDeploy) {
        this.blueFixedTokenBucketRateLimiter = BlueRateLimiterGenerator.generateFixedTokenBucketRateLimiter(reactiveStringRedisTemplate, scheduler, rateLimiterDeploy.getReplenishRate(), rateLimiterDeploy.getBurstCapacity());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return SERVER_HTTP_REQUEST_IDENTITY_SYNC_KEY_GETTER.apply(exchange.getRequest())
                .flatMap(blueFixedTokenBucketRateLimiter::isAllowed)
                .flatMap(a ->
                        a ? chain.filter(exchange) : error(() -> new BlueException(TOO_MANY_REQUESTS))
                );
    }

    @Override
    public int getOrder() {
        return BLUE_RATE_LIMIT.order;
    }

}
