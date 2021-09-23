package com.blue.business.config.blue;

import com.blue.dubbo.api.conf.DubboConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * dubbo配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "dubbo")
public class BlueDubboConfig extends DubboConfParams {
}
