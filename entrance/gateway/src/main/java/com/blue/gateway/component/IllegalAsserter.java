package com.blue.gateway.component;

import com.blue.base.common.base.Asserter;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.base.model.exps.BlueException;
import com.blue.gateway.config.deploy.RiskControlDeploy;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_IP_PRE;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_JWT_PRE;
import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.BlueDataAttrKey.METHOD;
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

        illegalExpireDuration = Duration.of(illegalExpireSeconds, SECONDS);

        IP_MARKERS.put(true, (ip, resKey) ->
                isNotBlank(ip) ?
                        KEY_RES_MARKER.apply(ILLEGAL_IP_PREFIX + ip, ofNullable(resKey).filter(Asserter::isNotBlank).orElse(ALL_RESOURCE)) :
                        error(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message)));

        IP_MARKERS.put(false, (ip, resKey) ->
                isNotBlank(ip) ?
                        KEY_RES_CLEARER.apply(ILLEGAL_IP_PREFIX + ip, ofNullable(resKey).filter(Asserter::isNotBlank).orElse(ALL_RESOURCE)) :
                        error(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message)));

        JWT_MARKERS.put(true, (jwt, resKey) ->
                isNotBlank(jwt) ?
                        KEY_RES_MARKER.apply(ILLEGAL_JWT_PREFIX + jwt, ofNullable(resKey).filter(Asserter::isNotBlank).orElse(ALL_RESOURCE)) : just(false));

        JWT_MARKERS.put(false, (jwt, resKey) ->
                isNotBlank(jwt) ?
                        KEY_RES_CLEARER.apply(ILLEGAL_JWT_PREFIX + jwt, ofNullable(resKey).filter(Asserter::isNotBlank).orElse(ALL_RESOURCE)) : just(false));
    }

    private static final String
            ALL_RESOURCE = WILDCARD.identity,
            ILLEGAL_IP_PREFIX = ILLEGAL_IP_PRE.key,
            ILLEGAL_JWT_PREFIX = ILLEGAL_JWT_PRE.key;

    private Duration illegalExpireDuration;

    private static final Map<Boolean, BiFunction<String, String, Mono<Boolean>>>
            IP_MARKERS = new HashMap<>(4, 1.0f),
            JWT_MARKERS = new HashMap<>(4, 1.0f);

    /**
     * operators
     */
    private final BiFunction<String, String, Mono<Boolean>>
            KEY_RES_MARKER = (key, resKey) ->
            this.reactiveStringRedisTemplate
                    .opsForSet().members(key)
                    .any(s -> s.equals(ALL_RESOURCE) || s.equals(resKey))
                    .flatMap(b ->
                            b ? just(false) :
                                    this.reactiveStringRedisTemplate
                                            .opsForSet().add(key, resKey)
                                            .flatMap(l -> this.reactiveStringRedisTemplate.expire(key, illegalExpireDuration))),
            KEY_RES_CLEARER = (key, resKey) ->
                    isBlank(resKey) || ALL_RESOURCE.equals(resKey) ?
                            this.reactiveStringRedisTemplate.delete(key).map(l -> l > 0)
                            :
                            this.reactiveStringRedisTemplate.opsForSet().remove(key, resKey).map(l -> l > 0);

    /**
     * is valid resource?
     */
    private static final BiFunction<List<String>, String, Mono<Boolean>> RES_VALIDATOR = (tars, res) ->
            isEmpty(tars) || !(tars.contains(ALL_RESOURCE) || tars.contains(res)) ? just(true) : just(false);

    /**
     * validators
     */
    private final BiFunction<String, String, Mono<Boolean>> IP_VALIDATOR = (ip, res) ->
            this.reactiveStringRedisTemplate.opsForSet()
                    .members(ILLEGAL_IP_PREFIX + ip).collectList()
                    .flatMap(s -> RES_VALIDATOR.apply(s, res)),
            JWT_VALIDATOR = (jwt, res) ->
                    this.reactiveStringRedisTemplate.opsForSet()
                            .members(ILLEGAL_JWT_PREFIX + jwt).collectList()
                            .flatMap(s -> RES_VALIDATOR.apply(s, res));

    /**
     * marker
     */
    private static final Function<IllegalMarkEvent, Mono<Boolean>> ILLEGAL_MARKER = event -> {
        if (event == null)
            return just(false);

        String resourceKey = ofNullable(event.getResourceKey()).orElse(ALL_RESOURCE);
        boolean mark = ofNullable(event.getMark()).orElse(true);

        return zip(ofNullable(event.getJwt())
                        .filter(Asserter::isNotBlank)
                        .map(jwt -> JWT_MARKERS.get(mark).apply(jwt, resourceKey)).orElseGet(() -> just(false)),
                ofNullable(event.getIp())
                        .filter(Asserter::isNotBlank)
                        .map(ip -> IP_MARKERS.get(mark).apply(ip, resourceKey)).orElseGet(() -> just(false))
        ).flatMap(tuple2 -> just(tuple2.getT1() || tuple2.getT2()));
    };

    /**
     * validator
     */
    private final Function<ServerWebExchange, Mono<Boolean>> ILLEGAL_VALIDATOR = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();

        String resKey = REQ_RES_KEY_GENERATOR.apply(
                ofNullable(attributes.get(METHOD.key)).map(String::valueOf).orElse(""),
                ofNullable(attributes.get(BlueDataAttrKey.URI.key)).map(String::valueOf).orElse(""));

        return zip(
                ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                        .map(ip -> IP_VALIDATOR.apply(ip, resKey))
                        .orElseThrow(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message)),
                ofNullable(attributes.get(BlueDataAttrKey.JWT.key))
                        .map(String::valueOf).filter(Asserter::isNotBlank)
                        .map(jwt -> JWT_VALIDATOR.apply(jwt, resKey)).orElseGet(() -> just(true))
        ).flatMap(tuple2 -> just(tuple2.getT1() && tuple2.getT2()));
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
        return ILLEGAL_MARKER.apply(event);
    }

}