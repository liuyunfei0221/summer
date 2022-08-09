package com.blue.es.ioc;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.blue.es.api.conf.EsConf;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.es.api.generator.BlueEsGenerator.generateElasticsearchClient;

/**
 * es configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {EsConf.class})
@AutoConfiguration
public class BlueEsConfiguration {

    @Bean
    ElasticsearchClient elasticsearchClient(EsConf esConf) {
        return generateElasticsearchClient(esConf);
    }

}