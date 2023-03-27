package com.blue.verify.component.verify.impl;

import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.redis.component.BlueLeakyBucketRateLimiter;
import com.blue.verify.api.model.VerifyMessage;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.SmsVerifyDeploy;
import com.blue.verify.event.producer.VerifyMessageEventProducer;
import com.blue.verify.repository.entity.VerifyHistory;
import com.blue.verify.service.inter.VerifyHistoryService;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueHeader.VERIFY_KEY;
import static com.blue.basic.constant.common.RateLimitKeyPrefix.SMS_VERIFY_RATE_LIMIT_KEY_PRE;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static com.blue.basic.constant.verify.VerifyType.SMS;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * sms verify handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "DuplicatedCode", "JavadocDeclaration"})
public class SmsVerifyHandler implements VerifyHandler {

    private static final Logger LOGGER = getLogger(SmsVerifyHandler.class);

    private final VerifyMessageEventProducer verifyMessageEventProducer;

    private final VerifyService verifyService;

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final VerifyHistoryService verifyHistoryService;

    private final int VERIFY_LEN;
    private final Duration DEFAULT_DURATION;
    private final int ALLOW;
    private final long SEND_INTERVAL_MILLIS;

    public SmsVerifyHandler(VerifyMessageEventProducer verifyMessageEventProducer, VerifyService verifyService, BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter,
                            BlueIdentityProcessor blueIdentityProcessor, VerifyHistoryService verifyHistoryService, SmsVerifyDeploy smsVerifyDeploy) {
        this.verifyMessageEventProducer = verifyMessageEventProducer;
        this.verifyService = verifyService;
        this.blueLeakyBucketRateLimiter = blueLeakyBucketRateLimiter;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.verifyHistoryService = verifyHistoryService;

        Integer verifyLength = smsVerifyDeploy.getVerifyLength();
        if (isNull(verifyLength) || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Long expiresMillis = smsVerifyDeploy.getExpiresMillis();
        if (isNull(expiresMillis) || expiresMillis < 1L)
            throw new RuntimeException("expiresMillis can't be null or less than 1");

        Integer allow = smsVerifyDeploy.getAllow();
        if (isNull(allow) || allow < 1)
            throw new RuntimeException("allow can't be null or less than 1");

        Long sendIntervalMillis = smsVerifyDeploy.getSendIntervalMillis();
        if (isNull(sendIntervalMillis) || sendIntervalMillis < 1L)
            throw new RuntimeException("sendIntervalMillis can't be null or less than 1");

        this.VERIFY_LEN = verifyLength;
        this.DEFAULT_DURATION = Duration.of(expiresMillis, MILLIS);
        this.ALLOW = allow;
        this.SEND_INTERVAL_MILLIS = sendIntervalMillis;
    }

    private static final UnaryOperator<String> LIMIT_KEY_WRAPPER = key -> {
        if (isBlank(key))
            throw new BlueException(INVALID_PARAM);

        return SMS_VERIFY_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    private static final BiFunction<VerifyBusinessType, String, String> BUSINESS_KEY_WRAPPER = (type, key) -> {
        if (isNull(type) || isBlank(key))
            throw new RuntimeException("type or key can't be null");

        return type.identity + PAR_CONCATENATION.identity + key;
    };

    /**
     * recode verify history
     *
     * @param verifyBusinessType
     * @param destination
     * @param verify
     * @param serverRequest
     */
    private void recordVerify(VerifyBusinessType verifyBusinessType, String destination, String verify, ServerRequest serverRequest) {
        VerifyHistory verifyHistory = new VerifyHistory();

        verifyHistory.setId(blueIdentityProcessor.generate(VerifyHistory.class));
        verifyHistory.setVerifyType(SMS.identity);
        verifyHistory.setBusinessType(verifyBusinessType.identity);
        verifyHistory.setDestination(destination);
        verifyHistory.setVerify(verify);
        verifyHistory.setRequestIp(getIp(serverRequest));
        verifyHistory.setCreateTime(TIME_STAMP_GETTER.get());

        verifyHistoryService.insertVerifyHistory(verifyHistory)
                .doOnError(throwable -> LOGGER.info("recordVerify failed, verifyHistory = {}, throwable = {}", verifyHistory, throwable))
                .subscribe(vh -> LOGGER.info("vh = {}", vh));
    }

    @Override
    public Mono<String> handle(VerifyBusinessType verifyBusinessType, String destination, List<String> languages) {
        LOGGER.info("businessType = {}, destination = {}, languages = {}", verifyBusinessType, destination, languages);
        if (isNull(verifyBusinessType) || isBlank(destination))
            throw new BlueException(BAD_REQUEST);

//        String s = Symbol.HYPHEN.identity;

        //TODO verify destination/phone

        return blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(destination), ALLOW, SEND_INTERVAL_MILLIS)
                .flatMap(allowed ->
                        allowed ?
                                verifyService.generate(SMS, BUSINESS_KEY_WRAPPER.apply(verifyBusinessType, destination), VERIFY_LEN, DEFAULT_DURATION)
                                        .flatMap(verify ->
                                                fromRunnable(() -> verifyMessageEventProducer.send(new VerifyMessage(SMS.identity, verifyBusinessType.identity, destination, verify, languages)))
                                                        .then(just(verify)))
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS.status, TOO_MANY_REQUESTS.code, "operation too frequently")));
    }

    @Override
    public Mono<ServerResponse> handle(VerifyBusinessType verifyBusinessType, String destination, ServerRequest serverRequest) {
        return SERVER_REQUEST_IDENTITY_SYNC_KEY_GETTER.apply(serverRequest)
                .flatMap(identity -> blueLeakyBucketRateLimiter.isAllowed(identity, ALLOW, SEND_INTERVAL_MILLIS))
                .flatMap(allowed ->
                        allowed ?
                                this.handle(verifyBusinessType, destination, getAcceptLanguages(serverRequest))
                                        .flatMap(verify -> {
                                            LOGGER.warn("verifyKey = {}, verify = {}", destination, verify);

                                            return ok().contentType(APPLICATION_JSON)
                                                    .header(VERIFY_KEY.name, destination)
                                                    .body(success(serverRequest)
                                                            , BlueResponse.class)
                                                    .doOnSuccess(ig ->
                                                            recordVerify(verifyBusinessType, destination, verify, serverRequest));
                                        })
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS)));
    }

    @Override
    public Mono<Boolean> validate(VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable) {
        return verifyService.validate(SMS, BUSINESS_KEY_WRAPPER.apply(verifyBusinessType, key), verify, repeatable);
    }

    @Override
    public VerifyType targetType() {
        return SMS;
    }

}
