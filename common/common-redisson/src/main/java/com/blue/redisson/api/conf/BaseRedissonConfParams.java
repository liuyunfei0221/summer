package com.blue.redisson.api.conf;

import com.blue.redisson.constant.ServerMode;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;

import java.util.List;

/**
 * redisson params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaCommentsMustBeJavadocFormat"})
public abstract class BaseRedissonConfParams implements RedissonConf {

    protected ServerMode serverMode;

    //<editor-fold desc="cluster conf">
    protected transient List<String> nodes;
    //</editor-fold>

    //<editor-fold desc="standalone conf">
    protected transient String address;
    //</editor-fold>

    protected transient String password;

    protected Integer scanInterval;

    protected Boolean checkSlotsCoverage;

    protected Boolean tcpNoDelay;

    protected Integer masterConnectionMinimumIdleSize;

    protected Integer slaveConnectionMinimumIdleSize;

    protected Integer masterConnectionPoolSize;

    protected Integer slaveConnectionPoolSize;

    protected Integer subscriptionConnectionMinimumIdleSize;

    protected Integer subscriptionConnectionPoolSize;

    protected Integer subscriptionsPerConnection;

    protected ReadMode readMode;

    protected SubscriptionMode subscriptionMode;

    protected Integer timeout;

    protected Integer connectTimeout;

    protected Integer idleConnectionTimeout;

    protected Integer retryAttempts;

    protected Integer retryInterval;

    protected Integer connectionMinimumIdleSize;

    protected Integer connectionPoolSize;

    protected Boolean keepAlive;

    protected Integer dnsMonitoringInterval;

    protected Integer pingConnectionInterval;

    protected Integer executorCorePoolSize;

    protected Integer executorMaximumPoolSize;

    protected Long executorKeepAliveSeconds;

    protected Integer executorBlockingQueueCapacity;

    protected Long maxTryLockWaitingMillis;

    public BaseRedissonConfParams() {
    }

    @Override
    public ServerMode getServerMode() {
        return serverMode;
    }

    @Override
    public List<String> getNodes() {
        return nodes;
    }

    @Override
    public String getAddress() {
        return address;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Integer getScanInterval() {
        return scanInterval;
    }

    @Override
    public Boolean getCheckSlotsCoverage() {
        return checkSlotsCoverage;
    }

    @Override
    public Boolean getTcpNoDelay() {
        return tcpNoDelay;
    }

    @Override
    public Integer getMasterConnectionMinimumIdleSize() {
        return masterConnectionMinimumIdleSize;
    }

    @Override
    public Integer getSlaveConnectionMinimumIdleSize() {
        return slaveConnectionMinimumIdleSize;
    }

    @Override
    public Integer getMasterConnectionPoolSize() {
        return masterConnectionPoolSize;
    }

    @Override
    public Integer getSlaveConnectionPoolSize() {
        return slaveConnectionPoolSize;
    }

    @Override
    public Integer getSubscriptionConnectionMinimumIdleSize() {
        return subscriptionConnectionMinimumIdleSize;
    }

    @Override
    public Integer getSubscriptionConnectionPoolSize() {
        return subscriptionConnectionPoolSize;
    }

    @Override
    public Integer getSubscriptionsPerConnection() {
        return subscriptionsPerConnection;
    }

    @Override
    public ReadMode getReadMode() {
        return readMode;
    }

    @Override
    public SubscriptionMode getSubscriptionMode() {
        return subscriptionMode;
    }

    @Override
    public Integer getTimeout() {
        return timeout;
    }

    @Override
    public Integer getConnectTimeout() {
        return connectTimeout;
    }

    @Override
    public Integer getIdleConnectionTimeout() {
        return idleConnectionTimeout;
    }

    @Override
    public Integer getRetryAttempts() {
        return retryAttempts;
    }

    @Override
    public Integer getRetryInterval() {
        return retryInterval;
    }

    @Override
    public Integer getConnectionMinimumIdleSize() {
        return connectionMinimumIdleSize;
    }

    @Override
    public Integer getConnectionPoolSize() {
        return connectionPoolSize;
    }

    @Override
    public Boolean getKeepAlive() {
        return keepAlive;
    }

    @Override
    public Integer getDnsMonitoringInterval() {
        return dnsMonitoringInterval;
    }

    @Override
    public Integer getPingConnectionInterval() {
        return pingConnectionInterval;
    }

    @Override
    public Integer getExecutorCorePoolSize() {
        return executorCorePoolSize;
    }

    @Override
    public Integer getExecutorMaximumPoolSize() {
        return executorMaximumPoolSize;
    }

    @Override
    public Long getExecutorKeepAliveSeconds() {
        return executorKeepAliveSeconds;
    }

    @Override
    public Integer getExecutorBlockingQueueCapacity() {
        return executorBlockingQueueCapacity;
    }

    @Override
    public Long getMaxTryLockWaitingMillis() {
        return maxTryLockWaitingMillis;
    }

    public void setServerMode(ServerMode serverMode) {
        this.serverMode = serverMode;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setScanInterval(Integer scanInterval) {
        this.scanInterval = scanInterval;
    }

    public void setCheckSlotsCoverage(Boolean checkSlotsCoverage) {
        this.checkSlotsCoverage = checkSlotsCoverage;
    }

    public void setTcpNoDelay(Boolean tcpNoDelay) {
        this.tcpNoDelay = tcpNoDelay;
    }

    public void setMasterConnectionMinimumIdleSize(Integer masterConnectionMinimumIdleSize) {
        this.masterConnectionMinimumIdleSize = masterConnectionMinimumIdleSize;
    }

    public void setSlaveConnectionMinimumIdleSize(Integer slaveConnectionMinimumIdleSize) {
        this.slaveConnectionMinimumIdleSize = slaveConnectionMinimumIdleSize;
    }

    public void setMasterConnectionPoolSize(Integer masterConnectionPoolSize) {
        this.masterConnectionPoolSize = masterConnectionPoolSize;
    }

    public void setSlaveConnectionPoolSize(Integer slaveConnectionPoolSize) {
        this.slaveConnectionPoolSize = slaveConnectionPoolSize;
    }

    public void setSubscriptionConnectionMinimumIdleSize(Integer subscriptionConnectionMinimumIdleSize) {
        this.subscriptionConnectionMinimumIdleSize = subscriptionConnectionMinimumIdleSize;
    }

    public void setSubscriptionConnectionPoolSize(Integer subscriptionConnectionPoolSize) {
        this.subscriptionConnectionPoolSize = subscriptionConnectionPoolSize;
    }

    public void setSubscriptionsPerConnection(Integer subscriptionsPerConnection) {
        this.subscriptionsPerConnection = subscriptionsPerConnection;
    }

    public void setReadMode(ReadMode readMode) {
        this.readMode = readMode;
    }

    public void setSubscriptionMode(SubscriptionMode subscriptionMode) {
        this.subscriptionMode = subscriptionMode;
    }

    public void setTimeout(Integer timeout) {
        this.timeout = timeout;
    }

    public void setConnectTimeout(Integer connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public void setIdleConnectionTimeout(Integer idleConnectionTimeout) {
        this.idleConnectionTimeout = idleConnectionTimeout;
    }

    public void setRetryAttempts(Integer retryAttempts) {
        this.retryAttempts = retryAttempts;
    }

    public void setRetryInterval(Integer retryInterval) {
        this.retryInterval = retryInterval;
    }

    public void setConnectionMinimumIdleSize(Integer connectionMinimumIdleSize) {
        this.connectionMinimumIdleSize = connectionMinimumIdleSize;
    }

    public void setConnectionPoolSize(Integer connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }

    public void setDnsMonitoringInterval(Integer dnsMonitoringInterval) {
        this.dnsMonitoringInterval = dnsMonitoringInterval;
    }

    public void setPingConnectionInterval(Integer pingConnectionInterval) {
        this.pingConnectionInterval = pingConnectionInterval;
    }

    public void setExecutorCorePoolSize(Integer executorCorePoolSize) {
        this.executorCorePoolSize = executorCorePoolSize;
    }

    public void setExecutorMaximumPoolSize(Integer executorMaximumPoolSize) {
        this.executorMaximumPoolSize = executorMaximumPoolSize;
    }

    public void setExecutorKeepAliveSeconds(Long executorKeepAliveSeconds) {
        this.executorKeepAliveSeconds = executorKeepAliveSeconds;
    }

    public void setExecutorBlockingQueueCapacity(Integer executorBlockingQueueCapacity) {
        this.executorBlockingQueueCapacity = executorBlockingQueueCapacity;
    }

    public void setMaxTryLockWaitingMillis(Long maxTryLockWaitingMillis) {
        this.maxTryLockWaitingMillis = maxTryLockWaitingMillis;
    }

    @Override
    public String toString() {
        return "BaseRedissonConfParams{" +
                "serverMode=" + serverMode +
                ", nodes=" + nodes +
                ", address='" + address + '\'' +
                ", password='" + ":)" + '\'' +
                ", scanInterval=" + scanInterval +
                ", checkSlotsCoverage=" + checkSlotsCoverage +
                ", tcpNoDelay=" + tcpNoDelay +
                ", masterConnectionMinimumIdleSize=" + masterConnectionMinimumIdleSize +
                ", slaveConnectionMinimumIdleSize=" + slaveConnectionMinimumIdleSize +
                ", masterConnectionPoolSize=" + masterConnectionPoolSize +
                ", slaveConnectionPoolSize=" + slaveConnectionPoolSize +
                ", subscriptionConnectionMinimumIdleSize=" + subscriptionConnectionMinimumIdleSize +
                ", subscriptionConnectionPoolSize=" + subscriptionConnectionPoolSize +
                ", subscriptionsPerConnection=" + subscriptionsPerConnection +
                ", readMode=" + readMode +
                ", subscriptionMode=" + subscriptionMode +
                ", timeout=" + timeout +
                ", connectTimeout=" + connectTimeout +
                ", idleConnectionTimeout=" + idleConnectionTimeout +
                ", retryAttempts=" + retryAttempts +
                ", retryInterval=" + retryInterval +
                ", connectionMinimumIdleSize=" + connectionMinimumIdleSize +
                ", connectionPoolSize=" + connectionPoolSize +
                ", keepAlive=" + keepAlive +
                ", dnsMonitoringInterval=" + dnsMonitoringInterval +
                ", pingConnectionInterval=" + pingConnectionInterval +
                ", executorCorePoolSize=" + executorCorePoolSize +
                ", executorMaximumPoolSize=" + executorMaximumPoolSize +
                ", executorKeepAliveSeconds=" + executorKeepAliveSeconds +
                ", executorBlockingQueueCapacity=" + executorBlockingQueueCapacity +
                ", maxTryLockWaitingMillis=" + maxTryLockWaitingMillis +
                '}';
    }

}
