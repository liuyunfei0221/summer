package com.blue.base.config.blue;

import com.blue.mongo.api.conf.BaseMongoConfParams;
import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.connection.StreamFactoryFactory;
import org.bson.codecs.configuration.CodecRegistry;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mongo config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "mongo")
public class BlueBaseMongoConfig extends BaseMongoConfParams {

    @Override
    public CodecRegistry getCodecRegistry() {
        return null;
    }

    @Override
    public StreamFactoryFactory getStreamFactoryFactory() {
        return null;
    }

    @Override
    public ReadPreference getReadPreference() {
        return null;
    }

    @Override
    public ReadConcern getReadConcern() {
        return null;
    }

    @Override
    public WriteConcern getWriteConcern() {
        return null;
    }

}
