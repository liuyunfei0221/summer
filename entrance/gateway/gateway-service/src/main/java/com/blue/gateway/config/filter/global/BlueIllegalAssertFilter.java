package com.blue.gateway.config.filter.global;

import com.blue.basic.model.exps.BlueException;
import com.blue.gateway.component.illegal.IllegalAsserter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import static com.blue.basic.constant.common.ResponseElement.ILLEGAL_REQUEST;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_ILLEGAL_ASSERT;
import static reactor.core.publisher.Mono.error;

/**
 * illegal interceptor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"UnusedReturnValue"})
@Component
public final class BlueIllegalAssertFilter implements GlobalFilter, Ordered {

    private final IllegalAsserter illegalAsserter;

    public BlueIllegalAssertFilter(IllegalAsserter illegalAsserter) {
        this.illegalAsserter = illegalAsserter;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return illegalAsserter.assertIllegalRequest(exchange)
                .flatMap(a ->
                        a ? chain.filter(exchange) : error(() -> new BlueException(ILLEGAL_REQUEST)));
    }

    @Override
    public int getOrder() {
        return BLUE_ILLEGAL_ASSERT.order;
    }

}