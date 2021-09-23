package com.blue.base.component.executor.api.conf;

import java.util.concurrent.RejectedExecutionHandler;

/**
 * 线程池配置
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc"})
public abstract class ExecutorConfParam implements ExecutorConf {

    protected Integer corePoolSize;

    protected Integer maximumPoolSize;

    protected Long keepAliveTime;

    protected Integer blockingQueueCapacity;

    protected String threadNamePre;

    public ExecutorConfParam() {
    }

    public ExecutorConfParam(Integer corePoolSize, Integer maximumPoolSize, Long keepAliveTime,
                             Integer blockingQueueCapacity, String threadNamePre) {
        this.corePoolSize = corePoolSize;
        this.maximumPoolSize = maximumPoolSize;
        this.keepAliveTime = keepAliveTime;
        this.blockingQueueCapacity = blockingQueueCapacity;
        this.threadNamePre = threadNamePre;
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
    public Long getKeepAliveTime() {
        return keepAliveTime;
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
    public abstract RejectedExecutionHandler getRejectedExecutionHandler();

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveTime(Long keepAliveTime) {
        this.keepAliveTime = keepAliveTime;
    }

    public void setBlockingQueueCapacity(Integer blockingQueueCapacity) {
        this.blockingQueueCapacity = blockingQueueCapacity;
    }

    public void setThreadNamePre(String threadNamePre) {
        this.threadNamePre = threadNamePre;
    }

    @Override
    public String toString() {
        return "ExecutorConfParam{" +
                "corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveTime=" + keepAliveTime +
                ", blockingQueueCapacity=" + blockingQueueCapacity +
                ", threadNamePre='" + threadNamePre + '\'' +
                '}';
    }

}
