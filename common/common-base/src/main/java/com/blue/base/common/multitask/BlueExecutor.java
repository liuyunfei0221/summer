package com.blue.base.common.multitask;

import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;
import static reactor.util.Loggers.getLogger;

/**
 * 批处理执行器
 * <p>
 * 执行调度,将模板方法中的抽象实现下沉到所依赖的接口
 *
 * @param <T>
 * @param <R>
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueExecutor<T, R> {

    private final Logger LOGGER = getLogger(BlueExecutor.class);

    private static final int MAX_TASK = 2048;

    /**
     * 写超时时间
     */
    private static final int WRITE_TIME_OUT = 10;

    /**
     * 读超时时间
     */
    private static final long READ_TIME_OUT = 10L;

    /**
     * 主线程阻塞超时时间
     */
    private static final int MAIN_BLOCKING_TIME_OUT = 300;

    /**
     * 超时单位
     */
    private static final TimeUnit TIME_OUT_UNIT = SECONDS;

    /**
     * 需要处理的资源集合
     */
    private final List<T> resources;

    /**
     * 资源处理函数
     */
    private final BlueProcessor<T, R> processor;

    /**
     * 处理结果聚合器
     */
    private final BlueCollector<R> collector;

    /**
     * 多任务处理使用的线程池
     */
    private final ExecutorService executorService;

    /**
     * 使用阻塞处理当前多任务汇总逻辑
     */
    private final CountDownLatch countDownLatch;

    public BlueExecutor(List<T> resources, BlueProcessor<T, R> processor, ExecutorService executorService, Integer threads) {
        this.resources = resources;
        this.processor = processor;
        this.executorService = executorService;
        if (threads == null || threads < 1) {
            throw new RuntimeException("threads can't be null or less than 1");
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
     * 流程控制
     */
    private void currentComplete() {
        countDownLatch.countDown();
    }

    /**
     * 任务回调
     *
     * @param r 处理结果
     */
    void execute(R r) {
        try {
            collector.complete(r);
        } finally {
            currentComplete();
        }
    }

    /**
     * 执行批量任务并处理结果
     *
     * @return 聚合结果
     */
    public List<R> execute() {

        //执行处理
        resources.forEach(resource -> {
            if (resource != null) {
                executorService.submit(new BlueCommand<>(this, resource, processor));
            } else {
                currentComplete();
            }
        });

        //基于阻塞
        try {
            boolean await = countDownLatch.await(MAIN_BLOCKING_TIME_OUT, TIME_OUT_UNIT);
        } catch (InterruptedException e) {
            LOGGER.error("countDownLatch.await() throws an Exception = {}", e);
        }

        return collector.collect();
    }

    /**
     * 校验
     */
    private void argsAssert() {
        if (this.resources == null || this.resources.size() < 1)
            throw new RuntimeException("resources can't be null or empty");
        if (this.resources.size() > MAX_TASK)
            throw new RuntimeException("resources can't be more than " + MAX_TASK);
        if (this.processor == null)
            throw new RuntimeException("processor can't be null");
        if (this.executorService == null || this.executorService.isShutdown())
            throw new RuntimeException("executorService can't be null or shutdown");
        if (BlueExecutor.MAX_TASK < 1)
            throw new RuntimeException("MAX_TASK can't be less than 1");
        if (BlueExecutor.WRITE_TIME_OUT < 1)
            throw new RuntimeException("WRITE_TIME_OUT can't be less than 1");
        if (BlueExecutor.READ_TIME_OUT < 1L)
            throw new RuntimeException("READ_TIME_OUT can't be less than 1");
    }

}
