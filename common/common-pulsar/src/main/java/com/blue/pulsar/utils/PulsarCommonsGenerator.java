package com.blue.pulsar.utils;

import com.blue.pulsar.api.conf.ClientConf;
import com.blue.pulsar.api.conf.ConsumerConf;
import com.blue.pulsar.api.conf.ProducerConf;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.interceptor.ProducerInterceptor;
import org.slf4j.Logger;

import java.util.List;
import java.util.function.BinaryOperator;
import java.util.function.Function;

import static com.blue.pulsar.utils.FunctionParameterClzGetter.getConsumerParameterType;
import static java.time.Clock.system;
import static java.time.ZoneId.of;
import static java.util.Optional.ofNullable;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
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
                    .filter(n -> !"".equals(n))
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

            if (ofNullable(conf.getEnableProxy()).orElse(false))
                builder.proxyServiceUrl(conf.getProxyServiceUrl(), conf.getProxyProtocol());

            return builder.build();
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
            ofNullable(conf.getMaxPendingMessagesAcrossPartitions())
                    .ifPresent(builder::maxPendingMessagesAcrossPartitions);
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
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, java.util.function.Consumer<T> consumer, MessageListener<T> messageListener,
                                                   ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
        assertConsumerConf(pulsarClient, conf);
        if (consumer == null)
            throw new RuntimeException("consumer can't be null");

        //noinspection unchecked
        Class<T> clz = (Class<T>) getConsumerParameterType(consumer);
        return generateConsumer(pulsarClient, conf, clz, messageListener, consumerEventListener, interceptors, keySharedPolicy);
    }

    /**
     * generate consumer
     *
     * @param pulsarClient
     * @param conf
     * @param <T>
     * @return
     */
    public static <T> Consumer<T> generateConsumer(PulsarClient pulsarClient, ConsumerConf conf, Class<T> clz, MessageListener<T> messageListener,
                                                   ConsumerEventListener consumerEventListener, List<ConsumerInterceptor<T>> interceptors, KeySharedPolicy keySharedPolicy) {
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
        if (conf == null) throw new RuntimeException("conf can't be null");

        List<String> services = conf.getServices();
        if (services == null || services.size() < 1) throw new RuntimeException("services can't be null");

        if (ofNullable(conf.getEnableTls()).orElse(false)) {
            String tlsTrustCertsFilePath = conf.getTlsTrustCertsFilePath();
            if (tlsTrustCertsFilePath == null || "".equals(tlsTrustCertsFilePath))
                throw new RuntimeException("if enableTls, tlsTrustCertsFilePath can't be null");

            String tlsCertFilePath = conf.getTlsCertFilePath();
            if (tlsCertFilePath == null || "".equals(tlsCertFilePath))
                throw new RuntimeException("if enableTls, tlsCertFilePath can't be null");

            String tlsKeyFilePath = conf.getTlsKeyFilePath();
            if (tlsKeyFilePath == null || "".equals(tlsKeyFilePath))
                throw new RuntimeException("if enableTls, tlsKeyFilePath can't be null");

            Boolean tlsAllowInsecureConnection = conf.getTlsAllowInsecureConnection();
            if (tlsAllowInsecureConnection == null)
                throw new RuntimeException("if enableTls, tlsAllowInsecureConnection can't be null");

            Boolean tlsHostnameVerificationEnable = conf.getTlsHostnameVerificationEnable();
            if (tlsHostnameVerificationEnable == null)
                throw new RuntimeException("if enableTls, tlsHostnameVerificationEnable can't be null");
        }

        if ((ofNullable(conf.getEnableProxy()).orElse(false))) {
            String proxyServiceUrl = conf.getProxyServiceUrl();
            if (proxyServiceUrl == null || "".equals(proxyServiceUrl))
                throw new RuntimeException("if enableProxy, proxyServiceUrl can't be null");
            if (conf.getProxyProtocol() == null)
                throw new RuntimeException("if enableProxy, proxyProtocol can't be null");
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
        if (pulsarClient == null || conf == null || clz == null)
            throw new RuntimeException("pulsarClient or conf or clz can't be null");

        String topics = conf.getTopic();
        if (topics == null || "".equals(topics)) throw new RuntimeException("topics can't be null");

        String producerName = conf.getProducerName();
        if (producerName == null || "".equals(producerName)) throw new RuntimeException("producerName can't be null");

        if (conf.getAccessMode() == null) throw new RuntimeException("accessMode can't be null");

        if (conf.getMessageRoutingMode() == null) throw new RuntimeException("messageRoutingMode can't be null");

        if (conf.getCompressionType() == null) throw new RuntimeException("compressionType can't be null");

        if (ofNullable(conf.getEnableBatching()).orElse(false) &&
                ofNullable(conf.getEnableChunking()).orElse(false))
            throw new RuntimeException("batching and chunking can't be both enabled");

        if (ofNullable(conf.getEnableEncrypt()).orElse(false)) {
            String encryptionKey = conf.getEncryptionKey();
            if (encryptionKey == null || "".equals(encryptionKey))
                throw new RuntimeException("if enableEncrypt, encryptionKey can't be null");

            if (conf.getProducerCryptoFailureAction() == null)
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
        if (pulsarClient == null || conf == null)
            throw new RuntimeException("pulsarClient or conf can't be null");

        List<String> topics = conf.getTopics();
        String topicsPattern = conf.getTopicsPattern();

        //noinspection AlibabaAvoidComplexCondition
        if ((topics == null || topics.size() < 1) && (topicsPattern == null || "".equals(topicsPattern)))
            throw new RuntimeException("topics and topicsPattern can't be both null");

        if (isBlank(conf.getSubscriptionName()))
            throw new RuntimeException("subscriptionName can't be null");

        if (conf.getSubscriptionType() == null)
            throw new RuntimeException("subscriptionType can't be null");

        if (conf.getSubscriptionMode() == null)
            throw new RuntimeException("subscriptionMode can't be null");

        if (conf.getSubscriptionInitialPosition() == null)
            throw new RuntimeException("subscriptionInitialPosition can't be null");

        if (conf.getRegexSubscriptionMode() == null)
            throw new RuntimeException("regexSubscriptionMode can't be null");

        if (ofNullable(conf.getEnableEncrypt()).orElse(false)) {
            if (isBlank(conf.getEncryptionKey()))
                throw new RuntimeException("if enableEncrypt, encryptionKey can't be null or ''");

            if (conf.getConsumerCryptoFailureAction() == null)
                throw new RuntimeException("if enableEncrypt, consumerCryptoFailureAction can't be null");
        }

        if (ofNullable(conf.getEnableDeadLetter()).orElse(false)) {
            if (isBlank(conf.getDeadLetterTopic()))
                throw new RuntimeException("if enableDeadLetter, deadLetterTopic can't be null");

            if (isBlank(conf.getRetryLetterTopic()))
                throw new RuntimeException("if enableDeadLetter, retryLetterTopic can't be null");

            Integer maxRedeliverCount = conf.getMaxRedeliverCount();
            if (maxRedeliverCount == null || maxRedeliverCount < 1)
                throw new RuntimeException("if enableDeadLetter, maxRedeliverCount can't be null or less than 1");
        }

        if (ofNullable(conf.getEnableBatchReceive()).orElse(false)) {
            Integer batchReceiveMaxNumMessages = conf.getBatchReceiveMaxNumMessages();
            if (batchReceiveMaxNumMessages == null || batchReceiveMaxNumMessages < -1)
                throw new RuntimeException("batchReceiveMaxNumMessages can't be null or less than -1");

            Integer batchReceiveMaxNumBytes = conf.getBatchReceiveMaxNumBytes();
            if (batchReceiveMaxNumBytes == null || batchReceiveMaxNumBytes < 1)
                throw new RuntimeException("batchReceiveMaxNumBytes can't be null or less than 1");

            Integer batchReceiveTimeoutMillis = conf.getBatchReceiveTimeoutMillis();
            if (batchReceiveTimeoutMillis == null || batchReceiveTimeoutMillis < 1)
                throw new RuntimeException("batchReceiveTimeoutMillis can't be null or less than 1");
        }

        Integer pollDurationMills = conf.getPollDurationMills();
        if (pollDurationMills == null || pollDurationMills < 1)
            throw new RuntimeException("pollDurationMills can't be null or less than 1");

        if (conf.getEnableNegativeAcknowledge() == null)
            throw new RuntimeException("enableNegativeAcknowledge can't be null");

        Integer workingThreads = conf.getWorkingThreads();
        if (workingThreads == null || workingThreads < 1)
            throw new RuntimeException("workingThreads can't be null or less than 1");
    }
    //</editor-fold>

}
