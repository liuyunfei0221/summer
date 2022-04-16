package com.blue.auth.component.access;

import com.blue.auth.event.producer.AccessExpireProducer;
import com.blue.base.model.base.KeyExpireParam;
import com.blue.base.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import net.openhft.affinity.AffinityThreadFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.springframework.util.StringUtils.hasText;
import static reactor.core.publisher.Mono.*;
import static reactor.util.Loggers.getLogger;

/**
 * authInfo cache
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class AccessInfoCache {

    private static final Logger LOGGER = getLogger(AccessInfoCache.class);

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private AccessExpireProducer accessExpireProducer;

    private Scheduler scheduler;

    private ExecutorService executorService;

    /**
     * global expire millis
     */
    private Long globalExpireMillis;

    /**
     * millis
     */
    private final ChronoUnit UNIT = MILLIS;

    /**
     * global expire duration
     */
    private final Duration globalExpireDuration;

    /**
     * local cache
     */
    private final Cache<String, String> CACHE;

    private static final String THREAD_NAME_PRE = "AccessInfoCache-thread- ";
    private static final int RANDOM_LEN = 4;

    public AccessInfoCache(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessExpireProducer accessExpireProducer,
                           Scheduler scheduler, Integer refresherCorePoolSize, Integer refresherMaximumPoolSize, Long refresherKeepAliveSeconds,
                           Integer refresherBlockingQueueCapacity, Long globalExpireMillis, Long localExpireMillis, Integer capacity) {

        assertConf(reactiveStringRedisTemplate, accessExpireProducer,
                refresherCorePoolSize, refresherMaximumPoolSize, refresherKeepAliveSeconds,
                refresherBlockingQueueCapacity, globalExpireMillis, localExpireMillis, capacity);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.accessExpireProducer = accessExpireProducer;
        this.scheduler = scheduler;

        ThreadFactory threadFactory = new AffinityThreadFactory(THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN), SAME_CORE);

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.error("task has been reject: r = {}", r);
            r.run();
        };

        this.executorService = new ThreadPoolExecutor(refresherCorePoolSize, refresherMaximumPoolSize,
                refresherKeepAliveSeconds, SECONDS, new ArrayBlockingQueue<>(refresherBlockingQueueCapacity), threadFactory, rejectedExecutionHandler);

        this.globalExpireMillis = globalExpireMillis;
        this.globalExpireDuration = Duration.of(globalExpireMillis, UNIT);

        CaffeineConf caffeineConf = new CaffeineConfParams(capacity, Duration.of(localExpireMillis, MILLIS),
                AFTER_WRITE, executorService);

        this.CACHE = generateCache(caffeineConf);
    }

    /**
     * redis accessInfo refresher
     */
    private final BiConsumer<String, String> REDIS_ACCESS_REFRESHER = (keyId, accessInfo) -> {
        try {
            this.executorService.execute(() -> {
                if (hasText(keyId) && hasText(accessInfo)) {
                    accessExpireProducer.send(new KeyExpireParam(keyId, globalExpireMillis, UNIT));
                    LOGGER.warn("REDIS_ACCESS_REFRESHER -> SUCCESS, keyId = {}", keyId);
                } else {
                    LOGGER.error("keyId or accessInfo is empty, keyId = {}, accessInfo = {}", keyId, accessInfo);
                }
            });
        } catch (Exception e) {
            LOGGER.error("REDIS_ACCESS_REFRESHER error, e = {}", e);
        }
    };

    /**
     * redis accessInfo getter
     */
    private final Function<String, Mono<String>> REDIS_ACCESS_GETTER = keyId -> {
        LOGGER.warn("REDIS_ACCESS_GETTER, get accessInfo from redis, keyId = {}", keyId);

        return reactiveStringRedisTemplate.opsForValue().get(keyId)
                .switchIfEmpty(just(""))
                .flatMap(accessInfo -> {
                    if (!"".equals(accessInfo))
                        REDIS_ACCESS_REFRESHER.accept(keyId, accessInfo);
                    return just(accessInfo);
                }).publishOn(scheduler);
    };

    /**
     * get accessInfo value by keyId
     *
     * @param keyId
     * @return
     */
    public Mono<String> getAccessInfo(String keyId) {
        return isNotBlank(keyId) ?
                justOrEmpty(CACHE.getIfPresent(keyId)).switchIfEmpty(REDIS_ACCESS_GETTER.apply(keyId)).publishOn(scheduler)
                :
                error(() -> new BlueException(UNAUTHORIZED));
    }

    /**
     * cache accessInfo
     *
     * @param keyId
     * @param accessInfo
     */
    public Mono<Boolean> setAccessInfo(String keyId, String accessInfo) {
        LOGGER.info("setAccessInfo(), keyId = {},accessInfo = {}", keyId, accessInfo);
        return reactiveStringRedisTemplate.opsForValue()
                .set(keyId, accessInfo, globalExpireDuration)
                .publishOn(scheduler)
                .onErrorResume(throwable -> {
                    LOGGER.error("setAccessInfo(String keyId, String accessInfo) failed, throwable = {}", throwable);
                    return just(false);
                });
    }

    /**
     * invalid authInfo
     *
     * @param keyId
     */
    public Mono<Boolean> invalidAccessInfo(String keyId) {
        LOGGER.info("invalidAuthInfo(), keyId = {}", keyId);
        return
                isNotBlank(keyId)
                        ?
                        reactiveStringRedisTemplate.delete(keyId)
                                .flatMap(l -> {
                                    CACHE.invalidate(keyId);
                                    return just(l > 0L);
                                }).publishOn(scheduler)
                        :
                        just(false).publishOn(scheduler);
    }

    /**
     * invalid local authInfo
     *
     * @param keyId
     */
    public Mono<Boolean> invalidLocalAccessInfo(String keyId) {
        LOGGER.info("invalidLocalAuthInfo(), keyId = {}", keyId);
        try {
            if (isNotBlank(keyId))
                CACHE.invalidate(keyId);
            return just(true).publishOn(scheduler);
        } catch (Exception e) {
            return just(false).publishOn(scheduler);
        }
    }

    /**
     * assert conf
     */
    private static void assertConf(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessExpireProducer accessExpireProducer,
                                   Integer refresherCorePoolSize, Integer refresherMaximumPoolSize, Long refresherKeepAliveSeconds,
                                   Integer refresherBlockingQueueCapacity, Long globalExpireMillis, Long localExpireMillis, Integer capacity) {
        if (isNull(reactiveStringRedisTemplate))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "reactiveStringRedisTemplate can't be null");

        if (isNull(accessExpireProducer))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "authExpireProducer can't be null");

        if (isNull(refresherCorePoolSize) || refresherCorePoolSize < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "refresherCorePoolSize can't be null or less than 1");

        if (isNull(refresherMaximumPoolSize) || refresherMaximumPoolSize < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "refresherMaximumPoolSize can't be null or less than 1");

        if (isNull(refresherKeepAliveSeconds) || refresherKeepAliveSeconds < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "refresherKeepAliveSeconds can't be null or less than 1");

        if (isNull(refresherBlockingQueueCapacity) || refresherBlockingQueueCapacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "refresherBlockingQueueCapacity can't be null or less than 1");

        if (isNull(globalExpireMillis) || globalExpireMillis < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "globalExpireSeconds can't be null or less than 1");

        if (isNull(localExpireMillis) || localExpireMillis < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "localExpireSeconds can't be null or less than 1");

        if (localExpireMillis > globalExpireMillis)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "localExpireSeconds can't be null or greater than globalExpireSeconds");

        if (isNull(capacity) || capacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "capacity can't be null or less than 1");
    }

}
