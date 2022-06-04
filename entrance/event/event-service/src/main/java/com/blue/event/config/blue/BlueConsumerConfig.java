package com.blue.event.config.blue;


import com.blue.pulsar.api.conf.MultiConsumerConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * consumer config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "consumers")
public class BlueConsumerConfig extends MultiConsumerConfParams {
}
