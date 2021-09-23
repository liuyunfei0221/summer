package com.blue.risk.config.blue;


import com.blue.pulsar.api.conf.MultiConsumerConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * pulsar消费端配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "consumers")
public class BlueConsumerConfig extends MultiConsumerConfParams {
}
