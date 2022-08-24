package com.blue.redis.ioc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import reactor.core.scheduler.Scheduler;

import static com.blue.redis.api.generator.BlueRedisSchedulerGenerator.generateRedisScheduler;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * scheduler configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringFacetCodeInspection")
@ConditionalOnMissingBean(value = {Scheduler.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueRedisSchedulerConfiguration {

    @Bean
    Scheduler scheduler() {
        return generateRedisScheduler();
    }

}
