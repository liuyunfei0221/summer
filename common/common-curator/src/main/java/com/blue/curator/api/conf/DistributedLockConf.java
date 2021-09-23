package com.blue.curator.api.conf;

/**
 * zk分布式锁配置
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface DistributedLockConf {

    String getConnectString();

    String getAuthSchema();

    String getAuth();

    Integer getConnectionTimeoutMs();

    Integer getSessionTimeoutMs();

    Integer getMaxCloseWaitMs();

    Integer getRetryBaseSleepTimeMs();

    Integer getRetryMaxSleepTimeMs();

    Integer getRetryMaxRetries();

    String getNamespace();

    Boolean getCanBeReadOnly();

}
