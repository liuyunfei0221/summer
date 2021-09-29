package com.blue.es.ioc;

import com.blue.es.api.conf.EsConf;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;

import static com.blue.es.api.generator.BlueEsGenerator.generateElasticsearchTemplate;
import static com.blue.es.api.generator.BlueEsGenerator.generateRestHighLevelClient;

/**
 * es configuration
 *
 * @author DarkBlue
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringFacetCodeInspection"})
@ConditionalOnBean(value = {EsConf.class})
@Configuration
public class BlueEsConfiguration {

    @Bean
    RestHighLevelClient restHighLevelClient(EsConf esConf) {
        return generateRestHighLevelClient(esConf);
    }

    @Bean
    ElasticsearchRestTemplate elasticsearchRestTemplate(RestHighLevelClient restHighLevelClient) {
        return generateElasticsearchTemplate(restHighLevelClient);
    }

    @Bean
    ElasticsearchRestTemplate elasticsearchTemplate(ElasticsearchRestTemplate elasticsearchRestTemplate) {
        return elasticsearchRestTemplate;
    }

}
