package com.blue.base.component.scheduler.api.conf;

/**
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface SchedulerConf {

    Integer getThreadCap();

    Integer getQueuedTaskCap();

    String getThreadNamePre();

    Integer getTtlSeconds();

}
