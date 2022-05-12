package com.blue.auth.component.sync;

import com.blue.auth.config.deploy.BlockingDeploy;
import com.blue.auth.service.impl.RoleResRelationServiceImpl;
import com.blue.base.model.exps.BlueException;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.SyncKey.AUTHORITY_UPDATE_SYNC;
import static java.lang.System.currentTimeMillis;
import static java.lang.Thread.onSpinWait;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * auth sync processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavadocDeclaration", "unused"})
@Component
public final class AuthorityUpdateSyncProcessor {

    private static final Logger LOGGER = getLogger(RoleResRelationServiceImpl.class);

    private final RedissonClient redissonClient;

    public AuthorityUpdateSyncProcessor(RedissonClient redissonClient, BlockingDeploy blockingDeploy) {
        this.redissonClient = redissonClient;

        MAX_WAITING_FOR_REFRESH = blockingDeploy.getBlockingMillis();
    }

    private static long MAX_WAITING_FOR_REFRESH;

    /**
     * select
     *
     * @param cacheSup
     * @param predicate
     * @param dbSup
     * @param cacheSetter
     * @param <T>
     * @return
     */
    public <T> T selectGenericsWithCache(Supplier<T> cacheSup, Predicate<T> predicate, Supplier<T> dbSup, Consumer<T> cacheSetter) {
        return ofNullable(cacheSup.get())
                .filter(predicate)
                .orElseGet(() -> {
                    RLock lock = redissonClient.getLock(AUTHORITY_UPDATE_SYNC.key);
                    boolean tryLock = true;
                    try {
                        T res = cacheSup.get();
                        if (predicate.test(res))
                            return res;

                        tryLock = lock.tryLock();

                        if (tryLock) {
                            res = dbSup.get();
                            if (predicate.test(res))
                                cacheSetter.accept(res);

                            return res;
                        }

                        long start = currentTimeMillis();
                        while (!(tryLock = lock.tryLock()) && currentTimeMillis() - start <= MAX_WAITING_FOR_REFRESH)
                            onSpinWait();

                        res = cacheSup.get();

                        return predicate.test(res) ? res : dbSup.get();
                    } catch (Exception e) {
                        LOGGER.error("selectGenericsWithCache(Supplier<List<T>> cacheSup, Supplier<List<T>> dbSup, Consumer<List<T>> cacheSetter) failed," +
                                "cacheSup = {}, dbSup = {}, cacheSetter = {}, e = {}", cacheSup, dbSup, cacheSetter, e);
                        return dbSup.get();
                    } finally {
                        if (tryLock)
                            try {
                                lock.unlock();
                            } catch (Exception e) {
                                LOGGER.warn("selectGenericsWithCache(Supplier<List<T>> cacheSup, Supplier<List<T>> dbSup, Consumer<List<T>> cacheSetter) lock.unlock() failed," +
                                        "cacheSup = {}, dbSup = {}, cacheSetter = {}, e = {}", cacheSup, dbSup, cacheSetter, e);
                            }
                    }
                });
    }

    /**
     * handle func sync
     *
     * @param syncKey
     * @param sup
     * @param <T>
     * @return
     */
    public <T> T handleAuthorityUpdateWithSync(String syncKey, Supplier<T> sup) {
        if (isBlank(syncKey) || isNull(sup))
            throw new BlueException();

        RLock lock = redissonClient.getLock(syncKey);
        try {
            lock.lock();
            return sup.get();
        } catch (Exception e) {
            LOGGER.error("T handleAuthorityUpdateWithSync(String syncKey, Supplier<T> sup) failed, syncKey = {}, sup = {}, e = {}",
                    syncKey, sup, e);
            throw e;
        } finally {
            try {
                lock.unlock();
            } catch (Exception e) {
                LOGGER.error("lock.unlock() failed, e = {}", e);
            }
        }
    }

}
