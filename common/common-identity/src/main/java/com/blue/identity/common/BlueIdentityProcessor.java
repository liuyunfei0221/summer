package com.blue.identity.common;

import com.blue.base.constant.base.Symbol;
import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.core.BlueIdentityGenerator;
import com.blue.identity.core.SnowflakeIdentityParser;
import com.blue.identity.core.exp.IdentityException;
import com.blue.identity.core.param.IdGenParam;
import com.blue.identity.model.IdentityElement;
import reactor.util.Logger;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.blue.identity.core.ConfAsserter.assertConf;
import static java.time.Instant.now;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static reactor.util.Loggers.getLogger;

/**
 * IdentityProcessor is the Bean of the context.It also provides a simple static method for obtaining ID.
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueIdentityProcessor {

    private static final Logger LOGGER = getLogger(BlueIdentityProcessor.class);

    /**
     * key parts
     */
    private static final String HANDLER_KEY_PRE = "GEN:";
    private static final String PAR_CONCATENATION = Symbol.PAR_CONCATENATION.identity;

    /**
     * service name
     */
    private String serviceName;

    /**
     * param
     */
    private IdGenParam idGenParam;

    /**
     * generators holder
     */
    private final Map<String, BlueIdentityGenerator> GENERATORS = new ConcurrentHashMap<>();

    private static final String THREAD_NAME_PRE = "IdentityProcessor-thread- ";
    private static final int RANDOM_LEN = 4;

    /**
     * constructor
     *
     * @param identityConf
     */
    public BlueIdentityProcessor(IdentityConf identityConf) {
        LOGGER.info("BlueIdentityProcessor(IdentityConf identityConf), identityConf = {}", identityConf);

        if (identityConf == null)
            throw new IdentityException("identityConf can't be null");
        assertConf(identityConf);

        serviceName = identityConf.getServiceName() + PAR_CONCATENATION + identityConf.getDataCenter() + PAR_CONCATENATION + identityConf.getWorker();

        ThreadFactory threadFactory = r -> {
            Thread thread = new Thread(r, THREAD_NAME_PRE + randomAlphabetic(RANDOM_LEN));
            thread.setDaemon(true);
            return thread;
        };

        Boolean paddingScheduled = identityConf.getPaddingScheduled();
        idGenParam = new IdGenParam(identityConf.getDataCenter(), identityConf.getWorker(), ofNullable(identityConf.getLastSecondsGetter()).map(Supplier::get).filter(ls -> ls > 0).orElse(now().getEpochSecond()),
                identityConf.getBootSeconds(), identityConf.getMaximumTimeAlarm(), identityConf.getSecondsRecorder(), identityConf.getBufferPower(), identityConf.getPaddingFactor(),
                new ThreadPoolExecutor(identityConf.getPaddingCorePoolSize(), identityConf.getPaddingMaximumPoolSize(),
                        identityConf.getKeepAliveSeconds(), SECONDS, new ArrayBlockingQueue<>(identityConf.getPaddingBlockingQueueSize()),
                        threadFactory, (r, executor) -> {
                    LOGGER.error("Asynchronous padding thread rejected");
                    r.run();
                }),
                paddingScheduled,
                ofNullable(paddingScheduled).orElse(false) ?
                        new ScheduledThreadPoolExecutor(identityConf.getPaddingScheduledCorePoolSize(), Thread::new, (r, executor) -> {
                            LOGGER.error("Timed padding thread rejected");
                            r.run();
                        })
                        :
                        null
                , identityConf.getPaddingScheduledInitialDelayMillis(), identityConf.getPaddingScheduledDelayMillis());

        LOGGER.info("IdentityProcessor init success, idGenParam = {}", idGenParam);
    }

    /**
     * key gen
     */
    private final Function<Class<?>, String> KEY_GEN = entityClz -> {
        if (entityClz != null)
            return (HANDLER_KEY_PRE + serviceName.intern() + PAR_CONCATENATION + entityClz.getName().intern()).intern();

        throw new IdentityException("entityClz can't be null");
    };

    /**
     * handler getter
     */
    private final Function<String, BlueIdentityGenerator> HANDLER_GETTER = key -> {
        BlueIdentityGenerator generator = GENERATORS.get(key.intern());
        if (generator == null)
            synchronized (key.intern()) {
                if ((generator = GENERATORS.get(key)) == null) {
                    generator = new BlueIdentityGenerator(idGenParam);
                    GENERATORS.put(key, generator);
                    LOGGER.info("processor init, key = {}", key);
                }
            }
        return generator;
    };

    /**
     * Generate snowflake ID based on service name and table name
     *
     * @param entityClz
     * @return
     */
    public long generate(Class<?> entityClz) {
        return HANDLER_GETTER.apply(KEY_GEN.apply(entityClz).intern()).generate();
    }

    /**
     * Parse snowflake ID attributes
     *
     * @param id
     * @return
     */
    public IdentityElement parse(long id) {
        return SnowflakeIdentityParser.parse(id);
    }

}
