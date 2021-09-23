package com.blue.secure.config.blue;

import com.blue.base.component.reactrest.api.conf.ReactRestConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rest配置参数类
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@ConfigurationProperties(prefix = "reactrest")
public class BlueReactRestConfig extends ReactRestConfParams {
}