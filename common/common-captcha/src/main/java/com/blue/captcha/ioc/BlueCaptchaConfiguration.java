package com.blue.captcha.ioc;

import com.blue.captcha.api.conf.CaptchaConf;
import com.blue.captcha.component.CaptchaProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.captcha.api.generator.BlueCaptchaGenerator.generateCaptchaProcessor;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * captcha processor configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {CaptchaConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueCaptchaConfiguration {

    @Bean
    CaptchaProcessor captchaProcessor(CaptchaConf captchaConf) {
        return generateCaptchaProcessor(captchaConf);
    }

}