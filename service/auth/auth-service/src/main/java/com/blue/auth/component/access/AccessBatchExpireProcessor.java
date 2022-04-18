package com.blue.auth.component.access;

import com.blue.base.model.base.KeyExpireParam;
import com.blue.base.model.exps.BlueException;
import net.openhft.affinity.AffinityThreadFactory;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.util.Logger;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.blue.base.common.base.BlueChecker.isNotNull;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.of;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
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
public final class AccessBatchExpireProcessor {

    private static final Logger LOGGER = getLogger(AccessBatchExpireProcessor.class);

    private static final long THREAD_KEEP_ALIVE_SECONDS = 64L;

    private LinkedBlockingQueue<KeyExpireParam> bufferQueue;

    private final StringRedisTemplate stringRedisTemplate;

    private ExecutorService executorService;

    @SuppressWarnings({"FieldCanBeLocal"})
    private final ScheduledExecutorService batchExpireSchedule;

    private final int BATCH_EXPIRE_MAX_PER_HANDLE;

    private static final TimeUnit TIME_UNIT = MILLISECONDS;
    private static final String SCHEDULED_THREAD_NAME_PRE = "AccessBatchExpireProcessor-scheduled-thread- ";
    private static final String HANDLE_THREAD_NAME_PRE = "AccessBatchExpireProcessor-handle-thread- ";
    private static final int RANDOM_LEN = 4;

    public AccessBatchExpireProcessor(StringRedisTemplate stringRedisTemplate,
                                      Integer batchExpireMaxPerHandle, Integer batchExpireScheduledCorePoolSize,
                                      Long batchExpireScheduledInitialDelayMillis,
                                      Long batchExpireScheduledDelayMillis, Integer batchExpireQueueCapacity) {
        if (isNull(stringRedisTemplate))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "stringRedisTemplate can't be null");
        if (isNull(batchExpireMaxPerHandle) || batchExpireMaxPerHandle < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "batchExpireMaxPerHandle can't be null");
        if (isNull(batchExpireScheduledCorePoolSize) || batchExpireScheduledCorePoolSize < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "batchExpireScheduledCorePoolSize can't be null");
        if (isNull(batchExpireScheduledInitialDelayMillis) || batchExpireScheduledInitialDelayMillis < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "batchExpireScheduledInitialDelayMillis can't be null or less than 1");
        if (isNull(batchExpireScheduledDelayMillis) || batchExpireScheduledDelayMillis < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "batchExpireScheduledDelayMillis can't be null or less than 1");
        if (isNull(batchExpireQueueCapacity) || batchExpireQueueCapacity < 1)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "batchExpireQueueCapacity can't be null or less than 1");

        this.stringRedisTemplate = stringRedisTemplate;

        this.bufferQueue = new LinkedBlockingQueue<>(batchExpireQueueCapacity);

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.error("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution, : r = {}", r);
            r.run();
        };

        this.batchExpireSchedule = new ScheduledThreadPoolExecutor(batchExpireScheduledCorePoolSize,
                new AffinityThreadFactory(SCHEDULED_THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN), SAME_CORE),
                rejectedExecutionHandler);

        this.BATCH_EXPIRE_MAX_PER_HANDLE = batchExpireMaxPerHandle;

        this.batchExpireSchedule.scheduleWithFixedDelay(this::scheduledExpireTask, batchExpireScheduledInitialDelayMillis, batchExpireScheduledDelayMillis, TIME_UNIT);

        this.executorService = new ThreadPoolExecutor(batchExpireScheduledCorePoolSize, batchExpireScheduledCorePoolSize,
                THREAD_KEEP_ALIVE_SECONDS, SECONDS, new ArrayBlockingQueue<>(batchExpireQueueCapacity),
                new AffinityThreadFactory(HANDLE_THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN), SAME_CORE),
                rejectedExecutionHandler);
    }

    /**
     * convert duration to seconds
     */
    private static final BiFunction<Long, ChronoUnit, Long> SECONDS_CONVERTER = (expire, unit) ->
            of(expire, unit).toSeconds();

    /**
     * ignore fail sync putter
     */
    private final Consumer<KeyExpireParam> IGNORE_FAIL_ELEMENT_PUTTER = keyExpireParam -> {
        if (!bufferQueue.offer(keyExpireParam))
            LOGGER.warn("MAYBE_FAILED_ELEMENT_PUTTER failed, keyExpireParam = {}", keyExpireParam);
    };

    /**
     * async putter
     */
    private final Consumer<KeyExpireParam> ASYNC_ELEMENT_PUTTER = keyExpireParam ->
            executorService.execute(() -> IGNORE_FAIL_ELEMENT_PUTTER.accept(keyExpireParam));

    /**
     * element getter, maybe return null
     */
    private final Supplier<KeyExpireParam> NULLABLE_ELEMENT_GETTER = () -> bufferQueue.poll();

    /**
     * expire data and release data to queue b
     */
    private final BiConsumer<KeyExpireParam, RedisConnection> DATA_EXPIRE_WITH_WRAPPER_RELEASE_HANDLER = (keyExpireParam, connection) ->
            connection.expire(keyExpireParam.getKey().getBytes(UTF_8), SECONDS_CONVERTER.apply(keyExpireParam.getExpire(), keyExpireParam.getUnit()));

    /**
     * data expire task
     */
    private void handleExpireTask() {
        KeyExpireParam firstData = NULLABLE_ELEMENT_GETTER.get();
        if (isNotNull(firstData)) {
            stringRedisTemplate.executePipelined((RedisCallback<Boolean>) connection -> {
                DATA_EXPIRE_WITH_WRAPPER_RELEASE_HANDLER.accept(firstData, connection);

                int size = 1;
                KeyExpireParam data;
                while (size <= BATCH_EXPIRE_MAX_PER_HANDLE && isNotNull(data = NULLABLE_ELEMENT_GETTER.get())) {
                    DATA_EXPIRE_WITH_WRAPPER_RELEASE_HANDLER.accept(data, connection);
                    size++;
                }

                LOGGER.info("refreshed size: {}", size);
                return null;
            });
        }
    }

    /**
     * scheduled expire task
     */
    private void scheduledExpireTask() {
        try {
            handleExpireTask();
        } catch (Exception e) {
            LOGGER.error("void scheduledExpireTask(), e = {}", e);
        }
    }

    /**
     * params asserter
     */
    private static final Consumer<KeyExpireParam> DATA_ASSERTER = keyExpireParam -> {
        if (isNull(keyExpireParam))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "keyExpireParam can't be null");

        if (isBlank(keyExpireParam.getKey()))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "key can't be null or ''");

        Long expire = keyExpireParam.getExpire();
        if (isNull(expire) || expire < 1L)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "expire can't be null or less than 1");

        if (isNull(keyExpireParam.getUnit()))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "unit can't be null");
    };

    /**
     * timeout for refresh
     *
     * @param keyExpireParam
     * @return
     */
    public void expireKey(KeyExpireParam keyExpireParam) {
        LOGGER.info("expireKey(KeyExpireParam keyExpireParam), keyExpireParam = {}", keyExpireParam);
        DATA_ASSERTER.accept(keyExpireParam);

        ASYNC_ELEMENT_PUTTER.accept(keyExpireParam);
    }

}
