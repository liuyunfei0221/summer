package com.blue.captcha.service.impl;

import com.blue.captcha.service.inter.CaptchaService;
import com.blue.secure.api.model.ClientLoginParam;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import static reactor.core.publisher.Mono.just;

/**
 * captcha service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
@Service
public class CaptchaServiceImpl implements CaptchaService {

    /**
     * generate a image captcha for client login
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<byte[]> generateClientLoginImageCaptcha(ClientLoginParam clientLoginParam) {
        return just(new byte[0]);
    }

}
