package com.blue.secure.component.auth;

import com.blue.base.model.redis.KeyExpireParam;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.util.Logger;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.Thread.onSpinWait;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.of;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.util.Loggers.getLogger;

/**
 * refresh auth processor
 *
 * @author liuyunfei
 * @date 2021/8/19
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "AlibabaAvoidManuallyCreateThread", "AliControlFlowStatementWithoutBraces"})
public final class AuthBatchExpireProcessor {

    private static final Logger LOGGER = getLogger(AuthBatchExpireProcessor.class);

    private static final long THREAD_KEEP_ALIVE_SECONDS = 64L;

    private final LinkedBlockingQueue<ExpireData> QUEUE_FOR_HANDLE = new LinkedBlockingQueue<>();
    private final LinkedBlockingQueue<ExpireData> QUEUE_FOR_PACKAGE = new LinkedBlockingQueue<>();

    private final StringRedisTemplate stringRedisTemplate;

    private final ExecutorService executorService;

    @SuppressWarnings({"FieldCanBeLocal"})
    private final ScheduledExecutorService batchExpireSchedule;

    private final int BATCH_EXPIRE_MAX_PER_HANDLE;

    private static final TimeUnit TIME_UNIT = MILLISECONDS;
    private static final String SCHEDULED_THREAD_NAME_PRE = "AuthBatchExpireProcessor-scheduled-thread- ";
    private static final String HANDLE_THREAD_NAME_PRE = "AuthBatchExpireProcessor-handle-thread- ";
    private static final int RANDOM_LEN = 4;

    public AuthBatchExpireProcessor(StringRedisTemplate stringRedisTemplate,
                                    Integer batchExpireMaxPerHandle, Integer batchExpireScheduledCorePoolSize,
                                    Long batchExpireScheduledInitialDelayMillis,
                                    Long batchExpireScheduledDelayMillis, Integer batchExpireQueueCapacity) {

        if (stringRedisTemplate == null)
            throw new RuntimeException("stringRedisTemplate can't be null");
        if (batchExpireMaxPerHandle == null || batchExpireMaxPerHandle < 1)
            throw new RuntimeException("batchExpireMaxPerHandle can't be null");
        if (batchExpireScheduledCorePoolSize == null || batchExpireScheduledCorePoolSize < 1)
            throw new RuntimeException("batchExpireScheduledCorePoolSize can't be null");
        if (batchExpireScheduledInitialDelayMillis == null || batchExpireScheduledInitialDelayMillis < 1L)
            throw new RuntimeException("batchExpireScheduledInitialDelayMillis can't be null or less than 1");
        if (batchExpireScheduledDelayMillis == null || batchExpireScheduledDelayMillis < 1L)
            throw new RuntimeException("batchExpireScheduledDelayMillis can't be null or less than 1");
        if (batchExpireQueueCapacity == null || batchExpireQueueCapacity < 1)
            throw new RuntimeException("batchExpireQueueCapacity can't be null or less than 1");

        this.stringRedisTemplate = stringRedisTemplate;

        try {
            for (int i = 0; i < batchExpireQueueCapacity; i++)
                QUEUE_FOR_PACKAGE.put(new ExpireData());
        } catch (InterruptedException e) {
            throw new RuntimeException("QUEUE_FOR_PACKAGE put ExpireData failed, e = {0}", e);
        }

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.error("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution, : r = {}", r);
            r.run();
        };

        batchExpireSchedule = new ScheduledThreadPoolExecutor(batchExpireScheduledCorePoolSize, r -> {
            Thread thread = new Thread(r, SCHEDULED_THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN));
            thread.setDaemon(true);
            return thread;
        }, rejectedExecutionHandler);

        BATCH_EXPIRE_MAX_PER_HANDLE = batchExpireMaxPerHandle;

        this.batchExpireSchedule.scheduleWithFixedDelay(this::handleExpire, batchExpireScheduledInitialDelayMillis, batchExpireScheduledDelayMillis, TIME_UNIT);

        this.executorService = new ThreadPoolExecutor(batchExpireScheduledCorePoolSize, batchExpireScheduledCorePoolSize,
                THREAD_KEEP_ALIVE_SECONDS, SECONDS, new ArrayBlockingQueue<>(batchExpireQueueCapacity), r -> {
            Thread thread = new Thread(r, HANDLE_THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN));
            thread.setDaemon(true);
            return thread;
        }, rejectedExecutionHandler);
    }


    /**
     * convert duration to seconds
     */
    private static final BiFunction<Long, ChronoUnit, Long> SECONDS_CONVERTER = (expire, unit) ->
            of(expire, unit).toSeconds();

    /**
     * params asserter
     */
    private static final Consumer<KeyExpireParam> DATA_ASSERTER = keyExpireParam -> {
        if (keyExpireParam == null)
            throw new RuntimeException("keyExpireParam can't be null");

        if (isBlank(keyExpireParam.getKey()))
            throw new RuntimeException("key can't be null or ''");

        Long expire = keyExpireParam.getExpire();
        if (expire == null || expire < 1L)
            throw new RuntimeException("expire can't be null or less than 1");

        if (keyExpireParam.getUnit() == null)
            throw new RuntimeException("unit can't be null");
    };

    /**
     * putter
     */
    private final BiFunction<ExpireData, LinkedBlockingQueue<ExpireData>, Boolean> FAILED_STATUS_PUTTER = (expireData, queue) -> {
        try {
            queue.put(expireData);
            return false;
        } catch (InterruptedException e) {
            return true;
        }
    };

    /**
     * putter
     */
    private final BiConsumer<ExpireData, LinkedBlockingQueue<ExpireData>> BLOCKING_ELEMENT_PUTTER = (expireData, queue) -> {
        while (FAILED_STATUS_PUTTER.apply(expireData, queue))
            onSpinWait();
    };

    /**
     * getter
     */
    private final Function<LinkedBlockingQueue<ExpireData>, ExpireData> BLOCKING_ELEMENT_GETTER = queue -> {
        try {
            return queue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException("BLOCKING_ELEMENT_GETTER failed, e = {}", e);
        }
    };

    /**
     * getter
     */
    private final Function<LinkedBlockingQueue<ExpireData>, ExpireData> NON_BLOCKING_ELEMENT_GETTER = LinkedBlockingQueue::poll;

    /**
     * expire executor
     */
    private final BiConsumer<ExpireData, RedisConnection> DATA_EXPIRE_EXECUTOR = (expireData, connection) -> {
        try {
            connection.expire(expireData.key.getBytes(UTF_8), expireData.expireSeconds);
        } catch (Exception e) {
            LOGGER.error("expireData(ExpireData expireData, RedisConnection connection) failed, expireData = {}, e = {}", expireData, e);
        } finally {
            BLOCKING_ELEMENT_PUTTER.accept(expireData, QUEUE_FOR_PACKAGE);
        }
    };

    /**
     * expire refresher
     */
    private final Consumer<KeyExpireParam> DATA_EXPIRE_HANDLER = keyExpireParam -> {
        DATA_ASSERTER.accept(keyExpireParam);
        try {
            ExpireData expireData = BLOCKING_ELEMENT_GETTER.apply(QUEUE_FOR_PACKAGE);
            expireData.key = keyExpireParam.getKey();
            expireData.expireSeconds = SECONDS_CONVERTER.apply(keyExpireParam.getExpire(), keyExpireParam.getUnit());
            BLOCKING_ELEMENT_PUTTER.accept(expireData, QUEUE_FOR_HANDLE);
        } catch (Exception e) {
            LOGGER.error("DATA_EXPIRE_HANDLER failed, keyExpireParam 未能完成刷新, keyExpireParam = {}, e = {}", keyExpireParam, e);
        }
    };


    /**
     * expire interval
     */
    private void handleExpire() {
        try {
            ExpireData data = NON_BLOCKING_ELEMENT_GETTER.apply(QUEUE_FOR_HANDLE);
            if (data != null) {
                stringRedisTemplate.executePipelined((RedisCallback<Boolean>) connection -> {
                    int size = 1;
                    DATA_EXPIRE_EXECUTOR.accept(data, connection);
                    ExpireData ed;
                    while ((ed = NON_BLOCKING_ELEMENT_GETTER.apply(QUEUE_FOR_HANDLE)) != null && size <= BATCH_EXPIRE_MAX_PER_HANDLE) {
                        size++;
                        DATA_EXPIRE_EXECUTOR.accept(ed, connection);
                    }

                    LOGGER.info("refreshed size: {}", size);
                    return null;
                });
            }
        } catch (Exception e) {
            LOGGER.error("handle() failed, e = {0}", e);
        }
    }

    /**
     * timeout for refresh
     *
     * @param keyExpireParam
     * @return
     */
    public void expireKey(KeyExpireParam keyExpireParam) {
        LOGGER.info("expireKey(KeyExpireParam keyExpireParam), keyExpireParam = {}", keyExpireParam);
        executorService.execute(() -> DATA_EXPIRE_HANDLER.accept(keyExpireParam));
    }

    /**
     * expire data info
     */
    static final class ExpireData {
        String key;
        long expireSeconds;
    }

}
