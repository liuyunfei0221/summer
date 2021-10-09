package com.blue.redis.api.conf;

import com.blue.redis.constant.ServerMode;

import java.util.List;


/**
 * redis conf
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaCommentsMustBeJavadocFormat"})
public interface RedisConf {

    ServerMode getServerMode();

    //<editor-fold desc="cluster conf">
    List<String> getNodes();

    String getPassword();
    //</editor-fold>

    //<editor-fold desc="standalone conf">
    String getHost();

    Integer getPort();
    //</editor-fold>

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
