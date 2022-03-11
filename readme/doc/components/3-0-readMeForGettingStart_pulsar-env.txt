1.官网地址:   pulsar.apache.org

2.根据你的需要搭建pulsar环境
    以ubuntu server为例
    0. sudo passwd root            apt install net-tools
    1. sudo apt install openjdk-11-jdk
    2. wget https://archive.apache.org/dist/pulsar/pulsar-2.8.0/apache-pulsar-2.8.0-bin.tar.gz
    3. 设置权限 解压
    4. 按需修改standalone.conf中的配置(单机测试)
    5. ./pulsar standalone

3.修改项目中的配置,相关配置为


yml:

producers:
  configs:
    illegalMark:
      services: [ 192.168.0.124:6650 ]
      enableTls: false
      tlsTrustCertsFilePath:
      tlsCertFilePath:
      tlsKeyFilePath:
      tlsAllowInsecureConnection:
      tlsHostnameVerificationEnable:
      listenerName:
      operationTimeoutMillis: 30000
      statsIntervalMillis: 60000
      ioThreads: 1
      listenerThreads: 1
      connectionsPerBroker: 1
      useTcpNoDelay: true
      memoryLimitKiloBytes: 512
      maxConcurrentLookupRequests: 60
      maxLookupRequest: 50000
      maxLookupRedirects: 3
      maxNumberOfRejectedRequestPerConnection: 50
      keepAliveIntervalMillis: 30000
      connectionTimeoutMillis: 60000
      startingBackoffIntervalMillis: 300
      maxBackoffIntervalMillis: 600
      enableBusyWait: false
      clockZoneId: Asia/Shanghai
      enableTransaction: false
      enableProxy: false
      proxyServiceUrl:
      proxyProtocol: SNI
      topic: illegalMark
      producerName: dataIllegalMarkProducer000
      accessMode: Shared
      sendTimeoutMillis: 30000
      maxPendingMessages: 1000
      maxPendingMessagesAcrossPartitions: 5000
      blockIfQueueFull: false
      messageRoutingMode: RoundRobinPartition
      compressionType: NONE
      enableBatching: false
      enableChunking: true
      batchingMaxPublishDelayMillis: 1000
      roundRobinRouterBatchingPartitionSwitchFrequency: 2
      batchingMaxMessages: 512
      batchingMaxBytes: 512
      initialSequenceId: 0
      autoUpdatePartitions: true
      autoUpdatePartitionsIntervalMillis: 30000
      enableMultiSchema: false
      hashingSchemes: [ JavaStringHash ]
      enableEncrypt: false
      encryptionKey:
      producerCryptoFailureAction: FAIL


consumers:
  configs:
    requestEvent:
      services: [ 192.168.0.124:6650 ]
      enableTls: false
      tlsTrustCertsFilePath:
      tlsCertFilePath:
      tlsKeyFilePath:
      tlsAllowInsecureConnection:
      tlsHostnameVerificationEnable:
      listenerName:
      operationTimeoutMillis: 30000
      statsIntervalMillis: 60
      ioThreads: 1
      listenerThreads: 1
      connectionsPerBroker: 2
      useTcpNoDelay: true
      memoryLimitKiloBytes: 1024
      maxConcurrentLookupRequests: 60
      maxLookupRequest: 1000
      maxLookupRedirects: 2
      maxNumberOfRejectedRequestPerConnection: 50
      keepAliveIntervalMillis: 30000
      connectionTimeoutMillis: 10000
      startingBackoffIntervalMillis: 1000
      maxBackoffIntervalMillis: 30000
      enableBusyWait: true
      clockZoneId: Asia/Shanghai
      enableTransaction: false
      enableProxy: false
      proxyServiceUrl:
      proxyProtocol: SNI
      topics: [ requestEvent ]
      topicsPattern:
      subscriptionName: dataRequestEventSubscription
      ackTimeoutMillis: 2000
      enableAckReceipt: true
      ackTimeoutTickTimeMillis: 1000
      negativeAckRedeliveryDelayMillis: 100
      subscriptionType: Shared
      subscriptionMode: Durable
      receiverQueueSize: 1000
      acknowledgementsGroupTimeMillis: 100
      replicateSubscriptionState: false
      maxTotalReceiverQueueSizeAcrossPartitions: 5
      consumerName: dataRequestEventConsumer001
      readCompacted: false
      patternAutoDiscoveryPeriodMillis: 1
      priorityLevel: 0
      subscriptionInitialPosition: Latest
      regexSubscriptionMode: PersistentOnly
      autoUpdatePartitions: true
      autoUpdatePartitionsIntervalMillis: 30000
      batchReceivePolicy: DEFAULT_POLICY
      enableRetry: true
      enableBatchIndexAcknowledgment: false
      maxPendingChunkedMessage: 10
      autoAckOldestChunkedMessageOnQueueFull: true
      expireTimeOfIncompleteChunkedMessageMillis: 2000
      poolMessages: false
      enableEncrypt: false
      encryptionKey:
      consumerCryptoFailureAction: FAIL
      enableDeadLetter: true
      deadLetterTopic: requestEvent
      retryLetterTopic: requestEvent
      maxRedeliverCount: 1000
      enableBatchReceive: true
      batchReceiveMaxNumMessages: -1
      batchReceiveMaxNumBytes: 10485760
      batchReceiveTimeoutMillis: 100
      pollDurationMills: 1000
      workingThreads: 2
      enableNegativeAcknowledge: true
	  
	  
	  
	  
配置bean为:
	config/mq下的相关producer及consumer配置,请参考样本



请根据自己的需要自行定制