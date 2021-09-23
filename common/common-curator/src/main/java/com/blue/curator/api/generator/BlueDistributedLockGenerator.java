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

import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Optional.ofNullable;
import static org.apache.curator.framework.CuratorFrameworkFactory.builder;
import static org.springframework.util.StringUtils.hasText;

/**
 * 分布式锁创建工厂
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueDistributedLockGenerator {

    private final CuratorFramework client;

    public BlueDistributedLockGenerator(DistributedLockConf distributedLockConf) {
        confAsserter(distributedLockConf);

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

        if (retryBaseSleepTimeMs != null && retryMaxSleepTimeMs != null && retryMaxRetries != null)
            builder.retryPolicy(new BoundedExponentialBackoffRetry(distributedLockConf.getRetryBaseSleepTimeMs(),
                    distributedLockConf.getRetryMaxSleepTimeMs(), distributedLockConf.getRetryMaxRetries()));

        ofNullable(distributedLockConf.getNamespace()).ifPresent(builder::namespace);
        ofNullable(distributedLockConf.getCanBeReadOnly()).ifPresent(builder::canBeReadOnly);

        this.client = builder.build();
    }

    /**
     * 名称数量
     */
    private static final int
            MIN_NAME_SIZE = 1,
            MAX_NAME_SIZE = 8;

    /**
     * 校验锁名称
     */
    private static final Consumer<String> NAME_ASSERTER = name -> {
        if (name == null || "".equals(name))
            throw new RuntimeException("name can't be null");
    };

    /**
     * 校验锁名称集合
     */
    private static final Consumer<List<String>> NAMES_ASSERTER = names -> {
        if (names == null)
            throw new RuntimeException("names can't be null");

        int size = names.size();
        if (size < MIN_NAME_SIZE || size > MAX_NAME_SIZE)
            throw new RuntimeException("names.size() can't be less than " + MIN_NAME_SIZE + " or greater than " + MAX_NAME_SIZE);

        for (String name : names)
            NAME_ASSERTER.accept(name);
    };

    /**
     * 获取互斥锁
     *
     * @param name
     * @return
     */
    public InterProcessMutex getInterProcessMutexLock(String name) {
        NAME_ASSERTER.accept(name);
        return new InterProcessMutex(this.client, name);
    }

    /**
     * 获取读写锁
     *
     * @param name
     * @return
     */
    public InterProcessReadWriteLock getInterProcessReadWriteLock(String name) {
        NAME_ASSERTER.accept(name);
        return new InterProcessReadWriteLock(this.client, name);
    }

    /**
     * 获取不可重入互斥锁
     *
     * @param name
     * @return
     */
    public InterProcessSemaphoreMutex getInterProcessSemaphoreMutexLock(String name) {
        NAME_ASSERTER.accept(name);
        return new InterProcessSemaphoreMutex(this.client, name);
    }

    /**
     * 获取多锁/集合锁
     *
     * @param names
     * @return
     */
    public InterProcessMultiLock getInterProcessMultiLock(List<String> names) {
        NAMES_ASSERTER.accept(names);
        return new InterProcessMultiLock(this.client, names);
    }

    /**
     * 参数校验
     *
     * @param distributedLockConf
     */
    private static void confAsserter(DistributedLockConf distributedLockConf) {
        if (distributedLockConf == null)
            throw new RuntimeException("distributedLockConf can't be null");

        if (!hasText(distributedLockConf.getConnectString()))
            throw new RuntimeException("connectString can't be null or ''");
    }

}
