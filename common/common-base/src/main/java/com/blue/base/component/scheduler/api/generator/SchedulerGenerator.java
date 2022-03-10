package com.blue.base.component.scheduler.api.generator;

import com.blue.base.component.scheduler.api.conf.SchedulerConf;
import com.blue.base.model.exps.BlueException;
import net.openhft.affinity.AffinityThreadFactory;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.Symbol.PAR_CONCATENATION_DATABASE_URL;
import static java.util.Optional.ofNullable;
import static net.openhft.affinity.AffinityStrategies.DIFFERENT_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static reactor.core.scheduler.Schedulers.newBoundedElastic;
import static reactor.util.Loggers.getLogger;


/**
 * scheduler generator
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "DuplicatedCode", "JavaDoc"})
public final class SchedulerGenerator {

    private static final Logger LOGGER = getLogger(SchedulerGenerator.class);

    private static final String DEFAULT_THREAD_NAME_PRE = "blue-scheduler-thread" + PAR_CONCATENATION_DATABASE_URL.identity;
    private static final int RANDOM_LEN = 6;

    public static Scheduler generateScheduler(SchedulerConf schedulerConf) {
        LOGGER.info("Scheduler generateScheduler(SchedulerConf schedulerConf), schedulerConf = {}", schedulerConf);
        assertConf(schedulerConf);

        String threadNamePre = ofNullable(schedulerConf.getThreadNamePre())
                .map(p -> p + PAR_CONCATENATION_DATABASE_URL.identity)
                .orElse(DEFAULT_THREAD_NAME_PRE);

        return newBoundedElastic(schedulerConf.getThreadCap(), schedulerConf.getQueuedTaskCap(),
                new AffinityThreadFactory(threadNamePre + randomAlphabetic(RANDOM_LEN), DIFFERENT_CORE),
                schedulerConf.getTtlSeconds());
    }

    /**
     * assert conf
     *
     * @param schedulerConf
     */
    private static void assertConf(SchedulerConf schedulerConf) {
        Integer threadCap = schedulerConf.getThreadCap();
        if (threadCap == null || threadCap < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "threadCap can't be null or less than 1");

        Integer queuedTaskCap = schedulerConf.getQueuedTaskCap();
        if (queuedTaskCap == null || queuedTaskCap < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "queuedTaskCap can't be null or less than 1");

        Integer ttlSeconds = schedulerConf.getTtlSeconds();
        if (ttlSeconds == null || ttlSeconds < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "ttlSeconds can't be null or less than 1");
    }

}
