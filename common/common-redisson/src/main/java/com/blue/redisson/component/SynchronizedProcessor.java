package com.blue.redisson.component;

import com.blue.basic.model.exps.BlueException;
import com.blue.redisson.api.inter.HandleTask;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.blue.basic.common.base.BlueChecker.*;
import static com.blue.basic.constant.common.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * redisson sync processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration", "unused"})
public final class SynchronizedProcessor {

    private static final Logger LOGGER = getLogger(SynchronizedProcessor.class);

    private final RedissonClient redissonClient;

    public SynchronizedProcessor(RedissonClient redissonClient, long defaultMaxWaitingMillis) {
        this.redissonClient = redissonClient;
        DEFAULT_MAX_WAITING_MILLIS = defaultMaxWaitingMillis;
    }

    private final long DEFAULT_MAX_WAITING_MILLIS;

    private final boolean BREAK_ON_LOCK_FAIL = false;

    /**
     * get redisson client
     *
     * @return
     */
    public RedissonClient getRedissonClient() {
        return this.redissonClient;
    }

    /**
     * get default max waiting millis
     *
     * @return
     */
    public long getDefaultMaxWaitingMillis() {
        return this.DEFAULT_MAX_WAITING_MILLIS;
    }

    /**
     * get data from sups and handle cache
     *
     * @param syncKey
     * @param firstSup
     * @param secondSup
     * @param firstSupStorage
     * @param validator
     * @param <T>
     * @return
     */
    public <T> T handleSupByOrderedWithSetter(String syncKey, Supplier<T> firstSup, Supplier<T> secondSup, Consumer<T> firstSupStorage, Predicate<T> validator) {
        if (isBlank(syncKey) || isNull(firstSup) || isNull(secondSup) || isNull(firstSupStorage) || isNull(validator))
            throw new BlueException(INVALID_PARAM);

        return ofNullable(firstSup.get())
                .filter(validator)
                .orElseGet(() -> {
                    RLock lock = null;
                    boolean locked = false;
                    try {
                        locked = (lock = redissonClient.getLock(syncKey)).tryLock(DEFAULT_MAX_WAITING_MILLIS, MILLISECONDS);

                        if (!locked) {
                            lock.lock();
                            locked = true;
                        }

                        T res;
                        if (validator.test(res = firstSup.get()))
                            return res;

                        if (validator.test(res = secondSup.get()))
                            firstSupStorage.accept(res);

                        return res;
                    } catch (Exception e) {
                        LOGGER.error("handle failed, syncKey = {}, firstSup = {}, secondSup = {}, firstSupStorage = {}, validator = {}, e = {}", syncKey, firstSup, secondSup, firstSupStorage, validator, e);
                        if (e instanceof RuntimeException)
                            throw (RuntimeException) e;

                        throw new BlueException(INTERNAL_SERVER_ERROR);
                    } finally {
                        if (isNotNull(lock) && locked)
                            try {
                                lock.unlock();
                            } catch (Exception e) {
                                LOGGER.warn("lock.unlock()  failed, e = {}", e.getMessage());
                            }
                    }
                });
    }

