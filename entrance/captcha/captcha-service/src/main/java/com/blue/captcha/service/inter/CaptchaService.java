package com.blue.captcha.service.inter;

import com.blue.secure.api.model.ClientLoginParam;
import reactor.core.publisher.Mono;

/**
 * captcha service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface CaptchaService {

    /**
     * generate a image captcha for client login
     *
     * @param clientLoginParam
     * @return
     */
    Mono<byte[]> generateClientLoginImageCaptcha(ClientLoginParam clientLoginParam);

}
