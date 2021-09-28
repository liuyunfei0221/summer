package com.blue.gateway.config.filter.global;

import com.blue.base.constant.base.IllegalReason;
import com.blue.base.model.exps.BlueException;
import com.blue.gateway.config.deploy.RiskControlDeploy;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueDataAttrKey.*;
import static com.blue.base.constant.base.IllegalReason.UNKNOWN;
import static com.blue.base.constant.base.ResponseElement.NOT_ACCEPTABLE;
import static com.blue.gateway.config.filter.BlueFilterOrder.BLUE_ILLEGAL_INTERCEPT;
import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;

/**
 * 风控拦截过滤器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "unused", "AliControlFlowStatementWithoutBraces"})
@Component
public final class BlueIllegalInterceptFilter implements GlobalFilter, Ordered {

    private static final Logger LOGGER = Loggers.getLogger(BlueIllegalInterceptFilter.class);

    private final RiskControlDeploy riskControlDeploy;

    private final ExecutorService executorService;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public BlueIllegalInterceptFilter(RiskControlDeploy riskControlDeploy, ExecutorService executorService) {
        this.riskControlDeploy = riskControlDeploy;
        this.executorService = executorService;
    }

    /**
     * ip黑名单
     */
    private static Cache<String, String> illegalIpCacher;

    /**
     * jwt黑名单
     */
    private static Cache<String, String> illegalJwtCacher;

    /**
     * 非法ip断言
     */
    private static final Consumer<String> ILLEGAL_IP_ASSERTER = ip -> {
        String illegalReason = illegalIpCacher.getIfPresent(ip);
        if (illegalReason == null)
            return;

        LOGGER.warn("该ip触发风控拦截, ip = {}, illegalReason = {}", ip, illegalReason);
        throw new BlueException(NOT_ACCEPTABLE.status, NOT_ACCEPTABLE.code, illegalReason);
    };

    /**
     * 非法jwt断言
     */
    private static final Consumer<String> ILLEGAL_JWT_ASSERTER = jwt -> {
        String illegalReason = illegalJwtCacher.getIfPresent(jwt);
        if (illegalReason == null)
            return;

        LOGGER.warn("该jwt触发风控拦截, jwt = {}, illegalReason = {}", jwt, illegalReason);
        throw new BlueException(NOT_ACCEPTABLE.status, NOT_ACCEPTABLE.code, illegalReason);
    };

    /**
     * 过滤处理
     */
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


    /**
     * 初始化
     */
    @PostConstruct
    private void init() {
        Long illegalExpireSeconds = riskControlDeploy.getIllegalExpireSeconds();
        if (illegalExpireSeconds == null || illegalExpireSeconds < 1L)
            throw new RuntimeException("illegalExpireSeconds不能为空或小于1");

        Integer illegalCapacity = riskControlDeploy.getIllegalCapacity();
        if (illegalCapacity == null || illegalCapacity < 1)
            throw new RuntimeException("illegalCapacity不能为空或小于1");

        illegalIpCacher = newBuilder()
                .expireAfterAccess(of(illegalExpireSeconds, SECONDS))
                .executor(this.executorService)
                .maximumSize(illegalCapacity).build();

        illegalJwtCacher = newBuilder()
                .expireAfterAccess(of(illegalExpireSeconds, SECONDS))
                .executor(this.executorService)
                .maximumSize(illegalCapacity).build();
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ILLEGAL_FILTER.accept(exchange);
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return BLUE_ILLEGAL_INTERCEPT.order;
    }


    /**
     * 标记ip黑名单
     *
     * @param ip
     * @param illegalReason
     * @return
     */
    public boolean markIllegalIp(String ip, IllegalReason illegalReason) {
        LOGGER.info("ip = {}, illegalReason = {}", ip, illegalReason);
        try {
            illegalIpCacher.put(ip, ofNullable(illegalReason).orElse(UNKNOWN).reason.intern());
        } catch (Exception e) {
            LOGGER.error("ip = {}, e = {}", ip, e);
            return false;
        }
        return true;
    }

    /**
     * 标记jwt串黑名单
     *
     * @param jwt
     * @param illegalReason
     * @return
     */
    public boolean markIllegalJwt(String jwt, IllegalReason illegalReason) {
        LOGGER.info("jwt = {}, active = {}, illegalReason = {}", jwt, illegalReason);
        try {
            illegalJwtCacher.put(jwt, ofNullable(illegalReason).orElse(UNKNOWN).reason.intern());
        } catch (Exception e) {
            LOGGER.error("jwt = {},  e = {}", jwt, e);
            return false;
        }
        return true;
    }

    /**
     * 清除ip黑名单标记
     *
     * @param ip
     * @return
     */
    public boolean clearMarkIllegalIp(String ip) {
        LOGGER.info("ip = {}", ip);
        try {
            illegalIpCacher.invalidate(ip);
        } catch (Exception e) {
            LOGGER.error("ip = {}, e = {}", ip, e);
            return false;
        }
        return true;
    }

    /**
     * 清除jwt串黑名单标记
     *
     * @param jwt
     * @return
     */
    public boolean clearMarkIllegalJwt(String jwt) {
        LOGGER.info("jwt = {}", jwt);
        try {
            illegalJwtCacher.invalidate(jwt);
        } catch (Exception e) {
            LOGGER.error("jwt = {},  e = {}", jwt, e);
            return false;
        }
        return true;
    }

}
