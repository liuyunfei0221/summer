package com.blue.rocket.api.conf;

import org.apache.rocketmq.client.AccessChannel;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;

/**
 * producer params
 *
 * @author DarkBlue
 * @date 2021/8/9
 * @apiNote
 */
@SuppressWarnings({"AlibabaLowerCamelCaseVariableNaming", "SpellCheckingInspection"})
public class ProducerConfParams implements ProducerConf {

    private String namespace;

    private String namesrvAddr;

    private String producerGroup;

    private String createTopicKey;

    private String instanceName;

    private Integer defaultTopicQueueNums;

    private Integer sendMsgTimeout;

    private Integer compressMsgBodyOverHowmuch;

    private Integer retryTimesWhenSendFailed;

    private Integer retryTimesWhenSendAsyncFailed;

    private Boolean retryAnotherBrokerWhenNotStoreOK;

    private Integer maxMessageSize;

    private Boolean vipChannelEnabled;

    private ExecutorService asyncSenderExecutor;

    private ExecutorService callbackExecutor;

    private long[] latencyMax;

    private long[] notAvailableDuration;

    private Boolean sendMessageWithVIPChannel;

    private AccessChannel accessChannel;

    private Boolean useTLS;

    private String unitName;

    private Boolean unitMode;

    private Integer pollNameServerInterval;

    private Integer pullTimeDelayMillsWhenException;

    private Integer heartbeatBrokerInterval;

    private String clientIP;

    private Integer clientCallbackExecutorThreads;

    private Integer persistConsumerOffgetInterval;

    private Boolean sendLatencyFaultEnable;

