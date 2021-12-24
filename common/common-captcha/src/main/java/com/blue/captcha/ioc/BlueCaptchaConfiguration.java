package com.blue.captcha.ioc;

import com.blue.captcha.api.conf.CaptchaConf;
import com.blue.captcha.common.CaptchaProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.captcha.api.generator.BlueCaptchaGenerator.generateCaptchaProcessor;

/**
 * reactive rest configuration
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@ConditionalOnBean(value = {CaptchaConf.class})
@Configuration
public class BlueCaptchaConfiguration {

    @Bean
    CaptchaProcessor captchaProcessor(CaptchaConf captchaConf) {
        return generateCaptchaProcessor(captchaConf);
    }

}