package com.blue.pulsar.api.conf;

import org.apache.pulsar.client.api.ProxyProtocol;

import java.util.List;

/**
 * pulsar client conf
 *
 * @author DarkBlue
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

    String getProxyServiceUrl();

    ProxyProtocol getProxyProtocol();

    Boolean getEnableTransaction();

}
