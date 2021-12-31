package com.blue.file.config.filter.global;

import com.blue.base.model.exps.BlueException;
import com.blue.file.component.IllegalAsserter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import static com.blue.base.constant.base.ResponseElement.ILLEGAL_REQUEST;
import static com.blue.file.config.filter.BlueFilterOrder.BLUE_ILLEGAL_ASSERT;
import static reactor.core.publisher.Mono.error;

/**
 * illegal interceptor
 *
 * @author DarkBlue
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
