package com.blue.basic.component.executor.api.conf;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * global executor conf param
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public class ExecutorConfParam implements ExecutorConf {

    private Integer corePoolSize;

    private Integer maximumPoolSize;

    private Long keepAliveSeconds;

    private Integer blockingQueueCapacity;

    private String threadNamePre;

    private RejectedExecutionHandler rejectedExecutionHandler;

    public ExecutorConfParam() {
    }

    @Override
    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    @Override
    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    @Override
    public Long getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    @Override
    public Integer getBlockingQueueCapacity() {
        return blockingQueueCapacity;
    }

    @Override
    public String getThreadNamePre() {
        return threadNamePre;
    }

    @Override
    public RejectedExecutionHandler getRejectedExecutionHandler() {
        return rejectedExecutionHandler;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveSeconds(Long keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setBlockingQueueCapacity(Integer blockingQueueCapacity) {
        this.blockingQueueCapacity = blockingQueueCapacity;
    }

    public void setThreadNamePre(String threadNamePre) {
        this.threadNamePre = threadNamePre;
    }

    public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
        this.rejectedExecutionHandler = rejectedExecutionHandler;
    }

    @Override
    public String toString() {
        return "AbstractExecutorConfParam{" +
                "corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveSeconds=" + keepAliveSeconds +
                ", blockingQueueCapacity=" + blockingQueueCapacity +
                ", threadNamePre='" + threadNamePre + '\'' +
                ", rejectedExecutionHandler=" + rejectedExecutionHandler +
                '}';
    }

}