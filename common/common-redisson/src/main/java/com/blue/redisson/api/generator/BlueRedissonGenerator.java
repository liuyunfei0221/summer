package com.blue.redisson.api.generator;

import com.blue.base.model.exps.BlueException;
import com.blue.redisson.api.conf.RedissonConf;
import com.blue.redisson.constant.ServerMode;
import net.openhft.affinity.AffinityThreadFactory;
import org.redisson.api.RedissonClient;
import org.redisson.config.ClusterServersConfig;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.redisson.constant.ServerMode.CLUSTER;
import static com.blue.redisson.constant.ServerMode.SINGLE;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.SAME_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.redisson.Redisson.create;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * redisson components generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AlibabaRemoveCommentedCode", "AliControlFlowStatementWithoutBraces"})
public final class BlueRedissonGenerator {

    private static final Logger LOGGER = getLogger(BlueRedissonGenerator.class);

    private static final String THREAD_NAME_PRE = "redisson-thread- ";
    private static final int RANDOM_LEN = 6;

    private static final Map<ServerMode, Consumer<RedissonConf>> SERVER_MODE_ASSERTERS = new HashMap<>(4, 1.0f);

    private static final Map<ServerMode, BiConsumer<RedissonConf, Config>> CONF_PACKAGERS = new HashMap<>(4, 1.0f);

    static {
        SERVER_MODE_ASSERTERS.put(CLUSTER, redissonConf -> {
            List<String> nodes = redissonConf.getNodes();
            if (isEmpty(nodes))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "nodes can't be null or empty");
        });

