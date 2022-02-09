package com.blue.mongo.ioc;

import com.blue.mongo.api.conf.MongoConf;
import com.mongodb.MongoClientSettings;
import com.mongodb.reactivestreams.client.MongoClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import static com.blue.mongo.api.generator.BlueMongoGenerator.*;

/**
 * mongo configuration
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "SpringFacetCodeInspection", "SpringJavaInjectionPointsAutowiringInspection"})
@ConditionalOnBean(value = {MongoConf.class})
@Configuration
public class BlueMongoConfiguration {

    @Bean
    MongoClientSettings mongoClientSettings(MongoConf mongoConf) {
        return generateMongoClientSettings(mongoConf);
    }

    @Bean
    MongoClient mongoClient(MongoClientSettings mongoClientSettings) {
        return generateMongoClient(mongoClientSettings);
    }

    @Bean
    ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient, MongoConf mongoConf) {
        return generateReactiveMongoTemplate(mongoClient, mongoConf);
    }

}
