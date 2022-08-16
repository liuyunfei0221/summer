package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.component.BluePulsarConsumer;
import org.apache.pulsar.client.api.ConsumerEventListener;
import org.apache.pulsar.client.api.ConsumerInterceptor;
import org.apache.pulsar.client.api.KeySharedPolicy;
import org.apache.pulsar.client.api.MessageListener;

import java.io.Serializable;
import java.util.List;


/**
 * pulsar consumer generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
public final class BluePulsarConsumerGenerator {

    /**
     * generate consumer
     *
     * @param conf
     * @param consumer
     * @param messageListener
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarConsumer<T> generateConsumer(ConsumerConf conf, java.util.function.Consumer<T> consumer, MessageListener<T> messageListener, ConsumerEventListener consumerEventListener,
                                                                                  List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        return new BluePulsarConsumer<>(conf, consumer, messageListener, consumerEventListener, interceptors, keySharedPolicy);
    }

}
