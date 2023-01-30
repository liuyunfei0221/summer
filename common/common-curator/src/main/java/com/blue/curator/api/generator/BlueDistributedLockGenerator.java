package com.blue.curator.api.generator;

import com.blue.curator.api.conf.DistributedLockConf;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.imps.GzipCompressionProvider;
import org.apache.curator.framework.recipes.locks.InterProcessMultiLock;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.framework.recipes.locks.InterProcessReadWriteLock;
import org.apache.curator.framework.recipes.locks.InterProcessSemaphoreMutex;
import org.apache.curator.retry.BoundedExponentialBackoffRetry;

import java.util.List;
import java.util.function.Consumer;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static org.apache.curator.framework.CuratorFrameworkFactory.builder;
import static org.springframework.util.StringUtils.hasText;

/**
 * zk lock generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueDistributedLockGenerator {

    private final CuratorFramework client;

    public BlueDistributedLockGenerator(DistributedLockConf distributedLockConf) {
        assertConf(distributedLockConf);

        CuratorFrameworkFactory.Builder builder = builder()
                .connectString(distributedLockConf.getConnectString())
                .compressionProvider(new GzipCompressionProvider());

        String authSchema = distributedLockConf.getAuthSchema();
        String auth = distributedLockConf.getAuth();
        if (hasText(authSchema) && hasText(auth))
            builder.authorization(distributedLockConf.getAuthSchema(), distributedLockConf.getAuth().getBytes(UTF_8));

        ofNullable(distributedLockConf.getConnectionTimeoutMs()).ifPresent(builder::connectionTimeoutMs);
        ofNullable(distributedLockConf.getSessionTimeoutMs()).ifPresent(builder::sessionTimeoutMs);
        ofNullable(distributedLockConf.getMaxCloseWaitMs()).ifPresent(builder::maxCloseWaitMs);

        Integer retryBaseSleepTimeMs = distributedLockConf.getRetryBaseSleepTimeMs();
        Integer retryMaxSleepTimeMs = distributedLockConf.getRetryMaxSleepTimeMs();
        Integer retryMaxRetries = distributedLockConf.getRetryMaxRetries();

        if (!isNull(retryBaseSleepTimeMs) && !isNull(retryMaxSleepTimeMs) && !isNull(retryMaxRetries))
            builder.retryPolicy(new BoundedExponentialBackoffRetry(distributedLockConf.getRetryBaseSleepTimeMs(),
                    distributedLockConf.getRetryMaxSleepTimeMs(), distributedLockConf.getRetryMaxRetries()));

        ofNullable(distributedLockConf.getNamespace()).ifPresent(builder::namespace);
        ofNullable(distributedLockConf.getCanBeReadOnly()).ifPresent(builder::canBeReadOnly);

        this.client = builder.build();
    }

    /**
     * name size
     */
    private static final int
            MIN_NAME_SIZE = 1,
            MAX_NAME_SIZE = 8;

    /**
     * assert lock name
     */
    private static final Consumer<String> NAME_ASSERTER = name -> {
        if (isBlank(name))
            throw new RuntimeException("name can't be blank");
    };

    /**
     * assert lock name list
     */
    private static final Consumer<List<String>> NAMES_ASSERTER = names -> {
        if (isNull(names))
            throw new RuntimeException("names can't be null");

        int size = names.size();
        if (size < MIN_NAME_SIZE || size > MAX_NAME_SIZE)
            throw new RuntimeException("names.size() can't be less than " + MIN_NAME_SIZE + " or greater than " + MAX_NAME_SIZE);

        for (String name : names)
            NAME_ASSERTER.accept(name);
    };

    /**
     * get interProcessMutexLock
     *
     * @param name
     * @return
     */
    public InterProcessMutex getInterProcessMutexLock(String name) {
        NAME_ASSERTER.accept(name);
        return new InterProcessMutex(this.client, name);
    }

    /**
     * get interProcessReadWriteLock
     *
     * @param name
     * @return
     */
    public InterProcessReadWriteLock getInterProcessReadWriteLock(String name) {
        NAME_ASSERTER.accept(name);
        return new InterProcessReadWriteLock(this.client, name);
    }

    /**
     * get interProcessSemaphoreMutexLock
     *
     * @param name
     * @return
     */
    public InterProcessSemaphoreMutex getInterProcessSemaphoreMutexLock(String name) {
        NAME_ASSERTER.accept(name);
        return new InterProcessSemaphoreMutex(this.client, name);
    }

    /**
     * get interProcessMultiLock
     *
     * @param names
     * @return
     */
    public InterProcessMultiLock getInterProcessMultiLock(List<String> names) {
        NAMES_ASSERTER.accept(names);
        return new InterProcessMultiLock(this.client, names);
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void assertConf(DistributedLockConf conf) {
        if (isNull(conf))
            throw new RuntimeException("distributedLockConf can't be null");

        if (isBlank(conf.getConnectString()))
            throw new RuntimeException("connectString can't be blank");
    }

}
