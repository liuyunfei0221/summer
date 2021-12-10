package com.blue.mongo.api.generator;

import com.blue.base.model.exps.BlueException;
import com.blue.mongo.api.conf.AddressAttr;
import com.blue.mongo.api.conf.MongoConf;
import com.mongodb.MongoClientSettings;
import com.mongodb.ServerAddress;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoClients;
import org.springframework.data.mongodb.core.MongoClientSettingsFactoryBean;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.util.Logger;

import java.util.List;
import java.util.stream.Collectors;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * mongo components generator
 *
 * @author liuyunfei
 * @date 2021/9/15
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class BlueMongoGenerator {

    private static final Logger LOGGER = getLogger(BlueMongoGenerator.class);

    /**
     * generate clientSettingFactoryBean
     *
     * @param mongoConf
     * @return
     */
    public static MongoClientSettingsFactoryBean generateMongoClientSettingsFactoryBean(MongoConf mongoConf) {
        confAsserter(mongoConf);

        MongoClientSettingsFactoryBean mongoClientSettingsFactoryBean = new MongoClientSettingsFactoryBean();

        mongoClientSettingsFactoryBean.setClusterHosts(
                mongoConf.getAddressAttrs().stream().map(aa -> new ServerAddress(aa.getAddress(), aa.getPort())).collect(Collectors.toList()).toArray(ServerAddress[]::new));
        ofNullable(mongoConf.getCodecRegistry())
                .ifPresent(mongoClientSettingsFactoryBean::setCodecRegistry);
        ofNullable(mongoConf.getStreamFactoryFactory())
                .ifPresent(mongoClientSettingsFactoryBean::setStreamFactoryFactory);
        ofNullable(mongoConf.getReadPreference())
                .ifPresent(mongoClientSettingsFactoryBean::setReadPreference);
        ofNullable(mongoConf.getReadConcern())
                .ifPresent(mongoClientSettingsFactoryBean::setReadConcern);
        ofNullable(mongoConf.getRetryReads())
                .ifPresent(mongoClientSettingsFactoryBean::setRetryReads);
        ofNullable(mongoConf.getWriteConcern())
                .ifPresent(mongoClientSettingsFactoryBean::setWriteConcern);
        ofNullable(mongoConf.getRetryWrites())
                .ifPresent(mongoClientSettingsFactoryBean::setRetryWrites);
        ofNullable(mongoConf.getApplicationName())
                .ifPresent(mongoClientSettingsFactoryBean::setApplicationName);
        ofNullable(mongoConf.getUuidRepresentation())
                .ifPresent(mongoClientSettingsFactoryBean::setuUidRepresentation);
        ofNullable(mongoConf.getSocketConnectTimeoutMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setSocketConnectTimeoutMS);
        ofNullable(mongoConf.getSocketConnectTimeoutMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setSocketConnectTimeoutMS);
        ofNullable(mongoConf.getSocketReadTimeoutMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setSocketReadTimeoutMS);
        ofNullable(mongoConf.getSocketReceiveBufferSize())
                .ifPresent(mongoClientSettingsFactoryBean::setSocketReceiveBufferSize);
        ofNullable(mongoConf.getSocketSendBufferSize())
                .ifPresent(mongoClientSettingsFactoryBean::setSocketSendBufferSize);
        ofNullable(mongoConf.getClusterConnectionMode())
                .ifPresent(mongoClientSettingsFactoryBean::setClusterConnectionMode);
        ofNullable(mongoConf.getClusterType())
                .ifPresent(mongoClientSettingsFactoryBean::setCusterRequiredClusterType);
        ofNullable(mongoConf.getRequiredReplicaSetName())
                .ifPresent(mongoClientSettingsFactoryBean::setClusterRequiredReplicaSetName);
        ofNullable(mongoConf.getClusterLocalThresholdMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setClusterLocalThresholdMS);
        ofNullable(mongoConf.getServerSelectionTimeoutMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setClusterServerSelectionTimeoutMS);
        ofNullable(mongoConf.getPoolMaxSize())
                .ifPresent(mongoClientSettingsFactoryBean::setPoolMaxSize);
        ofNullable(mongoConf.getPoolMinSize())
                .ifPresent(mongoClientSettingsFactoryBean::setPoolMinSize);
        ofNullable(mongoConf.getPoolMaxWaitTimeMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setPoolMaxWaitTimeMS);
        ofNullable(mongoConf.getPoolMaxConnectionLifeTimeMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setPoolMaxConnectionLifeTimeMS);
        ofNullable(mongoConf.getPoolMaxConnectionIdleTimeMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setPoolMaxConnectionIdleTimeMS);
        ofNullable(mongoConf.getPoolMaintenanceInitialDelayMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setPoolMaintenanceInitialDelayMS);
        ofNullable(mongoConf.getPoolMaintenanceFrequencyMillis())
                .ifPresent(mongoClientSettingsFactoryBean::setPoolMaintenanceFrequencyMS);

        if (ofNullable(mongoConf.getSslEnabled()).orElse(false)) {
            mongoClientSettingsFactoryBean.setSslEnabled(true);
            mongoClientSettingsFactoryBean.setSslInvalidHostNameAllowed(mongoConf.getSslInvalidHostNameAllowed());
            mongoClientSettingsFactoryBean.setSslProvider(mongoConf.getSslProvider());
        }

        return mongoClientSettingsFactoryBean;
    }

    /**
     * generate mongoClient
     *
     * @param mongoClientSettingsFactoryBean
     * @return
     */
    public static MongoClient generateMongoClient(MongoClientSettingsFactoryBean mongoClientSettingsFactoryBean) {
        LOGGER.info("MongoClient generateMongoClient(MongoClientSettingsFactoryBean mongoClientSettingsFactoryBean), mongoClientSettingsFactoryBean = {}", mongoClientSettingsFactoryBean);

        try {
            MongoClientSettings mongoClientSettings = mongoClientSettingsFactoryBean.getObject();
            if (mongoClientSettings == null)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "mongoClientSettings can't be null", null);

            return MongoClients.create(mongoClientSettings);
        } catch (Exception e) {
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "MongoClients.create() failed, e = " + e, null);
        }
    }

    /**
     * generate template
     *
     * @param mongoClient
     * @param mongoConf
     * @return
     */
    public static ReactiveMongoTemplate generateReactiveMongoTemplate(MongoClient mongoClient, MongoConf mongoConf) {
        LOGGER.info("generateReactiveMongoTemplate(MongoClient mongoClient, String databaseName), mongoClient = {}, mongoConf = {}", mongoClient, mongoConf);
        confAsserter(mongoConf);

        return new ReactiveMongoTemplate(mongoClient, mongoConf.getDatabaseName());
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void confAsserter(MongoConf conf) {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null", null);

        List<AddressAttr> addressAttrs = conf.getAddressAttrs();
        if (isEmpty(addressAttrs))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "addressAttrs can't be null or empty", null);

        String databaseName = conf.getDatabaseName();
        if (isBlank(databaseName))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "databaseName can't be null or ''", null);
    }

}
