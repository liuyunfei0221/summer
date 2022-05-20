package com.blue.lake.event.producer;

import com.blue.base.component.lifecycle.inter.BlueLifecycle;
import com.blue.base.model.common.IllegalMarkEvent;
import com.blue.lake.config.blue.BlueProducerConfig;
import com.blue.pulsar.common.BluePulsarProducer;
import org.apache.pulsar.client.api.MessageId;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.constant.base.BlueTopic.ILLEGAL_MARK;
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

    private final BluePulsarProducer<IllegalMarkEvent> illegalMarkProducer;

    public IllegalMarkProducer(ExecutorService executorService, BlueProducerConfig blueProducerConfig) {
        this.executorService = executorService;
        this.illegalMarkProducer = generateProducer(blueProducerConfig.getByKey(ILLEGAL_MARK.name), IllegalMarkEvent.class);
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
        LOGGER.warn("illegalMarkProducer start...");
    }

    @Override
    public void stop() {
        this.illegalMarkProducer.shutdown();
        LOGGER.warn("illegalMarkProducer shutdown...");
    }

    /**
     * send message
     *
     * @param illegalMarkEvent
     */
    public void send(IllegalMarkEvent illegalMarkEvent) {
        CompletableFuture<MessageId> future = illegalMarkProducer.sendAsync(illegalMarkEvent);
        LOGGER.info("illegalMarkProducer send, illegalMarkEvent = {}", illegalMarkEvent);
        Consumer<MessageId> c = messageId ->
                LOGGER.info("illegalMarkProducer send success, illegalMarkEvent = {}, messageId = {}", illegalMarkEvent, messageId);
        future.thenAcceptAsync(c, executorService);
    }

}
