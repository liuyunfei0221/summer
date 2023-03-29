package com.blue.lake.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.lake.config.blue.BlueProducerConfig;
import com.blue.pulsar.component.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import org.apache.pulsar.client.api.PulsarClient;
import org.slf4j.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ILLEGAL_MARK;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.slf4j.LoggerFactory.getLogger;


/**
 * illegal mark producer
 *
 * @author liuyunfei
 */
@SuppressWarnings({"Duplicates", "JavaDoc", "unused"})
public final class IllegalMarkProducer implements BlueLifecycle {

    private static final Logger LOGGER = getLogger(IllegalMarkProducer.class);

    private final ExecutorService executorService;

    private final BluePulsarProducer<IllegalMarkEvent> pulsarProducer;

    public IllegalMarkProducer(PulsarClient pulsarClient, ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(pulsarClient, blueProducerConfig.getByKey(ILLEGAL_MARK.name), IllegalMarkEvent.class);
    }

    @Override
    public int startPrecedence() {
        return MIN_VALUE;
    }

    @Override
    public int stopPrecedence() {
        return MAX_VALUE;
    }

    @Override
    public void start() {
        LOGGER.warn("pulsarProducer start...");
    }

    @Override
    public void stop() {
        this.pulsarProducer.flush();
        this.pulsarProducer.close();
        LOGGER.warn("pulsarProducer shutdown...");
    }

    /**
     * send message
     *
     * @param illegalMarkEvent
     */
    public void send(IllegalMarkEvent illegalMarkEvent) {
        CompletableFuture<MessageId> future = pulsarProducer.sendAsync(illegalMarkEvent);
        LOGGER.info("pulsarProducer send, illegalMarkEvent = {}", illegalMarkEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("pulsarProducer send success, illegalMarkEvent = {}, messageId = {}", illegalMarkEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
