package com.blue.rocket.common;

import com.blue.rocket.api.conf.ProducerConf;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * rocket ordered producer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"DuplicatedCode", "unused"})
public final class BlueRocketTransProducerCreator extends TransactionMQProducer {

    private static final Logger LOGGER = getLogger(BlueRocketTransProducerCreator.class);

    public static TransactionMQProducer createTransProducer(ProducerConf conf, TransactionListener transactionListener, ExecutorService executorService) {

        LOGGER.info("createDefaultProducer(RocketProducerConf conf), conf = {}", conf);

        TransactionMQProducer rocketProducer = new TransactionMQProducer();

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
        rocketProducer.setTransactionListener(transactionListener);
        rocketProducer.setExecutorService(executorService);
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
