package com.blue.analyze.component.statistics;

import com.blue.analyze.component.statistics.inter.StatisticsCommand;
import com.blue.basic.model.event.DataEvent;
import org.slf4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNotNull;
import static java.util.Comparator.comparingInt;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.core.publisher.Mono.fromFuture;

/**
 * statistics chain processor
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
@Order(HIGHEST_PRECEDENCE)
public class StatisticsProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(StatisticsProcessor.class);

    private final ExecutorService executorService;

    public StatisticsProcessor(ExecutorService executorService) {
        this.executorService = executorService;
    }

    /**
     * statistics commands
     */
    private List<StatisticsCommand> commands;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, StatisticsCommand> beansOfType = applicationContext.getBeansOfType(StatisticsCommand.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("commands is empty");

        commands = beansOfType.values().stream()
                .sorted(comparingInt(c -> c.targetType().precedence)).collect(toList());

        LOGGER.info("commands = {}", commands);
    }

    private final Function<DataEvent, Boolean> PROCESSOR = dataEvent -> {
        if (isNotNull(dataEvent))
            for (StatisticsCommand command : commands) {
                command.analyze(dataEvent);
                command.summary(dataEvent);
            }

        return true;
    };

    /**
     * process and package data
     *
     * @param dataEvent
     */
    public Mono<Boolean> process(DataEvent dataEvent) {
        LOGGER.info("dataEvent = {}", dataEvent);
        return fromFuture(supplyAsync(() -> PROCESSOR.apply(dataEvent), executorService));
    }

}
