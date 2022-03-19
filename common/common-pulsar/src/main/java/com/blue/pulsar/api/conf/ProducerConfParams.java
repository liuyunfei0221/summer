package com.blue.pulsar.api.conf;

import org.apache.pulsar.client.api.*;

import java.util.List;

/**
 * pulsar producer params
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class ProducerConfParams implements ProducerConf {

    protected transient List<String> services;

    protected Boolean enableTls;

    protected transient String tlsTrustCertsFilePath;

    protected transient String tlsCertFilePath;

    protected transient String tlsKeyFilePath;

    protected Boolean tlsAllowInsecureConnection;

    protected Boolean tlsHostnameVerificationEnable;

    protected transient String listenerName;

    protected Integer operationTimeoutMillis;

    protected Long statsIntervalMillis;

    protected Integer ioThreads;

    protected Integer listenerThreads;

    protected Integer connectionsPerBroker;

    protected Boolean useTcpNoDelay;

    protected Integer memoryLimitKiloBytes;

    protected Integer maxConcurrentLookupRequests;

    protected Integer maxLookupRequest;

    protected Integer maxLookupRedirects;

    protected Integer maxNumberOfRejectedRequestPerConnection;

    protected Integer keepAliveIntervalMillis;

    protected Integer connectionTimeoutMillis;

    protected Long startingBackoffIntervalMillis;

    protected Long maxBackoffIntervalMillis;

    protected Boolean enableBusyWait;

    protected transient String clockZoneId;

    protected Boolean enableTransaction;

    protected Boolean enableProxy;

    protected transient String proxyServiceUrl;

    protected ProxyProtocol proxyProtocol;

    protected transient String topic;

    protected transient String producerName;

    protected ProducerAccessMode accessMode;

    protected Integer sendTimeoutMillis;

    protected Integer maxPendingMessages;

    protected Integer maxPendingMessagesAcrossPartitions;

    protected Boolean blockIfQueueFull;

    protected MessageRoutingMode messageRoutingMode;

    protected CompressionType compressionType;

    protected Boolean enableBatching;

    protected Boolean enableChunking;

    protected Long batchingMaxPublishDelayMillis;

    protected Integer roundRobinRouterBatchingPartitionSwitchFrequency;

    protected Integer batchingMaxMessages;

    protected Integer batchingMaxBytes;

    protected Long initialSequenceId;

    protected Boolean autoUpdatePartitions;

    protected Integer autoUpdatePartitionsIntervalMillis;

    protected Boolean enableMultiSchema;

    protected List<HashingScheme> hashingSchemes;

    protected Boolean enableEncrypt;

    protected transient String encryptionKey;

    protected ProducerCryptoFailureAction producerCryptoFailureAction;

    public ProducerConfParams() {
    }

    public ProducerConfParams(List<String> services, Boolean enableTls, String tlsTrustCertsFilePath, String tlsCertFilePath, String tlsKeyFilePath, Boolean tlsAllowInsecureConnection, Boolean tlsHostnameVerificationEnable, String listenerName, Integer operationTimeoutMillis, Long statsIntervalMillis, Integer ioThreads, Integer listenerThreads, Integer connectionsPerBroker, Boolean useTcpNoDelay, Integer memoryLimitKiloBytes, Integer maxConcurrentLookupRequests, Integer maxLookupRequest, Integer maxLookupRedirects, Integer maxNumberOfRejectedRequestPerConnection, Integer keepAliveIntervalMillis, Integer connectionTimeoutMillis, Long startingBackoffIntervalMillis, Long maxBackoffIntervalMillis, Boolean enableBusyWait, String clockZoneId, Boolean enableTransaction, Boolean enableProxy, String proxyServiceUrl, ProxyProtocol proxyProtocol, String topic, String producerName, ProducerAccessMode accessMode, Integer sendTimeoutMillis, Integer maxPendingMessages, Integer maxPendingMessagesAcrossPartitions, Boolean blockIfQueueFull, MessageRoutingMode messageRoutingMode, CompressionType compressionType, Boolean enableBatching, Boolean enableChunking, Long batchingMaxPublishDelayMillis, Integer roundRobinRouterBatchingPartitionSwitchFrequency, Integer batchingMaxMessages, Integer batchingMaxBytes, Long initialSequenceId, Boolean autoUpdatePartitions, Integer autoUpdatePartitionsIntervalMillis, Boolean enableMultiSchema, List<HashingScheme> hashingSchemes, Boolean enableEncrypt, String encryptionKey, ProducerCryptoFailureAction producerCryptoFailureAction) {
        this.services = services;
        this.enableTls = enableTls;
        this.tlsTrustCertsFilePath = tlsTrustCertsFilePath;
        this.tlsCertFilePath = tlsCertFilePath;
        this.tlsKeyFilePath = tlsKeyFilePath;
        this.tlsAllowInsecureConnection = tlsAllowInsecureConnection;
        this.tlsHostnameVerificationEnable = tlsHostnameVerificationEnable;
        this.listenerName = listenerName;
        this.operationTimeoutMillis = operationTimeoutMillis;
        this.statsIntervalMillis = statsIntervalMillis;
        this.ioThreads = ioThreads;
        this.listenerThreads = listenerThreads;
        this.connectionsPerBroker = connectionsPerBroker;
        this.useTcpNoDelay = useTcpNoDelay;
        this.memoryLimitKiloBytes = memoryLimitKiloBytes;
        this.maxConcurrentLookupRequests = maxConcurrentLookupRequests;
        this.maxLookupRequest = maxLookupRequest;
        this.maxLookupRedirects = maxLookupRedirects;
        this.maxNumberOfRejectedRequestPerConnection = maxNumberOfRejectedRequestPerConnection;
        this.keepAliveIntervalMillis = keepAliveIntervalMillis;
        this.connectionTimeoutMillis = connectionTimeoutMillis;
        this.startingBackoffIntervalMillis = startingBackoffIntervalMillis;
        this.maxBackoffIntervalMillis = maxBackoffIntervalMillis;
        this.enableBusyWait = enableBusyWait;
        this.clockZoneId = clockZoneId;
        this.enableTransaction = enableTransaction;
        this.enableProxy = enableProxy;
        this.proxyServiceUrl = proxyServiceUrl;
        this.proxyProtocol = proxyProtocol;
        this.topic = topic;
        this.producerName = producerName;
        this.accessMode = accessMode;
        this.sendTimeoutMillis = sendTimeoutMillis;
        this.maxPendingMessages = maxPendingMessages;
        this.maxPendingMessagesAcrossPartitions = maxPendingMessagesAcrossPartitions;
        this.blockIfQueueFull = blockIfQueueFull;
        this.messageRoutingMode = messageRoutingMode;
        this.compressionType = compressionType;
        this.enableBatching = enableBatching;
        this.enableChunking = enableChunking;
        this.batchingMaxPublishDelayMillis = batchingMaxPublishDelayMillis;
        this.roundRobinRouterBatchingPartitionSwitchFrequency = roundRobinRouterBatchingPartitionSwitchFrequency;
        this.batchingMaxMessages = batchingMaxMessages;
        this.batchingMaxBytes = batchingMaxBytes;
        this.initialSequenceId = initialSequenceId;
        this.autoUpdatePartitions = autoUpdatePartitions;
        this.autoUpdatePartitionsIntervalMillis = autoUpdatePartitionsIntervalMillis;
        this.enableMultiSchema = enableMultiSchema;
        this.hashingSchemes = hashingSchemes;
        this.enableEncrypt = enableEncrypt;
        this.encryptionKey = encryptionKey;
        this.producerCryptoFailureAction = producerCryptoFailureAction;
    }

    @Override
    public List<String> getServices() {
        return services;
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
    public String getListenerName() {
        return listenerName;
    }

    @Override
    public Integer getOperationTimeoutMillis() {
        return operationTimeoutMillis;
    }

    @Override
    public Long getStatsIntervalMillis() {
        return statsIntervalMillis;
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
    public Integer getMemoryLimitKiloBytes() {
        return memoryLimitKiloBytes;
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
    public Boolean getEnableTransaction() {
        return enableTransaction;
    }

    @Override
    public Boolean getEnableProxy() {
        return enableProxy;
    }

    @Override
    public String getProxyServiceUrl() {
        return proxyServiceUrl;
    }

    @Override
    public ProxyProtocol getProxyProtocol() {
        return proxyProtocol;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getProducerName() {
        return producerName;
    }

    @Override
    public ProducerAccessMode getAccessMode() {
        return accessMode;
    }

    @Override
    public Integer getSendTimeoutMillis() {
        return sendTimeoutMillis;
    }

    @Override
    public Integer getMaxPendingMessages() {
        return maxPendingMessages;
    }

    @Override
    public Integer getMaxPendingMessagesAcrossPartitions() {
        return maxPendingMessagesAcrossPartitions;
    }

    @Override
    public Boolean getBlockIfQueueFull() {
        return blockIfQueueFull;
    }

    @Override
    public MessageRoutingMode getMessageRoutingMode() {
        return messageRoutingMode;
    }

    @Override
    public CompressionType getCompressionType() {
        return compressionType;
    }

    @Override
    public Boolean getEnableBatching() {
        return enableBatching;
    }

    @Override
    public Boolean getEnableChunking() {
        return enableChunking;
    }

    @Override
    public Long getBatchingMaxPublishDelayMillis() {
        return batchingMaxPublishDelayMillis;
    }

    @Override
    public Integer getRoundRobinRouterBatchingPartitionSwitchFrequency() {
        return roundRobinRouterBatchingPartitionSwitchFrequency;
    }

    @Override
    public Integer getBatchingMaxMessages() {
        return batchingMaxMessages;
    }

    @Override
    public Integer getBatchingMaxBytes() {
        return batchingMaxBytes;
    }

    @Override
    public Long getInitialSequenceId() {
        return initialSequenceId;
    }

    @Override
    public Boolean getAutoUpdatePartitions() {
        return autoUpdatePartitions;
    }

    @Override
    public Integer getAutoUpdatePartitionsIntervalMillis() {
        return autoUpdatePartitionsIntervalMillis;
    }

    @Override
    public Boolean getEnableMultiSchema() {
        return enableMultiSchema;
    }

    @Override
    public List<HashingScheme> getHashingSchemes() {
        return hashingSchemes;
    }

    @Override
    public Boolean getEnableEncrypt() {
        return enableEncrypt;
    }

    @Override
    public String getEncryptionKey() {
        return encryptionKey;
    }

    @Override
    public ProducerCryptoFailureAction getProducerCryptoFailureAction() {
        return producerCryptoFailureAction;
    }

    public void setServices(List<String> services) {
        this.services = services;
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

    public void setListenerName(String listenerName) {
        this.listenerName = listenerName;
    }

    public void setOperationTimeoutMillis(Integer operationTimeoutMillis) {
        this.operationTimeoutMillis = operationTimeoutMillis;
    }

    public void setStatsIntervalMillis(Long statsIntervalMillis) {
        this.statsIntervalMillis = statsIntervalMillis;
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

    public void setMemoryLimitKiloBytes(Integer memoryLimitKiloBytes) {
        this.memoryLimitKiloBytes = memoryLimitKiloBytes;
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

    public void setEnableTransaction(Boolean enableTransaction) {
        this.enableTransaction = enableTransaction;
    }

    public void setEnableProxy(Boolean enableProxy) {
        this.enableProxy = enableProxy;
    }

    public void setProxyServiceUrl(String proxyServiceUrl) {
        this.proxyServiceUrl = proxyServiceUrl;
    }

    public void setProxyProtocol(ProxyProtocol proxyProtocol) {
        this.proxyProtocol = proxyProtocol;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setProducerName(String producerName) {
        this.producerName = producerName;
    }

    public void setAccessMode(ProducerAccessMode accessMode) {
        this.accessMode = accessMode;
    }

    public void setSendTimeoutMillis(Integer sendTimeoutMillis) {
        this.sendTimeoutMillis = sendTimeoutMillis;
    }

    public void setMaxPendingMessages(Integer maxPendingMessages) {
        this.maxPendingMessages = maxPendingMessages;
    }

    public void setMaxPendingMessagesAcrossPartitions(Integer maxPendingMessagesAcrossPartitions) {
        this.maxPendingMessagesAcrossPartitions = maxPendingMessagesAcrossPartitions;
    }

    public void setBlockIfQueueFull(Boolean blockIfQueueFull) {
        this.blockIfQueueFull = blockIfQueueFull;
    }

    public void setMessageRoutingMode(MessageRoutingMode messageRoutingMode) {
        this.messageRoutingMode = messageRoutingMode;
    }

    public void setCompressionType(CompressionType compressionType) {
        this.compressionType = compressionType;
    }

    public void setEnableBatching(Boolean enableBatching) {
        this.enableBatching = enableBatching;
    }

    public void setEnableChunking(Boolean enableChunking) {
        this.enableChunking = enableChunking;
    }

    public void setBatchingMaxPublishDelayMillis(Long batchingMaxPublishDelayMillis) {
        this.batchingMaxPublishDelayMillis = batchingMaxPublishDelayMillis;
    }

    public void setRoundRobinRouterBatchingPartitionSwitchFrequency(Integer roundRobinRouterBatchingPartitionSwitchFrequency) {
        this.roundRobinRouterBatchingPartitionSwitchFrequency = roundRobinRouterBatchingPartitionSwitchFrequency;
    }

    public void setBatchingMaxMessages(Integer batchingMaxMessages) {
        this.batchingMaxMessages = batchingMaxMessages;
    }

    public void setBatchingMaxBytes(Integer batchingMaxBytes) {
        this.batchingMaxBytes = batchingMaxBytes;
    }

    public void setInitialSequenceId(Long initialSequenceId) {
        this.initialSequenceId = initialSequenceId;
    }

    public void setAutoUpdatePartitions(Boolean autoUpdatePartitions) {
        this.autoUpdatePartitions = autoUpdatePartitions;
    }

    public void setAutoUpdatePartitionsIntervalMillis(Integer autoUpdatePartitionsIntervalMillis) {
        this.autoUpdatePartitionsIntervalMillis = autoUpdatePartitionsIntervalMillis;
    }

    public void setEnableMultiSchema(Boolean enableMultiSchema) {
        this.enableMultiSchema = enableMultiSchema;
    }

    public void setHashingSchemes(List<HashingScheme> hashingSchemes) {
        this.hashingSchemes = hashingSchemes;
    }

    public void setEnableEncrypt(Boolean enableEncrypt) {
        this.enableEncrypt = enableEncrypt;
    }

    public void setEncryptionKey(String encryptionKey) {
        this.encryptionKey = encryptionKey;
    }

    public void setProducerCryptoFailureAction(ProducerCryptoFailureAction producerCryptoFailureAction) {
        this.producerCryptoFailureAction = producerCryptoFailureAction;
    }

    @Override
    public String toString() {
        return "ProducerConfParams{" +
                "services=" + services +
                ", enableTls=" + enableTls +
                ", tlsTrustCertsFilePath='" + tlsTrustCertsFilePath + '\'' +
                ", tlsCertFilePath='" + tlsCertFilePath + '\'' +
                ", tlsKeyFilePath='" + tlsKeyFilePath + '\'' +
                ", tlsAllowInsecureConnection=" + tlsAllowInsecureConnection +
                ", tlsHostnameVerificationEnable=" + tlsHostnameVerificationEnable +
                ", listenerName='" + listenerName + '\'' +
                ", operationTimeoutMillis=" + operationTimeoutMillis +
                ", statsIntervalMillis=" + statsIntervalMillis +
                ", ioThreads=" + ioThreads +
                ", listenerThreads=" + listenerThreads +
                ", connectionsPerBroker=" + connectionsPerBroker +
                ", useTcpNoDelay=" + useTcpNoDelay +
                ", memoryLimitKiloBytes=" + memoryLimitKiloBytes +
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
                ", enableTransaction=" + enableTransaction +
                ", enableProxy=" + enableProxy +
                ", proxyServiceUrl='" + proxyServiceUrl + '\'' +
                ", proxyProtocol=" + proxyProtocol +
                ", topic='" + topic + '\'' +
                ", producerName='" + producerName + '\'' +
                ", accessMode=" + accessMode +
                ", sendTimeoutMillis=" + sendTimeoutMillis +
                ", maxPendingMessages=" + maxPendingMessages +
                ", maxPendingMessagesAcrossPartitions=" + maxPendingMessagesAcrossPartitions +
                ", blockIfQueueFull=" + blockIfQueueFull +
                ", messageRoutingMode=" + messageRoutingMode +
                ", compressionType=" + compressionType +
                ", enableBatching=" + enableBatching +
                ", enableChunking=" + enableChunking +
                ", batchingMaxPublishDelayMillis=" + batchingMaxPublishDelayMillis +
                ", roundRobinRouterBatchingPartitionSwitchFrequency=" + roundRobinRouterBatchingPartitionSwitchFrequency +
                ", batchingMaxMessages=" + batchingMaxMessages +
                ", batchingMaxBytes=" + batchingMaxBytes +
                ", initialSequenceId=" + initialSequenceId +
                ", autoUpdatePartitions=" + autoUpdatePartitions +
                ", autoUpdatePartitionsIntervalMillis=" + autoUpdatePartitionsIntervalMillis +
                ", enableMultiSchema=" + enableMultiSchema +
                ", hashingSchemes=" + hashingSchemes +
                ", enableEncrypt=" + enableEncrypt +
                ", encryptionKey='" + encryptionKey + '\'' +
                ", producerCryptoFailureAction=" + producerCryptoFailureAction +
                '}';
    }

}
