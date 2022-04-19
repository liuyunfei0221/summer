package com.blue.mongo.api.conf;

import com.mongodb.connection.ClusterConnectionMode;
import com.mongodb.connection.ClusterType;
import org.bson.UuidRepresentation;

import java.util.List;

/**
 * mongo params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public abstract class BaseMongoConfParams implements MongoConf {

    protected transient List<AddressAttr> addressAttrs;

    protected transient Boolean auth;

    protected transient String userName;

    protected transient String password;

    protected transient String database;

    protected Boolean retryReads;

    protected Boolean retryWrites;

    protected transient String applicationName;

    protected UuidRepresentation uuidRepresentation;

    protected Integer socketConnectTimeoutMillis;

    protected Integer socketReadTimeoutMillis;

    protected Integer socketReceiveBufferSize;

    protected Integer socketSendBufferSize;

    protected ClusterConnectionMode clusterConnectionMode;

    protected ClusterType clusterType;

    protected String requiredReplicaSetName;

    protected Integer clusterLocalThresholdMillis;

    protected Long serverSelectionTimeoutMillis;

    protected Integer poolMaxSize;

    protected Integer poolMinSize;

    protected Long poolMaxWaitTimeMillis;

    protected Long poolMaxConnectionLifeTimeMillis;

    protected Long poolMaxConnectionIdleTimeMillis;

    protected Long poolMaintenanceInitialDelayMillis;

    protected Long poolMaintenanceFrequencyMillis;

    protected Boolean sslEnabled;

    protected Boolean sslInvalidHostNameAllowed;

    public BaseMongoConfParams() {
    }

    @Override
    public List<AddressAttr> getAddressAttrs() {
        return addressAttrs;
    }

    @Override
    public Boolean getAuth() {
        return auth;
    }

    @Override
    public String getUserName() {
        return userName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public Boolean getRetryReads() {
        return retryReads;
    }

    @Override
    public Boolean getRetryWrites() {
        return retryWrites;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public UuidRepresentation getUuidRepresentation() {
        return uuidRepresentation;
    }

    @Override
    public Integer getSocketConnectTimeoutMillis() {
        return socketConnectTimeoutMillis;
    }

    @Override
    public Integer getSocketReadTimeoutMillis() {
        return socketReadTimeoutMillis;
    }

    @Override
    public Integer getSocketReceiveBufferSize() {
        return socketReceiveBufferSize;
    }

    @Override
    public Integer getSocketSendBufferSize() {
        return socketSendBufferSize;
    }

    @Override
    public ClusterConnectionMode getClusterConnectionMode() {
        return clusterConnectionMode;
    }

    @Override
    public ClusterType getClusterType() {
        return clusterType;
    }

    @Override
    public String getRequiredReplicaSetName() {
        return requiredReplicaSetName;
    }

    @Override
    public Integer getClusterLocalThresholdMillis() {
        return clusterLocalThresholdMillis;
    }

    @Override
    public Long getServerSelectionTimeoutMillis() {
        return serverSelectionTimeoutMillis;
    }

    @Override
    public Integer getPoolMaxSize() {
        return poolMaxSize;
    }

    @Override
    public Integer getPoolMinSize() {
        return poolMinSize;
    }

    @Override
    public Long getPoolMaxWaitTimeMillis() {
        return poolMaxWaitTimeMillis;
    }

    @Override
    public Long getPoolMaxConnectionLifeTimeMillis() {
        return poolMaxConnectionLifeTimeMillis;
    }

    @Override
    public Long getPoolMaxConnectionIdleTimeMillis() {
        return poolMaxConnectionIdleTimeMillis;
    }

    @Override
    public Long getPoolMaintenanceInitialDelayMillis() {
        return poolMaintenanceInitialDelayMillis;
    }

    @Override
    public Long getPoolMaintenanceFrequencyMillis() {
        return poolMaintenanceFrequencyMillis;
    }

    @Override
    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    @Override
    public Boolean getSslInvalidHostNameAllowed() {
        return sslInvalidHostNameAllowed;
    }

    public void setAddressAttrs(List<AddressAttr> addressAttrs) {
        this.addressAttrs = addressAttrs;
    }

    public void setAuth(Boolean auth) {
        this.auth = auth;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public void setRetryReads(Boolean retryReads) {
        this.retryReads = retryReads;
    }

    public void setRetryWrites(Boolean retryWrites) {
        this.retryWrites = retryWrites;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setUuidRepresentation(UuidRepresentation uuidRepresentation) {
        this.uuidRepresentation = uuidRepresentation;
    }

    public void setSocketConnectTimeoutMillis(Integer socketConnectTimeoutMillis) {
        this.socketConnectTimeoutMillis = socketConnectTimeoutMillis;
    }

    public void setSocketReadTimeoutMillis(Integer socketReadTimeoutMillis) {
        this.socketReadTimeoutMillis = socketReadTimeoutMillis;
    }

    public void setSocketReceiveBufferSize(Integer socketReceiveBufferSize) {
        this.socketReceiveBufferSize = socketReceiveBufferSize;
    }

    public void setSocketSendBufferSize(Integer socketSendBufferSize) {
        this.socketSendBufferSize = socketSendBufferSize;
    }

    public void setClusterConnectionMode(ClusterConnectionMode clusterConnectionMode) {
        this.clusterConnectionMode = clusterConnectionMode;
    }

    public void setClusterType(ClusterType clusterType) {
        this.clusterType = clusterType;
    }

    public void setRequiredReplicaSetName(String requiredReplicaSetName) {
        this.requiredReplicaSetName = requiredReplicaSetName;
    }

    public void setClusterLocalThresholdMillis(Integer clusterLocalThresholdMillis) {
        this.clusterLocalThresholdMillis = clusterLocalThresholdMillis;
    }

    public void setServerSelectionTimeoutMillis(Long serverSelectionTimeoutMillis) {
        this.serverSelectionTimeoutMillis = serverSelectionTimeoutMillis;
    }

    public void setPoolMaxSize(Integer poolMaxSize) {
        this.poolMaxSize = poolMaxSize;
    }

    public void setPoolMinSize(Integer poolMinSize) {
        this.poolMinSize = poolMinSize;
    }

    public void setPoolMaxWaitTimeMillis(Long poolMaxWaitTimeMillis) {
        this.poolMaxWaitTimeMillis = poolMaxWaitTimeMillis;
    }

    public void setPoolMaxConnectionLifeTimeMillis(Long poolMaxConnectionLifeTimeMillis) {
        this.poolMaxConnectionLifeTimeMillis = poolMaxConnectionLifeTimeMillis;
    }

    public void setPoolMaxConnectionIdleTimeMillis(Long poolMaxConnectionIdleTimeMillis) {
        this.poolMaxConnectionIdleTimeMillis = poolMaxConnectionIdleTimeMillis;
    }

    public void setPoolMaintenanceInitialDelayMillis(Long poolMaintenanceInitialDelayMillis) {
        this.poolMaintenanceInitialDelayMillis = poolMaintenanceInitialDelayMillis;
    }

    public void setPoolMaintenanceFrequencyMillis(Long poolMaintenanceFrequencyMillis) {
        this.poolMaintenanceFrequencyMillis = poolMaintenanceFrequencyMillis;
    }

    public void setSslEnabled(Boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    public void setSslInvalidHostNameAllowed(Boolean sslInvalidHostNameAllowed) {
        this.sslInvalidHostNameAllowed = sslInvalidHostNameAllowed;
    }

    @Override
    public String toString() {
        return "BaseMongoConfParams{" +
                "addressAttrs=" + addressAttrs +
                ", auth=" + auth +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", database='" + database + '\'' +
                ", retryReads=" + retryReads +
                ", retryWrites=" + retryWrites +
                ", applicationName='" + applicationName + '\'' +
                ", uuidRepresentation=" + uuidRepresentation +
                ", socketConnectTimeoutMillis=" + socketConnectTimeoutMillis +
                ", socketReadTimeoutMillis=" + socketReadTimeoutMillis +
                ", socketReceiveBufferSize=" + socketReceiveBufferSize +
                ", socketSendBufferSize=" + socketSendBufferSize +
                ", clusterConnectionMode=" + clusterConnectionMode +
                ", clusterType=" + clusterType +
                ", requiredReplicaSetName='" + requiredReplicaSetName + '\'' +
                ", clusterLocalThresholdMillis=" + clusterLocalThresholdMillis +
                ", serverSelectionTimeoutMillis=" + serverSelectionTimeoutMillis +
                ", poolMaxSize=" + poolMaxSize +
                ", poolMinSize=" + poolMinSize +
                ", poolMaxWaitTimeMillis=" + poolMaxWaitTimeMillis +
                ", poolMaxConnectionLifeTimeMillis=" + poolMaxConnectionLifeTimeMillis +
                ", poolMaxConnectionIdleTimeMillis=" + poolMaxConnectionIdleTimeMillis +
                ", poolMaintenanceInitialDelayMillis=" + poolMaintenanceInitialDelayMillis +
                ", poolMaintenanceFrequencyMillis=" + poolMaintenanceFrequencyMillis +
                ", sslEnabled=" + sslEnabled +
                ", sslInvalidHostNameAllowed=" + sslInvalidHostNameAllowed +
                '}';
    }

}
