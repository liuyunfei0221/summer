package com.blue.analyze.component.statistics;

import com.blue.analyze.component.statistics.inter.StatisticsCommand;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;

import static java.util.Comparator.comparingInt;
import static java.util.stream.Collectors.toList;
import static reactor.util.Loggers.getLogger;

/**
 * statistics chain processor
 *
 * @author liuyunfei
 */
@SuppressWarnings("JavaDoc")
@Component
public class StatisticsProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(StatisticsProcessor.class);

    /**
     * statistics commands
     */
    private List<StatisticsCommand> commands;

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
     * @param data
     */
    public void process(Map<String, String> data) {
        for (StatisticsCommand command : commands) {
            command.analyzeAndPackage(data);
            command.summary(data);
        }
    }

}
