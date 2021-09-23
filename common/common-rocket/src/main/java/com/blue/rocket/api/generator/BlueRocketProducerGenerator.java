package com.blue.rocket.api.generator;

import com.blue.rocket.api.conf.ProducerConf;
import com.blue.rocket.common.BlueRocketProducerCreator;
import com.blue.rocket.common.BlueRocketTransProducerCreator;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;

import java.util.concurrent.ExecutorService;

/**
 * pulsar消费者构建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public final class BlueRocketProducerGenerator {

    /**
     * 构建生产端
     *
     * @param conf
     * @return
     */
    public static DefaultMQProducer generateConsumer(ProducerConf conf) {
        assertParams(conf);
        return BlueRocketProducerCreator.createDefaultProducer(conf);
    }

    /**
     * 构建生产端
     *
     * @param conf
     * @return
     */
    public static TransactionMQProducer generateConsumer(ProducerConf conf, TransactionListener transactionListener, ExecutorService executorService) {
        return BlueRocketTransProducerCreator.createTransProducer(conf, transactionListener, executorService);
    }

    /**
     * 参数校验
     *
     * @param conf
     * @param <T>
     */
    private static <T> void assertParams(ProducerConf conf) {

    }


}
