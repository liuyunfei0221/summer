package com.blue.basic.component.executor.api.generator;

import com.blue.basic.component.executor.api.conf.ExecutorConf;
import net.openhft.affinity.AffinityThreadFactory;
import reactor.util.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.Symbol.HYPHEN;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.DIFFERENT_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static reactor.util.Loggers.getLogger;

/**
 * executor generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public final class BlueExecutorGenerator {

    private static final Logger LOGGER = getLogger(BlueExecutorGenerator.class);

    private static final String DEFAULT_THREAD_NAME_PRE = "blue-executor-thread" + HYPHEN.identity;
    private static final int RANDOM_LEN = 6;

    /**
     * generate executor
     *
     * @param executorConf
     * @return
     */
    public static ExecutorService generateExecutorService(ExecutorConf executorConf) {
        LOGGER.info("executorConf = {}", executorConf);
        assertConf(executorConf);

        String threadNamePre = ofNullable(executorConf.getThreadNamePre())
                .map(p -> p + HYPHEN.identity)
                .orElse(DEFAULT_THREAD_NAME_PRE);

        RejectedExecutionHandler rejectedExecutionHandler = executorConf.getRejectedExecutionHandler();

        if (isNull(rejectedExecutionHandler))
            rejectedExecutionHandler = (r, executor) -> {
                LOGGER.warn("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution");
                r.run();
            };

        return new ThreadPoolExecutor(executorConf.getCorePoolSize(),
                executorConf.getMaximumPoolSize(),
                executorConf.getKeepAliveSeconds(), SECONDS,
                new ArrayBlockingQueue<>(executorConf.getBlockingQueueCapacity()),
                new AffinityThreadFactory(threadNamePre + randomAlphabetic(RANDOM_LEN), DIFFERENT_CORE),
                rejectedExecutionHandler);
    }

    /**
     * assert conf
     *
     * @param conf
     */
    private static void assertConf(ExecutorConf conf) {
        Integer corePoolSize = conf.getCorePoolSize();
        if (isNull(corePoolSize) || corePoolSize < 1)
            throw new RuntimeException("corePoolSize can't be null or less than 1");

        Integer maximumPoolSize = conf.getMaximumPoolSize();
        if (isNull(maximumPoolSize) || maximumPoolSize < 1 || maximumPoolSize < corePoolSize)
            throw new RuntimeException("maximumPoolSize can't be null or less than 1 or less than corePoolSize");

        Long keepAliveSeconds = conf.getKeepAliveSeconds();
        if (isNull(keepAliveSeconds) || keepAliveSeconds < 1L)
            throw new RuntimeException("keepAliveSeconds can't be null or less than 1");

        Integer blockingQueueCapacity = conf.getBlockingQueueCapacity();
        if (isNull(blockingQueueCapacity) || blockingQueueCapacity < 1)
            throw new RuntimeException("blockingQueueCapacity can't be null or less than 1");
    }

}