package com.blue.base.component.scheduler.ioc;

import com.blue.base.component.scheduler.api.conf.SchedulerConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.scheduler.Scheduler;

import static com.blue.base.component.scheduler.api.generator.SchedulerGenerator.generateScheduler;

/**
 * rest configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {SchedulerConf.class})
@Configuration
public class BlueSchedulerConfiguration {

    @Bean
    public Scheduler scheduler(SchedulerConf schedulerConf) {
        return generateScheduler(schedulerConf);
    }

}
