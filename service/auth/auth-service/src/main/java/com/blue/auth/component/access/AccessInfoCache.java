package com.blue.auth.component.access;

import com.blue.auth.model.AccessInfo;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.Cache;
import com.google.gson.JsonSyntaxException;
import net.openhft.affinity.AffinityThreadFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.MILLIS_STAMP_SUP;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.UNAUTHORIZED;
import static com.blue.basic.constant.common.SpecialStringElement.EMPTY_VALUE;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
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

    private AccessBatchExpireProcessor accessBatchExpireProcessor;

    private Scheduler scheduler;

    private ExecutorService executorService;

    /**
     * global expire millis
     */
    private long globalExpiresMillis;

    /**
     * to handle expire
     */
    private long differenceToHandleExpire;

    /**
     * millis
     */
    private final ChronoUnit UNIT = MILLIS;

    /**
     * global expire duration
     */
    private Duration globalExpireDuration;

    /**
     * local cache
     */
    private Cache<String, String> cache;

    private static final String THREAD_NAME_PRE = "AccessInfoCache-thread-";
    private static final int RANDOM_LEN = 4;

    public AccessInfoCache(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessBatchExpireProcessor accessBatchExpireProcessor, Scheduler scheduler,
                           Integer refresherCorePoolSize, Integer refresherMaximumPoolSize, Long refresherKeepAliveSeconds,
                           Integer refresherBlockingQueueCapacity, Long globalExpiresMillis, Long localExpiresMillis, Long millisLeftToHandleExpire, Integer capacity) {

        assertConf(reactiveStringRedisTemplate, accessBatchExpireProcessor, refresherCorePoolSize, refresherMaximumPoolSize, refresherKeepAliveSeconds,
                refresherBlockingQueueCapacity, globalExpiresMillis, localExpiresMillis, millisLeftToHandleExpire, capacity);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.accessBatchExpireProcessor = accessBatchExpireProcessor;
        this.scheduler = scheduler;

        ThreadFactory threadFactory = new AffinityThreadFactory(THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN), SAME_CORE);

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.error("task has been reject: r = {}", r);
            r.run();
        };

        this.executorService = new ThreadPoolExecutor(refresherCorePoolSize, refresherMaximumPoolSize,
                refresherKeepAliveSeconds, SECONDS, new ArrayBlockingQueue<>(refresherBlockingQueueCapacity), threadFactory, rejectedExecutionHandler);

        this.globalExpiresMillis = globalExpiresMillis;
        this.differenceToHandleExpire = globalExpiresMillis - millisLeftToHandleExpire;

        this.globalExpireDuration = Duration.of(globalExpiresMillis, UNIT);

        CaffeineConf caffeineConf = new CaffeineConfParams(capacity, Duration.of(localExpiresMillis, MILLIS),
                AFTER_WRITE, executorService);

        this.cache = generateCache(caffeineConf);
    }

    private final Predicate<Long> EXPIRE_PRE = loginMillisTimeStamp ->
            (MILLIS_STAMP_SUP.get() > (loginMillisTimeStamp + differenceToHandleExpire));

    /**
     * redis accessInfo expire
     */
    private final BiConsumer<String, AccessInfo> ACCESS_EXPIRE_PROCESSOR = (keyId, accessInfo) -> {
        try {
            this.executorService.execute(() -> {
                if (isNotBlank(keyId) && isNotNull(accessInfo)) {
                    if (EXPIRE_PRE.test(accessInfo.getLoginMillisTimeStamp()))
                        accessBatchExpireProcessor.expireKey(keyId, globalExpiresMillis, UNIT);

                    LOGGER.warn("ACCESS_EXPIRE_PROCESSOR -> SUCCESS, keyId = {}", keyId);
                } else {
                    LOGGER.error("keyId or accessInfo is empty, keyId = {}, accessInfo = {}", keyId, accessInfo);
                }
            });
        } catch (Exception e) {
            LOGGER.error("ACCESS_EXPIRE_PROCESSOR error, e = {}", e);
        }
    };

    /**
     * redis accessInfo getter
     */
    private final Function<String, Mono<String>> REDIS_ACCESS_WITH_LOCAL_CACHE_GETTER = keyId -> {
        LOGGER.warn("REDIS_ACCESS_WITH_LOCAL_CACHE_GETTER, get accessInfo from redis and set in caff, keyId = {}", keyId);

        return reactiveStringRedisTemplate.opsForValue().get(keyId)
                .publishOn(scheduler)
                .flatMap(accessInfo -> {
                    if (!EMPTY_VALUE.value.equals(accessInfo))
                        cache.put(keyId, accessInfo);

                    return just(accessInfo);
                });
    };

    /**
     * cache accessInfo getter
     */
    private final Function<String, Mono<String>> ACCESS_GETTER_WITH_CACHE = keyId -> {
        LOGGER.warn("ACCESS_GETTER_WITH_CACHE, get accessInfo from cache, keyId = {}", keyId);
        if (isBlank(keyId))
            return error(() -> new BlueException(UNAUTHORIZED));

        return justOrEmpty(cache.getIfPresent(keyId))
                .switchIfEmpty(defer(() -> REDIS_ACCESS_WITH_LOCAL_CACHE_GETTER.apply(keyId)));
    };

    /**
     * cache accessInfo setter
     */
    private final BiFunction<String, AccessInfo, Mono<Boolean>> ACCESS_SETTER_WITH_CACHE = (keyId, accessInfo) -> {
        LOGGER.info("ACCESS_SETTER_WITH_CACHE, keyId = {}, accessInfo = {}", keyId, accessInfo);
        if (isBlank(keyId) || isNull(accessInfo))
            return error(() -> new BlueException(BAD_REQUEST));

        return reactiveStringRedisTemplate.opsForValue()
                .set(keyId, GSON.toJson(accessInfo), globalExpireDuration)
                .publishOn(scheduler)
                .onErrorResume(throwable -> {
                    LOGGER.error("setAccessInfo(String keyId, String accessInfo) failed, throwable = {}", throwable);
                    return just(false);
                })
                .doOnSuccess(ig -> cache.invalidate(keyId));
    };

    /**
     * accessInfo parser
     */
    private final Function<String, AccessInfo> ACCESS_INFO_PARSER = authInfoStr -> {
        try {
            return GSON.fromJson(authInfoStr, AccessInfo.class);
        } catch (JsonSyntaxException e) {
            throw new BlueException(UNAUTHORIZED);
        }
    };

    /**
     * get accessInfo value by keyId
     *
     * @param keyId
     * @return
     */
    public Mono<AccessInfo> getAccessInfo(String keyId) {
        return isNotBlank(keyId) ?
                ACCESS_GETTER_WITH_CACHE.apply(keyId).publishOn(scheduler)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                        .flatMap(v ->
                                isNotBlank(v) ?
                                        just(ACCESS_INFO_PARSER.apply(v))
                                                .doOnSuccess(accessInfo ->
                                                        ACCESS_EXPIRE_PROCESSOR.accept(keyId, accessInfo))
                                        :
                                        error(() -> new BlueException(UNAUTHORIZED)))
                :
                error(() -> new BlueException(UNAUTHORIZED));
    }

    /**
     * cache accessInfo
     *
     * @param keyId
     * @param accessInfo
     */
    public Mono<Boolean> setAccessInfo(String keyId, AccessInfo accessInfo) {
        LOGGER.info("setAccessInfo(), keyId = {}, accessInfo = {}", keyId, accessInfo);
        return ACCESS_SETTER_WITH_CACHE.apply(keyId, accessInfo);
    }

    /**
     * invalid authInfo
     *
     * @param keyId
     */
    public Mono<Boolean> invalidAccessInfo(String keyId) {
        LOGGER.info("invalidAuthInfo(), keyId = {}", keyId);
        return isNotBlank(keyId) ?
                reactiveStringRedisTemplate.delete(keyId)
                        .publishOn(scheduler)
                        .map(l -> l > 0L)
                        .doOnEach(ig -> cache.invalidate(keyId))
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
                cache.invalidate(keyId);

            return just(true).publishOn(scheduler);
        } catch (Exception e) {
            return just(false).publishOn(scheduler);
        }
    }

    /**
     * assert conf
     */
    private static void assertConf(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessBatchExpireProcessor accessBatchExpireProcessor, Integer refresherCorePoolSize,
                                   Integer refresherMaximumPoolSize, Long refresherKeepAliveSeconds, Integer refresherBlockingQueueCapacity,
                                   Long globalExpiresMillis, Long localExpiresMillis, Long millisLeftToHandleExpire, Integer capacity) {
        if (isNull(reactiveStringRedisTemplate))
            throw new RuntimeException("reactiveStringRedisTemplate can't be null");

        if (isNull(accessBatchExpireProcessor))
            throw new RuntimeException("accessBatchExpireProcessor can't be null");

        if (isNull(refresherCorePoolSize) || refresherCorePoolSize < 1)
            throw new RuntimeException("refresherCorePoolSize can't be null or less than 1");

        if (isNull(refresherMaximumPoolSize) || refresherMaximumPoolSize < 1)
            throw new RuntimeException("refresherMaximumPoolSize can't be null or less than 1");

        if (isNull(refresherKeepAliveSeconds) || refresherKeepAliveSeconds < 1)
            throw new RuntimeException("refresherKeepAliveSeconds can't be null or less than 1");

        if (isNull(refresherBlockingQueueCapacity) || refresherBlockingQueueCapacity < 1)
            throw new RuntimeException("refresherBlockingQueueCapacity can't be null or less than 1");

        if (isNull(globalExpiresMillis) || globalExpiresMillis < 1L)
            throw new RuntimeException("globalExpiresSecond can't be null or less than 1");

        if (isNull(localExpiresMillis) || localExpiresMillis < 1L)
            throw new RuntimeException("localExpiresSecond can't be null or less than 1");

        if (localExpiresMillis > globalExpiresMillis)
            throw new RuntimeException("localExpiresSecond can't be null or greater than globalExpiresSecond");

        if (isNull(millisLeftToHandleExpire) || millisLeftToHandleExpire < 1)
            throw new RuntimeException("millisLeftToHandleExpire can't be null or less than 1");

        if (globalExpiresMillis <= millisLeftToHandleExpire)
            throw new RuntimeException("globalExpiresMillis can't be less than  or equals millisLeftToHandleExpire");

        if (isNull(capacity) || capacity < 1)
            throw new RuntimeException("capacity can't be null or less than 1");
    }

}