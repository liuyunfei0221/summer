package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.component.BluePulsarBatchListener;
import com.blue.pulsar.component.BluePulsarListener;
import org.apache.pulsar.client.api.ConsumerEventListener;
import org.apache.pulsar.client.api.ConsumerInterceptor;
import org.apache.pulsar.client.api.KeySharedPolicy;
import org.apache.pulsar.client.api.MessageListener;

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
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(ConsumerConf conf, Consumer<T> consumer) {
        return new BluePulsarListener<>(conf, consumer, null, null, null, null);
    }

    /**
     * generate listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener) {
        return new BluePulsarListener<>(conf, consumer, messageListener, null, null, null);
    }

    /**
     * generate listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener) {
        return new BluePulsarListener<>(conf, consumer, messageListener, consumerEventListener, null, null);
    }

    /**
     * generate listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener,
                                                                                  ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors) {
        return new BluePulsarListener<>(conf, consumer, messageListener, consumerEventListener, interceptors, null);
    }

    /**
     * generate listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarListener<T> generateListener(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener,
                                                                                  List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return new BluePulsarListener<>(conf, consumer, messageListener, consumerEventListener, interceptors, keySharedPolicy);
    }
    
    /**
     * generate batch listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type) {
        return new BluePulsarBatchListener<>(conf, consumer, type, null, null, null, null);
    }

    /**
     * generate batch listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, MessageListener<T> messageListener) {
        return new BluePulsarBatchListener<>(conf, consumer, type, messageListener, null, null, null);
    }

    /**
     * generate batch listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener) {
        return new BluePulsarBatchListener<>(conf, consumer, type, messageListener, consumerEventListener, null, null);
    }

    /**
     * generate batch listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, MessageListener<T> messageListener,
                                                                                            ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors) {
        return new BluePulsarBatchListener<>(conf, consumer, type, messageListener, consumerEventListener, interceptors, null);
    }

    /**
     * generate batch listener
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarBatchListener<T> generateBatchListener(ConsumerConf conf, Consumer<List<T>> consumer, Class<T> type, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener,
                                                                                            List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return new BluePulsarBatchListener<>(conf, consumer, type, messageListener, consumerEventListener, interceptors, keySharedPolicy);
    }

}
