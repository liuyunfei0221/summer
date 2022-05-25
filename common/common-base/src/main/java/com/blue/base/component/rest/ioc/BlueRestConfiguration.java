package com.blue.base.component.rest.ioc;

import com.blue.base.component.rest.api.conf.RestConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static com.blue.base.component.rest.api.generator.BlueRestGenerator.generateWebClient;

/**
 * reactive rest configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {RestConf.class})
@Configuration
public class BlueRestConfiguration {

    @Bean
    WebClient webClient(RestConf restConf) {
        return generateWebClient(restConf);
    }

}