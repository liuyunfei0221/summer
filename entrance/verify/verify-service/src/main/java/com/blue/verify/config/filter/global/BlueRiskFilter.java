package com.blue.verify.config.filter.global;

import com.blue.base.common.base.Asserter;
import com.blue.base.model.exps.BlueException;
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

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.BlueDataAttrKey.JWT;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.verify.config.filter.BlueFilterOrder.BLUE_RISK;
import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;

/**
 * illegal interceptor
 *
 * @author DarkBlue
 */
@SuppressWarnings({"UnusedReturnValue", "unused", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection", "FieldCanBeLocal"})
@Component
public final class BlueRiskFilter implements WebFilter, Ordered {

    private static final Logger LOGGER = Loggers.getLogger(BlueRiskFilter.class);

    private final ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BlueRiskFilter(ExecutorService executorService, RiskControlDeploy riskControlDeploy) {
        this.executorService = executorService;

        Long illegalExpireSeconds = riskControlDeploy.getIllegalExpireSeconds();
        if (illegalExpireSeconds == null || illegalExpireSeconds < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalExpireSeconds can't be null or less than 1");

        Integer illegalCapacity = riskControlDeploy.getIllegalCapacity();
        if (illegalCapacity == null || illegalCapacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalCapacity can't be null or less than 1");

        illegalJwtCache = newBuilder()
                .expireAfterAccess(of(illegalExpireSeconds, SECONDS))
                .executor(this.executorService)
                .maximumSize(illegalCapacity).build();

        illegalIpCache = newBuilder()
                .expireAfterAccess(of(illegalExpireSeconds, SECONDS))
                .executor(this.executorService)
                .maximumSize(illegalCapacity).build();
    }

    private static Cache<String, Boolean> illegalJwtCache;

    private static Cache<String, Boolean> illegalIpCache;

    private final Function<ServerWebExchange, Mono<Boolean>> ILLEGAL_ASSERTER = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();

        boolean valid = !(ofNullable(illegalIpCache.getIfPresent(
                ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                        .orElseThrow(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message)))).orElse(false)
                ||
                ofNullable(illegalJwtCache.getIfPresent(
                        ofNullable(attributes.get(JWT.key)).map(String::valueOf).orElse(""))).orElse(false));

        return  just(true);

        //return valid ?
        //        just(true)
        //        :
        //        error(() -> new BlueException(NOT_ACCEPTABLE.status, NOT_ACCEPTABLE.code, NOT_ACCEPTABLE.message));
    };

    @SuppressWarnings("NullableProblems")
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        return ILLEGAL_ASSERTER.apply(exchange)
                .flatMap(v -> chain.filter(exchange));
    }

    @Override
    public int getOrder() {
        return BLUE_RISK.order;
    }

    public boolean markIllegalIp(String ip, boolean mark) {
        LOGGER.info("markIllegalIp(String ip), ip = {}, mark = {}", ip, mark);
        try {
            if (mark) {
                illegalIpCache.put(ip, true);
            } else {
                illegalIpCache.invalidate(ip);
            }
        } catch (Exception e) {
            LOGGER.error("markIllegalIp(String ip, boolean mark) failed, ip = {}, mark = {}, e = {}", ip, mark, e);
            return false;
        }
        return true;
    }

    public boolean markIllegalJwt(String jwt, boolean mark) {
        LOGGER.info("markIllegalJwt(String jwt), jwt = {}, mark = {}", jwt, mark);
        try {
            if (mark) {
                illegalJwtCache.put(jwt, true);
            } else {
                illegalJwtCache.invalidate(jwt);
            }
        } catch (Exception e) {
            LOGGER.error("markIllegalJwt(String jwt, boolean mark) failed, jwt = {}, mark = {},  e = {}", jwt, mark, e);
            return false;
        }
        return true;
    }

}
