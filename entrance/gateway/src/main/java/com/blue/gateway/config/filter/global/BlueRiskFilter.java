package com.blue.gateway.config.filter.global;

import com.blue.base.common.base.Asserter;
import com.blue.base.model.exps.BlueException;
import com.blue.gateway.config.deploy.RiskControlDeploy;
import com.blue.gateway.remote.consumer.RpcVerifyServiceConsumer;
import com.blue.verify.api.model.VerifyPair;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.base.CommonFunctions.SIMPLE_HEADER_VALUE_GETTER;
import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.BlueDataAttrKey.JWT;
import static com.blue.base.constant.base.BlueHeader.VERIFY_KEY;
import static com.blue.base.constant.base.BlueHeader.VERIFY_VALUE;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_RISK;
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
@SuppressWarnings({"UnusedReturnValue", "unused", "AliControlFlowStatementWithoutBraces", "FieldCanBeLocal"})
@Component
public final class BlueRiskFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = Loggers.getLogger(BlueRiskFilter.class);

    private RpcVerifyServiceConsumer rpcVerifyServiceConsumer;

    private final ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BlueRiskFilter(RpcVerifyServiceConsumer rpcVerifyServiceConsumer, ExecutorService executorService, RiskControlDeploy riskControlDeploy) {
        this.rpcVerifyServiceConsumer = rpcVerifyServiceConsumer;
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

    private final Function<ServerHttpRequest, Mono<Boolean>> VERIFY_ASSERTER = request -> {
        HttpHeaders headers = request.getHeaders();
        return rpcVerifyServiceConsumer.validate(new VerifyPair(SIMPLE_HEADER_VALUE_GETTER.apply(headers, VERIFY_KEY.name),
                        SIMPLE_HEADER_VALUE_GETTER.apply(headers, VERIFY_VALUE.name)), false)
                .flatMap(v -> v ? just(true) : error(() -> new BlueException(NOT_ACCEPTABLE.status, NOT_ACCEPTABLE.code, NOT_ACCEPTABLE.message)));
    };

    private final Function<ServerWebExchange, Mono<Boolean>> ILLEGAL_ASSERTER = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();
        boolean valid = !(ofNullable(illegalJwtCache.getIfPresent(
                ofNullable(attributes.get(JWT.key)).map(String::valueOf).orElse(""))).orElse(false)
                ||
                ofNullable(illegalIpCache.getIfPresent(
                        ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                                .orElseThrow(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message))
                )).orElse(false));

        return valid ?
                just(true)
                :
                VERIFY_ASSERTER.apply(exchange.getRequest());
    };

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
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
