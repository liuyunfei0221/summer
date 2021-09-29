package com.blue.mongo.api.conf;


import com.mongodb.ReadConcern;
import com.mongodb.ReadPreference;
import com.mongodb.WriteConcern;
import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;
import com.mongodb.connection.StreamFactoryFactory;
import org.bson.UuidRepresentation;
import org.bson.codecs.configuration.CodecRegistry;

import java.util.List;

/**
 * mongo conf
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MongoConf {

    List<AddressAttr> getAddressAttrs();

    String getDatabaseName();

    CodecRegistry getCodecRegistry();

    StreamFactoryFactory getStreamFactoryFactory();

    ReadPreference getReadPreference();

    ReadConcern getReadConcern();

    Boolean getRetryReads();

    WriteConcern getWriteConcern();

    Boolean getRetryWrites();

    String getApplicationName();

    UuidRepresentation getUuidRepresentation();

    Integer getSocketConnectTimeoutMillis();

    Integer getSocketReadTimeoutMillis();

    Integer getSocketReceiveBufferSize();

    Integer getSocketSendBufferSize();

    ClusterConnectionMode getClusterConnectionMode();

    ClusterType getClusterType();

    String getRequiredReplicaSetName();

    Integer getClusterLocalThresholdMillis();

    Long getServerSelectionTimeoutMillis();

    Integer getPoolMaxSize();

    Integer getPoolMinSize();

    Long getPoolMaxWaitTimeMillis();

    Long getPoolMaxConnectionLifeTimeMillis();

    Long getPoolMaxConnectionIdleTimeMillis();

    Long getPoolMaintenanceInitialDelayMillis();

    Long getPoolMaintenanceFrequencyMillis();

    Boolean getSslEnabled();

    Boolean getSslInvalidHostNameAllowed();

    String getSslProvider();

}
