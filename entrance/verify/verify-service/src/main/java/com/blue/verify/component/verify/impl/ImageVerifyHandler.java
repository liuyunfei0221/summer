package com.blue.verify.component.verify.impl;

import com.blue.basic.constant.common.RandomType;
import com.blue.basic.constant.verify.VerifyBusinessType;
import com.blue.basic.constant.verify.VerifyType;
import com.blue.basic.model.exps.BlueException;
import com.blue.captcha.component.CaptchaProcessor;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.redis.component.BlueLeakyBucketRateLimiter;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.ImageVerifyDeploy;
import com.blue.verify.repository.entity.VerifyHistory;
import com.blue.verify.service.inter.VerifyHistoryService;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.BlueRandomGenerator.generate;
import static com.blue.basic.common.base.CommonFunctions.*;
import static com.blue.basic.constant.common.BlueHeader.VERIFY_KEY;
import static com.blue.basic.constant.common.RateLimitKeyPrefix.IMAGE_VERIFY_RATE_LIMIT_KEY_PRE;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.TOO_MANY_REQUESTS;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION;
import static com.blue.basic.constant.verify.VerifyType.IMAGE;
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
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "JavadocDeclaration"})
public class ImageVerifyHandler implements VerifyHandler {

    private static final Logger LOGGER = getLogger(ImageVerifyHandler.class);

    private CaptchaProcessor captchaProcessor;

    private final VerifyService verifyService;

    private final BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final VerifyHistoryService verifyHistoryService;

    private ExecutorService executorService;

    private final int KEY_LEN;
    private final RandomType KEY_RANDOM_TYPE;
    private String imageType;
    private final int VERIFY_LEN;
    private final Duration DEFAULT_DURATION;
    private final int ALLOW;
    private final long SEND_INTERVAL_MILLIS;

    public ImageVerifyHandler(CaptchaProcessor captchaProcessor, VerifyService verifyService, BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter,
                              BlueIdentityProcessor blueIdentityProcessor, VerifyHistoryService verifyHistoryService, ExecutorService executorService, ImageVerifyDeploy imageVerifyDeploy) {
        this.captchaProcessor = captchaProcessor;
        this.verifyService = verifyService;
        this.blueLeakyBucketRateLimiter = blueLeakyBucketRateLimiter;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.verifyHistoryService = verifyHistoryService;
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

        Integer expiresMillis = imageVerifyDeploy.getExpiresMillis();
        if (isNull(expiresMillis) || expiresMillis < 1)
            throw new RuntimeException("expiresMillis can't be null or less than 1");

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
        this.DEFAULT_DURATION = Duration.of(expiresMillis, MILLIS);
        this.ALLOW = allow;
        this.SEND_INTERVAL_MILLIS = sendIntervalMillis;
    }

    private static final UnaryOperator<String> LIMIT_KEY_WRAPPER = key -> {
        if (isBlank(key))
            throw new RuntimeException("key can't be null");

        return IMAGE_VERIFY_RATE_LIMIT_KEY_PRE.prefix + key;
    };

    private static final BiFunction<VerifyBusinessType, String, String> BUSINESS_KEY_WRAPPER = (type, key) -> {
        if (isNull(type) || isBlank(key))
            throw new RuntimeException("type or key can't be null");

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
                                    error(() -> new RuntimeException("IMAGE_WRITER handle failed")));

    private final Consumer<FastByteArrayOutputStream> STREAM_CLOSER = stream -> {
        try {
            stream.close();
        } catch (Exception e) {
            LOGGER.error("FastByteArrayOutputStream close failed, e = {}", e);
        }
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
        verifyHistory.setVerifyType(IMAGE.identity);
        verifyHistory.setBusinessType(verifyBusinessType.identity);
        verifyHistory.setDestination(destination);
        verifyHistory.setVerify(verify);
        verifyHistory.setRequestIp(getIp(serverRequest));
        verifyHistory.setCreateTime(TIME_STAMP_GETTER.get());

        verifyHistoryService.insertVerifyHistory(verifyHistory)
                .doOnError(throwable -> LOGGER.info("imageVerifyHandler.recordVerify() failed, verifyHistory = {}, throwable = {}", verifyHistory, throwable))
                .subscribe(vh -> LOGGER.info("imageVerifyHandler.recordVerify() -> insertVerifyHistory(verifyHistory), vh = {}", vh));
    }

    @Override
    public Mono<String> handle(VerifyBusinessType verifyBusinessType, String destination) {
        LOGGER.info("ImageVerifyHandler -> Mono<String> handle(BusinessType businessType, String destination), businessType = {}, destination = {}", verifyBusinessType, destination);
        if (isNull(verifyBusinessType) || isBlank(destination))
            throw new BlueException(BAD_REQUEST);

        return verifyService.generate(IMAGE, BUSINESS_KEY_WRAPPER.apply(verifyBusinessType, destination), VERIFY_LEN, DEFAULT_DURATION);
    }

    @Override
    public Mono<ServerResponse> handle(VerifyBusinessType verifyBusinessType, String destination, ServerRequest serverRequest) {
        String verifyKey = isBlank(destination) ? generate(KEY_RANDOM_TYPE, KEY_LEN) : destination;

        return SERVER_REQUEST_IDENTITY_SYNC_KEY_GETTER.apply(serverRequest)
                .flatMap(identity -> blueLeakyBucketRateLimiter.isAllowed(LIMIT_KEY_WRAPPER.apply(identity), ALLOW, SEND_INTERVAL_MILLIS))
                .flatMap(allowed ->
                        allowed ?
                                this.handle(verifyBusinessType, verifyKey)
                                        .flatMap(verify -> {
                                            LOGGER.warn("Mono<ServerResponse> handle(String destination), verifyKey = {}, verify = {}", verifyKey, verify);

                                            return using(FastByteArrayOutputStream::new,
                                                    outputStream -> IMAGE_WRITER.apply(verify, outputStream)
                                                    , STREAM_CLOSER, true)
                                                    .flatMap(resource ->
                                                            ok().contentType(IMAGE_PNG)
                                                                    .header(CACHE_CONTROL, CACHE_CONTROL_VALUE)
                                                                    .header(VERIFY_KEY.name, verifyKey)
                                                                    .body(fromResource(resource))
                                                    ).doOnSuccess(ig ->
                                                            recordVerify(verifyBusinessType, destination, verify, serverRequest)
                                                    );
                                        })
                                :
                                error(() -> new BlueException(TOO_MANY_REQUESTS)));
    }

    @Override
    public Mono<Boolean> validate(VerifyBusinessType verifyBusinessType, String key, String verify, Boolean repeatable) {
        return verifyService.validate(IMAGE, BUSINESS_KEY_WRAPPER.apply(verifyBusinessType, key), verify, repeatable);
    }

    @Override
    public VerifyType targetType() {
        return IMAGE;
    }

}
