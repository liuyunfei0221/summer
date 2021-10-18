package com.blue.dubbo.api.conf;

import java.util.Map;

/**
 * dubbo params
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
public class DubboConfParams implements DubboConf {

    protected String metadataReportId;

    protected String metadataReportProtocol;

    protected String metadataReportAddress;

    protected Integer metadataReportPort;

    protected String metadataReportUsername;

    protected String metadataReportPassword;

    protected Integer metadataReportTimeout;

    protected String metadataReportGroup;

    protected Integer metadataReportRetryTimes;

    protected Integer metadataReportRetryPeriod;

    protected Boolean metadataReportCycleReport;

    protected Boolean metadataReportSyncReport;

    protected Boolean metadataReportCluster;

    protected String metadataReportRegistry;

    protected String metadataReportFile;

    protected Map<String, String> metadataReportParameters;

    protected String registryId;

    protected String registryAddress;

    protected String registryUsername;

    protected String registryPassword;

    protected Integer registryPort;

    protected String registryProtocol;

    protected String registryTransporter;

    protected String registryServer;

    protected String registryClient;

    protected String registryCluster;

    protected String registryZone;

    protected String registryGroup;

    protected String registryVersion;

    protected Integer registryTimeout;

    protected Integer registrySession;

    protected String registryFile;

    protected Boolean registryCheck;

    protected Boolean registryDynamic;

    protected Boolean registryRegister;

    protected Boolean registrySubscribe;

    protected Boolean registrySimplified;

    protected String registryExtraKeys;

    protected Boolean registryUseAsConfigCenter;

    protected Boolean registryUseAsMetadataCenter;

    protected String registryAccepts;

    protected Boolean registryPreferred;

    protected Integer registryWeight;

    protected Boolean registryPublishInterface;

    protected Boolean registryPublishInstance;

    protected Map<String, String> registryParameters;

    protected Boolean monitorEnable;

    protected String monitorId;

    protected String monitorProtocol;

    protected String monitorAddress;

    protected String monitorUsername;

    protected String monitorPassword;

    protected String monitorGroup;

    protected String monitorVersion;

    protected String monitorInterval;

    protected String applicationId;

    protected String applicationName;

    protected String applicationVersion;

    protected String applicationOwner;

    protected String applicationOrganization;

    protected String applicationArchitecture;

    protected String applicationEnvironment;

    protected String applicationCompiler;

    protected String applicationLogger;

    protected String applicationDumpDirectory;

    protected String applicationShutWait;

    protected String applicationMetadataType;

    protected Boolean applicationRegisterConsumer;

    protected String applicationRepository;

    protected Boolean applicationEnableFileCache;

    protected Boolean applicationPublishInterface;

    protected Boolean applicationPublishInstance;

    protected String applicationProtocol;

    protected Integer applicationMetadataServicePort;

    protected String applicationLivenessProbe;

    protected String applicationReadinessProbe;

    protected String applicationStartupProbe;

    protected Boolean applicationQosEnable;

    protected String applicationQosHost;

    protected Integer applicationQosPort;

    protected Boolean applicationQosAcceptForeignIp;

    protected Map<String, String> applicationParameters;

    protected String protocolId;

    protected String protocolName;

    protected String protocolHost;

    protected Integer protocolPort;

    protected String protocolContextpath;

    protected String protocolThreadpool;

    protected String protocolThreadname;

    protected Integer protocolCorethreads;

    protected Integer protocolThreads;

    protected Integer protocolIothreads;

    protected Integer protocolAlive;

    protected Integer protocolQueues;

    protected Integer protocolAccepts;

    protected String protocolCodec;

    protected String protocolSerialization;

    protected String protocolCharset;

    protected Integer protocolPayload;

    protected Integer protocolBuffer;

    protected Integer protocolHeartbeat;

    protected String protocolAccesslog;

    protected String protocolTransporter;

    protected String protocolExchanger;

    protected String protocolDispatcher;

    protected String protocolNetworker;

    protected String protocolServer;

    protected String protocolClient;

    protected String protocolTelnet;

    protected String protocolPrompt;

    protected String protocolStatus;

    protected Boolean protocolRegister;

    protected Boolean protocolKeepAlive;

    protected String protocolOptimizer;

    protected String protocolExtension;

    protected Boolean protocolSslEnabled;

    protected Map<String, String> protocolParameters;

    protected Boolean protocolDefault;

    protected String providerId;

    protected String providerInterfaceName;

    protected String providerStub;

    protected String providerProxy;

    protected String providerCluster;

    protected String providerListener;

    protected String providerOwner;

    protected Integer providerConnections;

    protected String providerLayer;

    protected String providerOnconnect;

    protected String providerOndisconnect;

    protected Integer providerCallbacks;

    protected String providerScope;

    protected String providerTag;

    protected Boolean providerAuth;

    protected String providerVersion;

    protected String providerGroup;

    protected Boolean providerDeprecated;

    protected Integer provideDelay;

    protected Boolean providerExport;

    protected Integer providerWeight;

    protected String providerDocument;

    protected Boolean providerDynamic;

    protected String providerToken;

    protected String providerAccesslog;

    protected Integer providerExecutes;

    protected Boolean providerRegister;

    protected Integer providerWarmup;

    protected String providerSerialization;

    protected Boolean providerExportAsync;

    protected Integer providerTimeout;

    protected Integer providerRetries;

    protected Integer providerActives;

    protected String providerLoadbalance;

    protected Boolean providerAsync;

    protected Boolean providerSent;

    protected String providerMock;

    protected String providerMerger;

    protected String providerCache;

    protected String providerValidation;

    protected Integer providerForks;

    protected String providerHost;

    protected String providerContextpath;

    protected String providerThreadpool;

    protected String providerThreadname;

    protected Integer providerThreads;

    protected Integer providerIothreads;

    protected Integer providerAlive;

    protected Integer providerQueues;

    protected Integer providerAccepts;

    protected String providerCodec;

    protected String providerCharset;

    protected Integer providerPayload;

    protected Integer providerBuffer;

    protected String providerTransporter;

    protected String providerExchanger;

    protected String providerDispatcher;

    protected String providerNetworker;

    protected String providerServer;

    protected String providerClient;

    protected String providerTelnet;

    protected String providerPrompt;

    protected String providerStatus;

    protected Integer providerWait;

    protected Integer providerExportThreadNum;

    protected Boolean providerExportBackground;

    protected Map<String, String> providerParameters;

    protected String consumerId;

    protected Integer consumerTimeout;

    protected Integer consumerRetries;

    protected Integer consumerActives;

    protected String consumerLoadbalance;

    protected Boolean consumerAsync;

    protected Boolean consumerSent;

    protected String consumerMock;

    protected String consumerMerger;

    protected String consumerCache;

    protected String consumerValidation;

    protected Integer consumerForks;

    protected String consumerInterfaceName;

    protected String consumerVersion;

    protected String consumerGroup;

    protected String consumerStub;

    protected String consumerProxy;

    protected String consumerCluster;

    protected String consumerListener;

    protected String consumerOwner;

    protected Integer consumerConnections;

    protected String consumerLayer;

    protected String consumerOnconnect;

    protected String consumerOndisconnect;

    protected Integer consumerCallbacks;

    protected String consumerScope;

    protected String consumerTag;

    protected Boolean consumerAuth;

    protected Boolean consumerCheck;

    protected Boolean consumerInit;

    protected String consumerGeneric;

    protected Boolean consumerLazy;

    protected String consumerReconnect;

    protected Boolean consumerSticky;

    protected String consumerProvidedBy;

    protected String consumerRouter;

    protected Boolean consumerReferAsync;

    protected String consumerClient;

    protected String consumerThreadpool;

    protected Integer consumerCorethreads;

    protected Integer consumerThreads;

    protected Integer consumerQueues;

    protected Integer consumerShareconnections;

    protected String consumerUrlMergeProcessor;

    protected Integer consumerReferThreadNum;

    protected Boolean consumerReferBackground;

    public DubboConfParams() {
    }

    public DubboConfParams(String metadataReportId, String metadataReportProtocol, String metadataReportAddress,
                           Integer metadataReportPort, String metadataReportUsername, String metadataReportPassword,
                           Integer metadataReportTimeout, String metadataReportGroup, Integer metadataReportRetryTimes,
                           Integer metadataReportRetryPeriod, Boolean metadataReportCycleReport,
                           Boolean metadataReportSyncReport, Boolean metadataReportCluster, String metadataReportRegistry,
                           String metadataReportFile, Map<String, String> metadataReportParameters, String registryId,
                           String registryAddress, String registryUsername, String registryPassword, Integer registryPort,
                           String registryProtocol, String registryTransporter, String registryServer, String registryClient,
                           String registryCluster, String registryZone, String registryGroup, String registryVersion,
                           Integer registryTimeout, Integer registrySession, String registryFile, Boolean registryCheck,
                           Boolean registryDynamic, Boolean registryRegister, Boolean registrySubscribe,
                           Boolean registrySimplified, String registryExtraKeys, Boolean registryUseAsConfigCenter,
                           Boolean registryUseAsMetadataCenter, String registryAccepts, Boolean registryPreferred,
                           Integer registryWeight, Boolean registryPublishInterface, Boolean registryPublishInstance,
                           Map<String, String> registryParameters, Boolean monitorEnable, String monitorId,
                           String monitorProtocol, String monitorAddress, String monitorUsername, String monitorPassword,
                           String monitorGroup, String monitorVersion, String monitorInterval, String applicationId,
                           String applicationName, String applicationVersion, String applicationOwner,
                           String applicationOrganization, String applicationArchitecture, String applicationEnvironment,
                           String applicationCompiler, String applicationLogger, String applicationDumpDirectory,
                           String applicationShutWait, String applicationMetadataType, Boolean applicationRegisterConsumer,
                           String applicationRepository, Boolean applicationEnableFileCache, Boolean applicationPublishInterface,
                           Boolean applicationPublishInstance, String applicationProtocol, Integer applicationMetadataServicePort,
                           String applicationLivenessProbe, String applicationReadinessProbe, String applicationStartupProbe,
                           Boolean applicationQosEnable, String applicationQosHost, Integer applicationQosPort,
                           Boolean applicationQosAcceptForeignIp, Map<String, String> applicationParameters,
                           String protocolId, String protocolName, String protocolHost, Integer protocolPort,
                           String protocolContextpath, String protocolThreadpool, String protocolThreadname,
                           Integer protocolCorethreads, Integer protocolThreads, Integer protocolIothreads,
                           Integer protocolAlive, Integer protocolQueues, Integer protocolAccepts, String protocolCodec,
                           String protocolSerialization, String protocolCharset, Integer protocolPayload, Integer protocolBuffer,
                           Integer protocolHeartbeat, String protocolAccesslog, String protocolTransporter,
                           String protocolExchanger, String protocolDispatcher, String protocolNetworker, String protocolServer,
                           String protocolClient, String protocolTelnet, String protocolPrompt, String protocolStatus,
                           Boolean protocolRegister, Boolean protocolKeepAlive, String protocolOptimizer,
                           String protocolExtension, Boolean protocolSslEnabled, Map<String, String> protocolParameters,
                           Boolean protocolDefault, String providerId, String providerInterfaceName, String providerStub,
                           String providerProxy, String providerCluster, String providerListener, String providerOwner,
                           Integer providerConnections, String providerLayer, String providerOnconnect, String providerOndisconnect,
                           Integer providerCallbacks, String providerScope, String providerTag, Boolean providerAuth,
                           String providerVersion, String providerGroup, Boolean providerDeprecated, Integer provideDelay,
                           Boolean providerExport, Integer providerWeight, String providerDocument, Boolean providerDynamic,
                           String providerToken, String providerAccesslog, Integer providerExecutes, Boolean providerRegister,
                           Integer providerWarmup, String providerSerialization, Boolean providerExportAsync,
                           Integer providerTimeout, Integer providerRetries, Integer providerActives, String providerLoadbalance,
                           Boolean providerAsync, Boolean providerSent, String providerMock, String providerMerger,
                           String providerCache, String providerValidation, Integer providerForks, String providerHost,
                           String providerContextpath, String providerThreadpool, String providerThreadname,
                           Integer providerThreads, Integer providerIothreads, Integer providerAlive, Integer providerQueues,
                           Integer providerAccepts, String providerCodec, String providerCharset, Integer providerPayload,
                           Integer providerBuffer, String providerTransporter, String providerExchanger, String providerDispatcher,
                           String providerNetworker, String providerServer, String providerClient, String providerTelnet,
                           String providerPrompt, String providerStatus, Integer providerWait, Integer providerExportThreadNum,
                           Boolean providerExportBackground, Map<String, String> providerParameters, String consumerId,
                           Integer consumerTimeout, Integer consumerRetries, Integer consumerActives, String consumerLoadbalance,
                           Boolean consumerAsync, Boolean consumerSent, String consumerMock, String consumerMerger,
                           String consumerCache, String consumerValidation, Integer consumerForks, String consumerInterfaceName,
                           String consumerVersion, String consumerGroup, String consumerStub, String consumerProxy,
                           String consumerCluster, String consumerListener, String consumerOwner, Integer consumerConnections,
                           String consumerLayer, String consumerOnconnect, String consumerOndisconnect, Integer consumerCallbacks,
                           String consumerScope, String consumerTag, Boolean consumerAuth, Boolean consumerCheck,
                           Boolean consumerInit, String consumerGeneric, Boolean consumerLazy, String consumerReconnect,
                           Boolean consumerSticky, String consumerProvidedBy, String consumerRouter, Boolean consumerReferAsync,
                           String consumerClient, String consumerThreadpool, Integer consumerCorethreads, Integer consumerThreads,
                           Integer consumerQueues, Integer consumerShareconnections, String consumerUrlMergeProcessor,
                           Integer consumerReferThreadNum, Boolean consumerReferBackground) {
        this.metadataReportId = metadataReportId;
        this.metadataReportProtocol = metadataReportProtocol;
        this.metadataReportAddress = metadataReportAddress;
        this.metadataReportPort = metadataReportPort;
        this.metadataReportUsername = metadataReportUsername;
        this.metadataReportPassword = metadataReportPassword;
        this.metadataReportTimeout = metadataReportTimeout;
        this.metadataReportGroup = metadataReportGroup;
        this.metadataReportRetryTimes = metadataReportRetryTimes;
        this.metadataReportRetryPeriod = metadataReportRetryPeriod;
        this.metadataReportCycleReport = metadataReportCycleReport;
        this.metadataReportSyncReport = metadataReportSyncReport;
        this.metadataReportCluster = metadataReportCluster;
        this.metadataReportRegistry = metadataReportRegistry;
        this.metadataReportFile = metadataReportFile;
        this.metadataReportParameters = metadataReportParameters;
        this.registryId = registryId;
        this.registryAddress = registryAddress;
        this.registryUsername = registryUsername;
        this.registryPassword = registryPassword;
        this.registryPort = registryPort;
        this.registryProtocol = registryProtocol;
        this.registryTransporter = registryTransporter;
        this.registryServer = registryServer;
        this.registryClient = registryClient;
        this.registryCluster = registryCluster;
        this.registryZone = registryZone;
        this.registryGroup = registryGroup;
        this.registryVersion = registryVersion;
        this.registryTimeout = registryTimeout;
        this.registrySession = registrySession;
        this.registryFile = registryFile;
        this.registryCheck = registryCheck;
        this.registryDynamic = registryDynamic;
        this.registryRegister = registryRegister;
        this.registrySubscribe = registrySubscribe;
        this.registrySimplified = registrySimplified;
        this.registryExtraKeys = registryExtraKeys;
        this.registryUseAsConfigCenter = registryUseAsConfigCenter;
        this.registryUseAsMetadataCenter = registryUseAsMetadataCenter;
        this.registryAccepts = registryAccepts;
        this.registryPreferred = registryPreferred;
        this.registryWeight = registryWeight;
        this.registryPublishInterface = registryPublishInterface;
        this.registryPublishInstance = registryPublishInstance;
        this.registryParameters = registryParameters;
        this.monitorEnable = monitorEnable;
        this.monitorId = monitorId;
        this.monitorProtocol = monitorProtocol;
        this.monitorAddress = monitorAddress;
        this.monitorUsername = monitorUsername;
        this.monitorPassword = monitorPassword;
        this.monitorGroup = monitorGroup;
        this.monitorVersion = monitorVersion;
        this.monitorInterval = monitorInterval;
        this.applicationId = applicationId;
        this.applicationName = applicationName;
        this.applicationVersion = applicationVersion;
        this.applicationOwner = applicationOwner;
        this.applicationOrganization = applicationOrganization;
        this.applicationArchitecture = applicationArchitecture;
        this.applicationEnvironment = applicationEnvironment;
        this.applicationCompiler = applicationCompiler;
        this.applicationLogger = applicationLogger;
        this.applicationDumpDirectory = applicationDumpDirectory;
        this.applicationShutWait = applicationShutWait;
        this.applicationMetadataType = applicationMetadataType;
        this.applicationRegisterConsumer = applicationRegisterConsumer;
        this.applicationRepository = applicationRepository;
        this.applicationEnableFileCache = applicationEnableFileCache;
        this.applicationPublishInterface = applicationPublishInterface;
        this.applicationPublishInstance = applicationPublishInstance;
        this.applicationProtocol = applicationProtocol;
        this.applicationMetadataServicePort = applicationMetadataServicePort;
        this.applicationLivenessProbe = applicationLivenessProbe;
        this.applicationReadinessProbe = applicationReadinessProbe;
        this.applicationStartupProbe = applicationStartupProbe;
        this.applicationQosEnable = applicationQosEnable;
        this.applicationQosHost = applicationQosHost;
        this.applicationQosPort = applicationQosPort;
        this.applicationQosAcceptForeignIp = applicationQosAcceptForeignIp;
        this.applicationParameters = applicationParameters;
        this.protocolId = protocolId;
        this.protocolName = protocolName;
        this.protocolHost = protocolHost;
        this.protocolPort = protocolPort;
        this.protocolContextpath = protocolContextpath;
        this.protocolThreadpool = protocolThreadpool;
        this.protocolThreadname = protocolThreadname;
        this.protocolCorethreads = protocolCorethreads;
        this.protocolThreads = protocolThreads;
        this.protocolIothreads = protocolIothreads;
        this.protocolAlive = protocolAlive;
        this.protocolQueues = protocolQueues;
        this.protocolAccepts = protocolAccepts;
        this.protocolCodec = protocolCodec;
        this.protocolSerialization = protocolSerialization;
        this.protocolCharset = protocolCharset;
        this.protocolPayload = protocolPayload;
        this.protocolBuffer = protocolBuffer;
        this.protocolHeartbeat = protocolHeartbeat;
        this.protocolAccesslog = protocolAccesslog;
        this.protocolTransporter = protocolTransporter;
        this.protocolExchanger = protocolExchanger;
        this.protocolDispatcher = protocolDispatcher;
        this.protocolNetworker = protocolNetworker;
        this.protocolServer = protocolServer;
        this.protocolClient = protocolClient;
        this.protocolTelnet = protocolTelnet;
        this.protocolPrompt = protocolPrompt;
        this.protocolStatus = protocolStatus;
        this.protocolRegister = protocolRegister;
        this.protocolKeepAlive = protocolKeepAlive;
        this.protocolOptimizer = protocolOptimizer;
        this.protocolExtension = protocolExtension;
        this.protocolSslEnabled = protocolSslEnabled;
        this.protocolParameters = protocolParameters;
        this.protocolDefault = protocolDefault;
        this.providerId = providerId;
        this.providerInterfaceName = providerInterfaceName;
        this.providerStub = providerStub;
        this.providerProxy = providerProxy;
        this.providerCluster = providerCluster;
        this.providerListener = providerListener;
        this.providerOwner = providerOwner;
        this.providerConnections = providerConnections;
        this.providerLayer = providerLayer;
        this.providerOnconnect = providerOnconnect;
        this.providerOndisconnect = providerOndisconnect;
        this.providerCallbacks = providerCallbacks;
        this.providerScope = providerScope;
        this.providerTag = providerTag;
        this.providerAuth = providerAuth;
        this.providerVersion = providerVersion;
        this.providerGroup = providerGroup;
        this.providerDeprecated = providerDeprecated;
        this.provideDelay = provideDelay;
        this.providerExport = providerExport;
        this.providerWeight = providerWeight;
        this.providerDocument = providerDocument;
        this.providerDynamic = providerDynamic;
        this.providerToken = providerToken;
        this.providerAccesslog = providerAccesslog;
        this.providerExecutes = providerExecutes;
        this.providerRegister = providerRegister;
        this.providerWarmup = providerWarmup;
        this.providerSerialization = providerSerialization;
        this.providerExportAsync = providerExportAsync;
        this.providerTimeout = providerTimeout;
        this.providerRetries = providerRetries;
        this.providerActives = providerActives;
        this.providerLoadbalance = providerLoadbalance;
        this.providerAsync = providerAsync;
        this.providerSent = providerSent;
        this.providerMock = providerMock;
        this.providerMerger = providerMerger;
        this.providerCache = providerCache;
        this.providerValidation = providerValidation;
        this.providerForks = providerForks;
        this.providerHost = providerHost;
        this.providerContextpath = providerContextpath;
        this.providerThreadpool = providerThreadpool;
        this.providerThreadname = providerThreadname;
        this.providerThreads = providerThreads;
        this.providerIothreads = providerIothreads;
        this.providerAlive = providerAlive;
        this.providerQueues = providerQueues;
        this.providerAccepts = providerAccepts;
        this.providerCodec = providerCodec;
        this.providerCharset = providerCharset;
        this.providerPayload = providerPayload;
        this.providerBuffer = providerBuffer;
        this.providerTransporter = providerTransporter;
        this.providerExchanger = providerExchanger;
        this.providerDispatcher = providerDispatcher;
        this.providerNetworker = providerNetworker;
        this.providerServer = providerServer;
        this.providerClient = providerClient;
        this.providerTelnet = providerTelnet;
        this.providerPrompt = providerPrompt;
        this.providerStatus = providerStatus;
        this.providerWait = providerWait;
        this.providerExportThreadNum = providerExportThreadNum;
        this.providerExportBackground = providerExportBackground;
        this.providerParameters = providerParameters;
        this.consumerId = consumerId;
        this.consumerTimeout = consumerTimeout;
        this.consumerRetries = consumerRetries;
        this.consumerActives = consumerActives;
        this.consumerLoadbalance = consumerLoadbalance;
        this.consumerAsync = consumerAsync;
        this.consumerSent = consumerSent;
        this.consumerMock = consumerMock;
        this.consumerMerger = consumerMerger;
        this.consumerCache = consumerCache;
        this.consumerValidation = consumerValidation;
        this.consumerForks = consumerForks;
        this.consumerInterfaceName = consumerInterfaceName;
        this.consumerVersion = consumerVersion;
        this.consumerGroup = consumerGroup;
        this.consumerStub = consumerStub;
        this.consumerProxy = consumerProxy;
        this.consumerCluster = consumerCluster;
        this.consumerListener = consumerListener;
        this.consumerOwner = consumerOwner;
        this.consumerConnections = consumerConnections;
        this.consumerLayer = consumerLayer;
        this.consumerOnconnect = consumerOnconnect;
        this.consumerOndisconnect = consumerOndisconnect;
        this.consumerCallbacks = consumerCallbacks;
        this.consumerScope = consumerScope;
        this.consumerTag = consumerTag;
        this.consumerAuth = consumerAuth;
        this.consumerCheck = consumerCheck;
        this.consumerInit = consumerInit;
        this.consumerGeneric = consumerGeneric;
        this.consumerLazy = consumerLazy;
        this.consumerReconnect = consumerReconnect;
        this.consumerSticky = consumerSticky;
        this.consumerProvidedBy = consumerProvidedBy;
        this.consumerRouter = consumerRouter;
        this.consumerReferAsync = consumerReferAsync;
        this.consumerClient = consumerClient;
        this.consumerThreadpool = consumerThreadpool;
        this.consumerCorethreads = consumerCorethreads;
        this.consumerThreads = consumerThreads;
        this.consumerQueues = consumerQueues;
        this.consumerShareconnections = consumerShareconnections;
        this.consumerUrlMergeProcessor = consumerUrlMergeProcessor;
        this.consumerReferThreadNum = consumerReferThreadNum;
        this.consumerReferBackground = consumerReferBackground;
    }

    @Override
    public String getMetadataReportId() {
        return metadataReportId;
    }

    @Override
    public String getMetadataReportProtocol() {
        return metadataReportProtocol;
    }

    @Override
    public String getMetadataReportAddress() {
        return metadataReportAddress;
    }

    @Override
    public Integer getMetadataReportPort() {
        return metadataReportPort;
    }

    @Override
    public String getMetadataReportUsername() {
        return metadataReportUsername;
    }

    @Override
    public String getMetadataReportPassword() {
        return metadataReportPassword;
    }

    @Override
    public Integer getMetadataReportTimeout() {
        return metadataReportTimeout;
    }

    @Override
    public String getMetadataReportGroup() {
        return metadataReportGroup;
    }

    @Override
    public Integer getMetadataReportRetryTimes() {
        return metadataReportRetryTimes;
    }

    @Override
    public Integer getMetadataReportRetryPeriod() {
        return metadataReportRetryPeriod;
    }

    @Override
    public Boolean getMetadataReportCycleReport() {
        return metadataReportCycleReport;
    }

    @Override
    public Boolean getMetadataReportSyncReport() {
        return metadataReportSyncReport;
    }

    @Override
    public Boolean getMetadataReportCluster() {
        return metadataReportCluster;
    }

    @Override
    public String getMetadataReportRegistry() {
        return metadataReportRegistry;
    }

    @Override
    public String getMetadataReportFile() {
        return metadataReportFile;
    }

    @Override
    public Map<String, String> getMetadataReportParameters() {
        return metadataReportParameters;
    }

    @Override
    public String getRegistryId() {
        return registryId;
    }

    @Override
    public String getRegistryAddress() {
        return registryAddress;
    }

    @Override
    public String getRegistryUsername() {
        return registryUsername;
    }

    @Override
    public String getRegistryPassword() {
        return registryPassword;
    }

    @Override
    public Integer getRegistryPort() {
        return registryPort;
    }

    @Override
    public String getRegistryProtocol() {
        return registryProtocol;
    }

    @Override
    public String getRegistryTransporter() {
        return registryTransporter;
    }

    @Override
    public String getRegistryServer() {
        return registryServer;
    }

    @Override
    public String getRegistryClient() {
        return registryClient;
    }

    @Override
    public String getRegistryCluster() {
        return registryCluster;
    }

    @Override
    public String getRegistryZone() {
        return registryZone;
    }

    @Override
    public String getRegistryGroup() {
        return registryGroup;
    }

    @Override
    public String getRegistryVersion() {
        return registryVersion;
    }

    @Override
    public Integer getRegistryTimeout() {
        return registryTimeout;
    }

    @Override
    public Integer getRegistrySession() {
        return registrySession;
    }

    @Override
    public String getRegistryFile() {
        return registryFile;
    }

    @Override
    public Boolean getRegistryCheck() {
        return registryCheck;
    }

    @Override
    public Boolean getRegistryDynamic() {
        return registryDynamic;
    }

    @Override
    public Boolean getRegistryRegister() {
        return registryRegister;
    }

    @Override
    public Boolean getRegistrySubscribe() {
        return registrySubscribe;
    }

    @Override
    public Boolean getRegistrySimplified() {
        return registrySimplified;
    }

    @Override
    public String getRegistryExtraKeys() {
        return registryExtraKeys;
    }

    @Override
    public Boolean getRegistryUseAsConfigCenter() {
        return registryUseAsConfigCenter;
    }

    @Override
    public Boolean getRegistryUseAsMetadataCenter() {
        return registryUseAsMetadataCenter;
    }

    @Override
    public String getRegistryAccepts() {
        return registryAccepts;
    }

    @Override
    public Boolean getRegistryPreferred() {
        return registryPreferred;
    }

    @Override
    public Integer getRegistryWeight() {
        return registryWeight;
    }

    @Override
    public Boolean getRegistryPublishInterface() {
        return registryPublishInterface;
    }

    @Override
    public Boolean getRegistryPublishInstance() {
        return registryPublishInstance;
    }

    @Override
    public Map<String, String> getRegistryParameters() {
        return registryParameters;
    }

    @Override
    public Boolean getMonitorEnable() {
        return monitorEnable;
    }

    @Override
    public String getMonitorId() {
        return monitorId;
    }

    @Override
    public String getMonitorProtocol() {
        return monitorProtocol;
    }

    @Override
    public String getMonitorAddress() {
        return monitorAddress;
    }

    @Override
    public String getMonitorUsername() {
        return monitorUsername;
    }

    @Override
    public String getMonitorPassword() {
        return monitorPassword;
    }

    @Override
    public String getMonitorGroup() {
        return monitorGroup;
    }

    @Override
    public String getMonitorVersion() {
        return monitorVersion;
    }

    @Override
    public String getMonitorInterval() {
        return monitorInterval;
    }

    @Override
    public String getApplicationId() {
        return applicationId;
    }

    @Override
    public String getApplicationName() {
        return applicationName;
    }

    @Override
    public String getApplicationVersion() {
        return applicationVersion;
    }

    @Override
    public String getApplicationOwner() {
        return applicationOwner;
    }

    @Override
    public String getApplicationOrganization() {
        return applicationOrganization;
    }

    @Override
    public String getApplicationArchitecture() {
        return applicationArchitecture;
    }

    @Override
    public String getApplicationEnvironment() {
        return applicationEnvironment;
    }

    @Override
    public String getApplicationCompiler() {
        return applicationCompiler;
    }

    @Override
    public String getApplicationLogger() {
        return applicationLogger;
    }

    @Override
    public String getApplicationDumpDirectory() {
        return applicationDumpDirectory;
    }

    @Override
    public String getApplicationShutWait() {
        return applicationShutWait;
    }

    @Override
    public String getApplicationMetadataType() {
        return applicationMetadataType;
    }

    @Override
    public Boolean getApplicationRegisterConsumer() {
        return applicationRegisterConsumer;
    }

    @Override
    public String getApplicationRepository() {
        return applicationRepository;
    }

    @Override
    public Boolean getApplicationEnableFileCache() {
        return applicationEnableFileCache;
    }

    @Override
    public Boolean getApplicationPublishInterface() {
        return applicationPublishInterface;
    }

    @Override
    public Boolean getApplicationPublishInstance() {
        return applicationPublishInstance;
    }

    @Override
    public String getApplicationProtocol() {
        return applicationProtocol;
    }

    @Override
    public Integer getApplicationMetadataServicePort() {
        return applicationMetadataServicePort;
    }

    @Override
    public String getApplicationLivenessProbe() {
        return applicationLivenessProbe;
    }

    @Override
    public String getApplicationReadinessProbe() {
        return applicationReadinessProbe;
    }

    @Override
    public String getApplicationStartupProbe() {
        return applicationStartupProbe;
    }

    @Override
    public Boolean getApplicationQosEnable() {
        return applicationQosEnable;
    }

    @Override
    public String getApplicationQosHost() {
        return applicationQosHost;
    }

    @Override
    public Integer getApplicationQosPort() {
        return applicationQosPort;
    }

    @Override
    public Boolean getApplicationQosAcceptForeignIp() {
        return applicationQosAcceptForeignIp;
    }

    @Override
    public Map<String, String> getApplicationParameters() {
        return applicationParameters;
    }

    @Override
    public String getProtocolId() {
        return protocolId;
    }

    @Override
    public String getProtocolName() {
        return protocolName;
    }

    @Override
    public String getProtocolHost() {
        return protocolHost;
    }

    @Override
    public Integer getProtocolPort() {
        return protocolPort;
    }

    @Override
    public String getProtocolContextpath() {
        return protocolContextpath;
    }

    @Override
    public String getProtocolThreadpool() {
        return protocolThreadpool;
    }

    @Override
    public String getProtocolThreadname() {
        return protocolThreadname;
    }

    @Override
    public Integer getProtocolCorethreads() {
        return protocolCorethreads;
    }

    @Override
    public Integer getProtocolThreads() {
        return protocolThreads;
    }

    @Override
    public Integer getProtocolIothreads() {
        return protocolIothreads;
    }

    @Override
    public Integer getProtocolAlive() {
        return protocolAlive;
    }

    @Override
    public Integer getProtocolQueues() {
        return protocolQueues;
    }

    @Override
    public Integer getProtocolAccepts() {
        return protocolAccepts;
    }

    @Override
    public String getProtocolCodec() {
        return protocolCodec;
    }

    @Override
    public String getProtocolSerialization() {
        return protocolSerialization;
    }

    @Override
    public String getProtocolCharset() {
        return protocolCharset;
    }

    @Override
    public Integer getProtocolPayload() {
        return protocolPayload;
    }

    @Override
    public Integer getProtocolBuffer() {
        return protocolBuffer;
    }

    @Override
    public Integer getProtocolHeartbeat() {
        return protocolHeartbeat;
    }

    @Override
    public String getProtocolAccesslog() {
        return protocolAccesslog;
    }

    @Override
    public String getProtocolTransporter() {
        return protocolTransporter;
    }

    @Override
    public String getProtocolExchanger() {
        return protocolExchanger;
    }

    @Override
    public String getProtocolDispatcher() {
        return protocolDispatcher;
    }

    @Override
    public String getProtocolNetworker() {
        return protocolNetworker;
    }

    @Override
    public String getProtocolServer() {
        return protocolServer;
    }

    @Override
    public String getProtocolClient() {
        return protocolClient;
    }

    @Override
    public String getProtocolTelnet() {
        return protocolTelnet;
    }

    @Override
    public String getProtocolPrompt() {
        return protocolPrompt;
    }

    @Override
    public String getProtocolStatus() {
        return protocolStatus;
    }

    @Override
    public Boolean getProtocolRegister() {
        return protocolRegister;
    }

    @Override
    public Boolean getProtocolKeepAlive() {
        return protocolKeepAlive;
    }

    @Override
    public String getProtocolOptimizer() {
        return protocolOptimizer;
    }

    @Override
    public String getProtocolExtension() {
        return protocolExtension;
    }

    @Override
    public Boolean getProtocolSslEnabled() {
        return protocolSslEnabled;
    }

    @Override
    public Map<String, String> getProtocolParameters() {
        return protocolParameters;
    }

    @Override
    public Boolean getProtocolDefault() {
        return protocolDefault;
    }

    @Override
    public String getProviderId() {
        return providerId;
    }

    @Override
    public String getProviderInterfaceName() {
        return providerInterfaceName;
    }

    @Override
    public String getProviderStub() {
        return providerStub;
    }

    @Override
    public String getProviderProxy() {
        return providerProxy;
    }

    @Override
    public String getProviderCluster() {
        return providerCluster;
    }

    @Override
    public String getProviderListener() {
        return providerListener;
    }

    @Override
    public String getProviderOwner() {
        return providerOwner;
    }

    @Override
    public Integer getProviderConnections() {
        return providerConnections;
    }

    @Override
    public String getProviderLayer() {
        return providerLayer;
    }

    @Override
    public String getProviderOnconnect() {
        return providerOnconnect;
    }

    @Override
    public String getProviderOndisconnect() {
        return providerOndisconnect;
    }

    @Override
    public Integer getProviderCallbacks() {
        return providerCallbacks;
    }

    @Override
    public String getProviderScope() {
        return providerScope;
    }

    @Override
    public String getProviderTag() {
        return providerTag;
    }

    @Override
    public Boolean getProviderAuth() {
        return providerAuth;
    }

    @Override
    public String getProviderVersion() {
        return providerVersion;
    }

    @Override
    public String getProviderGroup() {
        return providerGroup;
    }

    @Override
    public Boolean getProviderDeprecated() {
        return providerDeprecated;
    }

    @Override
    public Integer getProvideDelay() {
        return provideDelay;
    }

    @Override
    public Boolean getProviderExport() {
        return providerExport;
    }

    @Override
    public Integer getProviderWeight() {
        return providerWeight;
    }

    @Override
    public String getProviderDocument() {
        return providerDocument;
    }

    @Override
    public Boolean getProviderDynamic() {
        return providerDynamic;
    }

    @Override
    public String getProviderToken() {
        return providerToken;
    }

    @Override
    public String getProviderAccesslog() {
        return providerAccesslog;
    }

    @Override
    public Integer getProviderExecutes() {
        return providerExecutes;
    }

    @Override
    public Boolean getProviderRegister() {
        return providerRegister;
    }

    @Override
    public Integer getProviderWarmup() {
        return providerWarmup;
    }

    @Override
    public String getProviderSerialization() {
        return providerSerialization;
    }

    @Override
    public Boolean getProviderExportAsync() {
        return providerExportAsync;
    }

    @Override
    public Integer getProviderTimeout() {
        return providerTimeout;
    }

    @Override
    public Integer getProviderRetries() {
        return providerRetries;
    }

    @Override
    public Integer getProviderActives() {
        return providerActives;
    }

    @Override
    public String getProviderLoadbalance() {
        return providerLoadbalance;
    }

    @Override
    public Boolean getProviderAsync() {
        return providerAsync;
    }

    @Override
    public Boolean getProviderSent() {
        return providerSent;
    }

    @Override
    public String getProviderMock() {
        return providerMock;
    }

    @Override
    public String getProviderMerger() {
        return providerMerger;
    }

    @Override
    public String getProviderCache() {
        return providerCache;
    }

    @Override
    public String getProviderValidation() {
        return providerValidation;
    }

    @Override
    public Integer getProviderForks() {
        return providerForks;
    }

    @Override
    public String getProviderHost() {
        return providerHost;
    }

    @Override
    public String getProviderContextpath() {
        return providerContextpath;
    }

    @Override
    public String getProviderThreadpool() {
        return providerThreadpool;
    }

    @Override
    public String getProviderThreadname() {
        return providerThreadname;
    }

    @Override
    public Integer getProviderThreads() {
        return providerThreads;
    }

    @Override
    public Integer getProviderIothreads() {
        return providerIothreads;
    }

    @Override
    public Integer getProviderAlive() {
        return providerAlive;
    }

    @Override
    public Integer getProviderQueues() {
        return providerQueues;
    }

    @Override
    public Integer getProviderAccepts() {
        return providerAccepts;
    }

    @Override
    public String getProviderCodec() {
        return providerCodec;
    }

    @Override
    public String getProviderCharset() {
        return providerCharset;
    }

    @Override
    public Integer getProviderPayload() {
        return providerPayload;
    }

    @Override
    public Integer getProviderBuffer() {
        return providerBuffer;
    }

    @Override
    public String getProviderTransporter() {
        return providerTransporter;
    }

    @Override
    public String getProviderExchanger() {
        return providerExchanger;
    }

    @Override
    public String getProviderDispatcher() {
        return providerDispatcher;
    }

    @Override
    public String getProviderNetworker() {
        return providerNetworker;
    }

    @Override
    public String getProviderServer() {
        return providerServer;
    }

    @Override
    public String getProviderClient() {
        return providerClient;
    }

    @Override
    public String getProviderTelnet() {
        return providerTelnet;
    }

    @Override
    public String getProviderPrompt() {
        return providerPrompt;
    }

    @Override
    public String getProviderStatus() {
        return providerStatus;
    }

    @Override
    public Integer getProviderWait() {
        return providerWait;
    }

    @Override
    public Integer getProviderExportThreadNum() {
        return providerExportThreadNum;
    }

    @Override
    public Boolean getProviderExportBackground() {
        return providerExportBackground;
    }

    @Override
    public Map<String, String> getProviderParameters() {
        return providerParameters;
    }

    @Override
    public String getConsumerId() {
        return consumerId;
    }

    @Override
    public Integer getConsumerTimeout() {
        return consumerTimeout;
    }

    @Override
    public Integer getConsumerRetries() {
        return consumerRetries;
    }

    @Override
    public Integer getConsumerActives() {
        return consumerActives;
    }

    @Override
    public String getConsumerLoadbalance() {
        return consumerLoadbalance;
    }

    @Override
    public Boolean getConsumerAsync() {
        return consumerAsync;
    }

    @Override
    public Boolean getConsumerSent() {
        return consumerSent;
    }

    @Override
    public String getConsumerMock() {
        return consumerMock;
    }

    @Override
    public String getConsumerMerger() {
        return consumerMerger;
    }

    @Override
    public String getConsumerCache() {
        return consumerCache;
    }

    @Override
    public String getConsumerValidation() {
        return consumerValidation;
    }

    @Override
    public Integer getConsumerForks() {
        return consumerForks;
    }

    @Override
    public String getConsumerInterfaceName() {
        return consumerInterfaceName;
    }

    @Override
    public String getConsumerVersion() {
        return consumerVersion;
    }

    @Override
    public String getConsumerGroup() {
        return consumerGroup;
    }

    @Override
    public String getConsumerStub() {
        return consumerStub;
    }

    @Override
    public String getConsumerProxy() {
        return consumerProxy;
    }

    @Override
    public String getConsumerCluster() {
        return consumerCluster;
    }

    @Override
    public String getConsumerListener() {
        return consumerListener;
    }

    @Override
    public String getConsumerOwner() {
        return consumerOwner;
    }

    @Override
    public Integer getConsumerConnections() {
        return consumerConnections;
    }

    @Override
    public String getConsumerLayer() {
        return consumerLayer;
    }

    @Override
    public String getConsumerOnconnect() {
        return consumerOnconnect;
    }

    @Override
    public String getConsumerOndisconnect() {
        return consumerOndisconnect;
    }

    @Override
    public Integer getConsumerCallbacks() {
        return consumerCallbacks;
    }

    @Override
    public String getConsumerScope() {
        return consumerScope;
    }

    @Override
    public String getConsumerTag() {
        return consumerTag;
    }

    @Override
    public Boolean getConsumerAuth() {
        return consumerAuth;
    }

    @Override
    public Boolean getConsumerCheck() {
        return consumerCheck;
    }

    @Override
    public Boolean getConsumerInit() {
        return consumerInit;
    }

    @Override
    public String getConsumerGeneric() {
        return consumerGeneric;
    }

    @Override
    public Boolean getConsumerLazy() {
        return consumerLazy;
    }

    @Override
    public String getConsumerReconnect() {
        return consumerReconnect;
    }

    @Override
    public Boolean getConsumerSticky() {
        return consumerSticky;
    }

    @Override
    public String getConsumerProvidedBy() {
        return consumerProvidedBy;
    }

    @Override
    public String getConsumerRouter() {
        return consumerRouter;
    }

    @Override
    public Boolean getConsumerReferAsync() {
        return consumerReferAsync;
    }

    @Override
    public String getConsumerClient() {
        return consumerClient;
    }

    @Override
    public String getConsumerThreadpool() {
        return consumerThreadpool;
    }

    @Override
    public Integer getConsumerCorethreads() {
        return consumerCorethreads;
    }

    @Override
    public Integer getConsumerThreads() {
        return consumerThreads;
    }

    @Override
    public Integer getConsumerQueues() {
        return consumerQueues;
    }

    @Override
    public Integer getConsumerShareconnections() {
        return consumerShareconnections;
    }

    @Override
    public String getConsumerUrlMergeProcessor() {
        return consumerUrlMergeProcessor;
    }

    @Override
    public Integer getConsumerReferThreadNum() {
        return consumerReferThreadNum;
    }

    @Override
    public Boolean getConsumerReferBackground() {
        return consumerReferBackground;
    }

    public void setMetadataReportId(String metadataReportId) {
        this.metadataReportId = metadataReportId;
    }

    public void setMetadataReportProtocol(String metadataReportProtocol) {
        this.metadataReportProtocol = metadataReportProtocol;
    }

    public void setMetadataReportAddress(String metadataReportAddress) {
        this.metadataReportAddress = metadataReportAddress;
    }

    public void setMetadataReportPort(Integer metadataReportPort) {
        this.metadataReportPort = metadataReportPort;
    }

    public void setMetadataReportUsername(String metadataReportUsername) {
        this.metadataReportUsername = metadataReportUsername;
    }

    public void setMetadataReportPassword(String metadataReportPassword) {
        this.metadataReportPassword = metadataReportPassword;
    }

    public void setMetadataReportTimeout(Integer metadataReportTimeout) {
        this.metadataReportTimeout = metadataReportTimeout;
    }

    public void setMetadataReportGroup(String metadataReportGroup) {
        this.metadataReportGroup = metadataReportGroup;
    }

    public void setMetadataReportRetryTimes(Integer metadataReportRetryTimes) {
        this.metadataReportRetryTimes = metadataReportRetryTimes;
    }

    public void setMetadataReportRetryPeriod(Integer metadataReportRetryPeriod) {
        this.metadataReportRetryPeriod = metadataReportRetryPeriod;
    }

    public void setMetadataReportCycleReport(Boolean metadataReportCycleReport) {
        this.metadataReportCycleReport = metadataReportCycleReport;
    }

    public void setMetadataReportSyncReport(Boolean metadataReportSyncReport) {
        this.metadataReportSyncReport = metadataReportSyncReport;
    }

    public void setMetadataReportCluster(Boolean metadataReportCluster) {
        this.metadataReportCluster = metadataReportCluster;
    }

    public void setMetadataReportRegistry(String metadataReportRegistry) {
        this.metadataReportRegistry = metadataReportRegistry;
    }

    public void setMetadataReportFile(String metadataReportFile) {
        this.metadataReportFile = metadataReportFile;
    }

    public void setMetadataReportParameters(Map<String, String> metadataReportParameters) {
        this.metadataReportParameters = metadataReportParameters;
    }

    public void setRegistryId(String registryId) {
        this.registryId = registryId;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public void setRegistryUsername(String registryUsername) {
        this.registryUsername = registryUsername;
    }

    public void setRegistryPassword(String registryPassword) {
        this.registryPassword = registryPassword;
    }

    public void setRegistryPort(Integer registryPort) {
        this.registryPort = registryPort;
    }

    public void setRegistryProtocol(String registryProtocol) {
        this.registryProtocol = registryProtocol;
    }

    public void setRegistryTransporter(String registryTransporter) {
        this.registryTransporter = registryTransporter;
    }

    public void setRegistryServer(String registryServer) {
        this.registryServer = registryServer;
    }

    public void setRegistryClient(String registryClient) {
        this.registryClient = registryClient;
    }

    public void setRegistryCluster(String registryCluster) {
        this.registryCluster = registryCluster;
    }

    public void setRegistryZone(String registryZone) {
        this.registryZone = registryZone;
    }

    public void setRegistryGroup(String registryGroup) {
        this.registryGroup = registryGroup;
    }

    public void setRegistryVersion(String registryVersion) {
        this.registryVersion = registryVersion;
    }

    public void setRegistryTimeout(Integer registryTimeout) {
        this.registryTimeout = registryTimeout;
    }

    public void setRegistrySession(Integer registrySession) {
        this.registrySession = registrySession;
    }

    public void setRegistryFile(String registryFile) {
        this.registryFile = registryFile;
    }

    public void setRegistryCheck(Boolean registryCheck) {
        this.registryCheck = registryCheck;
    }

    public void setRegistryDynamic(Boolean registryDynamic) {
        this.registryDynamic = registryDynamic;
    }

    public void setRegistryRegister(Boolean registryRegister) {
        this.registryRegister = registryRegister;
    }

    public void setRegistrySubscribe(Boolean registrySubscribe) {
        this.registrySubscribe = registrySubscribe;
    }

    public void setRegistrySimplified(Boolean registrySimplified) {
        this.registrySimplified = registrySimplified;
    }

    public void setRegistryExtraKeys(String registryExtraKeys) {
        this.registryExtraKeys = registryExtraKeys;
    }

    public void setRegistryUseAsConfigCenter(Boolean registryUseAsConfigCenter) {
        this.registryUseAsConfigCenter = registryUseAsConfigCenter;
    }

    public void setRegistryUseAsMetadataCenter(Boolean registryUseAsMetadataCenter) {
        this.registryUseAsMetadataCenter = registryUseAsMetadataCenter;
    }

    public void setRegistryAccepts(String registryAccepts) {
        this.registryAccepts = registryAccepts;
    }

    public void setRegistryPreferred(Boolean registryPreferred) {
        this.registryPreferred = registryPreferred;
    }

    public void setRegistryWeight(Integer registryWeight) {
        this.registryWeight = registryWeight;
    }

    public void setRegistryPublishInterface(Boolean registryPublishInterface) {
        this.registryPublishInterface = registryPublishInterface;
    }

    public void setRegistryPublishInstance(Boolean registryPublishInstance) {
        this.registryPublishInstance = registryPublishInstance;
    }

    public void setRegistryParameters(Map<String, String> registryParameters) {
        this.registryParameters = registryParameters;
    }

    public void setMonitorEnable(Boolean monitorEnable) {
        this.monitorEnable = monitorEnable;
    }

    public void setMonitorId(String monitorId) {
        this.monitorId = monitorId;
    }

    public void setMonitorProtocol(String monitorProtocol) {
        this.monitorProtocol = monitorProtocol;
    }

    public void setMonitorAddress(String monitorAddress) {
        this.monitorAddress = monitorAddress;
    }

    public void setMonitorUsername(String monitorUsername) {
        this.monitorUsername = monitorUsername;
    }

    public void setMonitorPassword(String monitorPassword) {
        this.monitorPassword = monitorPassword;
    }

    public void setMonitorGroup(String monitorGroup) {
        this.monitorGroup = monitorGroup;
    }

    public void setMonitorVersion(String monitorVersion) {
        this.monitorVersion = monitorVersion;
    }

    public void setMonitorInterval(String monitorInterval) {
        this.monitorInterval = monitorInterval;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public void setApplicationVersion(String applicationVersion) {
        this.applicationVersion = applicationVersion;
    }

    public void setApplicationOwner(String applicationOwner) {
        this.applicationOwner = applicationOwner;
    }

    public void setApplicationOrganization(String applicationOrganization) {
        this.applicationOrganization = applicationOrganization;
    }

    public void setApplicationArchitecture(String applicationArchitecture) {
        this.applicationArchitecture = applicationArchitecture;
    }

    public void setApplicationEnvironment(String applicationEnvironment) {
        this.applicationEnvironment = applicationEnvironment;
    }

    public void setApplicationCompiler(String applicationCompiler) {
        this.applicationCompiler = applicationCompiler;
    }

    public void setApplicationLogger(String applicationLogger) {
        this.applicationLogger = applicationLogger;
    }

    public void setApplicationDumpDirectory(String applicationDumpDirectory) {
        this.applicationDumpDirectory = applicationDumpDirectory;
    }

    public void setApplicationShutWait(String applicationShutWait) {
        this.applicationShutWait = applicationShutWait;
    }

    public void setApplicationMetadataType(String applicationMetadataType) {
        this.applicationMetadataType = applicationMetadataType;
    }

    public void setApplicationRegisterConsumer(Boolean applicationRegisterConsumer) {
        this.applicationRegisterConsumer = applicationRegisterConsumer;
    }

    public void setApplicationRepository(String applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public void setApplicationEnableFileCache(Boolean applicationEnableFileCache) {
        this.applicationEnableFileCache = applicationEnableFileCache;
    }

    public void setApplicationPublishInterface(Boolean applicationPublishInterface) {
        this.applicationPublishInterface = applicationPublishInterface;
    }

    public void setApplicationPublishInstance(Boolean applicationPublishInstance) {
        this.applicationPublishInstance = applicationPublishInstance;
    }

    public void setApplicationProtocol(String applicationProtocol) {
        this.applicationProtocol = applicationProtocol;
    }

    public void setApplicationMetadataServicePort(Integer applicationMetadataServicePort) {
        this.applicationMetadataServicePort = applicationMetadataServicePort;
    }

    public void setApplicationLivenessProbe(String applicationLivenessProbe) {
        this.applicationLivenessProbe = applicationLivenessProbe;
    }

    public void setApplicationReadinessProbe(String applicationReadinessProbe) {
        this.applicationReadinessProbe = applicationReadinessProbe;
    }

    public void setApplicationStartupProbe(String applicationStartupProbe) {
        this.applicationStartupProbe = applicationStartupProbe;
    }

    public void setApplicationQosEnable(Boolean applicationQosEnable) {
        this.applicationQosEnable = applicationQosEnable;
    }

    public void setApplicationQosHost(String applicationQosHost) {
        this.applicationQosHost = applicationQosHost;
    }

    public void setApplicationQosPort(Integer applicationQosPort) {
        this.applicationQosPort = applicationQosPort;
    }

    public void setApplicationQosAcceptForeignIp(Boolean applicationQosAcceptForeignIp) {
        this.applicationQosAcceptForeignIp = applicationQosAcceptForeignIp;
    }

    public void setApplicationParameters(Map<String, String> applicationParameters) {
        this.applicationParameters = applicationParameters;
    }

    public void setProtocolId(String protocolId) {
        this.protocolId = protocolId;
    }

    public void setProtocolName(String protocolName) {
        this.protocolName = protocolName;
    }

    public void setProtocolHost(String protocolHost) {
        this.protocolHost = protocolHost;
    }

    public void setProtocolPort(Integer protocolPort) {
        this.protocolPort = protocolPort;
    }

    public void setProtocolContextpath(String protocolContextpath) {
        this.protocolContextpath = protocolContextpath;
    }

    public void setProtocolThreadpool(String protocolThreadpool) {
        this.protocolThreadpool = protocolThreadpool;
    }

    public void setProtocolThreadname(String protocolThreadname) {
        this.protocolThreadname = protocolThreadname;
    }

    public void setProtocolCorethreads(Integer protocolCorethreads) {
        this.protocolCorethreads = protocolCorethreads;
    }

    public void setProtocolThreads(Integer protocolThreads) {
        this.protocolThreads = protocolThreads;
    }

    public void setProtocolIothreads(Integer protocolIothreads) {
        this.protocolIothreads = protocolIothreads;
    }

    public void setProtocolAlive(Integer protocolAlive) {
        this.protocolAlive = protocolAlive;
    }

    public void setProtocolQueues(Integer protocolQueues) {
        this.protocolQueues = protocolQueues;
    }

    public void setProtocolAccepts(Integer protocolAccepts) {
        this.protocolAccepts = protocolAccepts;
    }

    public void setProtocolCodec(String protocolCodec) {
        this.protocolCodec = protocolCodec;
    }

    public void setProtocolSerialization(String protocolSerialization) {
        this.protocolSerialization = protocolSerialization;
    }

    public void setProtocolCharset(String protocolCharset) {
        this.protocolCharset = protocolCharset;
    }

    public void setProtocolPayload(Integer protocolPayload) {
        this.protocolPayload = protocolPayload;
    }

    public void setProtocolBuffer(Integer protocolBuffer) {
        this.protocolBuffer = protocolBuffer;
    }

    public void setProtocolHeartbeat(Integer protocolHeartbeat) {
        this.protocolHeartbeat = protocolHeartbeat;
    }

    public void setProtocolAccesslog(String protocolAccesslog) {
        this.protocolAccesslog = protocolAccesslog;
    }

    public void setProtocolTransporter(String protocolTransporter) {
        this.protocolTransporter = protocolTransporter;
    }

    public void setProtocolExchanger(String protocolExchanger) {
        this.protocolExchanger = protocolExchanger;
    }

    public void setProtocolDispatcher(String protocolDispatcher) {
        this.protocolDispatcher = protocolDispatcher;
    }

    public void setProtocolNetworker(String protocolNetworker) {
        this.protocolNetworker = protocolNetworker;
    }

    public void setProtocolServer(String protocolServer) {
        this.protocolServer = protocolServer;
    }

    public void setProtocolClient(String protocolClient) {
        this.protocolClient = protocolClient;
    }

    public void setProtocolTelnet(String protocolTelnet) {
        this.protocolTelnet = protocolTelnet;
    }

    public void setProtocolPrompt(String protocolPrompt) {
        this.protocolPrompt = protocolPrompt;
    }

    public void setProtocolStatus(String protocolStatus) {
        this.protocolStatus = protocolStatus;
    }

    public void setProtocolRegister(Boolean protocolRegister) {
        this.protocolRegister = protocolRegister;
    }

    public void setProtocolKeepAlive(Boolean protocolKeepAlive) {
        this.protocolKeepAlive = protocolKeepAlive;
    }

    public void setProtocolOptimizer(String protocolOptimizer) {
        this.protocolOptimizer = protocolOptimizer;
    }

    public void setProtocolExtension(String protocolExtension) {
        this.protocolExtension = protocolExtension;
    }

    public void setProtocolSslEnabled(Boolean protocolSslEnabled) {
        this.protocolSslEnabled = protocolSslEnabled;
    }

    public void setProtocolParameters(Map<String, String> protocolParameters) {
        this.protocolParameters = protocolParameters;
    }

    public void setProtocolDefault(Boolean protocolDefault) {
        this.protocolDefault = protocolDefault;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public void setProviderInterfaceName(String providerInterfaceName) {
        this.providerInterfaceName = providerInterfaceName;
    }

    public void setProviderStub(String providerStub) {
        this.providerStub = providerStub;
    }

    public void setProviderProxy(String providerProxy) {
        this.providerProxy = providerProxy;
    }

    public void setProviderCluster(String providerCluster) {
        this.providerCluster = providerCluster;
    }

    public void setProviderListener(String providerListener) {
        this.providerListener = providerListener;
    }

    public void setProviderOwner(String providerOwner) {
        this.providerOwner = providerOwner;
    }

    public void setProviderConnections(Integer providerConnections) {
        this.providerConnections = providerConnections;
    }

    public void setProviderLayer(String providerLayer) {
        this.providerLayer = providerLayer;
    }

    public void setProviderOnconnect(String providerOnconnect) {
        this.providerOnconnect = providerOnconnect;
    }

    public void setProviderOndisconnect(String providerOndisconnect) {
        this.providerOndisconnect = providerOndisconnect;
    }

    public void setProviderCallbacks(Integer providerCallbacks) {
        this.providerCallbacks = providerCallbacks;
    }

    public void setProviderScope(String providerScope) {
        this.providerScope = providerScope;
    }

    public void setProviderTag(String providerTag) {
        this.providerTag = providerTag;
    }

    public void setProviderAuth(Boolean providerAuth) {
        this.providerAuth = providerAuth;
    }

    public void setProviderVersion(String providerVersion) {
        this.providerVersion = providerVersion;
    }

    public void setProviderGroup(String providerGroup) {
        this.providerGroup = providerGroup;
    }

    public void setProviderDeprecated(Boolean providerDeprecated) {
        this.providerDeprecated = providerDeprecated;
    }

    public void setProvideDelay(Integer provideDelay) {
        this.provideDelay = provideDelay;
    }

    public void setProviderExport(Boolean providerExport) {
        this.providerExport = providerExport;
    }

    public void setProviderWeight(Integer providerWeight) {
        this.providerWeight = providerWeight;
    }

    public void setProviderDocument(String providerDocument) {
        this.providerDocument = providerDocument;
    }

    public void setProviderDynamic(Boolean providerDynamic) {
        this.providerDynamic = providerDynamic;
    }

    public void setProviderToken(String providerToken) {
        this.providerToken = providerToken;
    }

    public void setProviderAccesslog(String providerAccesslog) {
        this.providerAccesslog = providerAccesslog;
    }

    public void setProviderExecutes(Integer providerExecutes) {
        this.providerExecutes = providerExecutes;
    }

    public void setProviderRegister(Boolean providerRegister) {
        this.providerRegister = providerRegister;
    }

    public void setProviderWarmup(Integer providerWarmup) {
        this.providerWarmup = providerWarmup;
    }

    public void setProviderSerialization(String providerSerialization) {
        this.providerSerialization = providerSerialization;
    }

    public void setProviderExportAsync(Boolean providerExportAsync) {
        this.providerExportAsync = providerExportAsync;
    }

    public void setProviderTimeout(Integer providerTimeout) {
        this.providerTimeout = providerTimeout;
    }

    public void setProviderRetries(Integer providerRetries) {
        this.providerRetries = providerRetries;
    }

    public void setProviderActives(Integer providerActives) {
        this.providerActives = providerActives;
    }

    public void setProviderLoadbalance(String providerLoadbalance) {
        this.providerLoadbalance = providerLoadbalance;
    }

    public void setProviderAsync(Boolean providerAsync) {
        this.providerAsync = providerAsync;
    }

    public void setProviderSent(Boolean providerSent) {
        this.providerSent = providerSent;
    }

    public void setProviderMock(String providerMock) {
        this.providerMock = providerMock;
    }

    public void setProviderMerger(String providerMerger) {
        this.providerMerger = providerMerger;
    }

    public void setProviderCache(String providerCache) {
        this.providerCache = providerCache;
    }

    public void setProviderValidation(String providerValidation) {
        this.providerValidation = providerValidation;
    }

    public void setProviderForks(Integer providerForks) {
        this.providerForks = providerForks;
    }

    public void setProviderHost(String providerHost) {
        this.providerHost = providerHost;
    }

    public void setProviderContextpath(String providerContextpath) {
        this.providerContextpath = providerContextpath;
    }

    public void setProviderThreadpool(String providerThreadpool) {
        this.providerThreadpool = providerThreadpool;
    }

    public void setProviderThreadname(String providerThreadname) {
        this.providerThreadname = providerThreadname;
    }

    public void setProviderThreads(Integer providerThreads) {
        this.providerThreads = providerThreads;
    }

    public void setProviderIothreads(Integer providerIothreads) {
        this.providerIothreads = providerIothreads;
    }

    public void setProviderAlive(Integer providerAlive) {
        this.providerAlive = providerAlive;
    }

    public void setProviderQueues(Integer providerQueues) {
        this.providerQueues = providerQueues;
    }

    public void setProviderAccepts(Integer providerAccepts) {
        this.providerAccepts = providerAccepts;
    }

    public void setProviderCodec(String providerCodec) {
        this.providerCodec = providerCodec;
    }

    public void setProviderCharset(String providerCharset) {
        this.providerCharset = providerCharset;
    }

    public void setProviderPayload(Integer providerPayload) {
        this.providerPayload = providerPayload;
    }

    public void setProviderBuffer(Integer providerBuffer) {
        this.providerBuffer = providerBuffer;
    }

    public void setProviderTransporter(String providerTransporter) {
        this.providerTransporter = providerTransporter;
    }

    public void setProviderExchanger(String providerExchanger) {
        this.providerExchanger = providerExchanger;
    }

    public void setProviderDispatcher(String providerDispatcher) {
        this.providerDispatcher = providerDispatcher;
    }

    public void setProviderNetworker(String providerNetworker) {
        this.providerNetworker = providerNetworker;
    }

    public void setProviderServer(String providerServer) {
        this.providerServer = providerServer;
    }

    public void setProviderClient(String providerClient) {
        this.providerClient = providerClient;
    }

    public void setProviderTelnet(String providerTelnet) {
        this.providerTelnet = providerTelnet;
    }

    public void setProviderPrompt(String providerPrompt) {
        this.providerPrompt = providerPrompt;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    public void setProviderWait(Integer providerWait) {
        this.providerWait = providerWait;
    }

    public void setProviderExportThreadNum(Integer providerExportThreadNum) {
        this.providerExportThreadNum = providerExportThreadNum;
    }

    public void setProviderExportBackground(Boolean providerExportBackground) {
        this.providerExportBackground = providerExportBackground;
    }

    public void setProviderParameters(Map<String, String> providerParameters) {
        this.providerParameters = providerParameters;
    }

    public void setConsumerId(String consumerId) {
        this.consumerId = consumerId;
    }

    public void setConsumerTimeout(Integer consumerTimeout) {
        this.consumerTimeout = consumerTimeout;
    }

    public void setConsumerRetries(Integer consumerRetries) {
        this.consumerRetries = consumerRetries;
    }

    public void setConsumerActives(Integer consumerActives) {
        this.consumerActives = consumerActives;
    }

    public void setConsumerLoadbalance(String consumerLoadbalance) {
        this.consumerLoadbalance = consumerLoadbalance;
    }

    public void setConsumerAsync(Boolean consumerAsync) {
        this.consumerAsync = consumerAsync;
    }

    public void setConsumerSent(Boolean consumerSent) {
        this.consumerSent = consumerSent;
    }

    public void setConsumerMock(String consumerMock) {
        this.consumerMock = consumerMock;
    }

    public void setConsumerMerger(String consumerMerger) {
        this.consumerMerger = consumerMerger;
    }

    public void setConsumerCache(String consumerCache) {
        this.consumerCache = consumerCache;
    }

    public void setConsumerValidation(String consumerValidation) {
        this.consumerValidation = consumerValidation;
    }

    public void setConsumerForks(Integer consumerForks) {
        this.consumerForks = consumerForks;
    }

    public void setConsumerInterfaceName(String consumerInterfaceName) {
        this.consumerInterfaceName = consumerInterfaceName;
    }

    public void setConsumerVersion(String consumerVersion) {
        this.consumerVersion = consumerVersion;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public void setConsumerStub(String consumerStub) {
        this.consumerStub = consumerStub;
    }

    public void setConsumerProxy(String consumerProxy) {
        this.consumerProxy = consumerProxy;
    }

    public void setConsumerCluster(String consumerCluster) {
        this.consumerCluster = consumerCluster;
    }

    public void setConsumerListener(String consumerListener) {
        this.consumerListener = consumerListener;
    }

    public void setConsumerOwner(String consumerOwner) {
        this.consumerOwner = consumerOwner;
    }

    public void setConsumerConnections(Integer consumerConnections) {
        this.consumerConnections = consumerConnections;
    }

    public void setConsumerLayer(String consumerLayer) {
        this.consumerLayer = consumerLayer;
    }

    public void setConsumerOnconnect(String consumerOnconnect) {
        this.consumerOnconnect = consumerOnconnect;
    }

    public void setConsumerOndisconnect(String consumerOndisconnect) {
        this.consumerOndisconnect = consumerOndisconnect;
    }

    public void setConsumerCallbacks(Integer consumerCallbacks) {
        this.consumerCallbacks = consumerCallbacks;
    }

    public void setConsumerScope(String consumerScope) {
        this.consumerScope = consumerScope;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public void setConsumerAuth(Boolean consumerAuth) {
        this.consumerAuth = consumerAuth;
    }

    public void setConsumerCheck(Boolean consumerCheck) {
        this.consumerCheck = consumerCheck;
    }

    public void setConsumerInit(Boolean consumerInit) {
        this.consumerInit = consumerInit;
    }

    public void setConsumerGeneric(String consumerGeneric) {
        this.consumerGeneric = consumerGeneric;
    }

    public void setConsumerLazy(Boolean consumerLazy) {
        this.consumerLazy = consumerLazy;
    }

    public void setConsumerReconnect(String consumerReconnect) {
        this.consumerReconnect = consumerReconnect;
    }

    public void setConsumerSticky(Boolean consumerSticky) {
        this.consumerSticky = consumerSticky;
    }

    public void setConsumerProvidedBy(String consumerProvidedBy) {
        this.consumerProvidedBy = consumerProvidedBy;
    }

    public void setConsumerRouter(String consumerRouter) {
        this.consumerRouter = consumerRouter;
    }

    public void setConsumerReferAsync(Boolean consumerReferAsync) {
        this.consumerReferAsync = consumerReferAsync;
    }

    public void setConsumerClient(String consumerClient) {
        this.consumerClient = consumerClient;
    }

    public void setConsumerThreadpool(String consumerThreadpool) {
        this.consumerThreadpool = consumerThreadpool;
    }

    public void setConsumerCorethreads(Integer consumerCorethreads) {
        this.consumerCorethreads = consumerCorethreads;
    }

    public void setConsumerThreads(Integer consumerThreads) {
        this.consumerThreads = consumerThreads;
    }

    public void setConsumerQueues(Integer consumerQueues) {
        this.consumerQueues = consumerQueues;
    }

    public void setConsumerShareconnections(Integer consumerShareconnections) {
        this.consumerShareconnections = consumerShareconnections;
    }

    public void setConsumerUrlMergeProcessor(String consumerUrlMergeProcessor) {
        this.consumerUrlMergeProcessor = consumerUrlMergeProcessor;
    }

    public void setConsumerReferThreadNum(Integer consumerReferThreadNum) {
        this.consumerReferThreadNum = consumerReferThreadNum;
    }

    public void setConsumerReferBackground(Boolean consumerReferBackground) {
        this.consumerReferBackground = consumerReferBackground;
    }

    @Override
    public String toString() {
        return "DubboConfParams{" +
                "metadataReportId='" + metadataReportId + '\'' +
                ", metadataReportProtocol='" + metadataReportProtocol + '\'' +
                ", metadataReportAddress='" + metadataReportAddress + '\'' +
                ", metadataReportPort=" + metadataReportPort +
                ", metadataReportUsername='" + metadataReportUsername + '\'' +
                ", metadataReportPassword='" + metadataReportPassword + '\'' +
                ", metadataReportTimeout=" + metadataReportTimeout +
                ", metadataReportGroup='" + metadataReportGroup + '\'' +
                ", metadataReportRetryTimes=" + metadataReportRetryTimes +
                ", metadataReportRetryPeriod=" + metadataReportRetryPeriod +
                ", metadataReportCycleReport=" + metadataReportCycleReport +
                ", metadataReportSyncReport=" + metadataReportSyncReport +
                ", metadataReportCluster=" + metadataReportCluster +
                ", metadataReportRegistry='" + metadataReportRegistry + '\'' +
                ", metadataReportFile='" + metadataReportFile + '\'' +
                ", metadataReportParameters=" + metadataReportParameters +
                ", registryId='" + registryId + '\'' +
                ", registryAddress='" + registryAddress + '\'' +
                ", registryUsername='" + registryUsername + '\'' +
                ", registryPassword='" + registryPassword + '\'' +
                ", registryPort=" + registryPort +
                ", registryProtocol='" + registryProtocol + '\'' +
                ", registryTransporter='" + registryTransporter + '\'' +
                ", registryServer='" + registryServer + '\'' +
                ", registryClient='" + registryClient + '\'' +
                ", registryCluster='" + registryCluster + '\'' +
                ", registryZone='" + registryZone + '\'' +
                ", registryGroup='" + registryGroup + '\'' +
                ", registryVersion='" + registryVersion + '\'' +
                ", registryTimeout=" + registryTimeout +
                ", registrySession=" + registrySession +
                ", registryFile='" + registryFile + '\'' +
                ", registryCheck=" + registryCheck +
                ", registryDynamic=" + registryDynamic +
                ", registryRegister=" + registryRegister +
                ", registrySubscribe=" + registrySubscribe +
                ", registrySimplified=" + registrySimplified +
                ", registryExtraKeys='" + registryExtraKeys + '\'' +
                ", registryUseAsConfigCenter=" + registryUseAsConfigCenter +
                ", registryUseAsMetadataCenter=" + registryUseAsMetadataCenter +
                ", registryAccepts='" + registryAccepts + '\'' +
                ", registryPreferred=" + registryPreferred +
                ", registryWeight=" + registryWeight +
                ", registryPublishInterface=" + registryPublishInterface +
                ", registryPublishInstance=" + registryPublishInstance +
                ", registryParameters=" + registryParameters +
                ", monitorEnable=" + monitorEnable +
                ", monitorId='" + monitorId + '\'' +
                ", monitorProtocol='" + monitorProtocol + '\'' +
                ", monitorAddress='" + monitorAddress + '\'' +
                ", monitorUsername='" + monitorUsername + '\'' +
                ", monitorPassword='" + monitorPassword + '\'' +
                ", monitorGroup='" + monitorGroup + '\'' +
                ", monitorVersion='" + monitorVersion + '\'' +
                ", monitorInterval='" + monitorInterval + '\'' +
                ", applicationId='" + applicationId + '\'' +
                ", applicationName='" + applicationName + '\'' +
                ", applicationVersion='" + applicationVersion + '\'' +
                ", applicationOwner='" + applicationOwner + '\'' +
                ", applicationOrganization='" + applicationOrganization + '\'' +
                ", applicationArchitecture='" + applicationArchitecture + '\'' +
                ", applicationEnvironment='" + applicationEnvironment + '\'' +
                ", applicationCompiler='" + applicationCompiler + '\'' +
                ", applicationLogger='" + applicationLogger + '\'' +
                ", applicationDumpDirectory='" + applicationDumpDirectory + '\'' +
                ", applicationShutWait='" + applicationShutWait + '\'' +
                ", applicationMetadataType='" + applicationMetadataType + '\'' +
                ", applicationRegisterConsumer=" + applicationRegisterConsumer +
                ", applicationRepository='" + applicationRepository + '\'' +
                ", applicationEnableFileCache=" + applicationEnableFileCache +
                ", applicationPublishInterface=" + applicationPublishInterface +
                ", applicationPublishInstance=" + applicationPublishInstance +
                ", applicationProtocol='" + applicationProtocol + '\'' +
                ", applicationMetadataServicePort=" + applicationMetadataServicePort +
                ", applicationLivenessProbe='" + applicationLivenessProbe + '\'' +
                ", applicationReadinessProbe='" + applicationReadinessProbe + '\'' +
                ", applicationStartupProbe='" + applicationStartupProbe + '\'' +
                ", applicationQosEnable=" + applicationQosEnable +
                ", applicationQosHost='" + applicationQosHost + '\'' +
                ", applicationQosPort=" + applicationQosPort +
                ", applicationQosAcceptForeignIp=" + applicationQosAcceptForeignIp +
                ", applicationParameters=" + applicationParameters +
                ", protocolId='" + protocolId + '\'' +
                ", protocolName='" + protocolName + '\'' +
                ", protocolHost='" + protocolHost + '\'' +
                ", protocolPort=" + protocolPort +
                ", protocolContextpath='" + protocolContextpath + '\'' +
                ", protocolThreadpool='" + protocolThreadpool + '\'' +
                ", protocolThreadname='" + protocolThreadname + '\'' +
                ", protocolCorethreads=" + protocolCorethreads +
                ", protocolThreads=" + protocolThreads +
                ", protocolIothreads=" + protocolIothreads +
                ", protocolAlive=" + protocolAlive +
                ", protocolQueues=" + protocolQueues +
                ", protocolAccepts=" + protocolAccepts +
                ", protocolCodec='" + protocolCodec + '\'' +
                ", protocolSerialization='" + protocolSerialization + '\'' +
                ", protocolCharset='" + protocolCharset + '\'' +
                ", protocolPayload=" + protocolPayload +
                ", protocolBuffer=" + protocolBuffer +
                ", protocolHeartbeat=" + protocolHeartbeat +
                ", protocolAccesslog='" + protocolAccesslog + '\'' +
                ", protocolTransporter='" + protocolTransporter + '\'' +
                ", protocolExchanger='" + protocolExchanger + '\'' +
                ", protocolDispatcher='" + protocolDispatcher + '\'' +
                ", protocolNetworker='" + protocolNetworker + '\'' +
                ", protocolServer='" + protocolServer + '\'' +
                ", protocolClient='" + protocolClient + '\'' +
                ", protocolTelnet='" + protocolTelnet + '\'' +
                ", protocolPrompt='" + protocolPrompt + '\'' +
                ", protocolStatus='" + protocolStatus + '\'' +
                ", protocolRegister=" + protocolRegister +
                ", protocolKeepAlive=" + protocolKeepAlive +
                ", protocolOptimizer='" + protocolOptimizer + '\'' +
                ", protocolExtension='" + protocolExtension + '\'' +
                ", protocolSslEnabled=" + protocolSslEnabled +
                ", protocolParameters=" + protocolParameters +
                ", protocolDefault=" + protocolDefault +
                ", providerId='" + providerId + '\'' +
                ", providerInterfaceName='" + providerInterfaceName + '\'' +
                ", providerStub='" + providerStub + '\'' +
                ", providerProxy='" + providerProxy + '\'' +
                ", providerCluster='" + providerCluster + '\'' +
                ", providerListener='" + providerListener + '\'' +
                ", providerOwner='" + providerOwner + '\'' +
                ", providerConnections=" + providerConnections +
                ", providerLayer='" + providerLayer + '\'' +
                ", providerOnconnect='" + providerOnconnect + '\'' +
                ", providerOndisconnect='" + providerOndisconnect + '\'' +
                ", providerCallbacks=" + providerCallbacks +
                ", providerScope='" + providerScope + '\'' +
                ", providerTag='" + providerTag + '\'' +
                ", providerAuth=" + providerAuth +
                ", providerVersion='" + providerVersion + '\'' +
                ", providerGroup='" + providerGroup + '\'' +
                ", providerDeprecated=" + providerDeprecated +
                ", provideDelay=" + provideDelay +
                ", providerExport=" + providerExport +
                ", providerWeight=" + providerWeight +
                ", providerDocument='" + providerDocument + '\'' +
                ", providerDynamic=" + providerDynamic +
                ", providerToken='" + providerToken + '\'' +
                ", providerAccesslog='" + providerAccesslog + '\'' +
                ", providerExecutes=" + providerExecutes +
                ", providerRegister=" + providerRegister +
                ", providerWarmup=" + providerWarmup +
                ", providerSerialization='" + providerSerialization + '\'' +
                ", providerExportAsync=" + providerExportAsync +
                ", providerTimeout=" + providerTimeout +
                ", providerRetries=" + providerRetries +
                ", providerActives=" + providerActives +
                ", providerLoadbalance='" + providerLoadbalance + '\'' +
                ", providerAsync=" + providerAsync +
                ", providerSent=" + providerSent +
                ", providerMock='" + providerMock + '\'' +
                ", providerMerger='" + providerMerger + '\'' +
                ", providerCache='" + providerCache + '\'' +
                ", providerValidation='" + providerValidation + '\'' +
                ", providerForks=" + providerForks +
                ", providerHost='" + providerHost + '\'' +
                ", providerContextpath='" + providerContextpath + '\'' +
                ", providerThreadpool='" + providerThreadpool + '\'' +
                ", providerThreadname='" + providerThreadname + '\'' +
                ", providerThreads=" + providerThreads +
                ", providerIothreads=" + providerIothreads +
                ", providerAlive=" + providerAlive +
                ", providerQueues=" + providerQueues +
                ", providerAccepts=" + providerAccepts +
                ", providerCodec='" + providerCodec + '\'' +
                ", providerCharset='" + providerCharset + '\'' +
                ", providerPayload=" + providerPayload +
                ", providerBuffer=" + providerBuffer +
                ", providerTransporter='" + providerTransporter + '\'' +
                ", providerExchanger='" + providerExchanger + '\'' +
                ", providerDispatcher='" + providerDispatcher + '\'' +
                ", providerNetworker='" + providerNetworker + '\'' +
                ", providerServer='" + providerServer + '\'' +
                ", providerClient='" + providerClient + '\'' +
                ", providerTelnet='" + providerTelnet + '\'' +
                ", providerPrompt='" + providerPrompt + '\'' +
                ", providerStatus='" + providerStatus + '\'' +
                ", providerWait=" + providerWait +
                ", providerExportThreadNum=" + providerExportThreadNum +
                ", providerExportBackground=" + providerExportBackground +
                ", providerParameters=" + providerParameters +
                ", consumerId='" + consumerId + '\'' +
                ", consumerTimeout=" + consumerTimeout +
                ", consumerRetries=" + consumerRetries +
                ", consumerActives=" + consumerActives +
                ", consumerLoadbalance='" + consumerLoadbalance + '\'' +
                ", consumerAsync=" + consumerAsync +
                ", consumerSent=" + consumerSent +
                ", consumerMock='" + consumerMock + '\'' +
                ", consumerMerger='" + consumerMerger + '\'' +
                ", consumerCache='" + consumerCache + '\'' +
                ", consumerValidation='" + consumerValidation + '\'' +
                ", consumerForks=" + consumerForks +
                ", consumerInterfaceName='" + consumerInterfaceName + '\'' +
                ", consumerVersion='" + consumerVersion + '\'' +
                ", consumerGroup='" + consumerGroup + '\'' +
                ", consumerStub='" + consumerStub + '\'' +
                ", consumerProxy='" + consumerProxy + '\'' +
                ", consumerCluster='" + consumerCluster + '\'' +
                ", consumerListener='" + consumerListener + '\'' +
                ", consumerOwner='" + consumerOwner + '\'' +
                ", consumerConnections=" + consumerConnections +
                ", consumerLayer='" + consumerLayer + '\'' +
                ", consumerOnconnect='" + consumerOnconnect + '\'' +
                ", consumerOndisconnect='" + consumerOndisconnect + '\'' +
                ", consumerCallbacks=" + consumerCallbacks +
                ", consumerScope='" + consumerScope + '\'' +
                ", consumerTag='" + consumerTag + '\'' +
                ", consumerAuth=" + consumerAuth +
                ", consumerCheck=" + consumerCheck +
                ", consumerInit=" + consumerInit +
                ", consumerGeneric='" + consumerGeneric + '\'' +
                ", consumerLazy=" + consumerLazy +
                ", consumerReconnect='" + consumerReconnect + '\'' +
                ", consumerSticky=" + consumerSticky +
                ", consumerProvidedBy='" + consumerProvidedBy + '\'' +
                ", consumerRouter='" + consumerRouter + '\'' +
                ", consumerReferAsync=" + consumerReferAsync +
                ", consumerClient='" + consumerClient + '\'' +
                ", consumerThreadpool='" + consumerThreadpool + '\'' +
                ", consumerCorethreads=" + consumerCorethreads +
                ", consumerThreads=" + consumerThreads +
                ", consumerQueues=" + consumerQueues +
                ", consumerShareconnections=" + consumerShareconnections +
                ", consumerUrlMergeProcessor='" + consumerUrlMergeProcessor + '\'' +
                ", consumerReferThreadNum=" + consumerReferThreadNum +
                ", consumerReferBackground=" + consumerReferBackground +
                '}';
    }
}
