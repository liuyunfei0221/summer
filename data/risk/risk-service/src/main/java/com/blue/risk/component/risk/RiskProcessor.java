package com.blue.risk.component.risk;

import com.blue.basic.model.exps.BlueException;
import com.blue.risk.api.model.RiskAsserted;
import com.blue.risk.api.model.RiskEvent;
import com.blue.risk.api.model.RiskStrategyInfo;
import com.blue.risk.component.risk.inter.RiskHandler;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Comparator.comparingInt;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.*;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * risk hit processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
@Component
@Order(HIGHEST_PRECEDENCE)
public class RiskProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(RiskProcessor.class);

    private ExecutorService executorService;

    public RiskProcessor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * sorted risk handlers
     */
    private List<RiskHandler> riskHandlers;

    /**
     * risk handlers mapping
     */
    private Map<Integer, RiskHandler> handlerMapping;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, RiskHandler> beansOfType = applicationContext.getBeansOfType(RiskHandler.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("riskHandlers is empty");

        Collection<RiskHandler> handlers = beansOfType.values();

        riskHandlers = handlers.stream().sorted(comparingInt(rh -> rh.targetType().precedence)).collect(toList());
        handlerMapping = handlers.stream().collect(toMap(rh -> rh.targetType().identity, Function.identity(), (a, b) -> a));

        LOGGER.info("riskHandlers = {}", riskHandlers);
    }

    private final Function<RiskEvent, RiskAsserted>
            EVENT_HANDLER_CHAIN = event -> {
        RiskAsserted riskAsserted = new RiskAsserted();

        if (isNull(event))
            return riskAsserted;

        for (RiskHandler handler : riskHandlers) {
            if (!handler.isEnable())
                continue;

            riskAsserted = handler.handle(event, riskAsserted);

            if (riskAsserted.getInterrupt())
                break;
        }

        return riskAsserted;
    },
            EVENT_VALIDATOR_CHAIN = event -> {
                RiskAsserted riskAsserted = new RiskAsserted();

                if (isNull(event))
                    return riskAsserted;

                for (RiskHandler handler : riskHandlers)
                    riskAsserted = handler.validate(event, riskAsserted);

                return riskAsserted;
            };

    private final Function<RiskEvent, Mono<RiskAsserted>>
            EVENT_HANDLER = event ->
            fromFuture(supplyAsync(() -> EVENT_HANDLER_CHAIN.apply(event), executorService)),
            EVENT_VALIDATOR = event ->
                    fromFuture(supplyAsync(() -> EVENT_VALIDATOR_CHAIN.apply(event), executorService));

    private final Consumer<RiskStrategyInfo> STRATEGY_ASSERTER = riskStrategyInfo -> {
        if (isNull(riskStrategyInfo))
            throw new BlueException(EMPTY_PARAM);
        riskStrategyInfo.asserts();

        ofNullable(riskStrategyInfo.getType())
                .map(handlerMapping::get)
                .ifPresent(handler -> handler.assertStrategy(riskStrategyInfo));
    };

    private final Function<RiskStrategyInfo, Mono<Boolean>> STRATEGY_UPDATER = riskStrategyInfo -> {
        if (isNull(riskStrategyInfo))
            throw new BlueException(EMPTY_PARAM);
        riskStrategyInfo.asserts();

        STRATEGY_ASSERTER.accept(riskStrategyInfo);

        return just(ofNullable(riskStrategyInfo.getType())
                .map(handlerMapping::get)
                .orElseThrow(() -> new BlueException(INVALID_PARAM))
                .updateStrategy(riskStrategyInfo));
    };

    /**
     * analyze event
     *
     * @param riskEvent
     * @return
     */
    public Mono<RiskAsserted> handle(RiskEvent riskEvent) {
        LOGGER.info("riskEvent = {}", riskEvent);
        return EVENT_HANDLER.apply(riskEvent);
    }

    /**
     * validate event
     *
     * @param riskEvent
     * @return
     */
    public Mono<RiskAsserted> validate(RiskEvent riskEvent) {
        LOGGER.info("riskEvent = {}", riskEvent);
        return EVENT_VALIDATOR.apply(riskEvent);
    }

    /**
     * assert strategy
     *
     * @param riskStrategyInfo
     */
    public void assertStrategy(RiskStrategyInfo riskStrategyInfo) {
        LOGGER.info("riskStrategyInfo = {}", riskStrategyInfo);
        STRATEGY_ASSERTER.accept(riskStrategyInfo);
    }

    /**
     * update strategy
     *
     * @param riskStrategyInfo
     * @return
     */
    public Mono<Boolean> updateStrategy(RiskStrategyInfo riskStrategyInfo) {
        LOGGER.info("riskStrategyInfo = {}", riskStrategyInfo);
        return STRATEGY_UPDATER.apply(riskStrategyInfo);
    }

    /**
     * select active risk strategy type
     *
     * @return
     */
    public Set<Integer> selectActiveTypes() {
        return handlerMapping.entrySet().stream()
                .filter(entry -> entry.getValue().isEnable())
                .map(Map.Entry::getKey)
                .collect(toSet());
    }

}
