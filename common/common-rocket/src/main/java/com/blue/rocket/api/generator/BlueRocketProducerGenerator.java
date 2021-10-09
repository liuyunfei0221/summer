package com.blue.rocket.api.generator;

import com.blue.rocket.api.conf.ProducerConf;
import com.blue.rocket.common.BlueRocketProducerCreator;
import com.blue.rocket.common.BlueRocketTransProducerCreator;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;

import java.util.concurrent.ExecutorService;

/**
 * rocket producer generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueRocketProducerGenerator {

    /**
     * generate producer
     *
     * @param conf
     * @return
     */
    public static DefaultMQProducer generateConsumer(ProducerConf conf) {
        assertParams(conf);
        return BlueRocketProducerCreator.createDefaultProducer(conf);
    }

    /**
     * generate transaction producer
     *
     * @param conf
     * @return
     */
    public static TransactionMQProducer generateConsumer(ProducerConf conf, TransactionListener transactionListener, ExecutorService executorService) {
        return BlueRocketTransProducerCreator.createTransProducer(conf, transactionListener, executorService);
    }

    /**
     * assert params
     *
     * @param conf
     * @param <T>
     */
    private static <T> void assertParams(ProducerConf conf) {
        if (conf == null)
            throw new RuntimeException("conf can't be null");
    }


}
