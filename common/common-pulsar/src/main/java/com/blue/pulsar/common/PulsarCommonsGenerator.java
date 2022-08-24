package com.blue.pulsar.common;

import com.blue.basic.common.base.BlueChecker;
import com.blue.pulsar.api.conf.*;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;
import org.apache.pulsar.client.impl.AutoClusterFailover;
import org.apache.pulsar.client.impl.auth.AuthenticationTls;
import org.slf4j.Logger;

import java.net.InetSocketAddress;
import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.FuncParamTypeProcessor.getConsumerParameterType;
import static java.time.Clock.system;
import static java.time.ZoneId.of;
import static java.util.Collections.singletonMap;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.pulsar.client.api.DeadLetterPolicy.builder;
import static org.apache.pulsar.client.api.Schema.JSON;
import static org.apache.pulsar.client.api.SizeUnit.KILO_BYTES;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * pulsar commons generator
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "AlibabaMethodTooLong", "AlibabaUndefineMagicConstant", "SpellCheckingInspection", "AlibabaAvoidComplexCondition"})
public final class PulsarCommonsGenerator {

    private static final Logger LOGGER = getLogger(PulsarCommonsGenerator.class);

    private static final String SCHEMA = "pulsar://";

    private static final String TLS_SCHEMA = "pulsar+ssl://";

    private static final String LIST_ELEMENT_PATH_SEPARATOR = ",";

    private static final String AUTH_PLUGIN_CLASS_NAME = "org.apache.pulsar.client.impl.auth.AuthenticationTls";

    /**
     * converter to url
     */
    public static final Function<List<String>, String> SERVICES_2_URL_CONVERTER = services -> {
        if (services == null || services.size() < 1)
            throw new RuntimeException("services can't be null");
        String url = services.stream().reduce((a, b) -> a + LIST_ELEMENT_PATH_SEPARATOR + b).orElse("");
        if ("".equals(url))
            throw new RuntimeException("url can't be null");
        return url;
    };

    private static final BinaryOperator<String> AUTH_PARAMS_STRING_GENERATOR = (tlsCertFilePath, tlsKeyFilePath) ->
            "tlsCertFile:" + tlsCertFilePath + "," + "tlsKeyFile:" + tlsKeyFilePath;

