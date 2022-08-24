package com.blue.pulsar.api.conf;

import java.util.List;

/**
 * pulsar client conf
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface ClientConf {

    List<String> getServices();

    String getListenerName();

    Integer getOperationTimeoutMillis();

    Integer getIoThreads();

    Integer getListenerThreads();

    Integer getConnectionsPerBroker();

    Boolean getUseTcpNoDelay();

    Boolean getEnableTls();

    String getTlsTrustCertsFilePath();

    String getTlsCertFilePath();

    String getTlsKeyFilePath();

    Boolean getTlsAllowInsecureConnection();

    Boolean getTlsHostnameVerificationEnable();

    Boolean getEnableJwtAuth();

    String getJwt();

    Integer getMemoryLimitKiloBytes();

    Long getStatsIntervalMillis();

    Integer getMaxConcurrentLookupRequests();

    Integer getMaxLookupRequest();

    Integer getMaxLookupRedirects();

    Integer getMaxNumberOfRejectedRequestPerConnection();

    Integer getKeepAliveIntervalMillis();

    Integer getConnectionTimeoutMillis();

    Long getStartingBackoffIntervalMillis();

    Long getMaxBackoffIntervalMillis();

    Boolean getEnableBusyWait();

    String getClockZoneId();

    Boolean getEnableProxy();

    Proxy getProxy();

    Boolean getEnableSocks5Proxy();

    Socks5Proxy getSocks5Proxy();

    Boolean getEnableTransaction();

    Boolean getEnableFailover();

    ClusterFailover getClusterFailover();

}
