package com.blue.redis.api.conf;

import com.blue.redis.constant.ServerMode;

import java.util.List;


/**
 * redis配置参数封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface RedisConf {

    ServerMode getServerMode();

    List<String> getNodes();

    String getPassword();

    String getHost();

    Integer getPort();

    Integer getMaxRedirects();

    Integer getMinIdle();

    Integer getMaxIdle();

    Integer getMaxTotal();

    Integer getMaxWaitMillis();

    Boolean getAutoReconnect();

    Float getBufferUsageRatio();

    Boolean getCancelCommandsOnReconnectFailure();

    Boolean getPingBeforeActivateConnection();

    Integer getRequestQueueSize();

    Boolean getPublishOnScheduler();

    Boolean getTcpNoDelay();

    Integer getConnectTimeout();

    Boolean getKeepAlive();

    Boolean getSuspendReconnectOnProtocolFailure();

    Integer getFixedTimeout();

    Long getCommandTimeout();

    Long getShutdownTimeout();

    Long getShutdownQuietPeriod();

    Boolean getShareNativeConnection();

    Long getEntryTtl();

    Boolean getExposeConnection();
}
