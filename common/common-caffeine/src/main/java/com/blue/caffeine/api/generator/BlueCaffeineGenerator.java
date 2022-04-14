package com.blue.caffeine.api.generator;


import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.constant.ExpireStrategy;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.lang.Integer.numberOfLeadingZeros;
import static java.util.Optional.ofNullable;

/**
 * caffeine component generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueCaffeineGenerator {

    private static final Map<ExpireStrategy, BiConsumer<Caffeine<Object, Object>, Duration>> EXPIRE_STRATEGY_PACKAGERS_HOLDER = new HashMap<>(4, 1.0f);

    static {
        EXPIRE_STRATEGY_PACKAGERS_HOLDER.put(AFTER_WRITE, Caffeine::expireAfterWrite);
        EXPIRE_STRATEGY_PACKAGERS_HOLDER.put(AFTER_ACCESS, Caffeine::expireAfterAccess);
    }

    /**
     * package expire info
     *
     * @param caffeine
     * @param expireStrategy
     * @param duration
     */
    private static void packageExpire(Caffeine<Object, Object> caffeine, ExpireStrategy expireStrategy, Duration duration) {
        if (expireStrategy == null)
            throw new RuntimeException("expireStrategy can't be null");

        BiConsumer<Caffeine<Object, Object>, Duration> packager = EXPIRE_STRATEGY_PACKAGERS_HOLDER.get(expireStrategy);
        if (packager == null)
            throw new RuntimeException("the packager for expireStrategy -> (" + expireStrategy + ") doesn't exist");

        packager.accept(caffeine, duration);
    }

    /**
     * cache size
     */
    private static final int DEFAULT_CAPACITY = 1 << 9;
    private static final int MAXIMUM_CAPACITY = 1 << 16;

    /**
     * create cache
     *
     * @param conf
     * @return
     */
    public static <K, V> Cache<K, V> generateCache(CaffeineConf conf) {
        confAsserter(conf);

        int n = -1 >>> numberOfLeadingZeros(ofNullable(conf.getMaximumSize()).filter(ms -> ms > 1).orElse(DEFAULT_CAPACITY) - 1);
        int cap = (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;

        Caffeine<Object, Object> caffeine = newBuilder();
        caffeine.maximumSize(cap);
        packageExpire(caffeine, conf.getExpireStrategy(), conf.getExpireDuration());
        ofNullable(conf.getExecutorService()).ifPresent(caffeine::executor);

        return caffeine.build();
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void confAsserter(CaffeineConf conf) {
        if (conf == null)
            throw new RuntimeException("caffeineConf can't be null");

        Duration expireDuration = conf.getExpireDuration();
        if (expireDuration == null)
            throw new RuntimeException("expireDuration can't be null");
    }

}
