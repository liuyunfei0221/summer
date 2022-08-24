package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.component.BluePulsarConsumer;
import org.apache.pulsar.client.api.*;

import java.io.Serializable;
import java.util.List;


/**
 * pulsar consumer generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BluePulsarConsumerGenerator {

    /**
     * generate consumer
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param messageListener
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarConsumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, java.util.function.Consumer<T> consumer, ConsumerEventListener consumerEventListener,
                                                                                  List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy, MessageListener<T> messageListener) {
        return new BluePulsarConsumer<>(pulsarClient, conf, consumer, consumerEventListener, interceptors, keySharedPolicy, messageListener);
    }

}
