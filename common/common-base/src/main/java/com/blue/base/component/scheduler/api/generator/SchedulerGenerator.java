package com.blue.base.component.scheduler.api.generator;

import com.blue.base.component.scheduler.api.conf.SchedulerConf;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import static java.util.Optional.ofNullable;


/**
 * scheduler generator
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
public class SchedulerGenerator {

    public static Scheduler generateScheduler(SchedulerConf schedulerConf) {
        return ofNullable(schedulerConf).map(SchedulerConf::getScheduler).orElseGet(Schedulers::boundedElastic);
    }

}
