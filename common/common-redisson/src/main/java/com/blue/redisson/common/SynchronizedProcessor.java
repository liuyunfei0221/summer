package com.blue.redisson.common;

import com.blue.base.model.exps.BlueException;
import com.blue.redisson.api.inter.HandleTask;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import reactor.util.Logger;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.blue.base.common.base.BlueChecker.*;
import static com.blue.base.constant.base.SyncKey.AUTHORITY_UPDATE_SYNC;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

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

    /**
     * get data from sups and handle cache
     *
     * @param firstSup
     * @param validator
     * @param secondSup
     * @param firstSupSetter
     * @param <T>
     * @return
     */
    public <T> T handleSupByOrderedWithSetter(Supplier<T> firstSup, Predicate<T> validator, Supplier<T> secondSup, Consumer<T> firstSupSetter) {
        if (isNull(firstSup) || isNull(validator) || isNull(secondSup) || isNull(firstSupSetter))
            throw new BlueException();

        return ofNullable(firstSup.get())
                .filter(validator)
                .orElseGet(() -> {
                    RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
                    boolean tryLock = true;
                    try {
                        T res = firstSup.get();
                        if (validator.test(res))
                            return res;

                        tryLock = lock.tryLock();

                        if (tryLock) {
                            res = secondSup.get();
                            if (validator.test(res))
                                firstSupSetter.accept(res);

                            return res;
                        }

                        long start = currentTimeMillis();
                        while (!(tryLock = lock.tryLock()) && currentTimeMillis() - start <= DEFAULT_MAX_WAITING_MILLIS)
                            onSpinWait();

                        res = firstSup.get();

                        return validator.test(res) ? res : secondSup.get();
                    } catch (Exception e) {
                        LOGGER.error("selectGenericsWithCache(Supplier<List<T>> firstSup, Supplier<List<T>> dbSup, Consumer<List<T>> cacheSetter) failed," +
                                "cacheSup = {}, secondSup = {}, firstSupSetter = {}, e = {}", firstSup, secondSup, firstSupSetter, e);
                        return secondSup.get();
                    } finally {
                        if (tryLock)
                            try {
                                lock.unlock();
                            } catch (Exception e) {
                                LOGGER.warn("selectGenericsWithCache(Supplier<List<T>> cacheSup, Supplier<List<T>> dbSup, Consumer<List<T>> cacheSetter) lock.unlock() failed," +
                                        "cacheSup = {}, dbSup = {}, cacheSetter = {}, e = {}", firstSup, secondSup, firstSupSetter, e);
                            }
                    }
                });
    }

    /**
     * get data from sup with lock
     *
     * @param syncKey
     * @param handlerSup
     * @param fallbackSup
     * @param <T>
     * @return
     */
    public <T> T handleSupWithLock(String syncKey, Supplier<T> handlerSup, Supplier<T> fallbackSup) {
        if (isBlank(syncKey) || isNull(handlerSup))
            throw new BlueException();

        RLock lock = redissonClient.getLock(syncKey);
        try {
            lock.lock();
            return handlerSup.get();
        } catch (Exception e) {
            LOGGER.error("T handleFuncWithLock(String syncKey, Supplier<T> handlerSup, Supplier<T> fallbackSup) failed, syncKey = {}, handlerSup = {}, fallbackSup = {}, e = {}",
                    syncKey, handlerSup, fallbackSup, e);

            if (isNotNull(fallbackSup))
                return fallbackSup.get();

            throw e;
        } finally {
            try {
                if (lock.isLocked())
                    lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
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
    public <T> T handleSupWithLock(String syncKey, Supplier<T> handlerSup) {
        return this.handleSupWithLock(syncKey, handlerSup, null);
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
    public <T> T handleSupWithTryLock(String syncKey, Supplier<T> handlerSup, Supplier<T> waitedSup, Supplier<T> fallbackSup,
                                      long maxWaitingMillis) {
        if (isBlank(syncKey) || isNull(handlerSup) || isNull(waitedSup) || maxWaitingMillis < 0L)
            throw new BlueException();

        RLock lock = redissonClient.getLock(syncKey);
        boolean tryLock = true;
        try {
            tryLock = lock.tryLock();
            if (tryLock)
                return handlerSup.get();

            long start = currentTimeMillis();
            while (!(tryLock = lock.tryLock()) && currentTimeMillis() - start <= maxWaitingMillis)
                onSpinWait();

            return waitedSup.get();
        } catch (Exception e) {
            LOGGER.error("T handleAuthorityUpdateWithTryLock(String syncKey, Supplier<T> handlerSup, Supplier<T> waitSuccessedSup, Supplier<T> fallbackSup, long maxWaitingMillis) failed, " +
                    "syncKey = {}, handlerSup = {}, waitedSup = {}, fallbackSup = {}, maxWaitingMillis = {}", syncKey, handlerSup, waitedSup, fallbackSup, maxWaitingMillis);

            if (isNotNull(fallbackSup))
                return fallbackSup.get();

            throw e;
        } finally {
            if (tryLock)
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.warn("handleAuthorityUpdateWithTryLock, lock.unlock() failed, e = {}", e);
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
     * handle task with lock
     *
     * @param syncKey
     * @param handleTask
     */
    public void handleTaskWithLock(String syncKey, HandleTask handleTask) {
        if (isBlank(syncKey) || isNull(handleTask))
            throw new BlueException();

        RLock lock = redissonClient.getLock(syncKey);
        try {
            lock.lock();
            handleTask.handle();
        } catch (Exception e) {
            LOGGER.error("handleTaskWithLock(String syncKey, HandleTask handleTask) failed, syncKey = {}, e = {}", syncKey, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
    }

    /**
     * handle task with try lock
     *
     * @param syncKey
     * @param handleTask
     * @param breakOnLockFail
     */
    public void handleTaskWithTryLock(String syncKey, HandleTask handleTask, boolean breakOnLockFail) {
        if (isBlank(syncKey) || isNull(handleTask))
            throw new BlueException();

        RLock lock = redissonClient.getLock(syncKey);
        boolean locked = false;
        try {
            locked = lock.tryLock();
            if (!locked && breakOnLockFail)
                return;

            lock.lock();
            locked = true;

            handleTask.handle();
        } catch (Exception e) {
            LOGGER.error("handleTaskWithTryLock(String syncKey, HandleTask handleTask, boolean breakOnLockFail) failed," +
                            " syncKey = {}, breakOnLockFail = {}, e = {}",
                    syncKey, breakOnLockFail, e);
            throw e;
        } finally {
            if (locked)
                try {
                    lock.unlock();
                } catch (Exception e) {
                    LOGGER.warn("handleTaskWithTryLock, lock.unlock() failed, e = {}", e);
                }
        }
    }

    /**
     * handle task with try lock
     *
     * @param syncKey
     * @param handleTask
     */
    public void handleTaskWithTryLock(String syncKey, HandleTask handleTask) {
        this.handleTaskWithTryLock(syncKey, handleTask, false);
    }

}
