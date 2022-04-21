package com.blue.verify.component.verify.impl;

import com.blue.base.constant.verify.BusinessType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.redis.api.generator.BlueRateLimiterGenerator;
import com.blue.redis.common.BlueLeakyBucketRateLimiter;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.MailVerifyDeploy;
import com.blue.verify.service.inter.MailService;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.SERVER_REQUEST_IP_SYNC_KEY_GETTER;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.VERIFY_KEY;
import static com.blue.base.constant.base.RateLimitKeyPrefix.MAIL_VERIFY_RATE_LIMIT_KEY_PRE;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static com.blue.base.constant.verify.VerifyType.MAIL;
import static java.time.temporal.ChronoUnit.MILLIS;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * mail verify handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "DuplicatedCode"})
public class MailVerifyHandler implements VerifyHandler {

    private static final Logger LOGGER = getLogger(MailVerifyHandler.class);

    private final MailService mailService;

    private final VerifyService verifyService;

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    private final int VERIFY_LEN;
    private final Duration DEFAULT_DURATION;
    private final int ALLOW;
    private final long SEND_INTERVAL_MILLIS;

    public MailVerifyHandler(MailService mailService, VerifyService verifyService, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                             Scheduler scheduler, MailVerifyDeploy mailVerifyDeploy) {
        this.mailService = mailService;
        this.verifyService = verifyService;
        this.blueLeakyBucketRateLimiter = BlueRateLimiterGenerator.generateLeakyBucketRateLimiter(reactiveStringRedisTemplate, scheduler);

        Integer verifyLength = mailVerifyDeploy.getVerifyLength();
        if (isNull(verifyLength) || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Long expireMillis = mailVerifyDeploy.getExpireMillis();
        if (isNull(expireMillis) || expireMillis < 1L)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        Integer allow = mailVerifyDeploy.getAllow();
        if (isNull(allow) || allow < 1)
            throw new RuntimeException("allow can't be null or less than 1");

        Long sendIntervalMillis = mailVerifyDeploy.getSendIntervalMillis();
        if (isNull(sendIntervalMillis) || sendIntervalMillis < 1L)
            throw new RuntimeException("sendIntervalMillis can't be null or less than 1");

        this.VERIFY_LEN = verifyLength;
        this.DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
        this.ALLOW = allow;
        this.SEND_INTERVAL_MILLIS = sendIntervalMillis;
    }

    private static final UnaryOperator<String> LIMIT_KEY_WRAPPER = key -> {
        if (isBlank(key))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be null");

        return MAIL_VERIFY_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    private static final BiFunction<BusinessType, String, String> BUSINESS_KEY_WRAPPER = (type, key) -> {
        if (isNull(type) || isBlank(key))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "type or key can't be null");

        return type.identity + PAR_CONCATENATION.identity + key;
    };

    @Override
    public Mono<String> handle(BusinessType businessType, String destination) {
        if (isNull(businessType) || isBlank(destination))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "businessType or destination can't be null");

        //TODO verify destination/email

        return blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(destination), ALLOW, SEND_INTERVAL_MILLIS)
                .flatMap(allowed ->
                        allowed ?
                                verifyService.generate(MAIL, BUSINESS_KEY_WRAPPER.apply(businessType, destination), VERIFY_LEN, DEFAULT_DURATION)
                                        .flatMap(verify ->
                                                mailService.send(destination, verify)
                                                        .flatMap(success -> success ?
                                                                just(verify)
                                                                :
                                                                error(() -> new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "send email verify failed"))))
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS.status, TOO_MANY_REQUESTS.code, "operation too frequently")));
    }

    @Override
    public Mono<ServerResponse> handle(BusinessType businessType, String destination, ServerRequest serverRequest) {
        return SERVER_REQUEST_IP_SYNC_KEY_GETTER.apply(serverRequest)
                .flatMap(syncIp -> blueLeakyBucketRateLimiter.isAllowed(syncIp, ALLOW, SEND_INTERVAL_MILLIS))
                .flatMap(allowed ->
                        allowed ?
                                this.handle(businessType, destination)
                                        .flatMap(vp ->
                                                ok().contentType(APPLICATION_JSON)
                                                        .header(VERIFY_KEY.name, destination)
                                                        .body(generate(OK.code, serverRequest)
                                                                , BlueResponse.class)
                                        )
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS.status, TOO_MANY_REQUESTS.code, "operation too frequently")));
    }

    @Override
    public Mono<Boolean> validate(BusinessType businessType, String key, String verify, Boolean repeatable) {
        return verifyService.validate(MAIL, BUSINESS_KEY_WRAPPER.apply(businessType, key), verify, repeatable);
    }

    @Override
    public VerifyType targetType() {
        return MAIL;
    }

}
