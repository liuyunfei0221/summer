package com.blue.redis.api.generator;

import reactor.core.scheduler.Scheduler;

import static reactor.core.scheduler.Schedulers.boundedElastic;


/**
 * redis scheduler generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"DuplicatedCode", "JavaDoc"})
public final class BlueRedisSchedulerGenerator {

    public static Scheduler generateRedisScheduler() {
        return boundedElastic();
    }

}
