package com.blue.portal.config.blue;

import com.blue.base.component.scheduler.api.conf.SchedulerConfParams;
import org.springframework.stereotype.Component;
import reactor.core.scheduler.Scheduler;

import static reactor.core.scheduler.Schedulers.boundedElastic;

/**
 * executor config
 *
 * @author DarkBlue
 */
@Component
public class BlueSchedulerConfig extends SchedulerConfParams {

    @Override
    public Scheduler getScheduler() {
        return boundedElastic();
    }

}
