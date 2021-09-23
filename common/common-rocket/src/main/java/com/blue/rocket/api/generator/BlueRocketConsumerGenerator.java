package com.blue.rocket.api.generator;

import com.blue.rocket.api.conf.ConsumerConf;
import com.blue.rocket.common.BlueRocketConsumer;

import java.util.function.Consumer;

/**
 * pulsar消费者构建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BlueRocketConsumerGenerator {

    /**
     * 构建消费端
     *
     * @param conf
     * @param consumer
     * @return
     */
    public static <T> BlueRocketConsumer<T> generateConsumer(ConsumerConf conf, Consumer<T> consumer) {
        assertParams(conf, consumer);
        return new BlueRocketConsumer<>(conf, consumer);
    }

    /**
     * 参数校验
     *
     * @param conf
     * @param consumer
     * @param <T>
     */
    private static <T> void assertParams(ConsumerConf conf, Consumer<T> consumer) {

    }


}
