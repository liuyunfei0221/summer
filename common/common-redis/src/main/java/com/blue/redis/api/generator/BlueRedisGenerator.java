package com.blue.redis.api.generator;

import com.blue.base.model.exps.BlueException;
import com.blue.redis.api.conf.RedisConf;
import com.blue.redis.constant.ServerMode;
import io.lettuce.core.ClientOptions;
import io.lettuce.core.SocketOptions;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.blue.base.constant.base.ResponseElement.INTERNAL_SERVER_ERROR;
import static com.blue.redis.constant.ServerMode.CLUSTER;
import static com.blue.redis.constant.ServerMode.SINGLE;
import static io.lettuce.core.protocol.DecodeBufferPolicies.ratio;
import static java.lang.Integer.parseInt;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * redis components generator
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "AliControlFlowStatementWithoutBraces"})
public final class BlueRedisGenerator {

    private static final String KEY_VALUE_SEPARATOR = ":";

    private static final Map<ServerMode, Consumer<RedisConf>> SERVER_MODE_ASSERTERS = new HashMap<>(4, 1.0f);

    private static final Map<ServerMode, Function<RedisConf, RedisConfiguration>> CONF_GENERATORS = new HashMap<>(4, 1.0f);

    static {
        SERVER_MODE_ASSERTERS.put(CLUSTER, conf -> {
            if (conf == null)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");

            List<String> nodes = conf.getNodes();
            if (isEmpty(nodes))
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "nodes can't be null or empty");
        });

