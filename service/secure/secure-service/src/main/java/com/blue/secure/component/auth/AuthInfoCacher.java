package com.blue.secure.component.auth;

import com.blue.base.model.exps.BlueException;
import com.blue.base.model.redis.KeyExpireParam;
import com.blue.caffeine.api.conf.CaffeineConf;
import com.blue.caffeine.api.conf.CaffeineConfParams;
import com.blue.secure.config.mq.producer.AuthExpireProducer;
import com.github.benmanes.caffeine.cache.Cache;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.UNAUTHORIZED;
import static com.blue.caffeine.api.generator.BlueCaffeineGenerator.generateCache;
import static com.blue.caffeine.constant.ExpireStrategy.AFTER_WRITE;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.springframework.util.StringUtils.hasText;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.justOrEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * authInfo缓存器
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class AuthInfoCacher {

    private static final Logger LOGGER = getLogger(AuthInfoCacher.class);

    private ReactiveStringRedisTemplate reactiveStringRedisTemplate;

    private AuthExpireProducer authExpireProducer;

    private ExecutorService executorService;

    /**
     * 过期时间
     */
    private Long globalExpireMillis;

    /**
     * 过期时间单位
     */
    private final ChronoUnit UNIT = MILLIS;

    /**
     * 全局过期时间
     */
    private final Duration globalExpireDuration;

    /**
     * 本地缓存器
     */
    private final Cache<String, String> cacher;

    private static final String THREAD_NAME_PRE = "JwtCacher-thread- ";
    private static final int RANDOM_LEN = 4;

    public AuthInfoCacher(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AuthExpireProducer authExpireProducer,
                          Integer refresherCorePoolSize, Integer refresherMaximumPoolSize, Long refresherKeepAliveTime,
                          Integer refresherBlockingQueueCapacity, Long globalExpireMillis, Long localExpireMillis, Integer capacity) {

        confAsserter(reactiveStringRedisTemplate, authExpireProducer,
                refresherCorePoolSize, refresherMaximumPoolSize, refresherKeepAliveTime,
                refresherBlockingQueueCapacity, globalExpireMillis, localExpireMillis, capacity);

        this.reactiveStringRedisTemplate = reactiveStringRedisTemplate;
        this.authExpireProducer = authExpireProducer;

        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r, THREAD_NAME_PRE + RandomStringUtils.randomAlphabetic(RANDOM_LEN));
            thread.setDaemon(true);
            return thread;
        };

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.error("处理JWT刷新任务推送的独立线程池任务堆积: r = {}", r);
            r.run();
        };

        this.executorService = new ThreadPoolExecutor(refresherCorePoolSize, refresherMaximumPoolSize,
                refresherKeepAliveTime, SECONDS, new ArrayBlockingQueue<>(refresherBlockingQueueCapacity), threadFactory, rejectedExecutionHandler);

        this.globalExpireMillis = globalExpireMillis;
        this.globalExpireDuration = Duration.of(globalExpireMillis, UNIT);

        CaffeineConf caffeineConf = new CaffeineConfParams(capacity, Duration.of(localExpireMillis, MILLIS),
                AFTER_WRITE, executorService);

        this.cacher = generateCache(caffeineConf);
    }

    /**
     * redis authInfo 刷新器
     */
    private final BiConsumer<String, String> REDIS_AUTH_REFRESHER = (keyId, authInfo) -> {
        try {
            this.executorService.execute(() -> {
                if (hasText(keyId) && hasText(authInfo)) {
                    authExpireProducer.send(new KeyExpireParam(keyId, globalExpireMillis, UNIT));
                    LOGGER.warn("REDIS_AUTH_REFRESHER -> SUCCESS, keyId = {}", keyId);
                } else {
                    LOGGER.error("keyId or authInfo is empty, keyId = {}, authInfo = {}", keyId, authInfo);
                }
            });
        } catch (Exception e) {
            LOGGER.error("REDIS_AUTH_REFRESHER error, e = {}", e);
        }
    };

    /**
     * redis authInfo 获取器
     */
    private final Function<String, Mono<String>> REDIS_AUTH_GETTER = keyId -> {
        LOGGER.warn("REDIS_AUTH_GETTER, get authInfo from redis, keyId = {}", keyId);

        return reactiveStringRedisTemplate.opsForValue().get(keyId)
                .switchIfEmpty(just(""))
                .flatMap(authInfo -> {
                    if (!"".equals(authInfo))
                        REDIS_AUTH_REFRESHER.accept(keyId, authInfo);
                    return just(authInfo);
                });
    };

    /**
     * 根据keyId获取authInfo value
     *
     * @param keyId
     * @return
     */
    public Mono<String> getAuthInfo(String keyId) {
        if (keyId == null || "".equals(keyId))
            throw new BlueException(UNAUTHORIZED.status, UNAUTHORIZED.code, UNAUTHORIZED.message);

        return justOrEmpty(cacher.getIfPresent(keyId))
                .switchIfEmpty(REDIS_AUTH_GETTER.apply(keyId));
    }

    /**
     * 缓存authInfo
     *
     * @param keyId
     * @param authInfo
     */
    public Mono<Boolean> setAuthInfo(String keyId, String authInfo) {
        LOGGER.info("setAuthInfo(), keyId = {},authInfo = {}", keyId, authInfo);

        return reactiveStringRedisTemplate.opsForValue()
                .set(keyId, authInfo, globalExpireDuration)
                .onErrorResume(throwable -> {
                    LOGGER.error("setAuthInfo(String keyId, String authInfo) failed, throwable = {}", throwable);
                    return just(false);
                });
    }

    /**
     * 清除authInfo
     *
     * @param keyId
     */
    public Mono<Boolean> invalidAuthInfo(String keyId) {
        LOGGER.info("invalidAuthInfo(), keyId = {}", keyId);
        return reactiveStringRedisTemplate.delete(keyId)
                .flatMap(l -> {
                    cacher.invalidate(keyId);
                    return just(l > 0L);
                });
    }

    /**
     * 清除本地authInfo
     *
     * @param keyId
     */
    public Mono<Boolean> invalidLocalAuthInfo(String keyId) {
        LOGGER.info("invalidLocalAuthInfo(), keyId = {}", keyId);
        try {
            if (keyId != null && !"".equals(keyId))
                cacher.invalidate(keyId);
            return just(true);
        } catch (Exception e) {
            return just(false);
        }
    }

    /**
     * 参数校验
     */
    private static void confAsserter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AuthExpireProducer authExpireProducer,
                                     Integer refresherCorePoolSize, Integer refresherMaximumPoolSize, Long refresherKeepAliveTime,
                                     Integer refresherBlockingQueueCapacity, Long globalExpireMillis, Long localExpireMillis, Integer capacity) {
        if (reactiveStringRedisTemplate == null)
            throw new RuntimeException("reactiveStringRedisTemplate不能为空");

        if (authExpireProducer == null)
            throw new RuntimeException("authExpireProducer不能为空");

        if (refresherCorePoolSize == null || refresherCorePoolSize < 1)
            throw new RuntimeException("refresherCorePoolSize不能为空或小于1");

        if (refresherMaximumPoolSize == null || refresherMaximumPoolSize < 1)
            throw new RuntimeException("refresherMaximumPoolSize能为空或小于1");

        if (refresherKeepAliveTime == null || refresherKeepAliveTime < 1)
            throw new RuntimeException("refresherKeepAliveTime不能为空或小于1");

        if (refresherBlockingQueueCapacity == null || refresherBlockingQueueCapacity < 1)
            throw new RuntimeException("refresherBlockingQueueCapacity不能为空或小于1");

        if (globalExpireMillis == null || globalExpireMillis < 1L)
            throw new RuntimeException("globalExpireSeconds不能为空或小于1");

        if (localExpireMillis == null || localExpireMillis < 1L)
            throw new RuntimeException("localExpireSeconds不能为空或小于1");

        if (localExpireMillis > globalExpireMillis)
            throw new RuntimeException("localExpireSeconds不能大于globalExpireSeconds");

        if (capacity == null || capacity < 1)
            throw new RuntimeException("capacity can't be null or less than 1");
    }

}
