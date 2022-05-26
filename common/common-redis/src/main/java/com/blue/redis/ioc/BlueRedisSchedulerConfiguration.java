package com.blue.redis.ioc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import reactor.core.scheduler.Scheduler;

import static com.blue.redis.api.generator.BlueRedisSchedulerGenerator.generateRedisScheduler;

/**
 * scheduler configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringFacetCodeInspection")
@ConditionalOnMissingBean(value = {Scheduler.class})
@AutoConfiguration
public class BlueRedisSchedulerConfiguration {

    @Bean
    Scheduler scheduler() {
        return generateRedisScheduler();
    }

}