    /**
     * generate client
     *
     * @param conf
     * @return
     */
    public static PulsarClient generateClient(ClientConf conf) {
        assertClient(conf);

        try {
            List<String> services = conf.getServices();
            ClientBuilder builder = PulsarClient.builder();

            ofNullable(conf.getListenerName())
                    .filter(BlueChecker::isNotBlank)
                    .ifPresent(builder::listenerName);

            if (ofNullable(conf.getEnableTls()).orElse(false)) {
                String serviceUrl = TLS_SCHEMA + SERVICES_2_URL_CONVERTER.apply(services);
                builder.serviceUrl(serviceUrl)
                        .tlsTrustCertsFilePath(conf.getTlsTrustCertsFilePath())
                        .authentication(AUTH_PLUGIN_CLASS_NAME, AUTH_PARAMS_STRING_GENERATOR
                                .apply(conf.getTlsCertFilePath(), conf.getTlsKeyFilePath()))
                        .allowTlsInsecureConnection(conf.getTlsAllowInsecureConnection())
                        .enableTlsHostnameVerification(conf.getTlsHostnameVerificationEnable());
            } else {
                String serviceUrl = SCHEMA + SERVICES_2_URL_CONVERTER.apply(services);
                builder.serviceUrl(serviceUrl);
            }

            if (ofNullable(conf.getEnableJwtAuth()).orElse(false))
                builder.authentication(AuthenticationFactory.token(conf.getJwt()));

            ofNullable(conf.getOperationTimeoutMillis())
                    .ifPresent(otm -> builder.operationTimeout(otm, MILLISECONDS));
            ofNullable(conf.getStatsIntervalMillis())
                    .ifPresent(sim -> builder.statsInterval(sim, MILLISECONDS));
            ofNullable(conf.getIoThreads())
                    .ifPresent(builder::ioThreads);
            ofNullable(conf.getListenerThreads())
                    .ifPresent(builder::listenerThreads);
            ofNullable(conf.getConnectionsPerBroker())
                    .ifPresent(builder::connectionsPerBroker);
            ofNullable(conf.getUseTcpNoDelay())
                    .ifPresent(builder::enableTcpNoDelay);
            ofNullable(conf.getMemoryLimitKiloBytes())
                    .ifPresent(mlkb -> builder.memoryLimit(mlkb, KILO_BYTES));
            ofNullable(conf.getMaxConcurrentLookupRequests())
                    .ifPresent(builder::maxConcurrentLookupRequests);
            ofNullable(conf.getMaxLookupRequest())
                    .ifPresent(builder::maxLookupRequests);
            ofNullable(conf.getMaxLookupRedirects())
                    .ifPresent(builder::maxLookupRedirects);
            ofNullable(conf.getMaxNumberOfRejectedRequestPerConnection())
                    .ifPresent(builder::maxNumberOfRejectedRequestPerConnection);
            ofNullable(conf.getKeepAliveIntervalMillis())
                    .ifPresent(kaim -> builder.keepAliveInterval(kaim, MILLISECONDS));
            ofNullable(conf.getConnectionTimeoutMillis())
                    .ifPresent(ctm -> builder.connectionTimeout(ctm, MILLISECONDS));
            ofNullable(conf.getStartingBackoffIntervalMillis())
                    .ifPresent(sbim -> builder.startingBackoffInterval(sbim, MILLISECONDS));
            ofNullable(conf.getMaxBackoffIntervalMillis())
                    .ifPresent(mbim -> builder.maxBackoffInterval(mbim, MILLISECONDS));
            ofNullable(conf.getEnableBusyWait())
                    .ifPresent(builder::enableBusyWait);
            ofNullable(conf.getClockZoneId())
                    .ifPresent(czi -> builder.clock(system(of(czi))));
            ofNullable(conf.getEnableTransaction())
                    .ifPresent(builder::enableTransaction);

            if (ofNullable(conf.getEnableProxy()).orElse(false)) {
                Proxy proxy = conf.getProxy();
                builder.proxyServiceUrl(proxy.getProxyServiceUrl(), proxy.getProxyProtocol());
            }

            if (ofNullable(conf.getEnableSocks5Proxy()).orElse(false)) {
                Socks5Proxy socks5Proxy = conf.getSocks5Proxy();
                builder.socks5ProxyAddress(InetSocketAddress.createUnresolved(socks5Proxy.getHost(), socks5Proxy.getPort()))
                        .socks5ProxyUsername(socks5Proxy.getUsername()).socks5ProxyPassword(socks5Proxy.getPassword());
            }

            PulsarClient pulsarClient = builder.build();

            if (ofNullable(conf.getEnableFailover()).orElse(false)) {
                AutoClusterFailoverBuilder autoClusterFailoverBuilder = AutoClusterFailover.builder();

                ClusterFailover clusterFailover = conf.getClusterFailover();
                if (ofNullable(conf.getEnableTls()).orElse(false)) {
                    autoClusterFailoverBuilder.primary(TLS_SCHEMA + clusterFailover.getPrimaryShard())
                            .secondary(clusterFailover.getSecondaryShards().stream().map(s -> TLS_SCHEMA + s).collect(toList()));
                } else {
                    autoClusterFailoverBuilder.primary(SCHEMA + clusterFailover.getPrimaryShard())
                            .secondary(clusterFailover.getSecondaryShards().stream().map(s -> SCHEMA + s).collect(toList()));
                }

                autoClusterFailoverBuilder
                        .failoverDelay(clusterFailover.getFailoverDelayMillis(), MILLISECONDS)
                        .switchBackDelay(clusterFailover.getSwitchBackDelayMillis(), MILLISECONDS)
                        .checkInterval(clusterFailover.getCheckIntervalMillis(), MILLISECONDS);

                ofNullable(clusterFailover.getSecondaryTlsTrustCertsFilePaths()).filter(BlueChecker::isNotEmpty)
                        .ifPresent(autoClusterFailoverBuilder::secondaryTlsTrustCertsFilePath);

                autoClusterFailoverBuilder.secondaryTlsTrustCertsFilePath(clusterFailover.getSecondaryTlsTrustCertsFilePaths());
                autoClusterFailoverBuilder.secondaryAuthentication(singletonMap(AUTH_PLUGIN_CLASS_NAME, new AuthenticationTls(clusterFailover.getCertFilePath(), clusterFailover.getKeyFilePath())));

                try (ServiceUrlProvider serviceUrlProvider = autoClusterFailoverBuilder.build()) {
                    serviceUrlProvider.initialize(pulsarClient);
                } catch (Exception e) {
                    LOGGER.error("process failover client failed, cause e = {0}", e);
                    throw new RuntimeException("process failover client failed, cause e = {}", e);
                }
            }

            return pulsarClient;
        } catch (Exception e) {
            LOGGER.error("generate client failed, cause e = {0}", e);
            throw new RuntimeException("generate client failed, cause e = {}", e);
        }
    }

