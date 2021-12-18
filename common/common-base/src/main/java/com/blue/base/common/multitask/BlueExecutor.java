package com.blue.base.common.multitask;

import com.blue.base.model.exps.BlueException;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.util.concurrent.TimeUnit.SECONDS;
import static reactor.util.Loggers.getLogger;

/**
 * batch executor
 *
 * @param <T>
 * @param <R>
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class BlueExecutor<T, R> {

    private final Logger LOGGER = getLogger(BlueExecutor.class);

    private static final int MAX_TASK = 2048;

    /**
     * write timeout
     */
    private static final int WRITE_TIME_OUT = 10;

    /**
     * read timeout
     */
    private static final long READ_TIME_OUT = 10L;

    /**
     * main thread blocking timeout
     */
    private static final int MAIN_BLOCKING_TIME_OUT = 300;

    /**
     * timeout unit
     */
    private static final TimeUnit TIME_OUT_UNIT = SECONDS;

    /**
     * resources to be processor
     */
    private final List<T> resources;

    /**
     * handle func
     */
    private final BlueProcessor<T, R> processor;

    /**
     * collector
     */
    private final BlueCollector<R> collector;

    /**
     * executor
     */
    private final ExecutorService executorService;

    /**
     * base blocking
     */
    private final CountDownLatch countDownLatch;

    public BlueExecutor(List<T> resources, BlueProcessor<T, R> processor, ExecutorService executorService, Integer threads) {
        this.resources = resources;
        this.processor = processor;
        this.executorService = executorService;
        if (threads == null || threads < 1) {
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "threads can't be null or less than 1");
        }
        this.collector = new DefaultBlueCollector<>(threads, WRITE_TIME_OUT, READ_TIME_OUT, TIME_OUT_UNIT);

        argsAssert();
        this.countDownLatch = new CountDownLatch(resources.size());
    }

    public BlueExecutor(List<T> resources, BlueProcessor<T, R> processor, ExecutorService executorService) {
        this.resources = resources;
        this.processor = processor;
        this.executorService = executorService;
        this.collector = new DefaultBlueCollector<>(4, WRITE_TIME_OUT, READ_TIME_OUT, TIME_OUT_UNIT);

        argsAssert();
        this.countDownLatch = new CountDownLatch(resources.size());
    }

    public BlueExecutor(List<T> resources, BlueProcessor<T, R> processor, BlueCollector<R> collector, ExecutorService executorService) {
        this.resources = resources;
        this.processor = processor;
        this.collector = collector;
        this.executorService = executorService;

        argsAssert();
        this.countDownLatch = new CountDownLatch(resources.size());
    }

    /**
     * step
     */
    private void currentComplete() {
        countDownLatch.countDown();
    }

    /**
     * complete callback
     *
     * @param r
     */
    void execute(R r) {
        try {
            collector.complete(r);
        } finally {
            currentComplete();
        }
    }

    /**
     * batch process
     *
     * @return collect result
     */
    public List<R> execute() {

        //process
        resources.forEach(resource -> {
            if (resource != null) {
                executorService.submit(new BlueCommand<>(this, resource, processor));
            } else {
                currentComplete();
            }
        });

        //base on blocking
        try {
            boolean await = countDownLatch.await(MAIN_BLOCKING_TIME_OUT, TIME_OUT_UNIT);
        } catch (InterruptedException e) {
            LOGGER.error("countDownLatch.await() throws an Exception = {}", e);
        }

        return collector.collect();
    }

    /**
     * assert
     */
    private void argsAssert() {
        if (this.resources == null || this.resources.size() < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "resources can't be null or empty");
        if (this.resources.size() > MAX_TASK)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "resources can't be more than " + MAX_TASK);
        if (this.processor == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "processor can't be null");
        if (this.executorService == null || this.executorService.isShutdown())
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "executorService can't be null or shutdown");
        if (BlueExecutor.MAX_TASK < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "MAX_TASK can't be less than 1");
        if (BlueExecutor.WRITE_TIME_OUT < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "WRITE_TIME_OUT can't be less than 1");
        if (BlueExecutor.READ_TIME_OUT < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "READ_TIME_OUT can't be less than 1");
    }

}
