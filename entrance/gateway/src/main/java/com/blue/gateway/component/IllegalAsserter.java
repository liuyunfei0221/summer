package com.blue.gateway.component;

import com.blue.base.common.base.Asserter;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.base.model.exps.BlueException;
import com.blue.gateway.config.deploy.RiskControlDeploy;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.Asserter.isBlank;
import static com.blue.base.common.base.Asserter.isEmpty;
import static com.blue.base.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_IP_PRE;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_JWT_PRE;
import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.UNKNOWN_IP;
import static com.blue.base.constant.base.Symbol.WILDCARD;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.*;

/**
 * illegal request asserter
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public final class IllegalAsserter {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public IllegalAsserter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, RiskControlDeploy riskControlDeploy) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;

        Long illegalExpireSeconds = riskControlDeploy.getIllegalExpireSeconds();
        if (illegalExpireSeconds == null || illegalExpireSeconds < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalExpireSeconds can't be null or less than 1");

        ILLEGAL_EXPIRE_DURATION = Duration.of(illegalExpireSeconds, SECONDS);

        IP_MARKERS.put(true, (ip, res) -> {
            if (isBlank(ip))
                return error(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message));

            String key = ILLEGAL_IP_PREFIX + ip;
            return this.reactiveStringRedisTemplate
                    .opsForSet().add(key, ofNullable(res).filter(Asserter::isNotBlank).orElse(ALL_RESOURCE))
                    .flatMap(l -> this.reactiveStringRedisTemplate.expire(key, ILLEGAL_EXPIRE_DURATION));
        });

        IP_MARKERS.put(false, (ip, res) -> {
            if (isBlank(ip))
                return error(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message));

            String key = ILLEGAL_IP_PREFIX + ip;
            return isBlank(res) || ALL_RESOURCE.equals(res) ?
                    this.reactiveStringRedisTemplate.delete(key).map(l -> l > 0)
                    :
                    this.reactiveStringRedisTemplate.opsForSet().remove(key, res).map(l -> l > 0);
        });

        JWT_MARKERS.put(true, (jwt, res) -> {
            if (isBlank(jwt))
                return just(false);

            String key = ILLEGAL_JWT_PREFIX + jwt;
            return this.reactiveStringRedisTemplate
                    .opsForSet().add(key, ofNullable(res).filter(Asserter::isNotBlank).orElse(ALL_RESOURCE))
                    .flatMap(l -> this.reactiveStringRedisTemplate.expire(key, ILLEGAL_EXPIRE_DURATION));
        });

        JWT_MARKERS.put(false, (jwt, res) -> {
            if (isBlank(jwt))
                return just(false);

            String key = ILLEGAL_JWT_PREFIX + jwt;
            return isBlank(res) || ALL_RESOURCE.equals(res) ?
                    this.reactiveStringRedisTemplate.delete(key).map(l -> l > 0)
                    :
                    this.reactiveStringRedisTemplate.opsForSet().remove(key, res).map(l -> l > 0);
        });
    }

    private static final String
            ALL_RESOURCE = WILDCARD.identity,
            ILLEGAL_IP_PREFIX = ILLEGAL_IP_PRE.key,
            ILLEGAL_JWT_PREFIX = ILLEGAL_JWT_PRE.key;

    private final Duration ILLEGAL_EXPIRE_DURATION;

    private static final Map<Boolean, BiFunction<String, String, Mono<Boolean>>>
            IP_MARKERS = new HashMap<>(4, 1.0f),
            JWT_MARKERS = new HashMap<>(4, 1.0f);

    private static final Function<IllegalMarkEvent, Mono<Boolean>> EVENT_HANDLER = event -> {
        if (event == null)
            return just(false);

        String resource = ofNullable(event.getResource()).orElse(ALL_RESOURCE);
        boolean mark = ofNullable(event.getMark()).orElse(true);

        return zip(ofNullable(event.getJwt())
                        .filter(Asserter::isNotBlank)
                        .map(jwt -> JWT_MARKERS.get(mark).apply(jwt, resource)).orElseGet(() -> just(false)),
                ofNullable(event.getIp())
                        .filter(Asserter::isNotBlank)
                        .map(ip -> IP_MARKERS.get(mark).apply(ip, resource)).orElseGet(() -> just(false))
        ).flatMap(tuple2 -> just(tuple2.getT1() || tuple2.getT2()));
    };

    private static final BiFunction<List<String>, String, Mono<Boolean>> RES_VALIDATOR = (tars, res) ->
            isEmpty(tars) || !(tars.contains(ALL_RESOURCE) || tars.contains(res)) ?
                    just(true) : just(false);

    private final BiFunction<String, String, Mono<Boolean>> IP_VALIDATOR = (ip, res) ->
            this.reactiveStringRedisTemplate.opsForSet()
                    .members(ILLEGAL_IP_PREFIX + ip).collectList()
                    .flatMap(s -> RES_VALIDATOR.apply(s, res));

    private final BiFunction<String, String, Mono<Boolean>> JWT_VALIDATOR = (jwt, res) ->
            this.reactiveStringRedisTemplate.opsForSet()
                    .members(ILLEGAL_JWT_PREFIX + jwt).collectList()
                    .flatMap(s -> RES_VALIDATOR.apply(s, res));

    private final Function<ServerWebExchange, Mono<Boolean>> ILLEGAL_VALIDATOR = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();
        ServerHttpRequest request = exchange.getRequest();
        String resource = REQ_RES_KEY_GENERATOR.apply(request.getMethodValue().intern(), request.getPath().value());

        return zip(
                ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                        .map(ip -> IP_VALIDATOR.apply(ip, resource))
                        .orElseThrow(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message)),
                ofNullable(attributes.get(BlueDataAttrKey.JWT.key))
                        .map(String::valueOf).filter(Asserter::isNotBlank)
                        .map(jwt -> JWT_VALIDATOR.apply(jwt, resource)).orElseGet(() -> just(true))
        ).flatMap(tuple2 ->
                just(tuple2.getT1() && tuple2.getT2()));
    };

    /**
     * assert request
     *
     * @param serverWebExchange
     * @return
     */
    public Mono<Boolean> assertIllegalRequest(ServerWebExchange serverWebExchange) {
        return ILLEGAL_VALIDATOR.apply(serverWebExchange);
    }

    /**
     * handle event
     *
     * @param event
     */
    public Mono<Boolean> handleIllegalMarkEvent(IllegalMarkEvent event) {
        return EVENT_HANDLER.apply(event);
    }

}