    /**
     * generate producer
     *
     * @param pulsarClient
     * @param conf
     * @param clz
     * @param messageRouter
     * @param batcherBuilder
     * @param interceptors
     * @param <T>
     * @return
     */
    public static <T> Producer<T> generateProducer(PulsarClient pulsarClient, ProducerConf conf, Class<T> clz, MessageRouter messageRouter,
                                                   BatcherBuilder batcherBuilder, List<ProducerInterceptor> interceptors) {
        assertProducerConf(pulsarClient, conf, clz);

        try {
            ProducerBuilder<T> builder = pulsarClient.newProducer(JSON(clz));
            builder.topic(conf.getTopic())
                    .producerName(conf.getProducerName())
                    .accessMode(conf.getAccessMode())
                    .messageRoutingMode(conf.getMessageRoutingMode())
                    .compressionType(conf.getCompressionType());

            ofNullable(conf.getSendTimeoutMillis())
                    .ifPresent(stm -> builder.sendTimeout(stm, MILLISECONDS));
            ofNullable(conf.getMaxPendingMessages())
                    .ifPresent(builder::maxPendingMessages);
            ofNullable(conf.getMaxPendingMessages())
                    .ifPresent(builder::maxPendingMessages);
            ofNullable(conf.getBlockIfQueueFull())
                    .ifPresent(builder::blockIfQueueFull);
            ofNullable(conf.getEnableBatching())
                    .ifPresent(builder::enableBatching);
            ofNullable(conf.getEnableChunking())
                    .ifPresent(builder::enableChunking);
            ofNullable(conf.getBlockIfQueueFull())
                    .ifPresent(builder::blockIfQueueFull);
            ofNullable(conf.getBatchingMaxPublishDelayMillis())
                    .ifPresent(bmpdm -> builder.batchingMaxPublishDelay(bmpdm, MILLISECONDS));
            ofNullable(conf.getRoundRobinRouterBatchingPartitionSwitchFrequency())
                    .ifPresent(builder::roundRobinRouterBatchingPartitionSwitchFrequency);
            ofNullable(conf.getBatchingMaxMessages())
                    .ifPresent(builder::batchingMaxMessages);
            ofNullable(conf.getBatchingMaxBytes())
                    .ifPresent(builder::batchingMaxBytes);
            ofNullable(conf.getInitialSequenceId())
                    .ifPresent(builder::initialSequenceId);
            ofNullable(conf.getAutoUpdatePartitions())
                    .ifPresent(builder::autoUpdatePartitions);
            ofNullable(conf.getAutoUpdatePartitionsIntervalMillis())
                    .ifPresent(aup -> builder.autoUpdatePartitionsInterval(aup, MILLISECONDS));
            ofNullable(conf.getEnableMultiSchema())
                    .ifPresent(builder::enableMultiSchema);
            ofNullable(conf.getHashingSchemes())
                    .filter(l -> l.size() > 0)
                    .ifPresent(l -> l.forEach(builder::hashingScheme));

            ofNullable(messageRouter)
                    .ifPresent(builder::messageRouter);

            if (conf.getEnableEncrypt())
                builder.defaultCryptoKeyReader(conf.getEncryptionKey()).cryptoFailureAction(conf.getProducerCryptoFailureAction());

            ofNullable(batcherBuilder)
                    .ifPresent(builder::batcherBuilder);

            if (interceptors != null && interceptors.size() > 0)
                builder.intercept(interceptors.toArray(org.apache.pulsar.client.api.interceptor.ProducerInterceptor[]::new));

            return builder.create();
        } catch (Exception e) {
            LOGGER.error("generate producer failed, cause e = {0}", e);
            throw new RuntimeException("generate producer failed, cause e = {}", e);
        }
    }

