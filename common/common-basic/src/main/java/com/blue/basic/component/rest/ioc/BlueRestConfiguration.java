package com.blue.basic.component.rest.ioc;

import com.blue.basic.component.rest.api.conf.RestConf;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.web.reactive.function.client.WebClient;

import static com.blue.basic.component.rest.api.generator.BlueRestGenerator.generateWebClient;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * reactive rest configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {RestConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueRestConfiguration {

    @Bean
    WebClient webClient(RestConf restConf) {
        return generateWebClient(restConf);
    }

}