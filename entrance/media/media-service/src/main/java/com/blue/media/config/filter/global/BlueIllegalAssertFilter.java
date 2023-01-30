package com.blue.media.config.filter.global;

import com.blue.basic.model.exps.BlueException;
import com.blue.media.component.illegal.IllegalAsserter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.blue.basic.constant.common.ResponseElement.ILLEGAL_REQUEST;
import static com.blue.media.config.filter.BlueFilterOrder.BLUE_ILLEGAL_ASSERT;
import static reactor.core.publisher.Mono.error;

/**
 * illegal interceptor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"UnusedReturnValue"})
@Component
public final class BlueIllegalAssertFilter implements WebFilter, Ordered {

    private final IllegalAsserter illegalAsserter;

    public BlueIllegalAssertFilter(IllegalAsserter illegalAsserter) {
        this.illegalAsserter = illegalAsserter;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return illegalAsserter.assertIllegalRequest(exchange)
                .flatMap(a ->
                        a ? chain.filter(exchange) : error(() -> new BlueException(ILLEGAL_REQUEST)));
    }

    @Override
    public int getOrder() {
        return BLUE_ILLEGAL_ASSERT.order;
    }

}