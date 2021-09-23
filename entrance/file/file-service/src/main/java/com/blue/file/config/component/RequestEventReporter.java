package com.blue.file.config.component;

import com.blue.base.model.event.data.DataEvent;
import com.blue.file.config.mq.producer.RequestEventProducer;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static reactor.util.Loggers.getLogger;

/**
 * 请求数据上报器
 *
 * @author DarkBlue
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
     * 信息发送器
     */
    private final Consumer<DataEvent> MESSAGE_SENDER = event -> {
        LOGGER.info("MESSAGE_SENDER send, event = {}", event);
        try {
            executorService.submit(() ->
                    requestEventProducer.send(event));
        } catch (Exception e) {
            LOGGER.error("MESSAGE_SENDER send failed, event = {},e = {}", event, e);
        }
    };

    /**
     * 上报数据
     *
     * @param dataEvent
     */
    public void report(DataEvent dataEvent) {
        try {
            MESSAGE_SENDER.accept(dataEvent);
            LOGGER.warn("上报数据成功, dataEvent = {}", dataEvent);
        } catch (Exception e) {
            LOGGER.error("上报数据失败, dataEvent = {}, e = {}", dataEvent, e);
        }
    }

}
