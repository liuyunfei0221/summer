package com.blue.file.config.filter.global;

import com.blue.base.constant.base.IllegalReason;
import com.blue.base.model.exps.BlueException;
import com.blue.file.config.deploy.RiskControlDeploy;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.IllegalReason.UNKNOWN;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.NOT_ACCEPTABLE;
import static com.blue.file.config.filter.BlueFilterOrder.BLUE_ILLEGAL_INTERCEPT;
import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;

/**
 * illegal interceptor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
public final class BlueIllegalInterceptFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = Loggers.getLogger(BlueIllegalInterceptFilter.class);

    private final RiskControlDeploy riskControlDeploy;

    private final ExecutorService executorService;

    public BlueIllegalInterceptFilter(RiskControlDeploy riskControlDeploy, ExecutorService executorService) {
        this.riskControlDeploy = riskControlDeploy;
        this.executorService = executorService;
    }

    private static Cache<String, String> illegalIpCache;

    private static Cache<String, String> illegalJwtCache;

    private static final Consumer<String> ILLEGAL_IP_ASSERTER = ip -> {
        String illegalReason = illegalIpCache.getIfPresent(ip);
        if (illegalReason == null)
            return;

        LOGGER.warn("The ip has been risk control interception, ip = {}, illegalReason = {}", ip, illegalReason);
        throw new BlueException(NOT_ACCEPTABLE.status, NOT_ACCEPTABLE.code, illegalReason);
    };

    private static final Consumer<String> ILLEGAL_JWT_ASSERTER = jwt -> {
        String illegalReason = illegalJwtCache.getIfPresent(jwt);
        if (illegalReason == null)
            return;

        LOGGER.warn("The jwt has been risk control interception, jwt = {}, illegalReason = {}", jwt, illegalReason);
        throw new BlueException(NOT_ACCEPTABLE.status, NOT_ACCEPTABLE.code, illegalReason);
    };

    private final Consumer<ServerWebExchange> ILLEGAL_FILTER = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();

        String clientIp = ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).orElse("");
        String jwt = ofNullable(attributes.get(JWT.key)).map(String::valueOf).orElse("");
        String requestId = ofNullable(attributes.get(REQUEST_ID.key)).map(String::valueOf).orElse("");

        ILLEGAL_IP_ASSERTER.accept(clientIp);
        ILLEGAL_JWT_ASSERTER.accept(jwt);

        ServerHttpRequest request = exchange.getRequest();
        LOGGER.info("blueIllegalInterceptFilter -> requestId = {}, method = {}, uri = {}, jwt = {}, clientIp = {}", requestId, request.getMethodValue().intern(), request.getURI().getRawPath(),
                jwt, clientIp);
    };

    @PostConstruct
    private void init() {
        Long illegalExpireSeconds = riskControlDeploy.getIllegalExpireSeconds();
        if (illegalExpireSeconds == null || illegalExpireSeconds < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalExpireSeconds can't be null or less than 1");

        Integer illegalCapacity = riskControlDeploy.getIllegalCapacity();
        if (illegalCapacity == null || illegalCapacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalCapacity can't be null or less than 1");

        illegalIpCache = newBuilder()
                .expireAfterAccess(of(illegalExpireSeconds, SECONDS))
                .executor(this.executorService)
                .maximumSize(illegalCapacity).build();

        illegalJwtCache = newBuilder()
                .expireAfterAccess(of(illegalExpireSeconds, SECONDS))
                .executor(this.executorService)
                .maximumSize(illegalCapacity).build();
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ILLEGAL_FILTER.accept(exchange);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return BLUE_ILLEGAL_INTERCEPT.order;
    }

    public boolean markIllegalIp(String ip, IllegalReason illegalReason) {
        LOGGER.info("ip = {}, illegalReason = {}", ip, illegalReason);
        try {
            illegalIpCache.put(ip, ofNullable(illegalReason).orElse(UNKNOWN).reason.intern());
        } catch (Exception e) {
            LOGGER.error("ip = {}, e = {}", ip, e);
            return false;
        }
        return true;
    }

    public boolean markIllegalJwt(String jwt, IllegalReason illegalReason) {
        LOGGER.info("jwt = {}, active = {}, illegalReason = {}", jwt, illegalReason);
        try {
            illegalJwtCache.put(jwt, ofNullable(illegalReason).orElse(UNKNOWN).reason.intern());
        } catch (Exception e) {
            LOGGER.error("jwt = {},  e = {}", jwt, e);
            return false;
        }
        return true;
    }

    public boolean clearMarkIllegalIp(String ip) {
        LOGGER.info("ip = {}", ip);
        try {
            illegalIpCache.invalidate(ip);
        } catch (Exception e) {
            LOGGER.error("ip = {}, e = {}", ip, e);
            return false;
        }
        return true;
    }

    public boolean clearMarkIllegalJwt(String jwt) {
        LOGGER.info("jwt = {}", jwt);
        try {
            illegalJwtCache.invalidate(jwt);
        } catch (Exception e) {
            LOGGER.error("jwt = {},  e = {}", jwt, e);
            return false;
        }
        return true;
    }

}
