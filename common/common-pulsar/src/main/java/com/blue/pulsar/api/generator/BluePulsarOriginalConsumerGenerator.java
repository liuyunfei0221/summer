package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.common.PulsarCommonsGenerator;
import org.apache.pulsar.client.api.*;

import java.util.List;

/**
 * pulsar original consumer generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaMethodTooLong", "AlibabaUndefineMagicConstant", "AlibabaAvoidComplexCondition", "unused"})
public final class BluePulsarOriginalConsumerGenerator {

    /**
     * generate consumer
     *
     * @param pulsarClient
     * @param conf
     * @param clz
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, Class<T> clz, ConsumerEventListener consumerEventListener,
                                                   List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return PulsarCommonsGenerator.generateConsumer(pulsarClient, conf, clz, consumerEventListener, interceptors, keySharedPolicy);
    }

    /**
     * generate consumer
     *
     * @param pulsarClient
     * @param conf
     * @param clz
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param messageListener
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, Class<T> clz, ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors,
                                                   KeySharedPolicy keySharedPolicy, MessageListener<T> messageListener) {
        return PulsarCommonsGenerator.generateConsumer(pulsarClient, conf, clz, consumerEventListener, interceptors, keySharedPolicy, messageListener);
    }

}
