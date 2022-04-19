package com.blue.verify.component.verify.impl;

import com.blue.base.constant.base.RandomType;
import com.blue.base.constant.verify.BusinessType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.redis.api.generator.BlueRateLimiterGenerator;
import com.blue.redis.common.BlueLeakyBucketRateLimiter;
import com.blue.verify.common.CaptchaProcessor;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.ImageVerifyDeploy;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.common.reactive.ReactiveCommonFunctions.SERVER_REQUEST_IDENTITY_SYNC_KEY_GETTER;
import static com.blue.base.constant.base.BlueHeader.VERIFY_KEY;
import static com.blue.base.constant.base.RateLimitKeyPrefix.IMAGE_VERIFY_RATE_LIMIT_KEY_PRE;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION;
import static com.blue.base.constant.verify.VerifyType.IMAGE;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.web.reactive.function.BodyInserters.fromResource;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * image verify handler
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused"})
public class ImageVerifyHandler implements VerifyHandler {

    private static final Logger LOGGER = getLogger(ImageVerifyHandler.class);

    private CaptchaProcessor captchaProcessor;

    private final VerifyService verifyService;

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    private ExecutorService executorService;

    private final int KEY_LEN;
    private final RandomType KEY_RANDOM_TYPE;
    private String imageType;
    private final int VERIFY_LEN;
    private final Duration DEFAULT_DURATION;
    private final int ALLOW;
    private final long SEND_INTERVAL_MILLIS;

    public ImageVerifyHandler(CaptchaProcessor captchaProcessor, VerifyService verifyService, ReactiveStringRedisTemplate reactiveStringRedisTemplate,
                              Scheduler scheduler, ExecutorService executorService, ImageVerifyDeploy imageVerifyDeploy) {
        this.captchaProcessor = captchaProcessor;
        this.verifyService = verifyService;
        this.blueLeakyBucketRateLimiter = BlueRateLimiterGenerator.generateLeakyBucketRateLimiter(reactiveStringRedisTemplate, scheduler);
        this.executorService = executorService;

        Integer keyLength = imageVerifyDeploy.getKeyLength();
        if (isNull(keyLength) || keyLength < 1)
            throw new RuntimeException("keyLength can't be null or less than 1");

        RandomType keyRandomType = imageVerifyDeploy.getKeyRandomType();
        if (isNull(keyRandomType))
            throw new RuntimeException("randomType can't be null");

        String imageType = imageVerifyDeploy.getImageType();
        if (isBlank(imageType))
            throw new RuntimeException("imageType can't be blank");

        Integer verifyLength = imageVerifyDeploy.getVerifyLength();
        if (isNull(verifyLength) || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Integer expireMillis = imageVerifyDeploy.getExpireMillis();
        if (isNull(expireMillis) || expireMillis < 1)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        Integer allow = imageVerifyDeploy.getAllow();
        if (isNull(allow) || allow < 1)
            throw new RuntimeException("allow can't be null or less than 1");

        Long sendIntervalMillis = imageVerifyDeploy.getSendIntervalMillis();
        if (isNull(sendIntervalMillis) || sendIntervalMillis < 1L)
            throw new RuntimeException("sendIntervalMillis can't be null or less than 1");

        this.KEY_LEN = keyLength;
        this.KEY_RANDOM_TYPE = keyRandomType;
        this.imageType = imageType;
        this.VERIFY_LEN = verifyLength;
        this.DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
        this.ALLOW = allow;
        this.SEND_INTERVAL_MILLIS = sendIntervalMillis;
    }

    private static final UnaryOperator<String> LIMIT_KEY_WRAPPER = key -> {
        if (isNull(key))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be null");

        return IMAGE_VERIFY_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    private static final BiFunction<BusinessType, String, String> BUSINESS_KEY_WRAPPER = (type, key) -> {
        if (isNull(type) || isNull(key))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "type or key can't be null");

        return type.identity + PAR_CONCATENATION.identity + key;
    };

    private static final String CACHE_CONTROL_VALUE = "no-store, no-cache, must-revalidate, must-revalidate";

    private final BiFunction<String, FastByteArrayOutputStream, Mono<ByteArrayResource>> IMAGE_WRITER = (verify, outputStream) ->
            fromFuture(supplyAsync(() -> {
                boolean write = false;
                try {
                    write = ImageIO.write(captchaProcessor.generateImage(verify), imageType, outputStream);
                } catch (IOException e) {
                    LOGGER.error("ImageIO.write(captchaProcessor.generateImage(verify), IMAGE_TYPE, outputStream) failed, e = {}", e);
                }
                return write;
            }, executorService))
                    .flatMap(b ->
                            b ? just(new ByteArrayResource(outputStream.toByteArray()))
                                    :
                                    error(() -> new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "IMAGE_WRITER handle failed")));

    private final Consumer<FastByteArrayOutputStream> STREAM_CLOSER = stream -> {
        try {
            stream.close();
        } catch (Exception e) {
            LOGGER.error("FastByteArrayOutputStream close failed, e = {}", e);
        }
    };

    @Override
    public Mono<String> handle(BusinessType businessType, String destination) {
        if (isNull(businessType) && isNotBlank(destination))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "verifyBusinessType can't be null");

        return verifyService.generate(IMAGE, BUSINESS_KEY_WRAPPER.apply(businessType, destination), VERIFY_LEN, DEFAULT_DURATION);
    }

    @Override
    public Mono<ServerResponse> handle(BusinessType businessType, String destination, ServerRequest serverRequest) {
        String key = isNotBlank(destination) ? destination : generateRandom(KEY_RANDOM_TYPE, KEY_LEN);

        return SERVER_REQUEST_IDENTITY_SYNC_KEY_GETTER.apply(serverRequest)
                .flatMap(identity -> blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(identity), ALLOW, SEND_INTERVAL_MILLIS))
                .flatMap(allowed ->
                        allowed ?
                                this.handle(businessType, generateRandom(KEY_RANDOM_TYPE, KEY_LEN))
                                        .flatMap(verify -> {
                                            LOGGER.info("Mono<ServerResponse> handle(String destination), key = {}, verify = {}", key, verify);

                                            return using(FastByteArrayOutputStream::new,
                                                    outputStream -> IMAGE_WRITER.apply(verify, outputStream)
                                                    , STREAM_CLOSER,
                                                    true)
                                                    .flatMap(resource ->
                                                            ok().contentType(IMAGE_PNG)
                                                                    .header(CACHE_CONTROL, CACHE_CONTROL_VALUE)
                                                                    .header(VERIFY_KEY.name, key)
                                                                    .body(fromResource(resource))
                                                    );
                                        })
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS.status, TOO_MANY_REQUESTS.code, "operation too frequently")));
    }

    @Override
    public Mono<Boolean> validate(BusinessType businessType, String key, String verify, Boolean repeatable) {
        return verifyService.validate(IMAGE, BUSINESS_KEY_WRAPPER.apply(businessType, key), verify, repeatable);
    }

    @Override
    public VerifyType targetType() {
        return IMAGE;
    }

}
