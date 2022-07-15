package com.blue.verify.config.filter.global;

import com.blue.basic.model.exps.BlueException;
import com.blue.redis.api.generator.BlueRateLimiterGenerator;
import com.blue.redis.component.BlueFixedTokenBucketRateLimiter;
import com.blue.verify.config.deploy.RateLimiterDeploy;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import static com.blue.basic.common.reactive.ReactiveCommonFunctions.SERVER_HTTP_REQUEST_IDENTITY_SYNC_KEY_GETTER;
import static com.blue.basic.constant.common.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_RATE_LIMIT;
import static reactor.core.publisher.Mono.error;

/**
 * rate limiter
 *
 * @author liuyunfei
 */
@Component
public final class BlueRateLimitFilter implements WebFilter, Ordered {

    private final BlueFixedTokenBucketRateLimiter blueFixedTokenBucketRateLimiter;

    public BlueRateLimitFilter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, RateLimiterDeploy rateLimiterDeploy) {
        this.blueFixedTokenBucketRateLimiter = BlueRateLimiterGenerator.generateFixedTokenBucketRateLimiter(reactiveStringRedisTemplate, scheduler, rateLimiterDeploy.getReplenishRate(), rateLimiterDeploy.getBurstCapacity());
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
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
