package com.blue.rocket.api.generator;

import com.blue.rocket.api.conf.ConsumerConf;
import com.blue.rocket.common.BlueRocketConsumer;

import java.util.function.Consumer;

/**
 * rocket consumer generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BlueRocketConsumerGenerator {

    /**
     * generate consumer
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
     * assert params
     *
     * @param conf
     * @param consumer
     * @param <T>
     */
    private static <T> void assertParams(ConsumerConf conf, Consumer<T> consumer) {

    }


}
