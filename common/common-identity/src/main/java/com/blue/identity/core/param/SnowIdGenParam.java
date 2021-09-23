package com.blue.identity.core.param;

import java.util.concurrent.ExecutorService;
import java.util.function.Consumer;

/**
 * params for SnowflakeIdentityGenerator
 *
 * @author DarkBlue
 */
public final class SnowIdGenParam {

    private final int dataCenter;

    private final int worker;

    private final Long lastSeconds;

    private final Long bootSeconds;

    private final Consumer<Long> maximumTimeAlarm;

    private final Consumer<Long> secondsRecorder;

    private final ExecutorService executorService;

    public SnowIdGenParam(int dataCenter, int worker, Long lastSeconds, Long bootSeconds, Consumer<Long> maximumTimeAlarm, Consumer<Long> secondsRecorder, ExecutorService executorService) {
        this.dataCenter = dataCenter;
        this.worker = worker;
        this.lastSeconds = lastSeconds;
        this.bootSeconds = bootSeconds;
        this.maximumTimeAlarm = maximumTimeAlarm;
        this.secondsRecorder = secondsRecorder;
        this.executorService = executorService;
    }

    public int getDataCenter() {
        return dataCenter;
    }

    public int getWorker() {
        return worker;
    }

    public Long getLastSeconds() {
        return lastSeconds;
    }

    public Long getBootSeconds() {
        return bootSeconds;
    }

    public Consumer<Long> getMaximumTimeAlarm() {
        return maximumTimeAlarm;
    }

    public Consumer<Long> getSecondsRecorder() {
        return secondsRecorder;
    }

    public ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    public String toString() {
        return "SnowIdGenParam{" +
                "dataCenter=" + dataCenter +
                ", worker=" + worker +
                ", lastSeconds=" + lastSeconds +
                ", bootSeconds=" + bootSeconds +
                ", maximumTimeAlarm=" + maximumTimeAlarm +
                ", secondsRecorder=" + secondsRecorder +
                ", executorService=" + executorService +
                '}';
    }

}
