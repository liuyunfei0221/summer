package com.blue.risk.component.risk;

import com.blue.basic.model.event.DataEvent;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.component.risk.inter.RiskHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static java.util.Comparator.comparingInt;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * risk hit processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(HIGHEST_PRECEDENCE)
public class RiskProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(RiskProcessor.class);

    private ExecutorService executorService;

    public RiskProcessor(ExecutorService executorService) {
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
            throw new RuntimeException("riskHandlers is empty");

        riskHandlers = beansOfType.values().stream()
                .sorted(comparingInt(rh -> rh.targetType().precedence))
                .collect(toList());
    }

    private final Function<DataEvent, RiskAsserted> EVENT_HANDLER_CHAIN = event -> {
        RiskAsserted riskAsserted = new RiskAsserted();

        if (isNull(event))
            return riskAsserted;

        for (RiskHandler handler : riskHandlers) {
            riskAsserted = handler.handleEvent(event, riskAsserted);

            if (riskAsserted.getInterrupt())
                break;
        }


        return riskAsserted;
    };

    private final Function<DataEvent, CompletableFuture<RiskAsserted>> EVENT_HANDLER = event ->
            supplyAsync(() -> EVENT_HANDLER_CHAIN.apply(event), executorService);

    /**
     * analyze event
     *
     * @param dataEvent
     * @return
     */
    public Mono<RiskAsserted> analyzeEvent(DataEvent dataEvent) {
        LOGGER.info("dataEvent = {}", dataEvent);
        return fromFuture(EVENT_HANDLER.apply(dataEvent));
    }

}
