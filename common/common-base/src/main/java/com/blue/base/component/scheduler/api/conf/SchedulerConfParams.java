package com.blue.base.component.scheduler.api.conf;

/**
 * scheduler conf param
 *
 * @author liuyunfei
 * @date 2021/9/10
 * @apiNote
 */
@SuppressWarnings("unused")
public class SchedulerConfParams implements SchedulerConf {

    protected Integer threadCap;

    protected Integer queuedTaskCap;

    protected String threadNamePre;

    protected Integer ttlSeconds;

    public SchedulerConfParams() {
    }

    public SchedulerConfParams(Integer threadCap, Integer queuedTaskCap, String threadNamePre, Integer ttlSeconds) {
        this.threadCap = threadCap;
        this.queuedTaskCap = queuedTaskCap;
        this.threadNamePre = threadNamePre;
        this.ttlSeconds = ttlSeconds;
    }

    @Override
    public Integer getThreadCap() {
        return threadCap;
    }

    @Override
    public Integer getQueuedTaskCap() {
        return queuedTaskCap;
    }

    @Override
    public String getThreadNamePre() {
        return threadNamePre;
    }

    @Override
    public Integer getTtlSeconds() {
        return ttlSeconds;
    }

    public void setThreadCap(Integer threadCap) {
        this.threadCap = threadCap;
    }

    public void setQueuedTaskCap(Integer queuedTaskCap) {
        this.queuedTaskCap = queuedTaskCap;
    }

    public void setThreadNamePre(String threadNamePre) {
        this.threadNamePre = threadNamePre;
    }

    public void setTtlSeconds(Integer ttlSeconds) {
        this.ttlSeconds = ttlSeconds;
    }

    @Override
    public String toString() {
        return "SchedulerConfParams{" +
                "threadCap=" + threadCap +
                ", queuedTaskCap=" + queuedTaskCap +
                ", threadNamePre='" + threadNamePre + '\'' +
                ", ttlSeconds=" + ttlSeconds +
                '}';
    }

}
