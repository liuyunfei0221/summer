package com.blue.verify.component.verify.impl;

import com.blue.base.constant.base.RandomType;
import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.verify.common.CaptchaProcessor;
import com.blue.verify.component.verify.inter.VerifyHandler;
import com.blue.verify.config.deploy.ImageVerifyDeploy;
import com.blue.verify.service.inter.VerifyService;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FastByteArrayOutputStream;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import static com.blue.base.common.base.BlueCheck.isBlank;
import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.BlueHeader.VERIFY_KEY;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
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
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "SpringJavaInjectionPointsAutowiringInspection"})
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ImageVerifyHandler implements VerifyHandler {

    private static final Logger LOGGER = getLogger(ImageVerifyHandler.class);

    private CaptchaProcessor captchaProcessor;

    private final VerifyService verifyService;

    private ExecutorService executorService;

    private final int KEY_LEN;
    private final RandomType KEY_RANDOM_TYPE;
    private final int VERIFY_LEN;
    private final Duration DEFAULT_DURATION;
    private String imageType;


    public ImageVerifyHandler(CaptchaProcessor captchaProcessor, VerifyService verifyService, ExecutorService executorService, ImageVerifyDeploy imageVerifyDeploy) {
        this.captchaProcessor = captchaProcessor;
        this.verifyService = verifyService;
        this.executorService = executorService;

        Integer keyLength = imageVerifyDeploy.getKeyLength();
        if (keyLength == null || keyLength < 1)
            throw new RuntimeException("keyLength can't be null or less than 1");

        RandomType keyRandomType = imageVerifyDeploy.getKeyRandomType();
        if (keyRandomType == null)
            throw new RuntimeException("randomType can't be null");

        Integer verifyLength = imageVerifyDeploy.getVerifyLength();
        if (verifyLength == null || verifyLength < 1)
            throw new RuntimeException("verifyLength can't be null or less than 1");

        Integer expireMillis = imageVerifyDeploy.getExpireMillis();
        if (expireMillis == null || expireMillis < 1)
            throw new RuntimeException("expireMillis can't be null or less than 1");

        String imageType = imageVerifyDeploy.getImageType();
        if (isBlank(imageType))
            throw new RuntimeException("imageType can't be blank");

        this.KEY_LEN = keyLength;
        this.KEY_RANDOM_TYPE = keyRandomType;
        this.VERIFY_LEN = verifyLength;
        this.DEFAULT_DURATION = Duration.of(expireMillis, MILLIS);
        this.imageType = imageType;
    }


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

    /**
     * generate an image verify
     *
     * @param destination
     * @return
     */
    @Override
    public Mono<ServerResponse> handle(String destination) {
        return verifyService.generate(IMAGE, generateRandom(KEY_RANDOM_TYPE, KEY_LEN), VERIFY_LEN, DEFAULT_DURATION)
                .flatMap(cp -> {
                    String key = cp.getKey();
                    String verify = cp.getVerify();

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
                });
    }

    @Override
    public VerifyType targetType() {
        return IMAGE;
    }

}
