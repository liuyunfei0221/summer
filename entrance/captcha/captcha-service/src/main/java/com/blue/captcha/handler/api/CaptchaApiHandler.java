package com.blue.captcha.handler.api;

import com.blue.base.model.exps.BlueException;
import com.blue.captcha.service.inter.CaptchaService;
import com.blue.secure.api.model.ClientLoginParam;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URLEncoder;

import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.BlueHeader.CONTENT_DISPOSITION;
import static com.blue.base.constant.base.RandomType.ALPHABETIC;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.EMPTY_PARAM;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;
import static org.springframework.web.reactive.function.BodyInserters.fromValue;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static reactor.core.publisher.Mono.error;


/**
 * captcha api handler
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "Duplicates"})
@Component
public final class CaptchaApiHandler {

    private final CaptchaService captchaService;

    public CaptchaApiHandler(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    /**
     * download
     *
     * @param serverRequest
     * @return
     */
    public Mono<ServerResponse> generateClientLoginImageCaptcha(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(ClientLoginParam.class)
                .switchIfEmpty(
                        error(new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, EMPTY_PARAM.message)))
                .flatMap(p ->
                        captchaService.generateClientLoginImageCaptcha(p)
                                .flatMap(bytes ->
                                        ok().contentType(APPLICATION_OCTET_STREAM)
                                                .header(CONTENT_DISPOSITION.name,
                                                        "attachment; filename=" + URLEncoder.encode(generateRandom(ALPHABETIC, 8), UTF_8))
                                                .body(fromValue(bytes))
                                ));
    }

}
