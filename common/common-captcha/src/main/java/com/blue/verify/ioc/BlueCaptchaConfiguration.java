package com.blue.verify.ioc;

import com.blue.verify.api.conf.CaptchaConf;
import com.blue.verify.common.CaptchaProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.verify.api.generator.BlueCaptchaGenerator.generateCaptchaProcessor;

/**
 * captcha processor configuration
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