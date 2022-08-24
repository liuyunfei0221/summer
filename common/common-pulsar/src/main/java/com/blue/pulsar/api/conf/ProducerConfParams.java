package com.blue.pulsar.api.conf;

import org.apache.pulsar.client.api.*;

import java.util.List;

/**
 * pulsar producer params
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "DuplicatedCode"})
public class ProducerConfParams implements ProducerConf {

    protected transient String topic;

    protected transient String producerName;

    protected ProducerAccessMode accessMode;

    protected Integer sendTimeoutMillis;

    protected Integer maxPendingMessages;

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

    public ProducerConfParams(String topic, String producerName, ProducerAccessMode accessMode, Integer sendTimeoutMillis, Integer maxPendingMessages,
                              Boolean blockIfQueueFull, MessageRoutingMode messageRoutingMode, CompressionType compressionType, Boolean enableBatching, Boolean enableChunking, Long batchingMaxPublishDelayMillis,
                              Integer roundRobinRouterBatchingPartitionSwitchFrequency, Integer batchingMaxMessages, Integer batchingMaxBytes, Long initialSequenceId, Boolean autoUpdatePartitions,
                              Integer autoUpdatePartitionsIntervalMillis, Boolean enableMultiSchema, List<HashingScheme> hashingSchemes, Boolean enableEncrypt, String encryptionKey, ProducerCryptoFailureAction producerCryptoFailureAction) {
        this.topic = topic;
        this.producerName = producerName;
        this.accessMode = accessMode;
        this.sendTimeoutMillis = sendTimeoutMillis;
        this.maxPendingMessages = maxPendingMessages;
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
                "topic='" + topic + '\'' +
                ", producerName='" + producerName + '\'' +
                ", accessMode=" + accessMode +
                ", sendTimeoutMillis=" + sendTimeoutMillis +
                ", maxPendingMessages=" + maxPendingMessages +
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