    /**
     * generate consumer
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, java.util.function.Consumer<T> consumer, ConsumerEventListener consumerEventListener,
                                                   List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        assertConsumerConf(pulsarClient, conf);
        if (consumer == null)
            throw new RuntimeException("consumer can't be null");

        //noinspection unchecked
        Class<T> clz = (Class<T>) getConsumerParameterType(consumer);
        return generateConsumer(pulsarClient, conf, clz, consumerEventListener, interceptors, keySharedPolicy, null);
    }

    /**
     * generate consumer
     *
     * @param pulsarClient
     * @param conf
     * @param consumer
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param messageListener
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, java.util.function.Consumer<T> consumer, ConsumerEventListener consumerEventListener,
                                                   List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy, MessageListener<T> messageListener) {
        assertConsumerConf(pulsarClient, conf);
        if (consumer == null)
            throw new RuntimeException("consumer can't be null");

        //noinspection unchecked
        Class<T> clz = (Class<T>) getConsumerParameterType(consumer);
        return generateConsumer(pulsarClient, conf, clz, consumerEventListener, interceptors, keySharedPolicy, messageListener);
    }

    /**
     * generate consumer
     *
     * @param pulsarClient
     * @param conf
     * @param clz
     * @param consumerEventListener
     * @param interceptors
     * @param keySharedPolicy
     * @param messageListener
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, Class<T> clz, ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors,
                                                   KeySharedPolicy keySharedPolicy, MessageListener<T> messageListener) {
        assertConsumerConf(pulsarClient, conf);
        if (clz == null)
            throw new RuntimeException("clz can't be null");

        try {
            ConsumerBuilder<T> builder = pulsarClient.newConsumer(JSON(clz));

            List<String> topics = conf.getTopics();
            if (topics != null) {
                builder.topics(topics);
            } else {
                builder.topicsPattern(conf.getTopicsPattern());
            }

            builder.subscriptionName(conf.getSubscriptionName())
                    .subscriptionType(conf.getSubscriptionType())
                    .subscriptionMode(conf.getSubscriptionMode())
                    .subscriptionInitialPosition(conf.getSubscriptionInitialPosition())
                    .subscriptionTopicsMode(conf.getRegexSubscriptionMode());

            ofNullable(conf.getAckTimeoutMillis())
                    .ifPresent(atm -> builder.ackTimeout(atm, MILLISECONDS));
            ofNullable(conf.getEnableAckReceipt())
                    .ifPresent(builder::isAckReceiptEnabled);
            ofNullable(conf.getAckTimeoutTickTimeMillis())
                    .ifPresent(attm -> builder.ackTimeoutTickTime(attm, MILLISECONDS));
            ofNullable(conf.getNegativeAckRedeliveryDelayMillis())
                    .ifPresent(nardm -> builder.negativeAckRedeliveryDelay(nardm, MILLISECONDS));
            ofNullable(conf.getReceiverQueueSize())
                    .ifPresent(builder::receiverQueueSize);
            ofNullable(conf.getAcknowledgementsGroupTimeMillis())
                    .ifPresent(agtm -> builder.acknowledgmentGroupTime(agtm, MILLISECONDS));
            ofNullable(conf.getReplicateSubscriptionState())
                    .ifPresent(builder::replicateSubscriptionState);
            ofNullable(conf.getMaxTotalReceiverQueueSizeAcrossPartitions())
                    .ifPresent(builder::maxTotalReceiverQueueSizeAcrossPartitions);
            ofNullable(conf.getConsumerName())
                    .ifPresent(builder::consumerName);
            ofNullable(conf.getReadCompacted())
                    .ifPresent(builder::readCompacted);
            ofNullable(conf.getPatternAutoDiscoveryPeriodMillis())
                    .ifPresent(adpm -> builder.patternAutoDiscoveryPeriod(adpm, MILLISECONDS));
            ofNullable(conf.getPriorityLevel())
                    .ifPresent(builder::priorityLevel);
            ofNullable(conf.getAutoUpdatePartitions())
                    .ifPresent(builder::autoUpdatePartitions);
            ofNullable(conf.getAutoUpdatePartitionsIntervalMillis())
                    .ifPresent(aupim -> builder.autoUpdatePartitionsInterval(aupim, MILLISECONDS));
            ofNullable(conf.getEnableRetry())
                    .ifPresent(builder::enableRetry);
            ofNullable(conf.getEnableBatchIndexAcknowledgment())
                    .ifPresent(builder::enableBatchIndexAcknowledgment);
            ofNullable(conf.getMaxPendingChunkedMessage())
                    .ifPresent(builder::maxPendingChunkedMessage);
            ofNullable(conf.getAutoAckOldestChunkedMessageOnQueueFull())
                    .ifPresent(builder::autoAckOldestChunkedMessageOnQueueFull);
            ofNullable(conf.getExpireTimeOfIncompleteChunkedMessageMillis())
                    .ifPresent(eticmm -> builder.expireTimeOfIncompleteChunkedMessage(eticmm, MILLISECONDS));
            ofNullable(conf.getPoolMessages())
                    .ifPresent(builder::poolMessages);
            ofNullable(messageListener)
                    .ifPresent(builder::messageListener);
            ofNullable(consumerEventListener)
                    .ifPresent(builder::consumerEventListener);

            if (interceptors != null && interceptors.size() > 0)
                //noinspection unchecked
                builder.intercept(interceptors.toArray(ConsumerInterceptor[]::new));

            if (conf.getEnableEncrypt())
                builder.defaultCryptoKeyReader(conf.getEncryptionKey())
                        .cryptoFailureAction(conf.getConsumerCryptoFailureAction());

            if (ofNullable(conf.getEnableDeadLetter()).orElse(false))
                builder.deadLetterPolicy(
                        builder().deadLetterTopic(conf.getDeadLetterTopic())
                                .retryLetterTopic(conf.getRetryLetterTopic())
                                .maxRedeliverCount(conf.getMaxRedeliverCount())
                                .build());

            ofNullable(keySharedPolicy)
                    .ifPresent(builder::keySharedPolicy);

            if (ofNullable(conf.getStartMessageIdInclusive()).orElse(false))
                builder.startMessageIdInclusive();

            if (ofNullable(conf.getEnableBatchReceive()).orElse(false))
                builder.batchReceivePolicy(BatchReceivePolicy.builder()
                        .maxNumMessages(conf.getBatchReceiveMaxNumMessages())
                        .maxNumBytes(conf.getBatchReceiveMaxNumBytes())
                        .timeout(conf.getBatchReceiveTimeoutMillis(), MILLISECONDS).build());

            return builder.subscribe();
        } catch (Exception e) {
            LOGGER.error("generate consumer failed, cause e = {0}", e);
            throw new RuntimeException("generate consumer failed, cause e = {}", e);
        }
    }

    //<editor-fold desc="assert params">

    /**
     * assert client params
     *
     * @param conf
     */
    private static void assertClient(ClientConf conf) {
        if (isNull(conf)) throw new RuntimeException("conf can't be null");

        if (isEmpty(conf.getServices()))
            throw new RuntimeException("services can't be null");

        if (ofNullable(conf.getEnableTls()).orElse(false)) {
            if (isBlank(conf.getTlsTrustCertsFilePath()))
                throw new RuntimeException("if enableTls, tlsTrustCertsFilePath can't be null");

            if (isBlank(conf.getTlsCertFilePath()))
                throw new RuntimeException("if enableTls, tlsCertFilePath can't be null");

            if (isBlank(conf.getTlsKeyFilePath()))
                throw new RuntimeException("if enableTls, tlsKeyFilePath can't be null");

            if (isNull(conf.getTlsAllowInsecureConnection()))
                throw new RuntimeException("if enableTls, tlsAllowInsecureConnection can't be null");

            if (isNull(conf.getTlsHostnameVerificationEnable()))
                throw new RuntimeException("if enableTls, tlsHostnameVerificationEnable can't be null");
        }

        if ((ofNullable(conf.getEnableProxy()).orElse(false))) {
            Proxy proxy = conf.getProxy();
            if (isNull(proxy))
                throw new RuntimeException("if enableProxy, proxy can't be null");

            String proxyServiceUrl = proxy.getProxyServiceUrl();
            if (isBlank(proxyServiceUrl))
                throw new RuntimeException("if enableProxy, proxyServiceUrl can't be null");
            if (isNull(proxy.getProxyProtocol()))
                throw new RuntimeException("if enableProxy, proxyProtocol can't be null");
        }

        if ((ofNullable(conf.getEnableSocks5Proxy()).orElse(false))) {
            Socks5Proxy socks5Proxy = conf.getSocks5Proxy();
            if (isNull(socks5Proxy))
                throw new RuntimeException("if enableSocks5Proxy, socks5Proxy can't be null");

            if (isBlank(socks5Proxy.getHost()))
                throw new RuntimeException("if enableSocks5Proxy, host can't be null");

            Integer port = socks5Proxy.getPort();
            if (isNull(port) || port < 1)
                throw new RuntimeException("if enableSocks5Proxy, port can't be null");
        }
    }

