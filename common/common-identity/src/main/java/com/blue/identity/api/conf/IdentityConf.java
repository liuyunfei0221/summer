package com.blue.identity.api.conf;

import java.util.function.Consumer;

/**
 * @author DarkBlue
 */

@SuppressWarnings("JavaDoc")
public interface IdentityConf {

    /**
     * 获取数据中心编号
     *
     * @return
     */
    Integer getDataCenter();

    /**
     * 获取机器编号
     *
     * @return
     */
    Integer getWorker();

    /**
     * 获取本服务实例名称
     *
     * @return
     */
    String getServiceName();

    /**
     * 获取上次记录时间点
     *
     * @return
     */
    Long getLastSeconds();

    /**
     * 获取项目上线时间点
     *
     * @return
     */
    Long getBootSeconds();

    /**
     * 到达最大时间戳报警器
     *
     * @return
     */
    Consumer<Long> getMaximumTimeAlarm();

    /**
     * 时间戳记录器
     *
     * @return
     */
    Consumer<Long> getSecondsRecorder();

    /**
     * 获取缓冲（2的幂数)
     *
     * @return
     */
    Integer getBufferPower();

    /**
     * 获取填充阈值(百分比)
     *
     * @return
     */
    Integer getPaddingFactor();

    /**
     * 获取异步填充线程池核心线程数
     *
     * @return
     */
    Integer getPaddingCorePoolSize();

    /**
     * 获取异步填充线程池最大线程数
     *
     * @return
     */
    Integer getPaddingMaximumPoolSize();

    /**
     * 获取线程keep alive 秒数
     *
     * @return
     */
    Long getKeepAliveSeconds();

    /**
     * 获取异步填充线程池阻塞队列长度
     *
     * @return
     */
    Integer getPaddingBlockingQueueSize();

    /**
     * 是否定时填充
     *
     * @return
     */
    Boolean getPaddingScheduled();

    /**
     * 获取定时填充线程池核心线程数
     *
     * @return
     */
    Integer getPaddingScheduledCorePoolSize();

    /**
     * 获取定时填充初始延迟/毫秒
     *
     * @return
     */
    Long getPaddingScheduledInitialDelayMillis();

    /**
     * 获取定时填充时间间隔/毫秒
     *
     * @return
     */
    Long getPaddingScheduledDelayMillis();

}
