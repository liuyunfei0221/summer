package com.blue.data.common.statistics;

import com.blue.base.component.common.BlueBeanDefinitionScanner;
import com.blue.data.common.statistics.inter.StatisticsCommand;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.lang.NonNull;
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
 * @date 2021/9/3
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Component
public final class StatisticsProcessor implements ResourceLoaderAware, ImportBeanDefinitionRegistrar, ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(StatisticsProcessor.class);

    private ResourceLoader resourceLoader;

    private static final String[] SCAN_PACKAGES = new String[]{"com.blue.data.common.statistics.impl"};
    private static final boolean USE_DEFAULT_FILTERS = false;

    private List<StatisticsCommand> commands;

    @Override
    public void setResourceLoader(@NonNull ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(@NonNull AnnotationMetadata importingClassMetadata, @NonNull BeanDefinitionRegistry registry) {
        BlueBeanDefinitionScanner scanner = new BlueBeanDefinitionScanner(registry, USE_DEFAULT_FILTERS);

        scanner.setResourceLoader(resourceLoader);
        scanner.addIncludeFilter(new AssignableTypeFilter(StatisticsCommand.class));
        scanner.doScan(SCAN_PACKAGES);

        ImportBeanDefinitionRegistrar.super.registerBeanDefinitions(importingClassMetadata, registry);
    }

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
