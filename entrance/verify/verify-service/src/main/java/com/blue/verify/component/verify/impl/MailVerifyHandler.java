package com.blue.verify.component.verify.impl;

import com.blue.basic.constant.verify.BusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.common.BlueResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.redis.component.BlueLeakyBucketRateLimiter;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.MailVerifyDeploy;
import com.blue.verify.repository.entity.VerifyHistory;
import com.blue.verify.service.inter.MailService;
import com.blue.verify.service.inter.VerifyHistoryService;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueHeader.VERIFY_KEY;
import static com.blue.basic.constant.common.RateLimitKeyPrefix.MAIL_VERIFY_RATE_LIMIT_KEY_PRE;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static com.blue.basic.constant.verify.VerifyType.MAIL;
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
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "DuplicatedCode", "JavadocDeclaration"})
public class MailVerifyHandler implements VerifyHandler {

    private static final Logger LOGGER = getLogger(MailVerifyHandler.class);

    private final MailService mailService;

    private final VerifyService verifyService;

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final VerifyHistoryService verifyHistoryService;

    private final int VERIFY_LEN;
    private final Duration DEFAULT_DURATION;
    private final int ALLOW;
    private final long SEND_INTERVAL_MILLIS;

    public MailVerifyHandler(MailService mailService, VerifyService verifyService, BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter,
                             BlueIdentityProcessor blueIdentityProcessor, VerifyHistoryService verifyHistoryService, MailVerifyDeploy mailVerifyDeploy) {
        this.mailService = mailService;
        this.verifyService = verifyService;
        this.blueLeakyBucketRateLimiter = blueLeakyBucketRateLimiter;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.verifyHistoryService = verifyHistoryService;

        Integer verifyLength = mailVerifyDeploy.getVerifyLength();
        if (isNull(verifyLength) || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Long expiresMillis = mailVerifyDeploy.getExpiresMillis();
        if (isNull(expiresMillis) || expiresMillis < 1L)
            throw new RuntimeException("expiresMillis can't be null or less than 1");

        Integer allow = mailVerifyDeploy.getAllow();
        if (isNull(allow) || allow < 1)
            throw new RuntimeException("allow can't be null or less than 1");

        Long sendIntervalMillis = mailVerifyDeploy.getSendIntervalMillis();
        if (isNull(sendIntervalMillis) || sendIntervalMillis < 1L)
            throw new RuntimeException("sendIntervalMillis can't be null or less than 1");

        this.VERIFY_LEN = verifyLength;
        this.DEFAULT_DURATION = Duration.of(expiresMillis, MILLIS);
        this.ALLOW = allow;
        this.SEND_INTERVAL_MILLIS = sendIntervalMillis;
    }

    private static final UnaryOperator<String> LIMIT_KEY_WRAPPER = key -> {
        if (isBlank(key))
            throw new RuntimeException("key can't be null");

        return MAIL_VERIFY_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    private static final BiFunction<BusinessType, String, String> BUSINESS_KEY_WRAPPER = (type, key) -> {
        if (isNull(type) || isBlank(key))
            throw new RuntimeException("type or key can't be null");

        return type.identity + PAR_CONCATENATION.identity + key;
    };

    /**
     * recode verify history
     *
     * @param businessType
     * @param destination
     * @param verify
     * @param serverRequest
     */
    private void recordVerify(BusinessType businessType, String destination, String verify, ServerRequest serverRequest) {
        VerifyHistory verifyHistory = new VerifyHistory();

        verifyHistory.setId(blueIdentityProcessor.generate(VerifyHistory.class));
        verifyHistory.setVerifyType(MAIL.identity);
        verifyHistory.setBusinessType(businessType.identity);
        verifyHistory.setDestination(destination);
        verifyHistory.setVerify(verify);
        verifyHistory.setRequestIp(getIp(serverRequest));
        verifyHistory.setCreateTime(TIME_STAMP_GETTER.get());

        verifyHistoryService.insertVerifyHistory(verifyHistory)
                .doOnError(throwable -> LOGGER.info("mailVerifyHandler.recordVerify() failed, verifyHistory = {}, throwable = {}", verifyHistory, throwable))
                .subscribe(vh -> LOGGER.info("mailVerifyHandler.recordVerify() -> insertVerifyHistory(verifyHistory), vh = {}", vh));
    }

    @Override
    public Mono<String> handle(BusinessType businessType, String destination) {
        LOGGER.info("MailVerifyHandler -> Mono<String> handle(BusinessType businessType, String destination), businessType = {}, destination = {}", businessType, destination);
        if (isNull(businessType) || isBlank(destination))
            throw new BlueException(BAD_REQUEST);

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
                                                                error(() -> new RuntimeException("send email verify failed"))))
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
                                        .flatMap(verify -> {
                                            LOGGER.warn("Mono<ServerResponse> handle(String destination), verifyKey = {}, verify = {}", destination, verify);

                                            return ok().contentType(APPLICATION_JSON)
                                                    .header(VERIFY_KEY.name, destination)
                                                    .body(success(serverRequest)
                                                            , BlueResponse.class)
                                                    .doOnSuccess(ig ->
                                                            recordVerify(businessType, destination, verify, serverRequest));
                                        })
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS)));
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
