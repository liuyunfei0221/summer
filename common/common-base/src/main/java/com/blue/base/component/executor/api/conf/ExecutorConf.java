package com.blue.base.component.executor.api.conf;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * global executor conf
 *
 * @author liuyunfei
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface ExecutorConf {

    Integer getCorePoolSize();

    Integer getMaximumPoolSize();

    Long getKeepAliveSeconds();

    Integer getBlockingQueueCapacity();

    String getThreadNamePre();

    RejectedExecutionHandler getRejectedExecutionHandler();

}
