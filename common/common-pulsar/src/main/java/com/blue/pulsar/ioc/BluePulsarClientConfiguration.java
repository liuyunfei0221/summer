package com.blue.pulsar.ioc;

import com.blue.pulsar.api.conf.ClientConf;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.pulsar.common.PulsarCommonsGenerator.generateClient;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * pulsar client configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "SpringJavaInjectionPointsAutowiringInspection"})
@ConditionalOnBean(value = {ClientConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BluePulsarClientConfiguration {

    @Bean
    PulsarClient pulsarClient(ClientConf clientConf) {
        return generateClient(clientConf);
    }

}
