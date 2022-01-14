package com.blue.base.component.scheduler.api.conf;

import reactor.core.scheduler.Scheduler;


/**
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface SchedulerConf {

    Scheduler getScheduler();

}
