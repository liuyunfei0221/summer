package com.blue.curator.api.conf;

/**
 * zk lock conf
 *
 * @author liuyunfei
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
