package com.blue.rocket.common;

import com.blue.rocket.api.conf.ProducerConf;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar发送者
 *
 * @author DarkBlue
 */
@SuppressWarnings("DuplicatedCode")
public final class BlueRocketProducerCreator extends DefaultMQProducer {

    private static final Logger LOGGER = getLogger(BlueRocketProducerCreator.class);

    public static DefaultMQProducer createDefaultProducer(ProducerConf conf) {

        LOGGER.info("createDefaultProducer(RocketProducerConf conf), conf = {}", conf);

        DefaultMQProducer rocketProducer = new DefaultMQProducer();

        rocketProducer.setNamespace(conf.getNamespace());
        rocketProducer.setNamesrvAddr(conf.getNamesrvAddr());
        rocketProducer.setProducerGroup(conf.getProducerGroup());
        rocketProducer.setCreateTopicKey(conf.getCreateTopicKey());
        rocketProducer.setInstanceName(conf.getInstanceName());
        rocketProducer.setDefaultTopicQueueNums(conf.getDefaultTopicQueueNums());
        rocketProducer.setSendMsgTimeout(conf.getSendMsgTimeout());
        rocketProducer.setCompressMsgBodyOverHowmuch(conf.getCompressMsgBodyOverHowmuch());
        rocketProducer.setRetryTimesWhenSendFailed(conf.getRetryTimesWhenSendFailed());
        rocketProducer.setRetryTimesWhenSendAsyncFailed(conf.getRetryTimesWhenSendAsyncFailed());
        rocketProducer.setRetryAnotherBrokerWhenNotStoreOK(conf.getRetryAnotherBrokerWhenNotStoreOK());
        rocketProducer.setMaxMessageSize(conf.getMaxMessageSize());
        rocketProducer.setVipChannelEnabled(conf.getVipChannelEnabled());
        rocketProducer.setAsyncSenderExecutor(conf.getAsyncSenderExecutor());
        rocketProducer.setCallbackExecutor(conf.getCallbackExecutor());
        rocketProducer.setLatencyMax(conf.getLatencyMax());
        rocketProducer.setNotAvailableDuration(conf.getNotAvailableDuration());
        rocketProducer.setSendMessageWithVIPChannel(conf.getSendMessageWithVIPChannel());
        rocketProducer.setAccessChannel(conf.getAccessChannel());
        rocketProducer.setUseTLS(conf.getUseTLS());
        rocketProducer.setUnitName(conf.getUnitName());
        rocketProducer.setUnitMode(conf.getUnitMode());
        rocketProducer.setPollNameServerInterval(conf.getPollNameServerInterval());
        rocketProducer.setPullTimeDelayMillsWhenException(conf.getPullTimeDelayMillsWhenException());
        rocketProducer.setHeartbeatBrokerInterval(conf.getHeartbeatBrokerInterval());
        rocketProducer.setClientIP(conf.getClientIP());
        rocketProducer.setClientCallbackExecutorThreads(conf.getClientCallbackExecutorThreads());
        rocketProducer.setPersistConsumerOffsetInterval(conf.getPersistConsumerOffgetInterval());
        rocketProducer.setSendLatencyFaultEnable(conf.getSendLatencyFaultEnable());

        return rocketProducer;
    }

}
