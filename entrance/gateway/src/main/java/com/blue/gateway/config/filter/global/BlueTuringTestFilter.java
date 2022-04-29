package com.blue.gateway.config.filter.global;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_TURING_TEST;

/**
 * rate limiter
 *
 * @author liuyunfei
 */
@Component
public final class BlueTuringTestFilter implements GlobalFilter, Ordered {

//    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

//    public BlueTuringTestFilter(RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer, Scheduler scheduler, RateLimiterDeploy rateLimiterDeploy) {
//
//    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        return SERVER_HTTP_REQUEST_IDENTITY_SYNC_KEY_GETTER.apply(exchange.getRequest())
//                .flatMap(blueTokenBucketRateLimiter::isAllowed)
//                .flatMap(a ->
//                        a ? chain.filter(exchange) : error(() -> new BlueException(TOO_MANY_REQUESTS))
//                );

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return BLUE_TURING_TEST.order;
    }

}
