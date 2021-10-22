package com.blue.captcha.config.blue;

import com.blue.dubbo.api.conf.DubboConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * dubbo config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "dubbo")
public class BlueDubboConfig extends DubboConfParams {
}
