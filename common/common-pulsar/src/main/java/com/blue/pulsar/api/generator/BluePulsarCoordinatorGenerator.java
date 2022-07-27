package com.blue.pulsar.api.generator;

import com.blue.pulsar.api.conf.ClientConf;
import com.blue.pulsar.common.BluePulsarTransConsumerCoordinator;
import com.blue.pulsar.common.BluePulsarTransProducerCoordinator;
import org.apache.pulsar.client.api.PulsarClient;


/**
 * pulsar coordinator generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
public final class BluePulsarCoordinatorGenerator {

    /**
     * generate producer coordinator
     *
     * @param conf
     * @return
     */
    public static BluePulsarTransProducerCoordinator generateProducerCoordinator(ClientConf conf) {
        return new BluePulsarTransProducerCoordinator(conf);
    }

    /**
     * generate producer coordinator
     *
     * @param pulsarClient
     * @return
     */
    public static BluePulsarTransProducerCoordinator generateProducerCoordinator(PulsarClient pulsarClient) {
        return new BluePulsarTransProducerCoordinator(pulsarClient);
    }

    /**
     * generate consumer coordinator
     *
     * @param conf
     * @return
     */
    public static BluePulsarTransConsumerCoordinator generateConsumerCoordinator(ClientConf conf) {
        return new BluePulsarTransConsumerCoordinator(conf);
    }

    /**
     * generate consumer coordinator
     *
     * @param pulsarClient
     * @return
     */
    public static BluePulsarTransConsumerCoordinator generateConsumerCoordinator(PulsarClient pulsarClient) {
        return new BluePulsarTransConsumerCoordinator(pulsarClient);
    }

}
