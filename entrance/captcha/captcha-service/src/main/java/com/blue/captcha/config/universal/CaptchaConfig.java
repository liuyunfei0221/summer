package com.blue.captcha.config.universal;

import com.blue.captcha.api.conf.CaptchaConfParams;
import com.blue.captcha.api.generator.BlueCaptchaGenerator;
import com.blue.captcha.common.CaptchaProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
        return BlueCaptchaGenerator.generateCaptchaProcessor(new CaptchaConfParams());
    }

}
