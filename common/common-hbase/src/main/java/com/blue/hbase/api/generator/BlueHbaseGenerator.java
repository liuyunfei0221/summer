package com.blue.hbase.api.generator;

import com.blue.basic.common.base.BlueChecker;
import com.blue.hbase.api.conf.HbaseConf;
import net.openhft.affinity.AffinityThreadFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.AsyncConnection;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import reactor.util.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.FileGetter.getFile;
import static com.blue.basic.common.base.FileGetter.getResource;
import static com.blue.basic.constant.common.BluePrefix.CLASS_PATH_PREFIX;
import static com.blue.basic.constant.common.Symbol.PAR_CONCATENATION_DATABASE_URL;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static net.openhft.affinity.AffinityStrategies.DIFFERENT_CORE;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang3.StringUtils.startsWith;
import static org.apache.hadoop.hbase.client.ConnectionFactory.createConnection;
import static org.springframework.util.CollectionUtils.isEmpty;
import static reactor.util.Loggers.getLogger;

/**
 * hbase components generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces", "DuplicatedCode"})
public final class BlueHbaseGenerator {

    private static final Logger LOGGER = getLogger(BlueHbaseGenerator.class);

    private static final String DEFAULT_THREAD_NAME_PRE = "blue-hbase-executor-thread" + PAR_CONCATENATION_DATABASE_URL.identity;
    private static final int RANDOM_LEN = 6;

    private static final Predicate<String> CLASS_PATH_PRE = location ->
            startsWith(location, CLASS_PATH_PREFIX.prefix);

    private static final Function<String, URL> URL_PARSER = location -> {
        try {
            if (CLASS_PATH_PRE.test(location)) {
                return getResource(location).getURL();
            } else {
                return getFile(location).toURI().toURL();
            }
        } catch (Exception e) {
            throw new RuntimeException("URL_PARSER, parse URL failed, location = " + location);
        }
    };

    private static final Function<HbaseConf, ExecutorService> EXECUTOR_SERVICE_GENERATOR = hbaseConf -> {
        LOGGER.info("Function<HbaseConf,ExecutorService> EXECUTOR_SERVICE_GENERATOR, hbaseConf = {}", hbaseConf);
        assertConf(hbaseConf);

        String threadNamePre = ofNullable(hbaseConf.getThreadNamePre())
                .map(p -> p + PAR_CONCATENATION_DATABASE_URL.identity)
                .orElse(DEFAULT_THREAD_NAME_PRE);

        RejectedExecutionHandler rejectedExecutionHandler = (r, executor) -> {
            LOGGER.warn("Trigger the thread pool rejection strategy and hand it over to the calling thread for execution");
            r.run();
        };

        return new ThreadPoolExecutor(hbaseConf.getCorePoolSize(), hbaseConf.getMaximumPoolSize(),
                hbaseConf.getKeepAliveSeconds(), SECONDS, new ArrayBlockingQueue<>(hbaseConf.getBlockingQueueCapacity()),
                new AffinityThreadFactory(threadNamePre + randomAlphabetic(RANDOM_LEN), DIFFERENT_CORE),
                rejectedExecutionHandler);
    };

    /**
     * generate configuration
     *
     * @param hbaseConf
     * @return
     */
    public static Configuration generateConfiguration(HbaseConf hbaseConf) {
        LOGGER.info("Configuration generateConfiguration(HbaseConf hbaseConf), hbaseConf = {}", hbaseConf);
        assertConf(hbaseConf);

        Configuration configuration = HBaseConfiguration.create();
        hbaseConf.getConfig().forEach(configuration::set);

        ofNullable(hbaseConf.getResourceLocations())
                .filter(BlueChecker::isNotEmpty)
                .ifPresent(locations ->
                        locations.forEach(location ->
                                configuration.addResource(URL_PARSER.apply(location))));

        ofNullable(hbaseConf.getQuietMode()).ifPresent(configuration::setQuietMode);
        ofNullable(hbaseConf.getRestrictSystemProps()).ifPresent(configuration::setRestrictSystemProps);
        ofNullable(hbaseConf.getAllowNullValueProperties()).ifPresent(configuration::setAllowNullValueProperties);

        return configuration;
    }

    /**
     * generate connection
     *
     * @param hbaseConfiguration
     * @return
     */
    public static Connection generateConnection(Configuration hbaseConfiguration, HbaseConf hbaseConf) {
        LOGGER.info("Connection generate(Configuration hbaseConfiguration, HbaseConf hbaseConf), hbaseConfiguration = {}, hbaseConf = {}", hbaseConfiguration, hbaseConf);
        if (isNull(hbaseConfiguration) || isNull(hbaseConf))
            throw new RuntimeException("configuration or hbaseConf can't be null");

        try {
            return createConnection(hbaseConfiguration, EXECUTOR_SERVICE_GENERATOR.apply(hbaseConf));
        } catch (IOException e) {
            throw new RuntimeException("Connection generateConnection(Configuration configuration, HbaseConf hbaseConf) failed, hbaseConfiguration = " + hbaseConfiguration + ", hbaseConf = " + hbaseConf);
        }
    }

    /**
     * generate connection
     *
     * @param hbaseConfiguration
     * @return
     */
    public static AsyncConnection generateAsyncConnection(Configuration hbaseConfiguration) {
        LOGGER.info("Connection generate(Configuration hbaseConfiguration, ExecutorService executorService), hbaseConfiguration = {}", hbaseConfiguration);
        if (isNull(hbaseConfiguration))
            throw new RuntimeException("configuration can't be null");

        return ConnectionFactory.createAsyncConnection(hbaseConfiguration).join();
    }

    /**
     * assert param
     *
     * @param conf
     */
    private static void assertConf(HbaseConf conf) {
        if (isNull(conf))
            throw new RuntimeException("conf can't be null");

        if (isEmpty(conf.getConfig()))
            throw new RuntimeException("esNodes can't be empty");

        Integer corePoolSize = conf.getCorePoolSize();
        if (isNull(corePoolSize) || corePoolSize < 1)
            throw new RuntimeException("corePoolSize can't be null or less than 1");

        Integer maximumPoolSize = conf.getMaximumPoolSize();
        if (isNull(maximumPoolSize) || maximumPoolSize < 1 || maximumPoolSize < corePoolSize)
            throw new RuntimeException("maximumPoolSize can't be null or less than 1 or less than corePoolSize");

        Long keepAliveSeconds = conf.getKeepAliveSeconds();
        if (isNull(keepAliveSeconds) || keepAliveSeconds < 1L)
            throw new RuntimeException("keepAliveSeconds can't be null or less than 1");

        Integer blockingQueueCapacity = conf.getBlockingQueueCapacity();
        if (isNull(blockingQueueCapacity) || blockingQueueCapacity < 1)
            throw new RuntimeException("blockingQueueCapacity can't be null or less than 1");
    }

}
