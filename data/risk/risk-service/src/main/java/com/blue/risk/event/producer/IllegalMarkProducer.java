package com.blue.risk.event.producer;

import com.blue.basic.component.lifecycle.inter.BlueLifecycle;
import com.blue.basic.model.event.IllegalMarkEvent;
import com.blue.pulsar.common.BluePulsarProducer;
import com.blue.risk.config.blue.BlueProducerConfig;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.basic.constant.common.BlueTopic.ILLEGAL_MARK;
import static com.blue.pulsar.api.generator.BluePulsarProducerGenerator.generateProducer;
import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static reactor.util.Loggers.getLogger;


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

    public IllegalMarkProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.pulsarProducer = generateProducer(blueProducerConfig.getByKey(ILLEGAL_MARK.name), IllegalMarkEvent.class);
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
