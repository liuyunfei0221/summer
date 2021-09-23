package com.blue.secure.config.blue;

import com.blue.pulsar.api.conf.MultiProducerConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * kafka生产者配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "producers")
public class BlueProducerConfig extends MultiProducerConfParams {
}
