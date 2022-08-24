package com.blue.basic.component.scheduler.ioc;

import com.blue.basic.component.scheduler.api.conf.SchedulerConf;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import reactor.core.scheduler.Scheduler;

import static com.blue.basic.component.scheduler.api.generator.SchedulerGenerator.generateScheduler;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * scheduler configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {SchedulerConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueSchedulerConfiguration {

    @Bean
    Scheduler scheduler(SchedulerConf schedulerConf) {
        return generateScheduler(schedulerConf);
    }

}
