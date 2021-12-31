package com.blue.file.config.filter.global;

import com.blue.base.common.base.Asserter;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.base.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.caffeine.constant.ExpireStrategy;
import com.blue.file.config.deploy.RiskControlDeploy;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.blue.base.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_IP_PRE;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_JWT_PRE;
import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static com.blue.base.constant.base.Symbol.WILDCARD;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.file.config.filter.BlueFilterOrder.BLUE_ILLEGAL_ASSERT;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;

/**
 * illegal interceptor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "FieldCanBeLocal", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueIllegalAssertFilter implements WebFilter, Ordered {

    private final ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BlueIllegalAssertFilter(ExecutorService executorService, RiskControlDeploy riskControlDeploy) {
        this.executorService = executorService;

        Integer illegalCapacity = riskControlDeploy.getIllegalCapacity();
        if (illegalCapacity == null || illegalCapacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalCapacity can't be null or less than 1");

        Long illegalExpireSeconds = riskControlDeploy.getIllegalExpireSeconds();
        if (illegalExpireSeconds == null || illegalExpireSeconds < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalExpireSeconds can't be null or less than 1");

        ExpireStrategy expireStrategy = riskControlDeploy.getExpireStrategy();
        if (expireStrategy == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "expireStrategy can't be null");

        CaffeineConf caffeineConf = new CaffeineConfParams(illegalCapacity, of(illegalExpireSeconds, SECONDS), expireStrategy, executorService);

        illegalIpCache = generateCache(caffeineConf);
        illegalJwtCache = generateCache(caffeineConf);

        IP_MARKERS.put(true, (ip, resKey) -> illegalIpCache.put(ILLEGAL_IP_PREFIX + ip + CONCATENATION + resKey, true));
        IP_MARKERS.put(false, (ip, resKey) -> illegalIpCache.invalidate(ILLEGAL_IP_PREFIX + ip + CONCATENATION + resKey));

        JWT_MARKERS.put(true, (jwt, resKey) -> illegalJwtCache.put(ILLEGAL_JWT_PREFIX + jwt + CONCATENATION + resKey, true));
        JWT_MARKERS.put(false, (jwt, resKey) -> illegalJwtCache.invalidate(ILLEGAL_JWT_PREFIX + jwt + CONCATENATION + resKey));
    }

    private static Cache<String, Boolean> illegalIpCache;
    private static Cache<String, Boolean> illegalJwtCache;

    private static final Map<Boolean, BiConsumer<String, String>> IP_MARKERS = new HashMap<>(4, 1.0f);
    private static final Map<Boolean, BiConsumer<String, String>> JWT_MARKERS = new HashMap<>(4, 1.0f);

    private static final String
            ALL_RESOURCE = WILDCARD.identity,
            CONCATENATION = PAR_CONCATENATION.identity,
            ILLEGAL_IP_PREFIX = ILLEGAL_IP_PRE.key,
            ILLEGAL_JWT_PREFIX = ILLEGAL_JWT_PRE.key;

    private final Consumer<ServerWebExchange> ILLEGAL_ASSERTER = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();

        String resourceKey = REQ_RES_KEY_GENERATOR.apply(
                ofNullable(attributes.get(METHOD.key)).map(String::valueOf).orElse(""),
                ofNullable(attributes.get(URI.key)).map(String::valueOf).orElse(""));

        String ip = ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                .orElseThrow(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message));

        if (ofNullable(illegalIpCache.getIfPresent(ILLEGAL_IP_PREFIX + ip + CONCATENATION + ALL_RESOURCE)).orElse(false)
                ||
                ofNullable(illegalIpCache.getIfPresent(ILLEGAL_IP_PREFIX + ip + CONCATENATION + resourceKey)).orElse(false))
            throw new BlueException(ILLEGAL_REQUEST);

        ofNullable(attributes.get(JWT.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                .ifPresent(jwt -> {
                    if (ofNullable(illegalJwtCache.getIfPresent(ILLEGAL_JWT_PREFIX + jwt + CONCATENATION + ALL_RESOURCE)).orElse(false)
                            ||
                            ofNullable(illegalJwtCache.getIfPresent(ILLEGAL_JWT_PREFIX + jwt + CONCATENATION + resourceKey)).orElse(false))
                        throw new BlueException(ILLEGAL_REQUEST);
                });
    };

    private static final Consumer<IllegalMarkEvent> EVENT_HANDLER = event -> {
        String resource = ofNullable(event.getResourceKey()).filter(Asserter::isNotBlank).orElse(ALL_RESOURCE).intern();
        boolean mark = ofNullable(event.getMark()).orElse(true);

        ofNullable(event.getJwt())
                .filter(Asserter::isNotBlank)
                .ifPresent(jwt -> JWT_MARKERS.get(mark).accept(jwt, resource));
        ofNullable(event.getIp())
                .filter(Asserter::isNotBlank)
                .ifPresent(ip -> IP_MARKERS.get(mark).accept(ip, resource));
    };

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ILLEGAL_ASSERTER.accept(exchange);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return BLUE_ILLEGAL_ASSERT.order;
    }

    /**
     * handle event
     *
     * @param event
     */
    public void handleIllegalMarkEvent(IllegalMarkEvent event) {
        executorService.execute(() -> EVENT_HANDLER.accept(event));
    }

}