    /**
     * assert producer params
     *
     * @param pulsarClient
     * @param conf
     * @param clz
     * @param <T>
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static <T> void assertProducerConf(PulsarClient pulsarClient, ProducerConf conf, Class<T> clz) {
        if (isNull(pulsarClient) || isNull(conf) || isNull(clz))
            throw new RuntimeException("pulsarClient or conf or clz can't be null");

        if (isBlank(conf.getTopic())) throw new RuntimeException("topics can't be null");

        if (isBlank(conf.getProducerName())) throw new RuntimeException("producerName can't be null");

        if (isNull(conf.getAccessMode())) throw new RuntimeException("accessMode can't be null");

        if (isNull(conf.getMessageRoutingMode())) throw new RuntimeException("messageRoutingMode can't be null");

        if (isNull(conf.getCompressionType())) throw new RuntimeException("compressionType can't be null");

        if (ofNullable(conf.getEnableBatching()).orElse(false) &&
                ofNullable(conf.getEnableChunking()).orElse(false))
            throw new RuntimeException("batching and chunking can't be both enabled");

        if (ofNullable(conf.getEnableEncrypt()).orElse(false)) {
            if (isBlank(conf.getEncryptionKey()))
                throw new RuntimeException("if enableEncrypt, encryptionKey can't be null");

            if (isNull(conf.getProducerCryptoFailureAction()))
                throw new RuntimeException("if enableEncrypt, producerCryptoFailureAction or consumer can't be null");
        }

    }

    /**
     * assert consumer params
     *
     * @param conf
     */
    @SuppressWarnings("AlibabaMethodTooLong")
    private static void assertConsumerConf(PulsarClient pulsarClient, ConsumerConf conf) {
        if (isNull(pulsarClient) || isNull(conf))
            throw new RuntimeException("pulsarClient or conf can't be null");

        //noinspection AlibabaAvoidComplexCondition
        if (isEmpty(conf.getTopics()) && isBlank(conf.getTopicsPattern()))
            throw new RuntimeException("topics and topicsPattern can't be both null");

        if (isBlank(conf.getSubscriptionName()))
            throw new RuntimeException("subscriptionName can't be null");

        if (isNull(conf.getSubscriptionType()))
            throw new RuntimeException("subscriptionType can't be null");

        if (isNull(conf.getSubscriptionMode()))
            throw new RuntimeException("subscriptionMode can't be null");

        if (isNull(conf.getSubscriptionInitialPosition()))
            throw new RuntimeException("subscriptionInitialPosition can't be null");

        if (isNull(conf.getRegexSubscriptionMode()))
            throw new RuntimeException("regexSubscriptionMode can't be null");

        if (ofNullable(conf.getEnableEncrypt()).orElse(false)) {
            if (isBlank(conf.getEncryptionKey()))
                throw new RuntimeException("if enableEncrypt, encryptionKey can't be null or ''");

            if (isNull(conf.getConsumerCryptoFailureAction()))
                throw new RuntimeException("if enableEncrypt, consumerCryptoFailureAction can't be null");
        }

        if (ofNullable(conf.getEnableDeadLetter()).orElse(false)) {
            if (isBlank(conf.getDeadLetterTopic()))
                throw new RuntimeException("if enableDeadLetter, deadLetterTopic can't be null");

            if (isBlank(conf.getRetryLetterTopic()))
                throw new RuntimeException("if enableDeadLetter, retryLetterTopic can't be null");

            Integer maxRedeliverCount = conf.getMaxRedeliverCount();
            if (isNull(maxRedeliverCount) || maxRedeliverCount < 1)
                throw new RuntimeException("if enableDeadLetter, maxRedeliverCount can't be null or less than 1");
        }

        if (ofNullable(conf.getEnableBatchReceive()).orElse(false)) {
            Integer batchReceiveMaxNumMessages = conf.getBatchReceiveMaxNumMessages();
            if (isNull(batchReceiveMaxNumMessages) || batchReceiveMaxNumMessages < -1)
                throw new RuntimeException("batchReceiveMaxNumMessages can't be null or less than -1");

            Integer batchReceiveMaxNumBytes = conf.getBatchReceiveMaxNumBytes();
            if (isNull(batchReceiveMaxNumBytes) || batchReceiveMaxNumBytes < 1)
                throw new RuntimeException("batchReceiveMaxNumBytes can't be null or less than 1");

            Integer batchReceiveTimeoutMillis = conf.getBatchReceiveTimeoutMillis();
            if (isNull(batchReceiveTimeoutMillis) || batchReceiveTimeoutMillis < 1)
                throw new RuntimeException("batchReceiveTimeoutMillis can't be null or less than 1");
        }

        Integer pollDurationMills = conf.getPollDurationMills();
        if (isNull(pollDurationMills) || pollDurationMills < 1)
            throw new RuntimeException("pollDurationMills can't be null or less than 1");

        if (conf.getEnableNegativeAcknowledge() == null)
            throw new RuntimeException("enableNegativeAcknowledge can't be null");

        Integer workingThreads = conf.getWorkingThreads();
        if (isNull(workingThreads) || workingThreads < 1)
            throw new RuntimeException("workingThreads can't be null or less than 1");
    }
    //</editor-fold>

}
