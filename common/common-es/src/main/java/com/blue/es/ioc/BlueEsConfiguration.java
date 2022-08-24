package com.blue.es.ioc;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.blue.es.api.conf.EsConf;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.es.api.generator.BlueEsGenerator.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * es configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {EsConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueEsConfiguration {

    @Bean
    RestClientTransport restClientTransport(EsConf esConf) {
        return generateRestClientTransport(esConf);
    }

    @Bean
    ElasticsearchClient elasticsearchClient(RestClientTransport restClientTransport, EsConf esConf) {
        return generateElasticsearchClient(restClientTransport, esConf);
    }

    @Bean
    ElasticsearchAsyncClient elasticsearchAsyncClient(RestClientTransport restClientTransport, EsConf esConf) {
        return generateElasticsearchAsyncClient(restClientTransport, esConf);
    }

}