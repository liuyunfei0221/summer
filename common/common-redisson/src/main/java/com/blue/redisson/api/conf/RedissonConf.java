package com.blue.redisson.api.conf;

import com.blue.redisson.constant.ServerMode;
import org.redisson.config.ReadMode;
import org.redisson.config.SubscriptionMode;
import org.redisson.connection.balancer.LoadBalancer;

import java.util.List;


/**
 * redisson conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "unused", "AlibabaCommentsMustBeJavadocFormat"})
public interface RedissonConf {

    ServerMode getServerMode();

    //<editor-fold desc="cluster conf">
    List<String> getNodes();

    String getPassword();
    //</editor-fold>

    //<editor-fold desc="standalone conf">
    String getHost();

    Integer getPort();
    //</editor-fold>

    Integer getScanInterval();

    LoadBalancer getLoadBalancer();

    Boolean getCheckSlotsCoverage();

    Boolean getTcpNoDelay();

    Integer getMasterConnectionMinimumIdleSize();

    Integer getSlaveConnectionMinimumIdleSize();

    Integer getMasterConnectionPoolSize();

    Integer getSlaveConnectionPoolSize();

    Integer getSubscriptionConnectionMinimumIdleSize();

    Integer getSubscriptionConnectionPoolSize();

    Integer getSubscriptionsPerConnection();

    ReadMode getReadMode();

    SubscriptionMode getSubscriptionMode();

    Integer getTimeout();

    Integer getConnectTimeout();

    Integer getIdleConnectionTimeout();

    Integer getRetryAttempts();

    Integer getRetryInterval();

    Integer getConnectionMinimumIdleSize();

    Integer getConnectionPoolSize();

    Boolean getKeepAlive();

    Integer getDnsMonitoringInterval();

    Integer getPingConnectionInterval();

    Integer getExecutorCorePoolSize();

    Integer getExecutorMaximumPoolSize();

    Long getExecutorKeepAliveSeconds();

    Integer getExecutorBlockingQueueCapacity();

    Long getMaxTryLockWaitingMillis();

}
