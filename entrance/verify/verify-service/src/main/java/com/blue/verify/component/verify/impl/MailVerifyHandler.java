package com.blue.verify.component.verify.impl;

import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.base.BlueResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.redis.api.generator.BlueRateLimiterGenerator;
import com.blue.redis.common.BlueLeakyBucketRateLimiter;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.MailVerifyDeploy;
import com.blue.verify.service.inter.MailService;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;
import java.util.function.UnaryOperator;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.generate;
import static com.blue.base.constant.base.BlueHeader.VERIFY_KEY;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.base.SyncKeyPrefix.MAIL_VERIFY_RATE_LIMIT_KEY_PRE;
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
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
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
        if (verifyLength == null || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Long expireMillis = mailVerifyDeploy.getExpireMillis();
        if (expireMillis == null || expireMillis < 1L)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        Integer allow = mailVerifyDeploy.getAllow();
        if (allow == null || allow < 1)
            throw new RuntimeException("allow can't be null or less than 1");

        Long sendIntervalMillis = mailVerifyDeploy.getSendIntervalMillis();
        if (sendIntervalMillis == null || sendIntervalMillis < 1L)
            throw new RuntimeException("sendIntervalMillis can't be null or less than 1");

        this.VERIFY_LEN = verifyLength;
        this.DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
        this.ALLOW = allow;
        this.SEND_INTERVAL_MILLIS = sendIntervalMillis;
    }

    private static final UnaryOperator<String> KEY_WRAPPER = key -> {
        if (key == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be null");

        return MAIL_VERIFY_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    /**
     * generate an image verify
     *
     * @param destination
     * @param serverRequest
     * @return
     */
    @Override
    public Mono<ServerResponse> handle(String destination, ServerRequest serverRequest) {
        LOGGER.info("Mono<ServerResponse> handle(String destination, ServerRequest serverRequest), email = {}", destination);
        //TODO verify destination/email

        return blueLeakyBucketRateLimiter.isAllowed(KEY_WRAPPER.apply(destination), ALLOW, SEND_INTERVAL_MILLIS)
                .flatMap(allowed ->
                        allowed ?
                                verifyService.generate(MAIL, destination, VERIFY_LEN, DEFAULT_DURATION)
                                        .flatMap(vp -> {
                                            String key = vp.getKey();

                                            return mailService.send(key, vp.getVerify())
                                                    .flatMap(success -> success ?
                                                            just(key)
                                                            :
                                                            error(() -> new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "send email verify failed")));
                                        })
                                        .flatMap(key ->
                                                ok().contentType(APPLICATION_JSON)
                                                        .header(VERIFY_KEY.name, key)
                                                        .body(generate(OK.code, serverRequest)
                                                                , BlueResponse.class)
                                        )
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS.status, TOO_MANY_REQUESTS.code, "operation too frequently")));
    }

    @Override
    public VerifyType targetType() {
        return MAIL;
    }

}
