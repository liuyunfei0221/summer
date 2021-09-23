package com.blue.gateway.config.filter.global;

import com.blue.base.constant.base.Symbol;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Function;

import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_INSTANCE_CIRCUIT_BREAKER;
import static java.util.Optional.ofNullable;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * 熔断过滤器
 *
 * @author DarkBlue
 */
@Component
public final class BlueInstanceCircuitBreakerFilter implements GlobalFilter, Ordered {

    private final CircuitBreakerRegistry circuitBreakerRegistry;

    public BlueInstanceCircuitBreakerFilter(CircuitBreakerRegistry circuitBreakerRegistry) {
        this.circuitBreakerRegistry = circuitBreakerRegistry;
    }

    private static final String UNKNOWN_HOST = Symbol.UNKNOWN.identity,
            KEY_VALUE_SEPARATOR = Symbol.KEY_VALUE_SEPARATOR.identity;

    private static final Function<URI, String> INSTANCE_ID_GETTER = uri ->
            ofNullable(uri.getHost()).orElse(UNKNOWN_HOST) + KEY_VALUE_SEPARATOR + uri.getPort();

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).transform(CircuitBreakerOperator.of(
                circuitBreakerRegistry.circuitBreaker(
                        INSTANCE_ID_GETTER.apply(exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).intern()
                )));
    }

    @Override
    public int getOrder() {
        return BLUE_INSTANCE_CIRCUIT_BREAKER.order;
    }

}
