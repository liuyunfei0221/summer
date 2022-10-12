package com.blue.analyze.component.statistics;

import com.blue.analyze.component.statistics.inter.StatisticsCommand;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.event.DataEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static java.util.Comparator.comparingInt;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.stream.Collectors.toList;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

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

    private final Function<Map<String, String>, Boolean> PROCESSOR = data -> {
        if (BlueChecker.isNotEmpty(data))
            for (StatisticsCommand command : commands) {
                command.analyzeAndPackage(data);
                command.summary(data);
            }

        return true;
    };

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        commands = applicationContext.getBeansOfType(StatisticsCommand.class)
                .values()
                .stream().sorted(comparingInt(StatisticsCommand::getPrecedence)).collect(toList());

        LOGGER.info("commands = {}", commands);
    }

    /**
     * process and package data
     *
     * @param dataEvent
     */
    public Mono<Boolean> process(DataEvent dataEvent) {
        LOGGER.info("Mono<Boolean> process(DataEvent dataEvent), dataEvent = {}", dataEvent);
        return fromFuture(supplyAsync(() -> PROCESSOR.apply(dataEvent.getEntries()), executorService));
    }

}
