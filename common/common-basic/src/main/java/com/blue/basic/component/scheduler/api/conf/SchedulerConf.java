package com.blue.basic.component.scheduler.api.conf;

/**
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public interface SchedulerConf {

    Integer getThreadCap();

    Integer getQueuedTaskCap();

    String getThreadNamePre();

    Integer getTtlSeconds();

}
