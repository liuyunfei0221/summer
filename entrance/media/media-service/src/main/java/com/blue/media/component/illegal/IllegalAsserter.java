package com.blue.media.component.illegal;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.IllegalMarkEvent;
import com.blue.media.config.deploy.RiskControlDeploy;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.CommonFunctions.REQ_RES_KEY_GENERATOR;
import static com.blue.base.constant.common.BlueDataAttrKey.*;
import static com.blue.base.constant.common.CacheKeyPrefix.ILLEGAL_IP_PRE;
import static com.blue.base.constant.common.CacheKeyPrefix.ILLEGAL_JWT_PRE;
import static com.blue.base.constant.common.SpecialStringElement.EMPTY_DATA;
import static com.blue.base.constant.common.Symbol.WILDCARD;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;

/**
 * illegal request asserter
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "FieldCanBeLocal"})
@Component
public final class IllegalAsserter {

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private Scheduler scheduler;

    public IllegalAsserter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler, RiskControlDeploy riskControlDeploy) {
        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.scheduler = scheduler;

        Long illegalExpiresSecond = riskControlDeploy.getIllegalExpiresSecond();
        if (isNull(illegalExpiresSecond) || illegalExpiresSecond < 1L)
            throw new RuntimeException("illegalExpiresSecond can't be null or less than 1");

        defaultIllegalExpireDuration = Duration.of(illegalExpiresSecond, SECONDS);

        MARKERS.put(true, MARKER);
        MARKERS.put(false, CLEARER);
    }

    private static final String
            ALL_RESOURCE = WILDCARD.identity,
            ILLEGAL_IP_PREFIX = ILLEGAL_IP_PRE.prefix,
            ILLEGAL_JWT_PREFIX = ILLEGAL_JWT_PRE.prefix;

    private final Duration defaultIllegalExpireDuration;

    private static final Map<Boolean, Function<IllegalMarkEvent, Mono<Boolean>>> MARKERS = new HashMap<>(4, 1.0f);

    private static final UnaryOperator<String>
            JWT_KEY_WRAPPER = jwt -> ILLEGAL_JWT_PREFIX + jwt,
            IP_KEY_WRAPPER = ip -> ILLEGAL_IP_PREFIX + ip;

    private Mono<Boolean> markWithExpire(String key, String resKey, Long expiresSecond) {
        return this.reactiveStringRedisTemplate
                .opsForSet().members(key)
                .any(s -> s.equals(ALL_RESOURCE) || s.equals(resKey))
                .flatMap(b ->
                        b ? just(false)
                                .publishOn(scheduler)
                                :
                                this.reactiveStringRedisTemplate
                                        .opsForSet().add(key, resKey)
                                        .publishOn(scheduler)
                                        .flatMap(l -> this.reactiveStringRedisTemplate.expire(key,
                                                ofNullable(expiresSecond).map(s -> Duration.of(s, SECONDS))
                                                        .orElse(defaultIllegalExpireDuration))));
    }

    private Mono<Boolean> clearMark(String key, String resKey) {
        return isBlank(resKey) || ALL_RESOURCE.equals(resKey) ?
                this.reactiveStringRedisTemplate.delete(key).publishOn(scheduler).map(l -> l > 0)
                :
                this.reactiveStringRedisTemplate.opsForSet().remove(key, resKey).publishOn(scheduler).map(l -> l > 0);
    }

    /**
     * operators
     */
    private final Function<IllegalMarkEvent, Mono<Boolean>>
            MARKER = event -> {
        String resKey = ofNullable(event.getResourceKey()).orElse(ALL_RESOURCE);
        return zip(ofNullable(event.getJwt())
                        .filter(BlueChecker::isNotBlank)
                        .map(JWT_KEY_WRAPPER)
                        .map(key -> markWithExpire(key, resKey, event.getIllegalExpiresSecond()))
                        .orElseGet(() -> just(false)),
                ofNullable(event.getIp())
                        .filter(BlueChecker::isNotBlank)
                        .map(IP_KEY_WRAPPER)
                        .map(key -> markWithExpire(key, resKey, event.getIllegalExpiresSecond()))
                        .orElseGet(() -> just(false))
        ).flatMap(tuple2 -> just(tuple2.getT1() || tuple2.getT2()));
    },
            CLEARER = event -> {
                String resKey = ofNullable(event.getResourceKey()).orElse(ALL_RESOURCE);
                return zip(ofNullable(event.getJwt())
                                .filter(BlueChecker::isNotBlank)
                                .map(JWT_KEY_WRAPPER)
                                .map(key -> clearMark(key, resKey))
                                .orElseGet(() -> just(false)),
                        ofNullable(event.getIp())
                                .filter(BlueChecker::isNotBlank)
                                .map(IP_KEY_WRAPPER)
                                .map(key -> clearMark(key, resKey))
                                .orElseGet(() -> just(false))
                ).flatMap(tuple2 -> just(tuple2.getT1() || tuple2.getT2()));
            };


    /**
     * resource validator
     */
    private static final BiFunction<List<String>, String, Mono<Boolean>> RES_VALIDATOR = (tars, res) ->
            isEmpty(tars) || !(tars.contains(res) || tars.contains(ALL_RESOURCE)) ? just(true) : just(false);

    /**
     * key validator
     */
    private final BiFunction<String, String, Mono<Boolean>> KEY_VALIDATOR = (key, res) ->
            this.reactiveStringRedisTemplate.opsForSet()
                    .members(key).publishOn(scheduler).collectList()
                    .flatMap(s -> RES_VALIDATOR.apply(s, res));

    /**
     * validator
     */
    private final Function<ServerWebExchange, Mono<Boolean>> ILLEGAL_VALIDATOR = exchange -> {
        Map<String, Object> attributes = exchange.getAttributes();

        String resKey = REQ_RES_KEY_GENERATOR.apply(
                ofNullable(attributes.get(METHOD.key)).map(String::valueOf).orElse(EMPTY_DATA.value),
                ofNullable(attributes.get(URI.key)).map(String::valueOf).orElse(EMPTY_DATA.value));

        return zip(
                ofNullable(attributes.get(JWT.key))
                        .map(String::valueOf)
                        .filter(BlueChecker::isNotBlank)
                        .map(JWT_KEY_WRAPPER)
                        .map(key -> KEY_VALIDATOR.apply(key, resKey))
                        .orElseGet(() -> just(true)),
                ofNullable(attributes.get(CLIENT_IP.key))
                        .map(String::valueOf)
                        .filter(BlueChecker::isNotBlank)
                        .map(IP_KEY_WRAPPER)
                        .map(key -> KEY_VALIDATOR.apply(key, resKey))
                        .orElseGet(() -> just(true))
        ).flatMap(tuple2 -> just(tuple2.getT1() && tuple2.getT2()));
    };

    /**
     * marker
     */
    private static final Function<IllegalMarkEvent, Mono<Boolean>> ILLEGAL_MARKER = event ->
            isNotNull(event) ? MARKERS.get(ofNullable(event.getMark()).orElse(true)).apply(event) : just(false);

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