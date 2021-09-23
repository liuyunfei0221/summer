package com.blue.base.common.multitask;

import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * 分片任务
 *
 * @param <T> 需要处理的资源
 * @param <R> 资源处理结果
 */
@SuppressWarnings("WeakerAccess")
final class BlueCommand<T, R> implements Runnable {

    private static final Logger LOGGER = getLogger(BlueCommand.class);

    /**
     * 批任务调度执行器
     */
    private final BlueExecutor<T, R> executor;

    /**
     * 需要处理的资源
     */
    private final T resource;

    /**
     * 回调器
     */
    private final BlueProcessor<T, R> processor;

    /**
     * 调用方TheExecutor已经校验,不再重复校验
     *
     * @param executor  调度器
     * @param resource  资源
     * @param processor 处理函数
     */
    public BlueCommand(BlueExecutor<T, R> executor, T resource, BlueProcessor<T, R> processor) {
        this.executor = executor;
        this.resource = resource;
        this.processor = processor;
    }

    @Override
    public void run() {
        try {
            executor.execute(this.processor.processor().apply(resource));
        } catch (Exception e) {
            LOGGER.error("run() failed, e = {}", e.toString());
            executor.execute(this.processor.failProcessor().apply(resource, e));
        }
    }

}
