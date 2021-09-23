package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.common.BluePulsarConsumer;
import org.apache.pulsar.client.api.ConsumerEventListener;
import org.apache.pulsar.client.api.ConsumerInterceptor;
import org.apache.pulsar.client.api.KeySharedPolicy;
import org.apache.pulsar.client.api.MessageListener;

import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

/**
 * pulsar消费者构建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "ControlFlowStatementWithoutBraces", "AlibabaAvoidComplexCondition"})
public final class BluePulsarConsumerGenerator {

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer) {
        return new BluePulsarConsumer<>(conf, consumer, null, null, null, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, null, null, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, consumerEventListener, null, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener,
                                                                                  ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, consumerEventListener, interceptors, null);
    }

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener,
                                                                                  List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, consumerEventListener, interceptors, keySharedPolicy);
    }

}
