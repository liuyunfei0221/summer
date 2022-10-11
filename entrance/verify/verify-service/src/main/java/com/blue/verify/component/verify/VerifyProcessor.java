package com.blue.verify.component.verify;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.common.TuringData;
import com.blue.basic.model.exps.BlueException;
import com.blue.redis.component.BlueLeakyBucketRateLimiter;
import com.blue.verify.api.model.VerifyParam;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.TuringDeploy;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.getIpReact;
import static com.blue.basic.common.base.ConstantProcessor.getVerifyBusinessTypeByIdentity;
import static com.blue.basic.common.base.TuringDataGetter.getTuringDataReact;
import static com.blue.basic.constant.common.BlueCommonThreshold.*;
import static com.blue.basic.constant.common.RateLimitKeyPrefix.TURING_TEST_LIMIT_KEY_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.verify.VerifyBusinessType.TURING_TEST;
import static com.blue.basic.constant.verify.VerifyType.IMAGE;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;
import static reactor.core.publisher.Mono.*;

/**
 * verify processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "ConstantConditions"})
@Component
public class VerifyProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    public VerifyProcessor(BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter, TuringDeploy turingDeploy) {
        this.blueLeakyBucketRateLimiter = blueLeakyBucketRateLimiter;

        Integer allow = turingDeploy.getAllow();
        if (isNull(allow) || allow < 1)
            throw new RuntimeException("allow can't be null or less than 1");

        Long intervalMillis = turingDeploy.getIntervalMillis();
        if (isNull(intervalMillis) || intervalMillis < 1L)
            throw new RuntimeException("intervalMillis can't be null or less than 1");

        this.ALLOW = allow;
        this.INTERVAL_MILLIS = intervalMillis;
        this.needTuringTestVerifyTypes = ofNullable(turingDeploy.getVerifyTypes())
                .map(vts -> vts.stream().map(vt -> vt.identity).collect(toSet()))
                .orElseGet(Collections::emptySet);
    }

    private static final Map<String, Set<String>> BT_ALLOWED_VTS = Stream.of(VerifyBusinessType.values())
            .collect(toMap(bt -> bt.identity, bt -> bt.allowedVerifyTypes.stream().map(vt -> vt.identity).collect(toSet())));

    private static final BiConsumer<String, String> ALLOWED_ASSERTER = (businessType, verifyType) -> {
        if (isBlank(businessType) || isBlank(verifyType))
            throw new BlueException(INVALID_PARAM);

        Set<String> verifyTypes = BT_ALLOWED_VTS.get(businessType);
        if (isEmpty(verifyTypes) || !verifyTypes.contains(verifyType))
            throw new BlueException(UNSUPPORTED_OPERATE);
    };

    private static final int VFK_LEN_MIN = (int) VF_K_LEN_MIN.value,
            VFK_LEN_MAX = (int) VF_K_LEN_MAX.value;

    private static final int VFV_LEN_MIN = (int) VF_V_LEN_MIN.value,
            VFV_LEN_MAX = (int) VF_V_LEN_MAX.value;

    private static final Consumer<String> KEY_ASSERTER = key -> {
        int len;
        if (isBlank(key) || (len = key.length()) < VFK_LEN_MIN || len > VFK_LEN_MAX)
            throw new BlueException(INVALID_PARAM);
    };

    private static final Consumer<String> VERIFY_ASSERTER = verify -> {
        int len;
        if (isBlank(verify) || (len = verify.length()) < VFV_LEN_MIN || len > VFV_LEN_MAX)
            throw new BlueException(INVALID_PARAM);
    };

    private static final UnaryOperator<String> TURING_LIMIT_KEY_WRAPPER = key -> {
        if (isBlank(key))
            throw new BlueException(INVALID_PARAM);

        return TURING_TEST_LIMIT_KEY_PRE.prefix + key;
    };

    private final int ALLOW;
    private final long INTERVAL_MILLIS;

    private Set<String> needTuringTestVerifyTypes;

    private final Predicate<String> NEED_TURING_TEST_PRE = needTuringTestVerifyTypes::contains;

    /**
     * verify type -> verify handler
     */
    private Map<String, VerifyHandler> verifyHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, VerifyHandler> beansOfType = applicationContext.getBeansOfType(VerifyHandler.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("verifyHandlers is empty");

        verifyHandlers = beansOfType.values().stream()
                .collect(toMap(vh -> vh.targetType().identity, vh -> vh, (a, b) -> a));
    }

    /**
     * generate for api
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param destination
     * @return
     */
    public Mono<String> handle(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String destination) {
        if (isNotNull(verifyType) && isNotNull(verifyBusinessType))
            return ofNullable(verifyHandlers.get(verifyType.identity))
                    .map(h -> {
                        ALLOWED_ASSERTER.accept(verifyBusinessType.identity, verifyType.identity);
                        return h.handle(verifyBusinessType, destination);
                    })
                    .orElseThrow(() -> new BlueException(INVALID_PARAM));

        return error(() -> new BlueException(INVALID_PARAM));
    }

    /**
     * generate for endpoint
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> handle(ServerRequest serverRequest) {
        return zip(
                getIpReact(serverRequest),
                getTuringDataReact(serverRequest),
                serverRequest.bodyToMono(VerifyParam.class).switchIfEmpty(defer(() -> error(() -> new BlueException(EMPTY_PARAM))))
        )
                .flatMap(tuple3 -> {
                    VerifyParam vp = tuple3.getT3();

                    String destination = vp.getDestination();
                    if (isNotBlank(destination) && destination.length() > VFK_LEN_MAX)
                        return error(() -> new BlueException(INVALID_PARAM));

                    String verifyType = vp.getVerifyType();
                    String businessType = vp.getBusinessType();
                    ALLOWED_ASSERTER.accept(businessType, verifyType);

                    return just(NEED_TURING_TEST_PRE.test(verifyType))
                            .flatMap(need -> {
                                if (need) {
                                    TuringData turingData = tuple3.getT2();
                                    return this.turingValidate(tuple3.getT1(), ALLOW, INTERVAL_MILLIS, turingData.getKey(), turingData.getVerify());
                                } else {
                                    return just(true);
                                }
                            })
                            .flatMap(allowed ->
                                    allowed ?
                                            ofNullable(verifyHandlers.get(verifyType))
                                                    .map(h -> h.handle(getVerifyBusinessTypeByIdentity(businessType), destination, serverRequest))
                                                    .orElseThrow(() -> new BlueException(INVALID_PARAM))
                                            :
                                            error(() -> new BlueException(NEED_TURING_TEST)));
                });
    }

    /**
     * validate verify
     *
     * @param verifyType
     * @param verifyBusinessType
     * @param key
     * @param verify
     * @param repeatable
     * @return
     */
    public Mono<Boolean> validate(VerifyType verifyType, VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable) {
        ALLOWED_ASSERTER.accept(verifyBusinessType.identity, verifyType.identity);
        KEY_ASSERTER.accept(key);
        VERIFY_ASSERTER.accept(verify);

        return ofNullable(verifyHandlers.get(verifyType.identity))
                .map(h -> h.validate(verifyBusinessType, key, verify, repeatable))
                .orElseThrow(() -> new BlueException(INVALID_IDENTITY));
    }

    /**
     * validate by turing test
     *
     * @param identity
     * @param allow
     * @param expiresMillis
     * @param key
     * @param verify
     * @return
     */
    public Mono<Boolean> turingValidate(String identity, Integer allow, Long expiresMillis, String key, String verify) {
        if (isBlank(identity) || !isGreaterThanZero(allow) || !isGreaterThanZero(expiresMillis))
            throw new BlueException(INVALID_PARAM);

        return blueLeakyBucketRateLimiter.isAllowed(TURING_LIMIT_KEY_WRAPPER.apply(identity), allow, expiresMillis)
                .flatMap(allowed ->
                        allowed ?
                                just(true)
                                :
                                ofNullable(verifyHandlers.get(IMAGE.identity))
                                        .map(h -> h.validate(TURING_TEST, key, verify, false))
                                        .orElseGet(() -> just(false)));
    }

}
