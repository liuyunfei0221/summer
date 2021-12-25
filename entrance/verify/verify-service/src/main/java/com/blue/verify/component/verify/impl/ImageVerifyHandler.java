package com.blue.verify.component.verify.impl;

import com.blue.base.constant.verify.VerifyType;
import com.blue.base.model.exps.BlueException;
import com.blue.verify.common.CaptchaProcessor;
import com.blue.verify.component.verify.inter.VerifyHandler;
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

import static com.blue.base.constant.base.BlueHeader.VERIFY;
import static com.blue.base.constant.base.RandomType.ALPHABETIC;
import static com.blue.base.constant.verify.VerifyType.IMAGE;
import static java.time.temporal.ChronoUnit.MINUTES;
import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.MediaType.IMAGE_PNG;
import static org.springframework.web.reactive.function.BodyInserters.fromResource;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;
import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/12/23
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
@Order(Ordered.LOWEST_PRECEDENCE - 1)
public class ImageVerifyHandler implements VerifyHandler {

    private static final Logger LOGGER = getLogger(ImageVerifyHandler.class);

    private final CaptchaProcessor captchaProcessor;

    private final VerifyService verifyService;

    public ImageVerifyHandler(CaptchaProcessor captchaProcessor, VerifyService verifyService) {
        this.captchaProcessor = captchaProcessor;
        this.verifyService = verifyService;
    }

    private static final int DEFAULT_VERIFY_LEN = 7;
    private static final Duration DEFAULT_DURATION = Duration.of(10L, MINUTES);

    private static final String IMAGE_TYPE = "png";

    private static final String CACHE_CONTROL_VALUE = "no-store, no-cache, must-revalidate, must-revalidate";


    /**
     * generate an image verify
     *
     * @param destination
     * @return
     */
    @Override
    public Mono<ServerResponse> handle(String destination) {
        return verifyService.generate(ALPHABETIC, DEFAULT_VERIFY_LEN, DEFAULT_DURATION)
                .flatMap(cp -> {
                    String key = cp.getKey();
                    String verify = cp.getVerify();

                    LOGGER.info("Mono<ServerResponse> handle(String destination), key = {}, verify = {}", key, verify);

                    ByteArrayResource resource;
                    try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {
                        //noinspection BlockingMethodInNonBlockingContext
                        ImageIO.write(captchaProcessor.generateImage(verify), IMAGE_TYPE, outputStream);
                        resource = new ByteArrayResource(outputStream.toByteArray());
                    } catch (IOException e) {
                        LOGGER.error("Mono<ServerResponse> handle(String destination) failed, e = {0}", e);
                        return error(BlueException::new);
                    }

                    return ok().contentType(IMAGE_PNG)
                            .header(CACHE_CONTROL, CACHE_CONTROL_VALUE)
                            .header(VERIFY.name, key)
                            .body(fromResource(resource));
                });
    }

    @Override
    public VerifyType targetType() {
        return IMAGE;
    }

}
