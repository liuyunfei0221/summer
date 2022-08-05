package com.blue.caffeine.api.generator;


import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.constant.ExpireStrategy;
import com.github.benmanes.caffeine.cache.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

import static com.blue.caffeine.constant.ExpireStrategy.AFTER_ACCESS;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static com.github.benmanes.caffeine.cache.Caffeine.newBuilder;
import static java.lang.Integer.numberOfLeadingZeros;
import static java.util.Objects.isNull;
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
        if (isNull(expireStrategy))
            throw new RuntimeException("expireStrategy can't be null");

        BiConsumer<Caffeine<Object, Object>, Duration> packager = EXPIRE_STRATEGY_PACKAGERS_HOLDER.get(expireStrategy);
        if (isNull(packager))
            throw new RuntimeException("the packager for expireStrategy -> (" + expireStrategy + ") doesn't exist");

        packager.accept(caffeine, duration);
    }

    /**
     * cache size
     */
    private static final int DEFAULT_CAPACITY = 1 << 9;
    private static final int MAXIMUM_CAPACITY = 1 << 16;

    private static final UnaryOperator<Integer> CAP_GETTER = maximumSize -> {
        int n = -1 >>> numberOfLeadingZeros(ofNullable(maximumSize).filter(ms -> ms > 1).orElse(DEFAULT_CAPACITY) - 1);
        return (n < 0) ? 1 : (n >= MAXIMUM_CAPACITY) ? MAXIMUM_CAPACITY : n + 1;
    };

    private static final Function<CaffeineConf, Caffeine<Object, Object>> BUILDER_GEN = conf -> {
        assertConf(conf);

        Caffeine<Object, Object> caffeine = newBuilder();
        caffeine.maximumSize(CAP_GETTER.apply(conf.getMaximumSize()));
        packageExpire(caffeine, conf.getExpireStrategy(), conf.getExpireDuration());
        ofNullable(conf.getExecutorService()).ifPresent(caffeine::executor);

        return caffeine;
    };

    /**
     * create cache
     *
     * @param conf
     * @return
     */
    public static <K, V> Cache<K, V> generateCache(CaffeineConf conf) {
        return BUILDER_GEN.apply(conf).build();
    }

    /**
     * create cache
     *
     * @param conf
     * @return
     */
    public static <K, V> AsyncCache<K, V> generateCacheAsyncCache(CaffeineConf conf) {
        return BUILDER_GEN.apply(conf).buildAsync();
    }

    /**
     * create cache
     *
     * @param conf
     * @return
     */
    public static <K, V> AsyncCache<K, V> generateCacheAsyncCache(CaffeineConf conf, CacheLoader<? super K, V> loader) {
        if (isNull(loader))
            throw new RuntimeException("loader can't be null");

        return BUILDER_GEN.apply(conf).buildAsync(loader);
    }

    /**
     * create cache
     *
     * @param conf
     * @return
     */
    public static <K, V> AsyncCache<K, V> generateCacheAsyncCache(CaffeineConf conf, AsyncCacheLoader<? super K, V> loader) {
        if (isNull(loader))
            throw new RuntimeException("loader can't be null");

        return BUILDER_GEN.apply(conf).buildAsync(loader);
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void assertConf(CaffeineConf conf) {
        if (isNull(conf))
            throw new RuntimeException("caffeineConf can't be null");

        Duration expireDuration = conf.getExpireDuration();
        if (isNull(expireDuration))
            throw new RuntimeException("expireDuration can't be null");
    }

}
