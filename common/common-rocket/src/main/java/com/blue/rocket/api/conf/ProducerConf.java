package com.blue.rocket.api.conf;

import org.apache.rocketmq.client.AccessChannel;

import java.util.concurrent.ExecutorService;

/**
 * producer conf
 *
 * @author DarkBlue
 * @date 2021/8/9
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaLowerCamelCaseVariableNaming"})
public interface ProducerConf {

    String getNamespace();

    String getNamesrvAddr();

    String getProducerGroup();

    String getCreateTopicKey();

    String getInstanceName();

    Integer getDefaultTopicQueueNums();

    Integer getSendMsgTimeout();

    Integer getCompressMsgBodyOverHowmuch();

    Integer getRetryTimesWhenSendFailed();

    Integer getRetryTimesWhenSendAsyncFailed();

    Boolean getRetryAnotherBrokerWhenNotStoreOK();

    Integer getMaxMessageSize();

    Boolean getVipChannelEnabled();

    ExecutorService getAsyncSenderExecutor();

    ExecutorService getCallbackExecutor();

    long[] getLatencyMax();

    long[] getNotAvailableDuration();

    Boolean getSendMessageWithVIPChannel();

    AccessChannel getAccessChannel();

    Boolean getUseTLS();

    String getUnitName();

    Boolean getUnitMode();

    Integer getPollNameServerInterval();

    Integer getPullTimeDelayMillsWhenException();

    Integer getHeartbeatBrokerInterval();

    String getClientIP();

    Integer getClientCallbackExecutorThreads();

    Integer getPersistConsumerOffgetInterval();

    Boolean getSendLatencyFaultEnable();

}
