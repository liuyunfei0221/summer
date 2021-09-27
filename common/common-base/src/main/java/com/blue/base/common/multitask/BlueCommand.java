package com.blue.base.common.multitask;

import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * collect command
 *
 * @param <T>
 * @param <R>
 */
@SuppressWarnings({"WeakerAccess", "JavaDoc"})
final class BlueCommand<T, R> implements Runnable {

    private static final Logger LOGGER = getLogger(BlueCommand.class);

    /**
     * executor
     */
    private final BlueExecutor<T, R> executor;

    /**
     * resource to be process
     */
    private final T resource;

    /**
     * processor
     */
    private final BlueProcessor<T, R> processor;

    /**
     * @param executor
     * @param resource
     * @param processor
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