        SERVER_MODE_ASSERTERS.put(SINGLE, redissonConf -> {
            String host = redissonConf.getHost();
            Integer port = redissonConf.getPort();
            if (isBlank(host) || port == null || port < 1)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "host can't be null or '', port can't be null or less than 1");
        });

        CONF_PACKAGERS.put(CLUSTER, BlueRedissonGenerator::configClusterServer);
        CONF_PACKAGERS.put(SINGLE, BlueRedissonGenerator::configSingleServer);
    }

    private static final Consumer<RedissonConf> SERVER_MODE_ASSERTER = redissonConf -> {
        if (redissonConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "redissonConf can't be null");

        ServerMode serverMode = redissonConf.getServerMode();
        if (serverMode == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "serverMode can't be null");

        Consumer<RedissonConf> asserter = SERVER_MODE_ASSERTERS.get(serverMode);
        if (asserter == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "unknown serverMode -> " + serverMode);

        asserter.accept(redissonConf);
    };

    private static final BiConsumer<RedissonConf, Config> CONF_PACKAGER = (redissonConf, conf) -> {
        if (redissonConf == null || conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "redissonConf or conf can't be null");

        ServerMode serverMode = redissonConf.getServerMode();
        if (serverMode == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "serverMode can't be null");

        BiConsumer<RedissonConf, Config> packager = CONF_PACKAGERS.get(serverMode);
        if (packager == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "unknown serverMode -> " + serverMode);

        packager.accept(redissonConf, conf);
    };

    /**
     * cluster
     *
     * @param redissonConf
     * @param config
     */
    private static void configClusterServer(RedissonConf redissonConf, Config config) {
        confAsserter(redissonConf);

        ClusterServersConfig serverConfig = config.useClusterServers();
        List<String> nodes = redissonConf.getNodes();
        nodes.forEach(address -> serverConfig.addNodeAddress("redis://" + address));

        ofNullable(redissonConf.getScanInterval())
                .ifPresent(serverConfig::setScanInterval);
        ofNullable(redissonConf.getLoadBalancer())
                .ifPresent(serverConfig::setLoadBalancer);
        ofNullable(redissonConf.getMasterConnectionMinimumIdleSize())
                .ifPresent(serverConfig::setMasterConnectionMinimumIdleSize);
        ofNullable(redissonConf.getSlaveConnectionMinimumIdleSize())
                .ifPresent(serverConfig::setSlaveConnectionMinimumIdleSize);
        ofNullable(redissonConf.getMasterConnectionPoolSize())
                .ifPresent(serverConfig::setMasterConnectionPoolSize);
        ofNullable(redissonConf.getSlaveConnectionPoolSize())
                .ifPresent(serverConfig::setSlaveConnectionPoolSize);
        ofNullable(redissonConf.getReadMode())
                .ifPresent(serverConfig::setReadMode);
        ofNullable(redissonConf.getSubscriptionMode())
                .ifPresent(serverConfig::setSubscriptionMode);
        ofNullable(redissonConf.getCheckSlotsCoverage())
                .ifPresent(serverConfig::setCheckSlotsCoverage);
        ofNullable(redissonConf.getPassword())
                .ifPresent(serverConfig::setPassword);
    }

    /**
     * standalone
     *
     * @param redissonConf
     * @param config
     */
    private static void configSingleServer(RedissonConf redissonConf, Config config) {
        confAsserter(redissonConf);

        SingleServerConfig serverConfig = config.useSingleServer()
                .setAddress("redis://" + redissonConf.getHost() + ":" + redissonConf.getPort());

        ofNullable(redissonConf.getConnectionMinimumIdleSize())
                .ifPresent(serverConfig::setConnectionMinimumIdleSize);
        ofNullable(redissonConf.getConnectionPoolSize())
                .ifPresent(serverConfig::setConnectionPoolSize);
        ofNullable(redissonConf.getKeepAlive())
                .ifPresent(serverConfig::setKeepAlive);
        ofNullable(redissonConf.getSubscriptionConnectionMinimumIdleSize())
                .ifPresent(serverConfig::setSubscriptionConnectionMinimumIdleSize);
        ofNullable(redissonConf.getSubscriptionConnectionPoolSize())
                .ifPresent(serverConfig::setSubscriptionConnectionPoolSize);
        ofNullable(redissonConf.getTcpNoDelay())
                .ifPresent(serverConfig::setTcpNoDelay);
        ofNullable(redissonConf.getRetryAttempts())
                .ifPresent(serverConfig::setRetryAttempts);
        ofNullable(redissonConf.getRetryInterval())
                .ifPresent(serverConfig::setRetryInterval);
        ofNullable(redissonConf.getTimeout())
                .ifPresent(serverConfig::setTimeout);
        ofNullable(redissonConf.getConnectTimeout())
                .ifPresent(serverConfig::setConnectTimeout);
        ofNullable(redissonConf.getIdleConnectionTimeout())
                .ifPresent(serverConfig::setIdleConnectionTimeout);
        ofNullable(redissonConf.getSubscriptionConnectionMinimumIdleSize())
                .ifPresent(serverConfig::setSubscriptionConnectionMinimumIdleSize);
        ofNullable(redissonConf.getSubscriptionConnectionPoolSize())
                .ifPresent(serverConfig::setSubscriptionConnectionPoolSize);
        ofNullable(redissonConf.getSubscriptionsPerConnection())
                .ifPresent(serverConfig::setSubscriptionsPerConnection);
        ofNullable(redissonConf.getTimeout())
                .ifPresent(serverConfig::setTimeout);
        ofNullable(redissonConf.getConnectTimeout())
                .ifPresent(serverConfig::setConnectTimeout);
        ofNullable(redissonConf.getIdleConnectionTimeout())
                .ifPresent(serverConfig::setIdleConnectionTimeout);
        ofNullable(redissonConf.getTcpNoDelay())
                .ifPresent(serverConfig::setTcpNoDelay);
        ofNullable(redissonConf.getRetryAttempts())
                .ifPresent(serverConfig::setRetryAttempts);
        ofNullable(redissonConf.getRetryInterval())
                .ifPresent(serverConfig::setRetryInterval);
        ofNullable(redissonConf.getDnsMonitoringInterval())
                .ifPresent(serverConfig::setDnsMonitoringInterval);
        ofNullable(redissonConf.getPingConnectionInterval())
                .ifPresent(serverConfig::setPingConnectionInterval);
    }

    /**
     * generate client
     *
     * @param redissonConf
     * @return
     */
    public static RedissonClient generateRedissonClient(RedissonConf redissonConf) {
        confAsserter(redissonConf);

        Config config = new Config();

        CONF_PACKAGER.accept(redissonConf, config);

        ThreadFactory threadFactory = new AffinityThreadFactory(THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN), SAME_CORE);

        BlockingQueue<Runnable> blockingQueue = new ArrayBlockingQueue<>(redissonConf.getExecutorBlockingQueueCapacity());

        ExecutorService executorService = new ThreadPoolExecutor(redissonConf.getExecutorCorePoolSize(),
                redissonConf.getExecutorMaximumPoolSize(),
                redissonConf.getExecutorKeepAliveSeconds(), SECONDS, blockingQueue, threadFactory, (r, executor) -> {
            LOGGER.warn("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution");
            r.run();
        });

        config.setExecutor(executorService);

        return create(config);
    }

    /**
     * assert params
     *
     * @param redissonConf
     */
    private static void confAsserter(RedissonConf redissonConf) {
        if (redissonConf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "redissonConf can't be null");

        ServerMode serverMode = redissonConf.getServerMode();
        if (serverMode == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "serverMode can't be null");

        SERVER_MODE_ASSERTER.accept(redissonConf);
    }

}
