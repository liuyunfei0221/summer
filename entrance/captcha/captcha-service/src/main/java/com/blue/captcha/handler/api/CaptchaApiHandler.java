package com.blue.captcha.handler.api;

import com.blue.captcha.service.inter.CaptchaService;
import com.blue.secure.api.model.ClientLoginParam;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.BlueHeader.CONTENT_DISPOSITION;
import static com.blue.base.constant.base.CommonException.EMPTY_PARAM_EXP;
import static com.blue.base.constant.base.RandomType.ALPHABETIC;
import static java.net.URLEncoder.encode;
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
                        error(EMPTY_PARAM_EXP.exp))
                .flatMap(p ->
                        captchaService.generateClientLoginImageCaptcha(p)
                                .flatMap(bytes ->
                                        ok().contentType(APPLICATION_OCTET_STREAM)
                                                .header(CONTENT_DISPOSITION.name,
                                                        "attachment; filename=" + encode(generateRandom(ALPHABETIC, 8), UTF_8))
                                                .body(fromValue(bytes))
                                ));
    }

}
