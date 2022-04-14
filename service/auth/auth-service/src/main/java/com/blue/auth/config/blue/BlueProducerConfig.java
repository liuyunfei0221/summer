package com.blue.auth.config.blue;

import com.blue.pulsar.api.conf.MultiProducerConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * producer config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "producers")
public class BlueProducerConfig extends MultiProducerConfParams {
}
