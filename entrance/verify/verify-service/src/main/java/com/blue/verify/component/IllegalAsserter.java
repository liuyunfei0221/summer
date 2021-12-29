package com.blue.verify.component;

import com.blue.base.common.base.Asserter;
import com.blue.base.constant.base.BlueDataAttrKey;
import com.blue.base.model.base.IllegalMarkEvent;
import com.blue.base.model.exps.BlueException;
import com.blue.verify.config.deploy.RiskControlDeploy;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

import static com.blue.base.common.base.Asserter.*;
import static com.blue.base.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_IP_PRE;
import static com.blue.base.constant.base.BlueCacheKey.ILLEGAL_JWT_PRE;
import static com.blue.base.constant.base.BlueDataAttrKey.CLIENT_IP;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.UNKNOWN_IP;
import static com.blue.base.constant.base.Symbol.WILDCARD;
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

    private static final Logger LOGGER = Loggers.getLogger(IllegalAsserter.class);

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    public IllegalAsserter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, RiskControlDeploy riskControlDeploy) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;

        Long illegalExpireSeconds = riskControlDeploy.getIllegalExpireSeconds();
        if (illegalExpireSeconds == null || illegalExpireSeconds < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "illegalExpireSeconds can't be null or less than 1");

        ILLEGAL_EXPIRE_DURATION = Duration.of(illegalExpireSeconds, ChronoUnit.SECONDS);

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


    private static final String ALL_RESOURCE = WILDCARD.identity;

    private static final String ILLEGAL_IP_PREFIX = ILLEGAL_IP_PRE.key;
    private static final String ILLEGAL_JWT_PREFIX = ILLEGAL_JWT_PRE.key;

    private final Duration ILLEGAL_EXPIRE_DURATION;


    private static final Map<Boolean, BiFunction<String, String, Mono<Boolean>>> IP_MARKERS = new HashMap<>(4, 1.0f);
    private static final Map<Boolean, BiFunction<String, String, Mono<Boolean>>> JWT_MARKERS = new HashMap<>(4, 1.0f);


    private static final Function<IllegalMarkEvent, Mono<Boolean>> EVENT_HANDLER = event -> {
        if (event == null)
            return just(false);

        String resource = ofNullable(event.getResource()).orElse(ALL_RESOURCE);
        boolean mark = ofNullable(event.getMark()).orElse(true);

        ofNullable(event.getJwt())
                .filter(Asserter::isNotBlank)
                .map(jwt -> JWT_MARKERS.get(mark).apply(jwt, resource)).orElse(just(false));
        ofNullable(event.getIp())
                .filter(Asserter::isNotBlank)
                .map(ip -> IP_MARKERS.get(mark).apply(ip, resource)).orElse(just(false));

        return zip(ofNullable(event.getJwt())
                        .filter(Asserter::isNotBlank)
                        .map(jwt -> JWT_MARKERS.get(mark).apply(jwt, resource)).orElse(just(false)),
                ofNullable(event.getIp())
                        .filter(Asserter::isNotBlank)
                        .map(ip -> IP_MARKERS.get(mark).apply(ip, resource)).orElse(just(false))
        ).flatMap(tuple2 -> just(tuple2.getT1() || tuple2.getT2()));
    };


    private final BiFunction<String, String, Mono<Boolean>> IP_ASSERTER = (ip, res) ->
            this.reactiveStringRedisTemplate.opsForSet()
                    .members(ILLEGAL_IP_PREFIX + ip).collectList()
                    .flatMap(s ->
                            isEmpty(s) || !(s.contains(ALL_RESOURCE) || s.contains(res)) ?
                                    just(true)
                                    :
                                    just(false));


    private final BiFunction<String, String, Mono<Boolean>> JWT_ASSERTER = (jwt, res) ->
            isNotBlank(jwt) ?
                    this.reactiveStringRedisTemplate.opsForSet()
                            .members(ILLEGAL_JWT_PREFIX + jwt).collectList()
                            .flatMap(s ->
                                    isEmpty(s) || !(s.contains(ALL_RESOURCE) || s.contains(res)) ?
                                            just(true)
                                            :
                                            just(false))
                    :
                    just(true);

    private final Function<ServerWebExchange, Mono<Boolean>> ILLEGAL_ASSERTER = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();
        ServerHttpRequest request = exchange.getRequest();
        String resource = REQ_RES_KEY_GENERATOR.apply(
                request.getMethodValue().intern(), request.getPath().value());

        return zip(
                IP_ASSERTER.apply(ofNullable(attributes.get(CLIENT_IP.key)).map(String::valueOf).filter(Asserter::isNotBlank)
                        .orElseThrow(() -> new BlueException(UNKNOWN_IP.status, UNKNOWN_IP.code, UNKNOWN_IP.message)), resource),
                JWT_ASSERTER.apply(ofNullable(attributes.get(BlueDataAttrKey.JWT.key)).map(String::valueOf).filter(Asserter::isNotBlank).orElse(""), resource)
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
        return ILLEGAL_ASSERTER.apply(serverWebExchange);
    }

    /**
     * handle event
     *
     * @param event
     */
    public Mono<Boolean> handleIllegalMarkEvent(IllegalMarkEvent event) {
        LOGGER.info("void handleIllegalMarkEvent(IllegalMarkEvent event), event = {}", event);
        return EVENT_HANDLER.apply(event);
    }

}
