package com.blue.gateway.config.filter.global;

import com.blue.basic.constant.common.Symbol;
import com.blue.gateway.config.deploy.CircuitBreakerDeploy;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.net.URI;
import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_INSTANCE_CIRCUIT_BREAKER;
import static io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator.of;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.Optional.ofNullable;
import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;
import static reactor.util.Loggers.getLogger;

/**
 * instance circuit breaker
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unchecked", "AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueInstanceCircuitBreakerFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = getLogger(BlueInstanceCircuitBreakerFilter.class);

    private CircuitBreakerRegistry circuitBreakerRegistry;

    public BlueInstanceCircuitBreakerFilter(CircuitBreakerDeploy circuitBreakerDeploy) {
        circuitBreakerRegistry = REGISTRY_GENERATOR.apply(circuitBreakerDeploy);
    }

    /**
     * generate CircuitBreakerRegistry by deploy
     */
    private static final Function<CircuitBreakerDeploy, CircuitBreakerRegistry> REGISTRY_GENERATOR = deploy -> {
        LOGGER.info("deploy = {}", deploy);

        if (isNull(deploy))
            throw new RuntimeException("deploy can't be null");

        Predicate<Throwable> expPredicate;

        String recordFailurePredicateClassName = deploy.getRecordFailurePredicateClassName();
        if (isBlank(recordFailurePredicateClassName))
            throw new RuntimeException("recordFailurePredicateClassName name can't be blank");
        try {
            expPredicate = (Predicate<Throwable>) Class.forName(recordFailurePredicateClassName).getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("generate recordFailurePredicate failed, e = " + e);
        }

        CircuitBreakerConfig.Builder builder = CircuitBreakerConfig.custom();
        builder.recordException(expPredicate);

        ofNullable(deploy.getFailureRateThreshold()).ifPresent(builder::failureRateThreshold);
        ofNullable(deploy.getSlowCallRateThreshold()).ifPresent(builder::slowCallRateThreshold);
        ofNullable(deploy.getSlowCallDurationThresholdMillis()).ifPresent(m -> builder.slowCallDurationThreshold(Duration.of(m, MILLIS)));
        ofNullable(deploy.getPermittedNumberOfCallsInHalfOpenState()).ifPresent(builder::permittedNumberOfCallsInHalfOpenState);
        ofNullable(deploy.getMaxWaitDurationInHalfOpenStateMillis()).ifPresent(m -> builder.maxWaitDurationInHalfOpenState(Duration.of(m, MILLIS)));
        ofNullable(deploy.getSlidingWindowType()).ifPresent(builder::slidingWindowType);
        ofNullable(deploy.getSlidingWindowSize()).ifPresent(builder::slidingWindowSize);
        ofNullable(deploy.getMinimumNumberOfCalls()).ifPresent(builder::minimumNumberOfCalls);
        ofNullable(deploy.getWaitDurationInOpenStateMillis()).ifPresent(m -> builder.waitDurationInOpenState(Duration.of(m, MILLIS)));
        ofNullable(deploy.getAutomaticTransitionFromOpenToHalfOpenEnabled()).ifPresent(builder::automaticTransitionFromOpenToHalfOpenEnabled);

        return CircuitBreakerRegistry.of(builder.build());
    };

    /**
     * unknown host, concat host and port
     */
    private static final String UNKNOWN_HOST = Symbol.UNKNOWN.identity,
            KEY_VALUE_SEPARATOR = Symbol.COLON.identity;

    /**
     * server instance name generator
     */
    private static final Function<URI, String> INSTANCE_ID_GETTER = uri ->
            (ofNullable(uri.getHost()).orElse(UNKNOWN_HOST).intern() + KEY_VALUE_SEPARATOR + uri.getPort()).intern();

    /**
     * server instance name and circuitBreaker mapping
     */
    private final Map<String, CircuitBreaker> CIRCUIT_BREAKER_HOLDER = new ConcurrentHashMap<>();

    /**
     * get circuitBreaker by server instance name, init circuitBreaker on first call
     */
    private final Function<String, CircuitBreaker> CIRCUIT_BREAKER_GETTER = name ->
            ofNullable(CIRCUIT_BREAKER_HOLDER.get(name))
                    .orElseGet(() -> {
                        CircuitBreaker circuitBreaker;
                        synchronized (this) {
                            circuitBreaker = CIRCUIT_BREAKER_HOLDER.get(name);
                            if (isNull(circuitBreaker)) {
                                circuitBreaker = circuitBreakerRegistry.circuitBreaker(name);
                                CIRCUIT_BREAKER_HOLDER.put(name, circuitBreaker);
                            }
                        }
                        return circuitBreaker;
                    });

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        return chain.filter(exchange).transform(of(CIRCUIT_BREAKER_GETTER.apply(INSTANCE_ID_GETTER.apply(exchange.getAttribute(GATEWAY_REQUEST_URL_ATTR)).intern())));
    }

    @Override
    public int getOrder() {
        return BLUE_INSTANCE_CIRCUIT_BREAKER.order;
    }

}