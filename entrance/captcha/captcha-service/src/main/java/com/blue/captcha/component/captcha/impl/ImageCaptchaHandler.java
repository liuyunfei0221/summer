package com.blue.captcha.component.captcha.impl;

import com.blue.base.constant.captcha.CaptchaType;
import com.blue.base.model.exps.BlueException;
import com.blue.captcha.common.CaptchaProcessor;
import com.blue.captcha.component.captcha.inter.CaptchaHandler;
import com.blue.captcha.service.inter.CaptchaService;
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

import static com.blue.base.constant.base.BlueHeader.CAPTCHA;
import static com.blue.base.constant.base.RandomType.ALPHABETIC;
import static com.blue.base.constant.captcha.CaptchaType.IMAGE;
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
public class ImageCaptchaHandler implements CaptchaHandler {

    private static final Logger LOGGER = getLogger(ImageCaptchaHandler.class);

    private final CaptchaProcessor captchaProcessor;

    private final CaptchaService captchaService;

    public ImageCaptchaHandler(CaptchaProcessor captchaProcessor, CaptchaService captchaService) {
        this.captchaProcessor = captchaProcessor;
        this.captchaService = captchaService;
    }

    private static final int DEFAULT_VERIFY_LEN = 7;
    private static final Duration DEFAULT_DURATION = Duration.of(10L, MINUTES);

    private static final String IMAGE_TYPE = "png";

    private static final String CACHE_CONTROL_VALUE = "no-store, no-cache, must-revalidate, must-revalidate";


    /**
     * generate an image captcha
     *
     * @param destination
     * @return
     */
    @Override
    public Mono<ServerResponse> handle(String destination) {
        return captchaService.generate(ALPHABETIC, DEFAULT_VERIFY_LEN, DEFAULT_DURATION)
                .flatMap(cp -> {
                    String key = cp.getKey();
                    String verify = cp.getVerify();

                    ByteArrayResource resource;
                    try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {
                        ImageIO.write(captchaProcessor.generateImage(verify), IMAGE_TYPE, outputStream);
                        resource = new ByteArrayResource(outputStream.toByteArray());
                    } catch (IOException e) {
                        LOGGER.error("Mono<ServerResponse> handle(String destination) failed, e = {0}", e);
                        return error(BlueException::new);
                    }

                    return ok().contentType(IMAGE_PNG)
                            .header(CACHE_CONTROL, CACHE_CONTROL_VALUE)
                            .header(CAPTCHA.name, key)
                            .body(fromResource(resource));
                });
    }

    @Override
    public CaptchaType targetType() {
        return IMAGE;
    }

}
