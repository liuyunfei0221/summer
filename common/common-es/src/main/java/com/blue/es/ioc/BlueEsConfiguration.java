package com.blue.es.ioc;

import com.blue.es.api.conf.EsConf;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.es.api.generator.BlueEsGenerator.generateRestClient;

/**
 * es configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {EsConf.class})
@Configuration
public class BlueEsConfiguration {

    @Bean
    RestClient restClient(EsConf esConf) {
        return generateRestClient(esConf);
    }

}
