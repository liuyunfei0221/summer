package com.blue.media.component.event;

import com.blue.basic.model.event.DataEvent;
import com.blue.media.event.producer.RequestEventProducer;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static reactor.util.Loggers.getLogger;

/**
 * request event reporter
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public final class RequestEventReporter {

    private static final Logger LOGGER = getLogger(RequestEventReporter.class);

    private ExecutorService executorService;

    private RequestEventProducer requestEventProducer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public RequestEventReporter(ExecutorService executorService, RequestEventProducer requestEventProducer) {
        this.executorService = executorService;
        this.requestEventProducer = requestEventProducer;
    }

    /**
     * message sender
     */
    private final Consumer<DataEvent> MESSAGE_SENDER = event -> {
        LOGGER.info("MESSAGE_SENDER send, event = {}", event);
        try {
            executorService.execute(() ->
                    requestEventProducer.send(event));
        } catch (Exception e) {
            LOGGER.error("MESSAGE_SENDER send failed, event = {},e = {}", event, e);
        }
    };

    /**
     * report data
     *
     * @param dataEvent
     */
    public void report(DataEvent dataEvent) {
        try {
            MESSAGE_SENDER.accept(dataEvent);
            LOGGER.warn("report(DataEvent dataEvent) success, dataEvent = {}", dataEvent);
        } catch (Exception e) {
            LOGGER.error("report(DataEvent dataEvent) failed, dataEvent = {}, e = {}", dataEvent, e);
        }
    }

}