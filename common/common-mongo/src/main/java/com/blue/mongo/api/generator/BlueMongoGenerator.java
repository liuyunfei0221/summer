package com.blue.mongo.api.generator;

import com.blue.mongo.api.conf.AddressAttr;
import com.blue.mongo.api.conf.MongoConf;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.slf4j.Logger;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import java.util.List;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * mongo components generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc", "AlibabaAvoidComplexCondition"})
public final class BlueMongoGenerator {

    private static final Logger LOGGER = getLogger(BlueMongoGenerator.class);

    /**
     * generate clientSettings
     *
     * @param mongoConf
     * @return
     */
    public static MongoClientSettings generateMongoClientSettings(MongoConf mongoConf) {
        assertConf(mongoConf);

        MongoClientSettings.Builder builder = MongoClientSettings.builder();

        if (ofNullable(mongoConf.getAuth()).orElse(false))
            builder.credential(MongoCredential.createCredential(mongoConf.getUserName(), mongoConf.getAuthBase(), mongoConf.getPassword().toCharArray()));

        builder.applyToClusterSettings(b -> {
            b.hosts(mongoConf.getAddressAttrs().stream().map(aa -> new ServerAddress(aa.getAddress(), aa.getPort())).collect(toList()));
            ofNullable(mongoConf.getClusterConnectionMode()).ifPresent(b::mode);
            ofNullable(mongoConf.getClusterType()).ifPresent(b::requiredClusterType);
            ofNullable(mongoConf.getRequiredReplicaSetName()).ifPresent(b::requiredReplicaSetName);
            ofNullable(mongoConf.getClusterLocalThresholdMillis()).ifPresent(p -> b.localThreshold(p, MILLISECONDS));
            ofNullable(mongoConf.getServerSelectionTimeoutMillis()).ifPresent(p -> b.serverSelectionTimeout(p, MILLISECONDS));
        });

        builder.applyToConnectionPoolSettings(b -> {
            ofNullable(mongoConf.getPoolMaxSize()).ifPresent(b::maxSize);
            ofNullable(mongoConf.getPoolMinSize()).ifPresent(b::minSize);
            ofNullable(mongoConf.getPoolMaxWaitTimeMillis()).ifPresent(p -> b.maxWaitTime(p, MILLISECONDS));
            ofNullable(mongoConf.getPoolMaxConnectionLifeTimeMillis()).ifPresent(p -> b.maxConnectionLifeTime(p, MILLISECONDS));
            ofNullable(mongoConf.getPoolMaxConnectionIdleTimeMillis()).ifPresent(p -> b.maxConnectionIdleTime(p, MILLISECONDS));
            ofNullable(mongoConf.getPoolMaintenanceInitialDelayMillis()).ifPresent(p -> b.maintenanceInitialDelay(p, MILLISECONDS));
            ofNullable(mongoConf.getPoolMaintenanceFrequencyMillis()).ifPresent(p -> b.maintenanceFrequency(p, MILLISECONDS));
        });

        builder.applyToSocketSettings(b -> {
            ofNullable(mongoConf.getSocketConnectTimeoutMillis()).ifPresent(p -> b.connectTimeout(p, MILLISECONDS));
            ofNullable(mongoConf.getSocketReadTimeoutMillis()).ifPresent(p -> b.readTimeout(p, MILLISECONDS));
            ofNullable(mongoConf.getSocketReceiveBufferSize()).ifPresent(b::receiveBufferSize);
            ofNullable(mongoConf.getSocketSendBufferSize()).ifPresent(b::sendBufferSize);
        });

        if (ofNullable(mongoConf.getSslEnabled()).orElse(false)) {
            builder.applyToSslSettings(b -> {
                b.enabled(true);
                b.invalidHostNameAllowed(mongoConf.getSslInvalidHostNameAllowed());
            });
        }

        ofNullable(mongoConf.getCodecRegistry())
                .ifPresent(builder::codecRegistry);
        ofNullable(mongoConf.getStreamFactoryFactory())
                .ifPresent(builder::streamFactoryFactory);
        ofNullable(mongoConf.getReadPreference())
                .ifPresent(builder::readPreference);
        ofNullable(mongoConf.getReadConcern())
                .ifPresent(builder::readConcern);
        ofNullable(mongoConf.getRetryReads())
                .ifPresent(builder::retryReads);
        ofNullable(mongoConf.getWriteConcern())
                .ifPresent(builder::writeConcern);
        ofNullable(mongoConf.getRetryWrites())
                .ifPresent(builder::retryWrites);
        ofNullable(mongoConf.getApplicationName())
                .ifPresent(builder::applicationName);
        ofNullable(mongoConf.getUuidRepresentation())
                .ifPresent(builder::uuidRepresentation);

        return builder.build();
    }

    /**
     * generate mongoClient
     *
     * @param mongoClientSettings
     * @return
     */
    public static MongoClient generateMongoClient(MongoClientSettings mongoClientSettings) {
        LOGGER.info("mongoClientSettings = {}", mongoClientSettings);
        if (isNull(mongoClientSettings))
            throw new RuntimeException("mongoClientSettings can't be null");

        return MongoClients.create(mongoClientSettings);
    }

    /**
     * generate template
     *
     * @param mongoClient
     * @param mongoConf
     * @return
     */
    public static ReactiveMongoTemplate generateReactiveMongoTemplate(MongoClient mongoClient, MongoConf mongoConf) {
        LOGGER.info("mongoClient = {}, mongoConf = {}", mongoClient, mongoConf);
        assertConf(mongoConf);

        return new ReactiveMongoTemplate(mongoClient, mongoConf.getDatabase());
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void assertConf(MongoConf conf) {
        if (isNull(conf))
            throw new RuntimeException("conf can't be null");

        List<AddressAttr> addressAttrs = conf.getAddressAttrs();
        if (isEmpty(addressAttrs))
            throw new RuntimeException("addressAttrs can't be null or empty");

        String database = conf.getDatabase();
        if (isBlank(database))
            throw new RuntimeException("database can't be null or ''");

        if (ofNullable(conf.getAuth()).orElse(false) && (isBlank(conf.getUserName()) || isBlank(conf.getPassword())))
            throw new RuntimeException("if auth, username and password can't be blank");
    }

}
