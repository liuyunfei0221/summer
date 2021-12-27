package com.blue.verify.config.filter.global;

import com.blue.base.common.base.Asserter;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.base.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.caffeine.constant.ExpireStrategy;
import com.blue.verify.config.deploy.RiskControlDeploy;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.BlueDataAttrKey.JWT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_ILLEGAL_ASSERT;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;

/**
 * illegal interceptor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection", "FieldCanBeLocal", "JavaDoc"})
@Component
public final class BlueIllegalAssertFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = Loggers.getLogger(BlueIllegalAssertFilter.class);

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

        IP_MARKERS.put(true, ip -> illegalIpCache.put(ip, true));
        IP_MARKERS.put(false, ip -> illegalIpCache.invalidate(ip));

        JWT_MARKERS.put(true, jwt -> illegalJwtCache.put(jwt, true));
        JWT_MARKERS.put(false, jwt -> illegalJwtCache.invalidate(jwt));
    }

    private static Cache<String, Boolean> illegalIpCache;
    private static Cache<String, Boolean> illegalJwtCache;

    private static final Map<Boolean, Consumer<String>> IP_MARKERS = new HashMap<>(4, 1.0f);
    private static final Map<Boolean, Consumer<String>> JWT_MARKERS = new HashMap<>(4, 1.0f);

    private final Consumer<ServerWebExchange> ILLEGAL_ASSERTER = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();

        ofNullable(illegalIpCache.getIfPresent(ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                .orElseThrow(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message))))
                .ifPresent(illegalIp -> {
                    if (illegalIp)
                        throw new BlueException(ILLEGAL_REQUEST);
                });

        ofNullable(attributes.get(JWT.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                .map(illegalJwtCache::getIfPresent)
                .ifPresent(illegalJwt -> {
                    if (illegalJwt)
                        throw new BlueException(ILLEGAL_REQUEST);
                });
    };

    private static final Consumer<IllegalMarkEvent> EVENT_HANDLER = event -> {
        if (event == null)
            return;

        boolean mark = ofNullable(event.getMark()).orElse(true);

        ofNullable(event.getJwt())
                .filter(jwt -> !"".equals(jwt))
                .ifPresent(jwt -> JWT_MARKERS.get(mark).accept(jwt));
        ofNullable(event.getIp())
                .filter(ip -> !"".equals(ip))
                .ifPresent(ip -> IP_MARKERS.get(mark).accept(ip));
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
        LOGGER.info("void handleIllegalMarkEvent(IllegalMarkEvent event), event = {}", event);
        executorService.execute(() -> EVENT_HANDLER.accept(event));
    }

}