        SERVER_MODE_ASSERTERS.put(SINGLE, conf -> {
            if (conf == null)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");

            Integer port = conf.getPort();
            if (isBlank(conf.getHost()) || port == null || port < 1)
                throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "host can't be null or '', port can't be null or less than 1");
        });

        CONF_GENERATORS.put(CLUSTER, BlueRedisGenerator::generateClusterConfiguration);
        CONF_GENERATORS.put(SINGLE, BlueRedisGenerator::generateStandConfiguration);
    }

    private static final Consumer<RedisConf> SERVER_MODE_ASSERTER = conf -> {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");

        ServerMode serverMode = conf.getServerMode();
        if (serverMode == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "serverMode can't be null");

        Consumer<RedisConf> asserter = SERVER_MODE_ASSERTERS.get(serverMode);
        if (asserter == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "unknown serverMode -> " + serverMode);

        asserter.accept(conf);
    };

    private static final Function<RedisConf, RedisConfiguration> CONF_GENERATOR = conf -> {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");

        ServerMode serverMode = conf.getServerMode();
        if (serverMode == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "serverMode can't be null");

        Function<RedisConf, RedisConfiguration> generator = CONF_GENERATORS.get(serverMode);
        if (generator == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "unknown serverMode -> " + serverMode);

        return generator.apply(conf);
    };

    /**
     * assert params
     *
     * @param conf
     */
    private static void confAsserter(RedisConf conf) {
        if (conf == null)
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "conf can't be null");

        SERVER_MODE_ASSERTER.accept(conf);
    }

    /**
     * generate redis configuration
     *
     * @param redisConf
     * @return
     */
    public static RedisConfiguration generateConfiguration(RedisConf redisConf) {
        confAsserter(redisConf);

        return CONF_GENERATOR.apply(redisConf);
    }

    /**
     * generate pool
     *
     * @param redisConf
     * @return
     */
    public static GenericObjectPoolConfig<ReactiveRedisConnection> generateGenericObjectPoolConfig(RedisConf redisConf) {
        confAsserter(redisConf);

        GenericObjectPoolConfig<ReactiveRedisConnection> genericObjectPoolConfig = new GenericObjectPoolConfig<>();

        ofNullable(redisConf.getMinIdle())
                .ifPresent(genericObjectPoolConfig::setMinIdle);
        ofNullable(redisConf.getMaxIdle())
                .ifPresent(genericObjectPoolConfig::setMaxIdle);
        ofNullable(redisConf.getMaxTotal())
                .ifPresent(genericObjectPoolConfig::setMaxTotal);
        ofNullable(redisConf.getMaxWaitMillis())
                .ifPresent(mwm -> genericObjectPoolConfig.setMaxWait(of(mwm, MILLIS)));

        return genericObjectPoolConfig;
    }

    /**
     * generate client options
     *
     * @param redisConf
     * @return
     */
    public static ClientOptions generateClientOptions(RedisConf redisConf) {
        confAsserter(redisConf);

        ClientOptions.Builder coBuilder = ClientOptions.builder()
                .disconnectedBehavior(ClientOptions.DisconnectedBehavior.REJECT_COMMANDS);

        ofNullable(redisConf.getAutoReconnect())
                .ifPresent(coBuilder::autoReconnect);
        ofNullable(redisConf.getBufferUsageRatio())
                .ifPresent(bur -> coBuilder.decodeBufferPolicy(ratio(bur)));
        ofNullable(redisConf.getCancelCommandsOnReconnectFailure())
                .ifPresent(coBuilder::cancelCommandsOnReconnectFailure);
        ofNullable(redisConf.getPingBeforeActivateConnection())
                .ifPresent(coBuilder::pingBeforeActivateConnection);
        ofNullable(redisConf.getRequestQueueSize())
                .ifPresent(coBuilder::requestQueueSize);
        ofNullable(redisConf.getPublishOnScheduler())
                .ifPresent(coBuilder::publishOnScheduler);
        ofNullable(redisConf.getAutoReconnect())
                .ifPresent(coBuilder::autoReconnect);
        ofNullable(redisConf.getAutoReconnect())
                .ifPresent(coBuilder::autoReconnect);

        SocketOptions.Builder soBuilder = SocketOptions.builder();
        ofNullable(redisConf.getTcpNoDelay())
                .ifPresent(soBuilder::tcpNoDelay);
        ofNullable(redisConf.getConnectTimeout())
                .ifPresent(cto -> soBuilder.connectTimeout(of(cto, SECONDS)));
        ofNullable(redisConf.getKeepAlive())
                .ifPresent(soBuilder::keepAlive);

        coBuilder.socketOptions(soBuilder.build());

        return coBuilder.build();
    }

    /**
     * generate client configuration
     *
     * @param redisConf
     * @return
     */
    public static LettuceClientConfiguration generateLettuceClientConfiguration(RedisConf redisConf, GenericObjectPoolConfig<ReactiveRedisConnection> genericObjectPoolConfig, ClientOptions clientOptions) {
        confAsserter(redisConf);

        LettucePoolingClientConfiguration.LettucePoolingClientConfigurationBuilder builder = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig).clientOptions(clientOptions);

        ofNullable(redisConf.getCommandTimeout())
                .ifPresent(cto -> builder.commandTimeout(of(cto, SECONDS)));
        ofNullable(redisConf.getShutdownTimeout())
                .ifPresent(sto -> builder.shutdownTimeout(of(sto, SECONDS)));
        ofNullable(redisConf.getShutdownQuietPeriod())
                .ifPresent(sqp -> builder.shutdownQuietPeriod(of(sqp, SECONDS)));

        return builder.build();
    }

    /**
     * generate connection factory
     *
     * @param redisConfiguration
     * @param lettuceClientConfiguration
     * @return
     */
    public static LettuceConnectionFactory generateConnectionFactory(RedisConf redisConf, RedisConfiguration redisConfiguration, LettuceClientConfiguration lettuceClientConfiguration) {
        confAsserter(redisConf);

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);

        ofNullable(redisConf.getShareNativeConnection())
                .ifPresent(lettuceConnectionFactory::setShareNativeConnection);

        return new LettuceConnectionFactory(redisConfiguration, lettuceClientConfiguration);
    }

    /**
     * generate template
     *
     * @param lettuceConnectionFactory
     * @return
     */
    public static RedisTemplate<Object, Object> generateRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory);
        return redisTemplate;
    }

    /**
     * generate template
     *
     * @param lettuceConnectionFactory
     * @return
     */
    public static StringRedisTemplate generateStringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return new StringRedisTemplate(lettuceConnectionFactory);
    }

    /**
     * generate template
     *
     * @param lettuceConnectionFactory
     * @return
     */
    public static ReactiveStringRedisTemplate generateReactiveStringRedisTemplate(RedisConf redisConf, LettuceConnectionFactory lettuceConnectionFactory) {
        confAsserter(redisConf);

        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(UTF_8);
        RedisSerializationContext.RedisSerializationContextBuilder<String, String> contextBuilder =
                RedisSerializationContext.newSerializationContext();

        RedisSerializationContext<String, String> redisSerializationContext = contextBuilder
                .key(stringRedisSerializer)
                .value(stringRedisSerializer)
                .hashKey(stringRedisSerializer)
                .hashValue(stringRedisSerializer)
                .build();

        return new ReactiveStringRedisTemplate(lettuceConnectionFactory, redisSerializationContext,
                ofNullable(redisConf.getExposeConnection()).orElse(false));
    }

    /**
     * generate template
     *
     * @param reactiveRedisConnectionFactory
     * @param clz
     * @param <T>
     * @return
     */
    public static <T> ReactiveRedisTemplate<String, T> generateReactiveRedisTemplate(RedisConf redisConf, ReactiveRedisConnectionFactory reactiveRedisConnectionFactory, Class<T> clz) {
        confAsserter(redisConf);

        if (Stream.of(clz.getInterfaces()).noneMatch(inter -> Serializable.class.getName().equals(inter.getName())))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "clz must be a implemented of serializable");

        RedisSerializationContext.RedisSerializationContextBuilder<String, T> contextBuilder =
                RedisSerializationContext.newSerializationContext();
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(UTF_8);

        @SuppressWarnings("unchecked")
        RedisSerializer<T> jdkSerializationRedisSerializer = (RedisSerializer<T>) new JdkSerializationRedisSerializer();
        RedisSerializationContext<String, T> redisSerializationContext = contextBuilder
                .key(stringRedisSerializer)
                .value(jdkSerializationRedisSerializer)
                .hashKey(stringRedisSerializer)
                .hashValue(jdkSerializationRedisSerializer)
                .build();

        return new ReactiveRedisTemplate<>(reactiveRedisConnectionFactory, redisSerializationContext,
                ofNullable(redisConf.getExposeConnection()).orElse(false));
    }

    /**
     * generate cluster configuration
     *
     * @param redisConf
     * @return
     */
    private static RedisConfiguration generateClusterConfiguration(RedisConf redisConf) {
        confAsserter(redisConf);

        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        try {
            List<String> nodes = redisConf.getNodes();
            for (String node : nodes) {
                String[] hostAndPort = node.split(KEY_VALUE_SEPARATOR);
                redisClusterConfiguration.addClusterNode(new RedisNode(hostAndPort[0], parseInt(hostAndPort[1])));
            }
        } catch (Exception e) {
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "redis init error,check args, e = " + e);
        }

        ofNullable(redisConf.getMaxRedirects())
                .ifPresent(redisClusterConfiguration::setMaxRedirects);
        ofNullable(redisConf.getPassword())
                .ifPresent(redisClusterConfiguration::setPassword);

        return redisClusterConfiguration;
    }

    /**
     * generate standalone configuration
     *
     * @param redisConf
     * @return
     */
    private static RedisConfiguration generateStandConfiguration(RedisConf redisConf) {
        confAsserter(redisConf);

        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();

        ofNullable(redisConf.getHost())
                .ifPresent(redisStandaloneConfiguration::setHostName);
        ofNullable(redisConf.getPort())
                .ifPresent(redisStandaloneConfiguration::setPort);

        return redisStandaloneConfiguration;
    }

}
