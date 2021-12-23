package com.blue.captcha.config.universal;

import com.blue.captcha.api.conf.CaptchaConfParams;
import com.blue.captcha.common.CaptchaProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.captcha.api.generator.BlueCaptchaGenerator.generateCaptchaProcessor;


/**
 * qrcode config
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaRemoveCommentedCode")
@Configuration
public class CaptchaConfig {

    @Bean
    CaptchaProcessor captchaProcessor() {
        return generateCaptchaProcessor(new CaptchaConfParams());
    }

}
