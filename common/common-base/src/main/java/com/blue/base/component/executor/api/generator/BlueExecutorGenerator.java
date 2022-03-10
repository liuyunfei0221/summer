package com.blue.base.component.executor.api.generator;

import com.blue.base.component.executor.api.conf.ExecutorConf;
import com.blue.base.model.exps.BlueException;
import net.openhft.affinity.AffinityThreadFactory;
import reactor.util.Logger;

import java.util.concurrent.*;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.DIFFERENT_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static reactor.util.Loggers.getLogger;

/**
 * executor generator
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public final class BlueExecutorGenerator {

    private static final Logger LOGGER = getLogger(BlueExecutorGenerator.class);

    private static final String DEFAULT_THREAD_NAME_PRE = "blue-executor-thread" + PAR_CONCATENATION_DATABASE_URL.identity;
    private static final int RANDOM_LEN = 6;

    public static ExecutorService generateExecutorService(ExecutorConf executorConf) {
        LOGGER.info("ExecutorService executorService(ExecutorConf executorConf), executorConf = {}", executorConf);
        assertConf(executorConf);

        String threadNamePre = ofNullable(executorConf.getThreadNamePre())
                .map(p -> p + PAR_CONCATENATION_DATABASE_URL.identity)
                .orElse(DEFAULT_THREAD_NAME_PRE);

        return new ThreadPoolExecutor(executorConf.getCorePoolSize(),
                executorConf.getMaximumPoolSize(),
                executorConf.getKeepAliveSeconds(), SECONDS,
                new ArrayBlockingQueue<>(executorConf.getBlockingQueueCapacity()),
                new AffinityThreadFactory(threadNamePre + randomAlphabetic(RANDOM_LEN), DIFFERENT_CORE),
                executorConf.getRejectedExecutionHandler());
    }

    /**
     * assert conf
     *
     * @param executorConf
     */
    private static void assertConf(ExecutorConf executorConf) {
        Integer corePoolSize = executorConf.getCorePoolSize();
        if (corePoolSize == null || corePoolSize < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "corePoolSize can't be null or less than 1");

        Integer maximumPoolSize = executorConf.getMaximumPoolSize();
        if (maximumPoolSize == null || maximumPoolSize < 1 || maximumPoolSize < corePoolSize)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "maximumPoolSize can't be null or less than 1 or less than corePoolSize");

        Long keepAliveSeconds = executorConf.getKeepAliveSeconds();
        if (keepAliveSeconds == null || keepAliveSeconds < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "keepAliveSeconds can't be null or less than 1");

        Integer blockingQueueCapacity = executorConf.getBlockingQueueCapacity();
        if (blockingQueueCapacity == null || blockingQueueCapacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "blockingQueueCapacity can't be null or less than 1");

        RejectedExecutionHandler rejectedExecutionHandler = executorConf.getRejectedExecutionHandler();
        if (rejectedExecutionHandler == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "rejectedExecutionHandler can't be null");
    }

}
