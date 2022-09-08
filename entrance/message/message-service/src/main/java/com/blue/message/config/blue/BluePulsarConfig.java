package com.blue.message.config.blue;


import com.blue.pulsar.api.conf.ClientConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * pulsar config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "pulsar")
public class BluePulsarConfig extends ClientConfParams {
}
