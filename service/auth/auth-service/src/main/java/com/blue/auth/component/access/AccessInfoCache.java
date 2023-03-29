package com.blue.auth.component.access;

import com.blue.auth.model.AccessInfo;
import com.blue.basic.model.exps.BlueException;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.github.benmanes.caffeine.cache.AsyncCache;
import com.google.gson.JsonSyntaxException;
import net.openhft.affinity.AffinityThreadFactory;
import org.slf4j.Logger;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.common.base.CommonFunctions.GSON;
import static com.blue.basic.common.base.CommonFunctions.MILLIS_STAMP_SUP;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.ResponseElement.UNAUTHORIZED;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCacheAsyncCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.slf4j.LoggerFactory.getLogger;
import static reactor.core.publisher.Mono.*;

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

    private ExecutorService executorService;

    /**
     * global expire millis
     */
    private long globalExpiresMillis;

    /**
     * expire threshold
     */
    private long globalExpiresMillisThreshold;

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
    private AsyncCache<String, String> cache;

    private static final String THREAD_NAME_PRE = "AccessInfoCache-thread-";
    private static final int RANDOM_LEN = 4;
    private static final long SEGMENT_PART = 3L;
    private static final long OFFSET_THRESHOLD = 2L;

    public AccessInfoCache(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessBatchExpireProcessor accessBatchExpireProcessor,
                           Integer refresherCorePoolSize, Integer refresherMaximumPoolSize, Long refresherKeepAliveSeconds,
                           Integer refresherBlockingQueueCapacity, Long globalExpiresMillis, Long localExpiresMillis, Integer capacity) {

        assertConf(reactiveStringRedisTemplate, accessBatchExpireProcessor, refresherCorePoolSize, refresherMaximumPoolSize, refresherKeepAliveSeconds,
                refresherBlockingQueueCapacity, globalExpiresMillis, localExpiresMillis, capacity);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.accessBatchExpireProcessor = accessBatchExpireProcessor;

        ThreadFactory threadFactory = new AffinityThreadFactory(THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN), SAME_CORE);

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.error("task has been reject: r = {}", r);
            r.run();
        };

        this.executorService = new ThreadPoolExecutor(refresherCorePoolSize, refresherMaximumPoolSize,
                refresherKeepAliveSeconds, SECONDS, new ArrayBlockingQueue<>(refresherBlockingQueueCapacity), threadFactory, rejectedExecutionHandler);

        this.globalExpiresMillis = globalExpiresMillis;

        this.globalExpiresMillisThreshold = globalExpiresMillis / SEGMENT_PART * OFFSET_THRESHOLD;

        this.globalExpireDuration = Duration.of(globalExpiresMillis, UNIT);

        CaffeineConf caffeineConf = new CaffeineConfParams(capacity, Duration.of(localExpiresMillis, MILLIS), AFTER_WRITE, executorService);

        this.cache = generateCacheAsyncCache(caffeineConf);
    }

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
     * expire?
     */
    private final BiPredicate<String, String> EXPIRE_PRE = (keyId, accessStr) -> {
        if (isBlank(keyId) && isBlank(accessStr))
            return false;

        Long loginMillisTimeStamp = ACCESS_INFO_PARSER.apply(accessStr).getLoginMillisTimeStamp();
        if (isNull(loginMillisTimeStamp))
            return false;

        long currentMillisTimeStamp = MILLIS_STAMP_SUP.get();

        return (currentMillisTimeStamp & 1L) == 0L && ((currentMillisTimeStamp - loginMillisTimeStamp) % globalExpiresMillis) > globalExpiresMillisThreshold;
    };

    /**
     * redis accessInfo expire
     */
    private final BiConsumer<String, String> ACCESS_EXPIRE_PROCESSOR = (keyId, access) ->
            this.executorService.execute(() -> {
                try {
                    if (EXPIRE_PRE.test(keyId, access))
                        accessBatchExpireProcessor.expireKey(keyId, globalExpiresMillis, UNIT);

                } catch (Exception e) {
                    LOGGER.error("expireKey failed, keyId = {}, access = {}, e = {}", keyId, access, e);
                }
            });

    /**
     * redis accessInfo getter
     */
    private final BiFunction<String, Executor, CompletableFuture<String>> REDIS_ACCESS_WITH_REDIS_CACHE_GETTER = (keyId, executor) ->
            reactiveStringRedisTemplate.opsForValue().get(keyId)
                    .doOnSuccess(v -> {
                        if (isNotBlank(v))
                            ACCESS_EXPIRE_PROCESSOR.accept(keyId, v);
                    }).toFuture();

    /**
     * cache accessInfo getter
     */
    private final Function<String, Mono<String>> ACCESS_GETTER_WITH_CACHE = keyId -> {
        if (isBlank(keyId))
            return error(() -> new BlueException(UNAUTHORIZED));

        return fromFuture(cache.get(keyId, REDIS_ACCESS_WITH_REDIS_CACHE_GETTER));
    };

    /**
     * cache accessInfo setter
     */
    private final BiFunction<String, AccessInfo, Mono<Boolean>> ACCESS_SETTER_WITH_CACHE = (keyId, accessInfo) -> {
        if (isBlank(keyId) || isNull(accessInfo))
            return error(() -> new BlueException(BAD_REQUEST));

        String access = GSON.toJson(accessInfo);
        return reactiveStringRedisTemplate.opsForValue()
                .set(keyId, access, globalExpireDuration)
                .onErrorResume(throwable -> {
                    LOGGER.error("setAccessInfo failed, throwable = {}", throwable);
                    return just(false);
                })
                .doOnSuccess(ig -> cache.put(keyId, supplyAsync(() -> access, executorService)));
    };

    /**
     * get accessInfo value by keyId
     *
     * @param keyId
     * @return
     */
    public Mono<AccessInfo> getAccessInfo(String keyId) {
        return isNotBlank(keyId) ?
                ACCESS_GETTER_WITH_CACHE.apply(keyId)
                        .switchIfEmpty(defer(() -> error(() -> new BlueException(UNAUTHORIZED))))
                        .flatMap(v ->
                                isNotBlank(v) ?
                                        just(ACCESS_INFO_PARSER.apply(v))
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
        return ACCESS_SETTER_WITH_CACHE.apply(keyId, accessInfo);
    }

    /**
     * invalid authInfo
     *
     * @param keyId
     */
    public Mono<Boolean> invalidAccessInfo(String keyId) {
        return isNotBlank(keyId) ?
                reactiveStringRedisTemplate.delete(keyId)
                        .map(l -> l > 0L)
                        .doFinally(ig -> cache.synchronous().invalidate(keyId))
                :
                just(false);
    }

    /**
     * invalid local authInfo
     *
     * @param keyId
     */
    public Mono<Boolean> invalidLocalAccessInfo(String keyId) {
        try {
            if (isNotBlank(keyId))
                cache.synchronous().invalidate(keyId);

            return just(true);
        } catch (Exception e) {
            return just(false);
        }
    }

    /**
     * assert conf
     */
    private static void assertConf(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessBatchExpireProcessor accessBatchExpireProcessor, Integer refresherCorePoolSize,
                                   Integer refresherMaximumPoolSize, Long refresherKeepAliveSeconds, Integer refresherBlockingQueueCapacity,
                                   Long globalExpiresMillis, Long localExpiresMillis, Integer capacity) {
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

        if (isNull(capacity) || capacity < 1)
            throw new RuntimeException("capacity can't be null or less than 1");
    }

}