package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ProducerConf;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.BatcherBuilder;
import org.apache.pulsar.client.api.MessageRouter;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;

import java.io.Serializable;
import java.util.List;


/**
 * pulsar producer generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc"})
public final class BluePulsarProducerGenerator {

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz) {
        return new BluePulsarProducer<>(conf, clz, null, null, null);
    }

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz, MessageRouter messageRouter) {
        return new BluePulsarProducer<>(conf, clz, messageRouter, null, null);
    }

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param messageRouter
     * @param batcherBuilder
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz, MessageRouter messageRouter,
                                                                                  BatcherBuilder batcherBuilder) {
        return new BluePulsarProducer<>(conf, clz, messageRouter, batcherBuilder, null);
    }

    /**
     * generate producer
     *
     * @param conf
     * @param clz
     * @param messageRouter
     * @param batcherBuilder
     * @param interceptors
     * @param <T>
     * @return
     */
    public static <T extends Serializable> BluePulsarProducer<T> generateProducer(ProducerConf conf, Class<T> clz, MessageRouter messageRouter,
                                                                                  BatcherBuilder batcherBuilder, List<ProducerInterceptor> interceptors) {
        return new BluePulsarProducer<>(conf, clz, messageRouter, batcherBuilder, interceptors);
    }

}
