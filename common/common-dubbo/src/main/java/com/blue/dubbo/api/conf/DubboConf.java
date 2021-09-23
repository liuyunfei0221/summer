package com.blue.dubbo.api.conf;

import java.util.Map;

/**
 * dubbo配置类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "AlibabaCommentsMustBeJavadocFormat"})
public interface DubboConf {

    //<editor-fold desc="metadataReport配置">
    String getMetadataReportId();

    String getMetadataReportProtocol();

    String getMetadataReportAddress();

    Integer getMetadataReportPort();

    String getMetadataReportUsername();

    String getMetadataReportPassword();

    Integer getMetadataReportTimeout();

    String getMetadataReportGroup();

    Integer getMetadataReportRetryTimes();

    Integer getMetadataReportRetryPeriod();

    Boolean getMetadataReportCycleReport();

    Boolean getMetadataReportSyncReport();

    Boolean getMetadataReportCluster();

    String getMetadataReportRegistry();

    String getMetadataReportFile();

    Map<String, String> getMetadataReportParameters();
    //</editor-fold>

    //<editor-fold desc="registry配置">
    String getRegistryId();

    String getRegistryAddress();

    String getRegistryUsername();

    String getRegistryPassword();

    Integer getRegistryPort();

    String getRegistryProtocol();

    String getRegistryTransporter();

    String getRegistryServer();

    String getRegistryClient();

    String getRegistryCluster();

    String getRegistryZone();

    String getRegistryGroup();

    String getRegistryVersion();

    Integer getRegistryTimeout();

    Integer getRegistrySession();

    String getRegistryFile();

    Boolean getRegistryCheck();

    Boolean getRegistryDynamic();

    Boolean getRegistryRegister();

    Boolean getRegistrySubscribe();

    Boolean getRegistrySimplified();

    String getRegistryExtraKeys();

    Boolean getRegistryUseAsConfigCenter();

    Boolean getRegistryUseAsMetadataCenter();

    String getRegistryAccepts();

    Boolean getRegistryPreferred();

    Integer getRegistryWeight();

    Boolean getRegistryPublishInterface();

    Boolean getRegistryPublishInstance();

    Map<String, String> getRegistryParameters();
    //</editor-fold>

    //<editor-fold desc="monitor配置">
    Boolean getMonitorEnable();

    String getMonitorId();

    String getMonitorProtocol();

    String getMonitorAddress();

    String getMonitorUsername();

    String getMonitorPassword();

    String getMonitorGroup();

    String getMonitorVersion();

    String getMonitorInterval();
    //</editor-fold>

    //<editor-fold desc="application配置">
    String getApplicationId();

    String getApplicationName();

    String getApplicationVersion();

    String getApplicationOwner();

    String getApplicationOrganization();

    String getApplicationArchitecture();

    String getApplicationEnvironment();

    String getApplicationCompiler();

    String getApplicationLogger();

    String getApplicationDumpDirectory();

    String getApplicationShutWait();

    String getApplicationMetadataType();

    Boolean getApplicationRegisterConsumer();

    String getApplicationRepository();

    Boolean getApplicationEnableFileCache();

    Boolean getApplicationPublishInterface();

    Boolean getApplicationPublishInstance();

    String getApplicationProtocol();

    Integer getApplicationMetadataServicePort();

    String getApplicationLivenessProbe();

    String getApplicationReadinessProbe();

    String getApplicationStartupProbe();

    Map<String, String> getApplicationParameters();

    Boolean getApplicationQosEnable();

    String getApplicationQosHost();

    Integer getApplicationQosPort();

    Boolean getApplicationQosAcceptForeignIp();
    //</editor-fold>

    //<editor-fold desc="protocol配置">
    String getProtocolId();

    String getProtocolName();

    String getProtocolHost();

    Integer getProtocolPort();

    String getProtocolContextpath();

    String getProtocolThreadpool();

    String getProtocolThreadname();

    Integer getProtocolCorethreads();

    Integer getProtocolThreads();

    Integer getProtocolIothreads();

    Integer getProtocolAlive();

    Integer getProtocolQueues();

    Integer getProtocolAccepts();

    String getProtocolCodec();

    String getProtocolSerialization();

    String getProtocolCharset();

    Integer getProtocolPayload();

    Integer getProtocolBuffer();

    Integer getProtocolHeartbeat();

    String getProtocolAccesslog();

    String getProtocolTransporter();

    String getProtocolExchanger();

    String getProtocolDispatcher();

    String getProtocolNetworker();

    String getProtocolServer();

    String getProtocolClient();

    String getProtocolTelnet();

    String getProtocolPrompt();

    String getProtocolStatus();

    Boolean getProtocolRegister();

    Boolean getProtocolKeepAlive();

    String getProtocolOptimizer();

    String getProtocolExtension();

    Boolean getProtocolSslEnabled();

    Map<String, String> getProtocolParameters();

    Boolean getProtocolDefault();
    //</editor-fold>

    //<editor-fold desc="provider配置">
    String getProviderId();

    String getProviderInterfaceName();

    String getProviderStub();

    String getProviderProxy();

    String getProviderCluster();

    String getProviderListener();

    String getProviderOwner();

    Integer getProviderConnections();

    String getProviderLayer();

    String getProviderOnconnect();

    String getProviderOndisconnect();

    Integer getProviderCallbacks();

    String getProviderScope();

    String getProviderTag();

    Boolean getProviderAuth();

    String getProviderVersion();

    String getProviderGroup();

    Boolean getProviderDeprecated();

    Integer getProvideDelay();

    Boolean getProviderExport();

    Integer getProviderWeight();

    String getProviderDocument();

    Boolean getProviderDynamic();

    String getProviderToken();

    String getProviderAccesslog();

    Integer getProviderExecutes();

    Boolean getProviderRegister();

    Integer getProviderWarmup();

    String getProviderSerialization();

    Boolean getProviderExportAsync();

    Integer getProviderTimeout();

    Integer getProviderRetries();

    Integer getProviderActives();

    String getProviderLoadbalance();

    Boolean getProviderAsync();

    Boolean getProviderSent();

    String getProviderMock();

    String getProviderMerger();

    String getProviderCache();

    String getProviderValidation();

    Integer getProviderForks();

    String getProviderHost();

    String getProviderContextpath();

    String getProviderThreadpool();

    String getProviderThreadname();

    Integer getProviderThreads();

    Integer getProviderIothreads();

    Integer getProviderAlive();

    Integer getProviderQueues();

    Integer getProviderAccepts();

    String getProviderCodec();

    String getProviderCharset();

    Integer getProviderPayload();

    Integer getProviderBuffer();

    String getProviderTransporter();

    String getProviderExchanger();

    String getProviderDispatcher();

    String getProviderNetworker();

    String getProviderServer();

    String getProviderClient();

    String getProviderTelnet();

    String getProviderPrompt();

    String getProviderStatus();

    Integer getProviderWait();

    Integer getProviderExportThreadNum();

    Boolean getProviderExportBackground();

    Map<String, String> getProviderParameters();

    //</editor-fold>

    //<editor-fold desc="consumer配置">
    String getConsumerId();

    Integer getConsumerTimeout();

    Integer getConsumerRetries();

    Integer getConsumerActives();

    String getConsumerLoadbalance();

    Boolean getConsumerAsync();

    Boolean getConsumerSent();

    String getConsumerMock();

    String getConsumerMerger();

    String getConsumerCache();

    String getConsumerValidation();

    Integer getConsumerForks();

    String getConsumerInterfaceName();

    String getConsumerVersion();

    String getConsumerGroup();

    String getConsumerStub();

    String getConsumerProxy();

    String getConsumerCluster();

    String getConsumerListener();

    String getConsumerOwner();

    Integer getConsumerConnections();

    String getConsumerLayer();

    String getConsumerOnconnect();

    String getConsumerOndisconnect();

    Integer getConsumerCallbacks();

    String getConsumerScope();

    String getConsumerTag();

    Boolean getConsumerAuth();

    Boolean getConsumerCheck();

    Boolean getConsumerInit();

    String getConsumerGeneric();

    Boolean getConsumerLazy();

    String getConsumerReconnect();

    Boolean getConsumerSticky();

    String getConsumerProvidedBy();

    String getConsumerRouter();

    Boolean getConsumerReferAsync();

    String getConsumerClient();

    String getConsumerThreadpool();

    Integer getConsumerCorethreads();

    Integer getConsumerThreads();

    Integer getConsumerQueues();

    Integer getConsumerShareconnections();

    String getConsumerUrlMergeProcessor();

    Integer getConsumerReferThreadNum();

    Boolean getConsumerReferBackground();
    //</editor-fold>

}

