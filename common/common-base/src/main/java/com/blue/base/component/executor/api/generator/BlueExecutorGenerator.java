package com.blue.base.component.executor.api.generator;

import com.blue.base.component.executor.api.conf.ExecutorConf;
import reactor.util.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static reactor.util.Loggers.getLogger;

/**
 * executor generator
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BlueExecutorGenerator {

    private static final Logger LOGGER = getLogger(BlueExecutorGenerator.class);

    private static final String DEFAULT_THREAD_NAME_PRE = "blue-executor-thread" + PAR_CONCATENATION_DATABASE_URL.identity;
    private static final int RANDOM_LEN = 6;

    public static ExecutorService createExecutorService(ExecutorConf executorConf) {
        LOGGER.info("ExecutorService executorService(ExecutorConf executorConf), executorConf = {}", executorConf);
        assertConf(executorConf);

        String threadNamePre = ofNullable(executorConf.getThreadNamePre())
                .map(tnp -> tnp + PAR_CONCATENATION_DATABASE_URL.identity)
                .orElse(DEFAULT_THREAD_NAME_PRE);

        return new ThreadPoolExecutor(executorConf.getCorePoolSize(),
                executorConf.getMaximumPoolSize(),
                executorConf.getKeepAliveTime(), SECONDS,
                new ArrayBlockingQueue<>(executorConf.getBlockingQueueCapacity()),
                r -> {
                    Thread thread = new Thread(r, threadNamePre + randomAlphabetic(RANDOM_LEN));
                    thread.setDaemon(true);
                    return thread;
                }, executorConf.getRejectedExecutionHandler());
    }

    /**
     * 校验
     *
     * @param executorConf
     */
    private static void assertConf(ExecutorConf executorConf) {
        Integer corePoolSize = executorConf.getCorePoolSize();
        if (corePoolSize == null || corePoolSize < 1)
            throw new RuntimeException("corePoolSize can't be null or less than 1");

        Integer maximumPoolSize = executorConf.getMaximumPoolSize();
        if (maximumPoolSize == null || maximumPoolSize < 1 || maximumPoolSize < corePoolSize)
            throw new RuntimeException("maximumPoolSize can't be null or less than 1 or less than corePoolSize");

        Long keepAliveTime = executorConf.getKeepAliveTime();
        if (keepAliveTime == null || keepAliveTime < 1L)
            throw new RuntimeException("keepAliveTime can't be null or less than 1");

        Integer blockingQueueCapacity = executorConf.getBlockingQueueCapacity();
        if (blockingQueueCapacity == null || blockingQueueCapacity < 1)
            throw new RuntimeException("blockingQueueCapacity can't be null or less than 1");

        RejectedExecutionHandler rejectedExecutionHandler = executorConf.getRejectedExecutionHandler();
        if (rejectedExecutionHandler == null)
            throw new RuntimeException("rejectedExecutionHandler can't be null");
    }

}
