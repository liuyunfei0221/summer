package com.blue.analyze.config.blue;

import com.blue.basic.component.message.api.conf.MessageConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * message config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "message")
public class BlueMessageConfig extends MessageConfParams {
}
