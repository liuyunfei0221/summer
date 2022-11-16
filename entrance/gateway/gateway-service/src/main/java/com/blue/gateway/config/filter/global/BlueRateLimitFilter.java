package com.blue.gateway.config.filter.global;

import com.blue.basic.model.exps.BlueException;
import com.blue.gateway.config.deploy.RateLimiterDeploy;
import com.blue.redis.component.BlueFixedTokenBucketRateLimiter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.blue.basic.common.base.CommonFunctions.REQUEST_IDENTITY_SYNC_KEY_GETTER;
import static com.blue.basic.constant.common.BlueDataAttrKey.CLIENT_IP;
import static com.blue.basic.constant.common.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_RATE_LIMIT;
import static com.blue.redis.api.generator.BlueRateLimiterGenerator.generateFixedTokenBucketRateLimiter;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.error;

/**
 * rate limiter
 *
 * @author liuyunfei
 */
@Component
public final class BlueRateLimitFilter implements GlobalFilter, Ordered {

    private final BlueFixedTokenBucketRateLimiter blueFixedTokenBucketRateLimiter;

    public BlueRateLimitFilter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, RateLimiterDeploy rateLimiterDeploy) {
        this.blueFixedTokenBucketRateLimiter = generateFixedTokenBucketRateLimiter(reactiveStringRedisTemplate, rateLimiterDeploy.getReplenishRate(), rateLimiterDeploy.getBurstCapacity());
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return REQUEST_IDENTITY_SYNC_KEY_GETTER.apply(ofNullable(exchange.getAttributes().get(CLIENT_IP.key)).map(String::valueOf).orElse(EMPTY_VALUE.value))
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
