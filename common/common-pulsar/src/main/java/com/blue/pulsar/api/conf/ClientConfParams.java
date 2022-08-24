package com.blue.pulsar.api.conf;

import java.util.List;

/**
 * client conf params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public class ClientConfParams implements ClientConf {

    protected List<String> services;

    protected String listenerName;

    protected Integer operationTimeoutMillis;

    protected Integer ioThreads;

    protected Integer listenerThreads;

    protected Integer connectionsPerBroker;

    protected Boolean useTcpNoDelay;

    protected Boolean enableTls;

    protected String tlsTrustCertsFilePath;

    protected String tlsCertFilePath;

    protected String tlsKeyFilePath;

    protected Boolean tlsAllowInsecureConnection;

    protected Boolean tlsHostnameVerificationEnable;

    protected Boolean enableJwtAuth;

    protected String jwt;

    protected Integer memoryLimitKiloBytes;

    protected Long statsIntervalMillis;

    protected Integer maxConcurrentLookupRequests;

    protected Integer maxLookupRequest;

    protected Integer maxLookupRedirects;

    protected Integer maxNumberOfRejectedRequestPerConnection;

    protected Integer keepAliveIntervalMillis;

    protected Integer connectionTimeoutMillis;

    protected Long startingBackoffIntervalMillis;

    protected Long maxBackoffIntervalMillis;

    protected Boolean enableBusyWait;

    protected String clockZoneId;

    protected Boolean enableProxy;

    protected Proxy proxy;

    protected Boolean enableSocks5Proxy;

    protected Socks5Proxy socks5Proxy;

    protected Boolean enableTransaction;

    protected Boolean enableFailover;

    protected ClusterFailover clusterFailover;

    @Override
    public List<String> getServices() {
        return services;
    }

    @Override
    public String getListenerName() {
        return listenerName;
    }

    @Override
    public Integer getOperationTimeoutMillis() {
        return operationTimeoutMillis;
    }

    @Override
    public Integer getIoThreads() {
        return ioThreads;
    }

    @Override
    public Integer getListenerThreads() {
        return listenerThreads;
    }

    @Override
    public Integer getConnectionsPerBroker() {
        return connectionsPerBroker;
    }

    @Override
    public Boolean getUseTcpNoDelay() {
        return useTcpNoDelay;
    }

    @Override
    public Boolean getEnableTls() {
        return enableTls;
    }

    @Override
    public String getTlsTrustCertsFilePath() {
        return tlsTrustCertsFilePath;
    }

    @Override
    public String getTlsCertFilePath() {
        return tlsCertFilePath;
    }

    @Override
    public String getTlsKeyFilePath() {
        return tlsKeyFilePath;
    }

    @Override
    public Boolean getTlsAllowInsecureConnection() {
        return tlsAllowInsecureConnection;
    }

    @Override
    public Boolean getTlsHostnameVerificationEnable() {
        return tlsHostnameVerificationEnable;
    }

    @Override
    public Boolean getEnableJwtAuth() {
        return enableJwtAuth;
    }

    @Override
    public String getJwt() {
        return jwt;
    }

    @Override
    public Integer getMemoryLimitKiloBytes() {
        return memoryLimitKiloBytes;
    }

    @Override
    public Long getStatsIntervalMillis() {
        return statsIntervalMillis;
    }

    @Override
    public Integer getMaxConcurrentLookupRequests() {
        return maxConcurrentLookupRequests;
    }

    @Override
    public Integer getMaxLookupRequest() {
        return maxLookupRequest;
    }

    @Override
    public Integer getMaxLookupRedirects() {
        return maxLookupRedirects;
    }

    @Override
    public Integer getMaxNumberOfRejectedRequestPerConnection() {
        return maxNumberOfRejectedRequestPerConnection;
    }

    @Override
    public Integer getKeepAliveIntervalMillis() {
        return keepAliveIntervalMillis;
    }

    @Override
    public Integer getConnectionTimeoutMillis() {
        return connectionTimeoutMillis;
    }

    @Override
    public Long getStartingBackoffIntervalMillis() {
        return startingBackoffIntervalMillis;
    }

    @Override
    public Long getMaxBackoffIntervalMillis() {
        return maxBackoffIntervalMillis;
    }

    @Override
    public Boolean getEnableBusyWait() {
        return enableBusyWait;
    }

    @Override
    public String getClockZoneId() {
        return clockZoneId;
    }

    @Override
    public Boolean getEnableProxy() {
        return enableProxy;
    }

    @Override
    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public Boolean getEnableSocks5Proxy() {
        return enableSocks5Proxy;
    }

    @Override
    public Socks5Proxy getSocks5Proxy() {
        return socks5Proxy;
    }

    @Override
    public Boolean getEnableTransaction() {
        return enableTransaction;
    }

    @Override
    public Boolean getEnableFailover() {
        return enableFailover;
    }

    @Override
    public ClusterFailover getClusterFailover() {
        return clusterFailover;
    }

    public void setServices(List<String> services) {
        this.services = services;
    }

    public void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    public void setOperationTimeoutMillis(Integer operationTimeoutMillis) {
        this.operationTimeoutMillis = operationTimeoutMillis;
    }

    public void setIoThreads(Integer ioThreads) {
        this.ioThreads = ioThreads;
    }

    public void setListenerThreads(Integer listenerThreads) {
        this.listenerThreads = listenerThreads;
    }

    public void setConnectionsPerBroker(Integer connectionsPerBroker) {
        this.connectionsPerBroker = connectionsPerBroker;
    }

    public void setUseTcpNoDelay(Boolean useTcpNoDelay) {
        this.useTcpNoDelay = useTcpNoDelay;
    }

    public void setEnableTls(Boolean enableTls) {
        this.enableTls = enableTls;
    }

    public void setTlsTrustCertsFilePath(String tlsTrustCertsFilePath) {
        this.tlsTrustCertsFilePath = tlsTrustCertsFilePath;
    }

    public void setTlsCertFilePath(String tlsCertFilePath) {
        this.tlsCertFilePath = tlsCertFilePath;
    }

    public void setTlsKeyFilePath(String tlsKeyFilePath) {
        this.tlsKeyFilePath = tlsKeyFilePath;
    }

    public void setTlsAllowInsecureConnection(Boolean tlsAllowInsecureConnection) {
        this.tlsAllowInsecureConnection = tlsAllowInsecureConnection;
    }

    public void setTlsHostnameVerificationEnable(Boolean tlsHostnameVerificationEnable) {
        this.tlsHostnameVerificationEnable = tlsHostnameVerificationEnable;
    }

    public void setEnableJwtAuth(Boolean enableJwtAuth) {
        this.enableJwtAuth = enableJwtAuth;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public void setMemoryLimitKiloBytes(Integer memoryLimitKiloBytes) {
        this.memoryLimitKiloBytes = memoryLimitKiloBytes;
    }

    public void setStatsIntervalMillis(Long statsIntervalMillis) {
        this.statsIntervalMillis = statsIntervalMillis;
    }

    public void setMaxConcurrentLookupRequests(Integer maxConcurrentLookupRequests) {
        this.maxConcurrentLookupRequests = maxConcurrentLookupRequests;
    }

    public void setMaxLookupRequest(Integer maxLookupRequest) {
        this.maxLookupRequest = maxLookupRequest;
    }

    public void setMaxLookupRedirects(Integer maxLookupRedirects) {
        this.maxLookupRedirects = maxLookupRedirects;
    }

    public void setMaxNumberOfRejectedRequestPerConnection(Integer maxNumberOfRejectedRequestPerConnection) {
        this.maxNumberOfRejectedRequestPerConnection = maxNumberOfRejectedRequestPerConnection;
    }

    public void setKeepAliveIntervalMillis(Integer keepAliveIntervalMillis) {
        this.keepAliveIntervalMillis = keepAliveIntervalMillis;
    }

    public void setConnectionTimeoutMillis(Integer connectionTimeoutMillis) {
        this.connectionTimeoutMillis = connectionTimeoutMillis;
    }

    public void setStartingBackoffIntervalMillis(Long startingBackoffIntervalMillis) {
        this.startingBackoffIntervalMillis = startingBackoffIntervalMillis;
    }

    public void setMaxBackoffIntervalMillis(Long maxBackoffIntervalMillis) {
        this.maxBackoffIntervalMillis = maxBackoffIntervalMillis;
    }

    public void setEnableBusyWait(Boolean enableBusyWait) {
        this.enableBusyWait = enableBusyWait;
    }

    public void setClockZoneId(String clockZoneId) {
        this.clockZoneId = clockZoneId;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public void setEnableSocks5Proxy(Boolean enableSocks5Proxy) {
        this.enableSocks5Proxy = enableSocks5Proxy;
    }

    public void setSocks5Proxy(Socks5Proxy socks5Proxy) {
        this.socks5Proxy = socks5Proxy;
    }

    public void setEnableTransaction(Boolean enableTransaction) {
        this.enableTransaction = enableTransaction;
    }

    public void setEnableFailover(Boolean enableFailover) {
        this.enableFailover = enableFailover;
    }

    public void setClusterFailover(ClusterFailover clusterFailover) {
        this.clusterFailover = clusterFailover;
    }

    @Override
    public String toString() {
        return "ClientConfParams{" +
                "services=" + services +
                ", listenerName='" + listenerName + '\'' +
                ", operationTimeoutMillis=" + operationTimeoutMillis +
                ", ioThreads=" + ioThreads +
                ", listenerThreads=" + listenerThreads +
                ", connectionsPerBroker=" + connectionsPerBroker +
                ", useTcpNoDelay=" + useTcpNoDelay +
                ", enableTls=" + enableTls +
                ", tlsTrustCertsFilePath='" + tlsTrustCertsFilePath + '\'' +
                ", tlsCertFilePath='" + tlsCertFilePath + '\'' +
                ", tlsKeyFilePath='" + tlsKeyFilePath + '\'' +
                ", tlsAllowInsecureConnection=" + tlsAllowInsecureConnection +
                ", tlsHostnameVerificationEnable=" + tlsHostnameVerificationEnable +
                ", enableJwtAuth=" + enableJwtAuth +
                ", jwt='" + jwt + '\'' +
                ", memoryLimitKiloBytes=" + memoryLimitKiloBytes +
                ", statsIntervalMillis=" + statsIntervalMillis +
                ", maxConcurrentLookupRequests=" + maxConcurrentLookupRequests +
                ", maxLookupRequest=" + maxLookupRequest +
                ", maxLookupRedirects=" + maxLookupRedirects +
                ", maxNumberOfRejectedRequestPerConnection=" + maxNumberOfRejectedRequestPerConnection +
                ", keepAliveIntervalMillis=" + keepAliveIntervalMillis +
                ", connectionTimeoutMillis=" + connectionTimeoutMillis +
                ", startingBackoffIntervalMillis=" + startingBackoffIntervalMillis +
                ", maxBackoffIntervalMillis=" + maxBackoffIntervalMillis +
                ", enableBusyWait=" + enableBusyWait +
                ", clockZoneId='" + clockZoneId + '\'' +
                ", enableProxy=" + enableProxy +
                ", proxy=" + proxy +
                ", enableSocks5Proxy=" + enableSocks5Proxy +
                ", socks5Proxy=" + socks5Proxy +
                ", enableTransaction=" + enableTransaction +
                ", enableFailover=" + enableFailover +
                ", clusterFailover=" + clusterFailover +
                '}';
    }

}
