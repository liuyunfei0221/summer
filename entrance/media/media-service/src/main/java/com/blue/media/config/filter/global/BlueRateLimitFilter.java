package com.blue.media.config.filter.global;

import com.blue.base.model.exps.BlueException;
import com.blue.media.config.deploy.RateLimiterDeploy;
import com.blue.redis.common.TokenBucketRateLimiter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.REQUEST_IDENTITY_GETTER;
import static com.blue.base.constant.base.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.media.config.filter.BlueFilterOrder.BLUE_RATE_LIMIT;
import static reactor.core.publisher.Mono.error;

/**
 * rate limiter
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Component
public final class BlueRateLimitFilter implements WebFilter, Ordered {

    private final TokenBucketRateLimiter tokenBucketRateLimiter;

    public BlueRateLimitFilter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, RateLimiterDeploy rateLimiterDeploy) {
        this.tokenBucketRateLimiter = new TokenBucketRateLimiter(reactiveStringRedisTemplate, scheduler, rateLimiterDeploy.getReplenishRate(), rateLimiterDeploy.getBurstCapacity());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return REQUEST_IDENTITY_GETTER.apply(exchange.getRequest())
                .flatMap(tokenBucketRateLimiter::isAllowed)
                .flatMap(a ->
                        a ? chain.filter(exchange) : error(() -> new BlueException(TOO_MANY_REQUESTS))
                );
    }

    @Override
    public int getOrder() {
        return BLUE_RATE_LIMIT.order;
    }

}
