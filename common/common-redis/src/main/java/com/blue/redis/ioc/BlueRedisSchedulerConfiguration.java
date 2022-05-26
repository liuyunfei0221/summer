package com.blue.redis.ioc;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;

import static com.blue.redis.api.generator.BlueRedisSchedulerGenerator.generateRedisScheduler;

/**
 * scheduler configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringFacetCodeInspection")
@ConditionalOnMissingBean(value = {Scheduler.class})
@Configuration
public class BlueRedisSchedulerConfiguration {

    @Bean
    public Scheduler scheduler() {
        return generateRedisScheduler();
    }

}
