package com.blue.verify.config.blue;

import com.blue.verify.api.conf.CaptchaConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author liuyunfei
 * @date 2021/12/24
 * @apiNote
 */
@Component
@ConfigurationProperties(prefix = "image")
public class BlueCaptchaImageConfig extends CaptchaConfParams {
}