    /**
     * get data from sup with sync
     *
     * @param syncKey
     * @param handlerSup
     * @param fallbackSup
     * @param <T>
     * @return
     */
    public <T> T handleSupWithSync(String syncKey, Supplier<T> handlerSup, Supplier<T> fallbackSup) {
        if (isBlank(syncKey) || isNull(handlerSup))
            throw new BlueException(INVALID_PARAM);

        RLock lock = null;
        try {
            lock = redissonClient.getLock(syncKey);
            lock.lock();

            return handlerSup.get();
        } catch (Exception e) {
            LOGGER.error("handle failed, syncKey = {}, handlerSup = {}, fallbackSup = {}, e = {}",
                    syncKey, handlerSup, fallbackSup, e);

            if (isNotNull(fallbackSup))
                return fallbackSup.get();

            throw e;
        } finally {
            if (isNotNull(lock) && lock.isLocked())
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.error("lock.unlock() failed, e = {}", e.getMessage());
                }
        }
    }

    /**
     * get data from sup with lock
     *
     * @param syncKey
     * @param handlerSup
     * @param <T>
     * @return
     */
    public <T> T handleSupWithSync(String syncKey, Supplier<T> handlerSup) {
        return this.handleSupWithSync(syncKey, handlerSup, null);
    }

    /**
     * get data from sup with try lock
     *
     * @param syncKey
     * @param handlerSup
     * @param waitedSup
     * @param fallbackSup
     * @param maxWaitingMillis
     * @param <T>
     * @return
     */
    public <T> T handleSupWithTryLock(String syncKey, Supplier<T> handlerSup, Supplier<T> waitedSup, Supplier<T> fallbackSup, long maxWaitingMillis) {
        if (isBlank(syncKey) || isNull(handlerSup) || isNull(waitedSup) || maxWaitingMillis < 0L)
            throw new BlueException(INVALID_PARAM);

        RLock lock = null;
        boolean locked = false;
        try {
            lock = redissonClient.getLock(syncKey);
            locked = lock.tryLock();

            if (locked)
                return handlerSup.get();

            locked = lock.tryLock(maxWaitingMillis, MILLISECONDS);

            return waitedSup.get();
        } catch (Exception e) {
            LOGGER.error("handle failed, syncKey = {}, handlerSup = {}, waitedSup = {}, fallbackSup = {}, maxWaitingMillis = {}",
                    syncKey, handlerSup, waitedSup, fallbackSup, maxWaitingMillis);

            if (isNotNull(fallbackSup))
                return fallbackSup.get();

            if (e instanceof RuntimeException)
                throw (RuntimeException) e;

            throw new BlueException(INTERNAL_SERVER_ERROR);
        } finally {
            if (isNotNull(lock) && locked)
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.warn("lock.unlock() failed, e = {}", e.getMessage());
                }
        }
    }

    /**
     * get data from sup with try lock
     *
     * @param syncKey
     * @param handlerSup
     * @param waitedSup
     * @param fallbackSup
     * @param <T>
     * @return
     */
    public <T> T handleSupWithTryLock(String syncKey, Supplier<T> handlerSup, Supplier<T> waitedSup, Supplier<T> fallbackSup) {
        return this.handleSupWithTryLock(syncKey, handlerSup, waitedSup, fallbackSup, DEFAULT_MAX_WAITING_MILLIS);
    }

    /**
     * get data from sup with try lock
     *
     * @param syncKey
     * @param handlerSup
     * @param waitedSup
     * @param maxWaitingMillis
     * @param <T>
     * @return
     */
    public <T> T handleSupWithTryLock(String syncKey, Supplier<T> handlerSup, Supplier<T> waitedSup, long maxWaitingMillis) {
        return this.handleSupWithTryLock(syncKey, handlerSup, waitedSup, null, maxWaitingMillis);
    }

    /**
     * get data from sup with try lock
     *
     * @param syncKey
     * @param handlerSup
     * @param waitedSup
     * @param <T>
     * @return
     */
    public <T> T handleSupWithTryLock(String syncKey, Supplier<T> handlerSup, Supplier<T> waitedSup) {
        return this.handleSupWithTryLock(syncKey, handlerSup, waitedSup, null, DEFAULT_MAX_WAITING_MILLIS);
    }

    /**
     * handle task with sync
     *
     * @param syncKey
     * @param handleTask
     * @param fallbackTask
     */
    public void handleTaskWithSync(String syncKey, HandleTask handleTask, HandleTask fallbackTask) {
        if (isBlank(syncKey) || isNull(handleTask))
            throw new BlueException(INVALID_PARAM);

        RLock lock = null;
        try {
            lock = redissonClient.getLock(syncKey);
            lock.lock();

            handleTask.handle();
        } catch (Exception e) {
            LOGGER.error("handle failed, syncKey = {}, handleTask = {}, fallbackTask = {}, e = {}",
                    syncKey, handleTask, fallbackTask, e);

            if (isNotNull(fallbackTask)) {
                fallbackTask.handle();
                return;
            }

            throw e;
        } finally {
            if (isNotNull(lock) && lock.isLocked())
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.error("lock.unlock() failed, e = {}", e.getMessage());
                }
        }
    }

    /**
     * handle task with lock
     *
     * @param syncKey
     * @param handleTask
     */
    public void handleTaskWithSync(String syncKey, HandleTask handleTask) {
        this.handleTaskWithSync(syncKey, handleTask, null);
    }

    /**
     * handle task with try lock
     *
     * @param syncKey
     * @param handleTask
     * @param breakOnTryLockFail
     */
    public void handleTaskWithTryLock(String syncKey, HandleTask handleTask, HandleTask fallbackTask, boolean breakOnTryLockFail) {
        if (isBlank(syncKey) || isNull(handleTask))
            throw new BlueException(INVALID_PARAM);

        RLock lock = null;
        boolean locked = false;
        try {
            lock = redissonClient.getLock(syncKey);
            locked = lock.tryLock();

            if (!locked && breakOnTryLockFail)
                return;

            lock.lock();
            locked = true;

            handleTask.handle();
        } catch (Exception e) {
            LOGGER.error("handle failed, syncKey = {}, handleTask = {}, fallbackTask = {}, breakOnTryLockFail = {}, e = {}",
                    syncKey, handleTask, fallbackTask, breakOnTryLockFail, e);

            if (isNotNull(fallbackTask)) {
                fallbackTask.handle();
                return;
            }

            throw e;
        } finally {
            if (isNotNull(lock) && locked)
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.warn("lock.unlock() failed, e = {}", e.getMessage());
                }
        }
    }

    /**
     * handle task with try lock
     *
     * @param syncKey
     * @param handleTask
     * @param fallbackTask
     */
    public void handleTaskWithTryLock(String syncKey, HandleTask handleTask, HandleTask fallbackTask) {
        this.handleTaskWithTryLock(syncKey, handleTask, fallbackTask, BREAK_ON_LOCK_FAIL);
    }

    /**
     * handle task with try lock
     *
     * @param syncKey
     * @param handleTask
     * @param breakOnLockFail
     */
    public void handleTaskWithTryLock(String syncKey, HandleTask handleTask, boolean breakOnLockFail) {
        this.handleTaskWithTryLock(syncKey, handleTask, null, breakOnLockFail);
    }

    /**
     * handle task with try lock
     *
     * @param syncKey
     * @param handleTask
     */
    public void handleTaskWithTryLock(String syncKey, HandleTask handleTask) {
        this.handleTaskWithTryLock(syncKey, handleTask, null, BREAK_ON_LOCK_FAIL);
    }

}