    @Override
    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }

    @Override
    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    @Override
    public String getCreateTopicKey() {
        return createTopicKey;
    }

    public void setCreateTopicKey(String createTopicKey) {
        this.createTopicKey = createTopicKey;
    }

    @Override
    public String getInstanceName() {
        return instanceName;
    }

    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    @Override
    public Integer getDefaultTopicQueueNums() {
        return defaultTopicQueueNums;
    }

    public void setDefaultTopicQueueNums(Integer defaultTopicQueueNums) {
        this.defaultTopicQueueNums = defaultTopicQueueNums;
    }

    @Override
    public Integer getSendMsgTimeout() {
        return sendMsgTimeout;
    }

    public void setSendMsgTimeout(Integer sendMsgTimeout) {
        this.sendMsgTimeout = sendMsgTimeout;
    }

    @Override
    public Integer getCompressMsgBodyOverHowmuch() {
        return compressMsgBodyOverHowmuch;
    }

    public void setCompressMsgBodyOverHowmuch(Integer compressMsgBodyOverHowmuch) {
        this.compressMsgBodyOverHowmuch = compressMsgBodyOverHowmuch;
    }

    @Override
    public Integer getRetryTimesWhenSendFailed() {
        return retryTimesWhenSendFailed;
    }

    public void setRetryTimesWhenSendFailed(Integer retryTimesWhenSendFailed) {
        this.retryTimesWhenSendFailed = retryTimesWhenSendFailed;
    }

    @Override
    public Integer getRetryTimesWhenSendAsyncFailed() {
        return retryTimesWhenSendAsyncFailed;
    }

    public void setRetryTimesWhenSendAsyncFailed(Integer retryTimesWhenSendAsyncFailed) {
        this.retryTimesWhenSendAsyncFailed = retryTimesWhenSendAsyncFailed;
    }

    @Override
    public Boolean getRetryAnotherBrokerWhenNotStoreOK() {
        return retryAnotherBrokerWhenNotStoreOK;
    }

    public void setRetryAnotherBrokerWhenNotStoreOK(Boolean retryAnotherBrokerWhenNotStoreOK) {
        this.retryAnotherBrokerWhenNotStoreOK = retryAnotherBrokerWhenNotStoreOK;
    }

    @Override
    public Integer getMaxMessageSize() {
        return maxMessageSize;
    }

    public void setMaxMessageSize(Integer maxMessageSize) {
        this.maxMessageSize = maxMessageSize;
    }

    @Override
    public Boolean getVipChannelEnabled() {
        return vipChannelEnabled;
    }

    public void setVipChannelEnabled(Boolean vipChannelEnabled) {
        this.vipChannelEnabled = vipChannelEnabled;
    }

    @Override
    public ExecutorService getAsyncSenderExecutor() {
        return asyncSenderExecutor;
    }

    public void setAsyncSenderExecutor(ExecutorService asyncSenderExecutor) {
        this.asyncSenderExecutor = asyncSenderExecutor;
    }

    @Override
    public ExecutorService getCallbackExecutor() {
        return callbackExecutor;
    }

    public void setCallbackExecutor(ExecutorService callbackExecutor) {
        this.callbackExecutor = callbackExecutor;
    }

    @Override
    public long[] getLatencyMax() {
        return latencyMax;
    }

    public void setLatencyMax(long[] latencyMax) {
        this.latencyMax = latencyMax;
    }

    @Override
    public long[] getNotAvailableDuration() {
        return notAvailableDuration;
    }

    public void setNotAvailableDuration(long[] notAvailableDuration) {
        this.notAvailableDuration = notAvailableDuration;
    }

    @Override
    public Boolean getSendMessageWithVIPChannel() {
        return sendMessageWithVIPChannel;
    }

    public void setSendMessageWithVIPChannel(Boolean sendMessageWithVIPChannel) {
        this.sendMessageWithVIPChannel = sendMessageWithVIPChannel;
    }

    @Override
    public AccessChannel getAccessChannel() {
        return accessChannel;
    }

    public void setAccessChannel(AccessChannel accessChannel) {
        this.accessChannel = accessChannel;
    }

    @Override
    public Boolean getUseTLS() {
        return useTLS;
    }

    public void settUseTLS(Boolean useTLS) {
        this.useTLS = useTLS;
    }

    @Override
    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public Boolean getUnitMode() {
        return unitMode;
    }

    public void setUnitMode(Boolean unitMode) {
        this.unitMode = unitMode;
    }

    @Override
    public Integer getPollNameServerInterval() {
        return pollNameServerInterval;
    }

    public void setPollNameServerInterval(Integer pollNameServerInterval) {
        this.pollNameServerInterval = pollNameServerInterval;
    }

    @Override
    public Integer getPullTimeDelayMillsWhenException() {
        return pullTimeDelayMillsWhenException;
    }

    public void setPullTimeDelayMillsWhenException(Integer pullTimeDelayMillsWhenException) {
        this.pullTimeDelayMillsWhenException = pullTimeDelayMillsWhenException;
    }

    @Override
    public Integer getHeartbeatBrokerInterval() {
        return heartbeatBrokerInterval;
    }

    public void setHeartbeatBrokerInterval(Integer heartbeatBrokerInterval) {
        this.heartbeatBrokerInterval = heartbeatBrokerInterval;
    }

    @Override
    public String getClientIP() {
        return clientIP;
    }

    public void setClientIP(String clientIP) {
        this.clientIP = clientIP;
    }

    @Override
    public Integer getClientCallbackExecutorThreads() {
        return clientCallbackExecutorThreads;
    }

    public void setClientCallbackExecutorThreads(Integer clientCallbackExecutorThreads) {
        this.clientCallbackExecutorThreads = clientCallbackExecutorThreads;
    }

    @Override
    public Integer getPersistConsumerOffgetInterval() {
        return persistConsumerOffgetInterval;
    }

    public void setPersistConsumerOffgetInterval(Integer persistConsumerOffgetInterval) {
        this.persistConsumerOffgetInterval = persistConsumerOffgetInterval;
    }

    @Override
    public Boolean getSendLatencyFaultEnable() {
        return sendLatencyFaultEnable;
    }

    public void setSendLatencyFaultEnable(Boolean sendLatencyFaultEnable) {
        this.sendLatencyFaultEnable = sendLatencyFaultEnable;
    }

    @Override
    public String toString() {
        return "RocketProducerParams{" +
                "namespace='" + namespace + '\'' +
                ", namesrvAddr='" + namesrvAddr + '\'' +
                ", producerGroup='" + producerGroup + '\'' +
                ", createTopicKey='" + createTopicKey + '\'' +
                ", instanceName='" + instanceName + '\'' +
                ", defaultTopicQueueNums=" + defaultTopicQueueNums +
                ", sendMsgTimeout=" + sendMsgTimeout +
                ", compressMsgBodyOverHowmuch=" + compressMsgBodyOverHowmuch +
                ", retryTimesWhenSendFailed=" + retryTimesWhenSendFailed +
                ", retryTimesWhenSendAsyncFailed=" + retryTimesWhenSendAsyncFailed +
                ", retryAnotherBrokerWhenNotStoreOK=" + retryAnotherBrokerWhenNotStoreOK +
                ", maxMessageSize=" + maxMessageSize +
                ", vipChannelEnabled=" + vipChannelEnabled +
                ", asyncSenderExecutor=" + asyncSenderExecutor +
                ", callbackExecutor=" + callbackExecutor +
                ", latencyMax=" + Arrays.toString(latencyMax) +
                ", notAvailableDuration=" + Arrays.toString(notAvailableDuration) +
                ", sendMessageWithVIPChannel=" + sendMessageWithVIPChannel +
                ", accessChannel=" + accessChannel +
                ", useTLS=" + useTLS +
                ", unitName='" + unitName + '\'' +
                ", unitMode=" + unitMode +
                ", pollNameServerInterval=" + pollNameServerInterval +
                ", pullTimeDelayMillsWhenException=" + pullTimeDelayMillsWhenException +
                ", heartbeatBrokerInterval=" + heartbeatBrokerInterval +
                ", clientIP='" + clientIP + '\'' +
                ", clientCallbackExecutorThreads=" + clientCallbackExecutorThreads +
                ", persistConsumerOffgetInterval=" + persistConsumerOffgetInterval +
                ", sendLatencyFaultEnable=" + sendLatencyFaultEnable +
                '}';
    }

}
