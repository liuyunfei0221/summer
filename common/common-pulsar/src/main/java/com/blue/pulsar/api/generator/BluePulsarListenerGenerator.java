package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.component.BluePulsarBatchListener;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.ConsumerEventListener;
import org.apache.pulsar.client.api.ConsumerInterceptor;
import org.apache.pulsar.client.api.KeySharedPolicy;
import org.apache.pulsar.client.api.PulsarClient;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * pulsar consumer generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "ControlFlowStatementWithoutBraces", "AlibabaAvoidComplexCondition"})
public final class BluePulsarListenerGenerator {

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, null, null, null);
    }

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param consumerEventListener
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer, ConsumerEventListener consumerEventListener) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, consumerEventListener, null, null);
    }

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param consumerEventListener
     * @param interceptors
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer, ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, consumerEventListener, interceptors, null);
    }

    /**
     * generate listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<T> consumer, ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors,
                                                                                  KeySharedPolicy keySharedPolicy) {
        return new BluePulsarListener<>(pulsarClient, conf, consumer, consumerEventListener, interceptors, keySharedPolicy);
    }

    /**
     * generate batch listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param type
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type) {
        return new BluePulsarBatchListener<>(pulsarClient, conf, consumer, type, null, null, null);
    }

    /**
     * generate batch listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param type
     * @param consumerEventListener
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, ConsumerEventListener consumerEventListener) {
        return new BluePulsarBatchListener<>(pulsarClient, conf, consumer, type, consumerEventListener, null, null);
    }

    /**
     * generate batch listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param type
     * @param consumerEventListener
     * @param interceptors
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, ConsumerEventListener consumerEventListener,
                                                                                            List<ConsumerInterceptor<T>> interceptors) {
        return new BluePulsarBatchListener<>(pulsarClient, conf, consumer, type, consumerEventListener, interceptors, null);
    }

    /**
     * generate batch listener
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param type
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(PulsarClient pulsarClient, ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, ConsumerEventListener consumerEventListener,
                                                                                            List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return new BluePulsarBatchListener<>(pulsarClient, conf, consumer, type, consumerEventListener, interceptors, keySharedPolicy);
    }

}
