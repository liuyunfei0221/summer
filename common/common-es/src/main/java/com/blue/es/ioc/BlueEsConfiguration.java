package com.blue.es.ioc;

import com.blue.es.api.conf.EsConf;
import org.elasticsearch.client.RestClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.es.api.generator.BlueEsGenerator.generateRestClient;

/**
 * es configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringFacetCodeInspection"})
@ConditionalOnBean(value = {EsConf.class})
@AutoConfiguration
public class BlueEsConfiguration {

    @Bean
    RestClient restClient(EsConf esConf) {
        return generateRestClient(esConf);
    }

}
