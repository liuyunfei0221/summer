package com.blue.risk.service.impl;

import com.blue.base.model.base.DataEvent;
import com.blue.base.model.exps.BlueException;
import com.blue.risk.component.risk.inter.RiskHandler;
import com.blue.risk.service.inter.RiskService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.Comparator.comparingInt;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * risk analyse service impl
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
@Service
public class RiskServiceImpl implements RiskService, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(RiskServiceImpl.class);

    private ExecutorService executorService;

    public RiskServiceImpl(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * sorted event handlers
     */
    private List<RiskHandler> riskHandlers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, RiskHandler> beansOfType = applicationContext.getBeansOfType(RiskHandler.class);
        if (isEmpty(beansOfType))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "riskHandlers is empty");

        riskHandlers = beansOfType.values().stream()
                .sorted(comparingInt(RiskHandler::precedence))
                .collect(toList());
    }

    private final Consumer<DataEvent> EVENT_HANDLER_CHAIN = event -> {
        for (RiskHandler handler : riskHandlers)
            handler.handleEvent(event);
    };

    private final Consumer<DataEvent> EVENT_HANDLER = event ->
            executorService.execute(() -> EVENT_HANDLER_CHAIN.accept(event));

    /**
     * analyze event
     *
     * @param dataEvent
     * @return
     */
    @Override
    public Mono<Boolean> analyzeEvent(DataEvent dataEvent) {
        LOGGER.info(" void analyzeEvent(DataEvent dataEvent), dataEvent = {}", dataEvent);

        return fromFuture(supplyAsync(() -> {
            EVENT_HANDLER.accept(dataEvent);
            return true;
        }, executorService));
    }

}
