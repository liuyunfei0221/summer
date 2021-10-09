package com.blue.redisson.api.conf;

import com.blue.redisson.constant.ServerMode;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;

import java.util.List;

/**
 * redisson params
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaCommentsMustBeJavadocFormat"})
public abstract class BaseRedissonConfParams implements RedissonConf {

    protected ServerMode serverMode;

    //<editor-fold desc="cluster conf">
    protected List<String> nodes;

    protected String password;
    //</editor-fold>

    //<editor-fold desc="standalone conf">
    protected String host;

    protected Integer port;
    //</editor-fold>

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

    protected Long executorKeepAliveTime;

    protected Integer executorBlockingQueueCapacity;

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
    public String getPassword() {
        return password;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public Integer getPort() {
        return port;
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
    public Long getExecutorKeepAliveTime() {
        return executorKeepAliveTime;
    }

    @Override
    public Integer getExecutorBlockingQueueCapacity() {
        return executorBlockingQueueCapacity;
    }

    public void setServerMode(ServerMode serverMode) {
        this.serverMode = serverMode;
    }

    public void setNodes(List<String> nodes) {
        this.nodes = nodes;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(Integer port) {
        this.port = port;
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

    public void setExecutorKeepAliveTime(Long executorKeepAliveTime) {
        this.executorKeepAliveTime = executorKeepAliveTime;
    }

    public void setExecutorBlockingQueueCapacity(Integer executorBlockingQueueCapacity) {
        this.executorBlockingQueueCapacity = executorBlockingQueueCapacity;
    }

    @Override
    public String toString() {
        return "BaseRedissonConfParams{" +
                "serverMode=" + serverMode +
                ", nodes=" + nodes +
                ", password='" + password + '\'' +
                ", host='" + host + '\'' +
                ", port=" + port +
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
                ", executorKeepAliveTime=" + executorKeepAliveTime +
                ", executorBlockingQueueCapacity=" + executorBlockingQueueCapacity +
                '}';
    }

}
