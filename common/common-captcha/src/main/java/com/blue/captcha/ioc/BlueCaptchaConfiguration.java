package com.blue.captcha.ioc;

import com.blue.captcha.api.conf.CaptchaConf;
import com.blue.captcha.component.CaptchaProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.captcha.api.generator.BlueCaptchaGenerator.generateCaptchaProcessor;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpringFacetCodeInspection", "SpringJavaInjectionPointsAutowiringInspection"})
@ConditionalOnBean(value = {CaptchaConf.class})
@Configuration
public class BlueCaptchaConfiguration {

    @Bean
    CaptchaProcessor captchaProcessor(CaptchaConf captchaConf) {
        return generateCaptchaProcessor(captchaConf);
    }

}