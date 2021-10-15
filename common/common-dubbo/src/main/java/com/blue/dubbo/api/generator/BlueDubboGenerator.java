package com.blue.dubbo.api.generator;

import com.blue.dubbo.api.conf.DubboConf;
import org.apache.dubbo.config.*;

import static com.blue.dubbo.constant.FilterBeanName.NO_EXCEPTION_FILTER;
import static java.util.Optional.ofNullable;

/**
 * dubbo component generator
 *
 * @author liuyunfei
 * @date 2021/9/11
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "AlibabaMethodTooLong"})
public final class BlueDubboGenerator {

    /**
     * generate metadata report config
     *
     * @param dubboConf
     * @return
     */
    public static MetadataReportConfig generateMetadataReportConfig(DubboConf dubboConf) {
        assertConf(dubboConf);

        MetadataReportConfig metadataReportConfig = new MetadataReportConfig();

        if (ofNullable(dubboConf.getMetadataReportEnable()).orElse(false)) {
            ofNullable(dubboConf.getMetadataReportId())
                    .ifPresent(metadataReportConfig::setId);
            ofNullable(dubboConf.getMetadataReportAddress())
                    .ifPresent(metadataReportConfig::setAddress);
            ofNullable(dubboConf.getMetadataReportProtocol())
                    .ifPresent(metadataReportConfig::setProtocol);
            ofNullable(dubboConf.getMetadataReportPort())
                    .ifPresent(metadataReportConfig::setPort);
            ofNullable(dubboConf.getMetadataReportUsername())
                    .ifPresent(metadataReportConfig::setUsername);
            ofNullable(dubboConf.getMetadataReportPassword())
                    .ifPresent(metadataReportConfig::setPassword);
            ofNullable(dubboConf.getMetadataReportTimeout())
                    .ifPresent(metadataReportConfig::setTimeout);
            ofNullable(dubboConf.getMetadataReportGroup())
                    .ifPresent(metadataReportConfig::setGroup);
            ofNullable(dubboConf.getMetadataReportRetryTimes())
                    .ifPresent(metadataReportConfig::setRetryTimes);
            ofNullable(dubboConf.getMetadataReportRetryPeriod())
                    .ifPresent(metadataReportConfig::setRetryPeriod);
            ofNullable(dubboConf.getMetadataReportCycleReport())
                    .ifPresent(metadataReportConfig::setCycleReport);
            ofNullable(dubboConf.getMetadataReportSyncReport())
                    .ifPresent(metadataReportConfig::setSyncReport);
            ofNullable(dubboConf.getMetadataReportCluster())
                    .ifPresent(metadataReportConfig::setCluster);
            ofNullable(dubboConf.getMetadataReportRegistry())
                    .ifPresent(metadataReportConfig::setRegistry);
            ofNullable(dubboConf.getMetadataReportFile())
                    .ifPresent(metadataReportConfig::setFile);
            ofNullable(dubboConf.getMetadataReportParameters())
                    .ifPresent(metadataReportConfig::setParameters);
        }

        return metadataReportConfig;
    }

    /**
     * generate registry config
     *
     * @param dubboConf
     * @return
     */
    public static RegistryConfig generateRegistryConfig(DubboConf dubboConf) {
        assertConf(dubboConf);

        RegistryConfig registryConfig = new RegistryConfig();

        ofNullable(dubboConf.getRegistryId())
                .ifPresent(registryConfig::setId);
        ofNullable(dubboConf.getRegistryAddress())
                .ifPresent(registryConfig::setAddress);
        ofNullable(dubboConf.getRegistryUsername())
                .ifPresent(registryConfig::setUsername);
        ofNullable(dubboConf.getRegistryPassword())
                .ifPresent(registryConfig::setPassword);
        ofNullable(dubboConf.getRegistryPort())
                .ifPresent(registryConfig::setPort);
        ofNullable(dubboConf.getRegistryProtocol())
                .ifPresent(registryConfig::setProtocol);
        ofNullable(dubboConf.getRegistryTransporter())
                .ifPresent(registryConfig::setTransporter);
        ofNullable(dubboConf.getRegistryServer())
                .ifPresent(registryConfig::setServer);
        ofNullable(dubboConf.getRegistryClient())
                .ifPresent(registryConfig::setClient);
        ofNullable(dubboConf.getRegistryZone())
                .ifPresent(registryConfig::setZone);
        ofNullable(dubboConf.getRegistryCluster())
                .ifPresent(registryConfig::setCluster);
        ofNullable(dubboConf.getRegistryGroup())
                .ifPresent(registryConfig::setGroup);
        ofNullable(dubboConf.getRegistryVersion())
                .ifPresent(registryConfig::setVersion);
        ofNullable(dubboConf.getRegistryTimeout())
                .ifPresent(registryConfig::setTimeout);
        ofNullable(dubboConf.getRegistrySession())
                .ifPresent(registryConfig::setSession);
        ofNullable(dubboConf.getRegistryFile())
                .ifPresent(registryConfig::setFile);
        ofNullable(dubboConf.getRegistryCheck())
                .ifPresent(registryConfig::setCheck);
        ofNullable(dubboConf.getRegistryDynamic())
                .ifPresent(registryConfig::setDynamic);
        ofNullable(dubboConf.getRegistryRegister())
                .ifPresent(registryConfig::setRegister);
        ofNullable(dubboConf.getRegistrySubscribe())
                .ifPresent(registryConfig::setSubscribe);
        ofNullable(dubboConf.getRegistrySimplified())
                .ifPresent(registryConfig::setSimplified);
        ofNullable(dubboConf.getRegistryExtraKeys())
                .ifPresent(registryConfig::setExtraKeys);
        ofNullable(dubboConf.getRegistryUseAsConfigCenter())
                .ifPresent(registryConfig::setUseAsConfigCenter);
        ofNullable(dubboConf.getRegistryUseAsMetadataCenter())
                .ifPresent(registryConfig::setUseAsMetadataCenter);
        ofNullable(dubboConf.getRegistryAccepts())
                .ifPresent(registryConfig::setAccepts);
        ofNullable(dubboConf.getRegistryPreferred())
                .ifPresent(registryConfig::setPreferred);
        ofNullable(dubboConf.getRegistryWeight())
                .ifPresent(registryConfig::setWeight);
        ofNullable(dubboConf.getRegistryPublishInterface())
                .ifPresent(registryConfig::setPublishInterface);
        ofNullable(dubboConf.getRegistryPublishInstance())
                .ifPresent(registryConfig::setPublishInstance);
        ofNullable(dubboConf.getRegistryParameters())
                .ifPresent(registryConfig::updateParameters);

        return registryConfig;
    }

    /**
     * generate monitor config
     *
     * @param dubboConf
     * @return
     */
    public static MonitorConfig generateMonitorConfig(DubboConf dubboConf) {
        assertConf(dubboConf);

        MonitorConfig monitorConfig = new MonitorConfig();

        if (ofNullable(dubboConf.getMonitorEnable()).orElse(false)) {

            ofNullable(dubboConf.getMonitorId())
                    .ifPresent(monitorConfig::setId);
            ofNullable(dubboConf.getMonitorProtocol())
                    .ifPresent(monitorConfig::setProtocol);
            ofNullable(dubboConf.getMonitorAddress())
                    .ifPresent(monitorConfig::setAddress);
            ofNullable(dubboConf.getMonitorUsername())
                    .ifPresent(monitorConfig::setUsername);
            ofNullable(dubboConf.getMonitorPassword())
                    .ifPresent(monitorConfig::setPassword);
            ofNullable(dubboConf.getMonitorGroup())
                    .ifPresent(monitorConfig::setGroup);
            ofNullable(dubboConf.getMonitorVersion())
                    .ifPresent(monitorConfig::setVersion);
            ofNullable(dubboConf.getMonitorInterval())
                    .ifPresent(monitorConfig::setInterval);
        }

        return monitorConfig;
    }

    /**
     * generate application config
     *
     * @param dubboConf
     * @return
     */
    public static ApplicationConfig generateApplicationConfig(DubboConf dubboConf) {
        assertConf(dubboConf);

        ApplicationConfig applicationConfig = new ApplicationConfig();

        ofNullable(dubboConf.getApplicationId())
                .ifPresent(applicationConfig::setId);
        ofNullable(dubboConf.getApplicationName())
                .ifPresent(applicationConfig::setName);
        ofNullable(dubboConf.getRegistryId())
                .ifPresent(applicationConfig::setRegistryIds);
        ofNullable(dubboConf.getApplicationVersion())
                .ifPresent(applicationConfig::setVersion);
        ofNullable(dubboConf.getApplicationOwner())
                .ifPresent(applicationConfig::setOwner);
        ofNullable(dubboConf.getApplicationOrganization())
                .ifPresent(applicationConfig::setOrganization);
        ofNullable(dubboConf.getApplicationVersion())
                .ifPresent(applicationConfig::setVersion);
        ofNullable(dubboConf.getApplicationVersion())
                .ifPresent(applicationConfig::setVersion);
        ofNullable(dubboConf.getApplicationVersion())
                .ifPresent(applicationConfig::setVersion);
        ofNullable(dubboConf.getApplicationVersion())
                .ifPresent(applicationConfig::setVersion);
        ofNullable(dubboConf.getApplicationArchitecture())
                .ifPresent(applicationConfig::setArchitecture);
        ofNullable(dubboConf.getApplicationEnvironment())
                .ifPresent(applicationConfig::setEnvironment);
        ofNullable(dubboConf.getApplicationCompiler())
                .ifPresent(applicationConfig::setCompiler);
        ofNullable(dubboConf.getApplicationLogger())
                .ifPresent(applicationConfig::setLogger);
        ofNullable(dubboConf.getApplicationDumpDirectory())
                .ifPresent(applicationConfig::setDumpDirectory);

        ofNullable(dubboConf.getApplicationShutWait())
                .ifPresent(applicationConfig::setShutwait);
        ofNullable(dubboConf.getApplicationMetadataType())
                .ifPresent(applicationConfig::setMetadataType);
        ofNullable(dubboConf.getApplicationRegisterConsumer())
                .ifPresent(applicationConfig::setRegisterConsumer);
        ofNullable(dubboConf.getApplicationRepository())
                .ifPresent(applicationConfig::setRepository);
        ofNullable(dubboConf.getApplicationEnableFileCache())
                .ifPresent(applicationConfig::setEnableFileCache);
        ofNullable(dubboConf.getApplicationPublishInterface())
                .ifPresent(applicationConfig::setPublishInterface);
        ofNullable(dubboConf.getApplicationPublishInstance())
                .ifPresent(applicationConfig::setPublishInstance);
        ofNullable(dubboConf.getApplicationProtocol())
                .ifPresent(applicationConfig::setProtocol);
        ofNullable(dubboConf.getApplicationMetadataServicePort())
                .ifPresent(applicationConfig::setMetadataServicePort);
        ofNullable(dubboConf.getApplicationLivenessProbe())
                .ifPresent(applicationConfig::setLivenessProbe);
        ofNullable(dubboConf.getApplicationReadinessProbe())
                .ifPresent(applicationConfig::setReadinessProbe);
        ofNullable(dubboConf.getApplicationStartupProbe())
                .ifPresent(applicationConfig::setStartupProbe);
        ofNullable(dubboConf.getApplicationParameters())
                .ifPresent(applicationConfig::setParameters);

        Boolean qosEnable = ofNullable(dubboConf.getApplicationQosEnable()).orElse(false);
        applicationConfig.setQosEnable(qosEnable);
        if (qosEnable) {
            ofNullable(dubboConf.getApplicationQosHost())
                    .ifPresent(applicationConfig::setQosHost);
            ofNullable(dubboConf.getApplicationQosPort())
                    .ifPresent(applicationConfig::setQosPort);
            ofNullable(dubboConf.getApplicationQosAcceptForeignIp())
                    .ifPresent(applicationConfig::setQosAcceptForeignIp);
        }

        return applicationConfig;
    }

    /**
     * generate protocol config
     *
     * @param dubboConf
     * @return
     */
    public static ProtocolConfig generateProtocolConfig(DubboConf dubboConf) {
        assertConf(dubboConf);

        ProtocolConfig protocolConfig = new ProtocolConfig();

        ofNullable(dubboConf.getProtocolId())
                .ifPresent(protocolConfig::setId);
        ofNullable(dubboConf.getProtocolName())
                .ifPresent(protocolConfig::setName);
        ofNullable(dubboConf.getProtocolHost())
                .ifPresent(protocolConfig::setHost);
        ofNullable(dubboConf.getProtocolPort())
                .ifPresent(protocolConfig::setPort);
        ofNullable(dubboConf.getProtocolContextpath())
                .ifPresent(protocolConfig::setContextpath);
        ofNullable(dubboConf.getProtocolThreadpool())
                .ifPresent(protocolConfig::setThreadpool);
        ofNullable(dubboConf.getProtocolThreadname())
                .ifPresent(protocolConfig::setThreadname);
        ofNullable(dubboConf.getProtocolCorethreads())
                .ifPresent(protocolConfig::setCorethreads);
        ofNullable(dubboConf.getProtocolThreads())
                .ifPresent(protocolConfig::setThreads);
        ofNullable(dubboConf.getProtocolIothreads())
                .ifPresent(protocolConfig::setIothreads);
        ofNullable(dubboConf.getProtocolAlive())
                .ifPresent(protocolConfig::setAlive);
        ofNullable(dubboConf.getProtocolQueues())
                .ifPresent(protocolConfig::setQueues);
        ofNullable(dubboConf.getProtocolAccepts())
                .ifPresent(protocolConfig::setAccepts);
        ofNullable(dubboConf.getProtocolCodec())
                .ifPresent(protocolConfig::setCodec);
        ofNullable(dubboConf.getProtocolSerialization())
                .ifPresent(protocolConfig::setSerialization);
        ofNullable(dubboConf.getProtocolCharset())
                .ifPresent(protocolConfig::setCharset);
        ofNullable(dubboConf.getProtocolPayload())
                .ifPresent(protocolConfig::setPayload);
        ofNullable(dubboConf.getProtocolBuffer())
                .ifPresent(protocolConfig::setBuffer);
        ofNullable(dubboConf.getProtocolHeartbeat())
                .ifPresent(protocolConfig::setHeartbeat);
        ofNullable(dubboConf.getProtocolAccesslog())
                .ifPresent(protocolConfig::setAccesslog);
        ofNullable(dubboConf.getProtocolTransporter())
                .ifPresent(protocolConfig::setTransporter);
        ofNullable(dubboConf.getProtocolExchanger())
                .ifPresent(protocolConfig::setExchanger);
        ofNullable(dubboConf.getProtocolDispatcher())
                .ifPresent(protocolConfig::setDispatcher);
        ofNullable(dubboConf.getProtocolNetworker())
                .ifPresent(protocolConfig::setNetworker);
        ofNullable(dubboConf.getProtocolServer())
                .ifPresent(protocolConfig::setServer);
        ofNullable(dubboConf.getProtocolClient())
                .ifPresent(protocolConfig::setClient);
        ofNullable(dubboConf.getProtocolTelnet())
                .ifPresent(protocolConfig::setTelnet);
        ofNullable(dubboConf.getProtocolPrompt())
                .ifPresent(protocolConfig::setPrompt);
        ofNullable(dubboConf.getProtocolStatus())
                .ifPresent(protocolConfig::setStatus);
        ofNullable(dubboConf.getProtocolRegister())
                .ifPresent(protocolConfig::setRegister);
        ofNullable(dubboConf.getProtocolKeepAlive())
                .ifPresent(protocolConfig::setKeepAlive);
        ofNullable(dubboConf.getProtocolOptimizer())
                .ifPresent(protocolConfig::setOptimizer);
        ofNullable(dubboConf.getProtocolExtension())
                .ifPresent(protocolConfig::setExtension);
        ofNullable(dubboConf.getProtocolSslEnabled())
                .ifPresent(protocolConfig::setSslEnabled);
        ofNullable(dubboConf.getProtocolParameters())
                .ifPresent(protocolConfig::setParameters);
        ofNullable(dubboConf.getProtocolDefault())
                .ifPresent(protocolConfig::setDefault);

        return protocolConfig;
    }

    /**
     * generate provider config
     *
     * @param dubboConf
     * @return
     */
    @SuppressWarnings("AlibabaRemoveCommentedCode")
    public static ProviderConfig generateProviderConfig(DubboConf dubboConf) {
        assertConf(dubboConf);

        ProviderConfig providerConfig = new ProviderConfig();

        ofNullable(dubboConf.getProviderId())
                .ifPresent(providerConfig::setId);
        ofNullable(dubboConf.getProviderInterfaceName())
                .ifPresent(providerConfig::setInterface);
        ofNullable(dubboConf.getProviderVersion())
                .ifPresent(providerConfig::setVersion);
        ofNullable(dubboConf.getProviderGroup())
                .ifPresent(providerConfig::setGroup);
        ofNullable(dubboConf.getProviderStub())
                .ifPresent(providerConfig::setStub);
        ofNullable(dubboConf.getProviderProxy())
                .ifPresent(providerConfig::setProxy);
        ofNullable(dubboConf.getProviderCluster())
                .ifPresent(providerConfig::setCluster);
        providerConfig.setFilter(NO_EXCEPTION_FILTER.name);
        ofNullable(dubboConf.getProviderListener())
                .ifPresent(providerConfig::setListener);
        ofNullable(dubboConf.getProviderOwner())
                .ifPresent(providerConfig::setOwner);
        ofNullable(dubboConf.getProviderConnections())
                .ifPresent(providerConfig::setConnections);
        ofNullable(dubboConf.getProviderLayer())
                .ifPresent(providerConfig::setLayer);
        ofNullable(dubboConf.getRegistryId())
                .ifPresent(providerConfig::setRegistryIds);
        ofNullable(dubboConf.getProviderOnconnect())
                .ifPresent(providerConfig::setOnconnect);
        ofNullable(dubboConf.getProviderOndisconnect())
                .ifPresent(providerConfig::setOndisconnect);
        ofNullable(dubboConf.getProviderCallbacks())
                .ifPresent(providerConfig::setCallbacks);
        ofNullable(dubboConf.getProviderScope())
                .ifPresent(providerConfig::setScope);
        ofNullable(dubboConf.getProviderTag())
                .ifPresent(providerConfig::setTag);
        ofNullable(dubboConf.getProviderAuth())
                .ifPresent(providerConfig::setAuth);
        ofNullable(dubboConf.getProtocolId())
                .ifPresent(providerConfig::setProtocolIds);
        ofNullable(dubboConf.getProviderVersion())
                .ifPresent(providerConfig::setVersion);
        ofNullable(dubboConf.getProviderGroup())
                .ifPresent(providerConfig::setGroup);
        ofNullable(dubboConf.getProviderDeprecated())
                .ifPresent(providerConfig::setDeprecated);
        ofNullable(dubboConf.getProvideDelay())
                .ifPresent(providerConfig::setDelay);
        ofNullable(dubboConf.getProviderExport())
                .ifPresent(providerConfig::setExport);
        ofNullable(dubboConf.getProviderWeight())
                .ifPresent(providerConfig::setWait);
        ofNullable(dubboConf.getProviderDocument())
                .ifPresent(providerConfig::setDocument);
        ofNullable(dubboConf.getProviderDynamic())
                .ifPresent(providerConfig::setDynamic);
        ofNullable(dubboConf.getProviderToken())
                .ifPresent(providerConfig::setToken);
        ofNullable(dubboConf.getProviderAccesslog())
                .ifPresent(providerConfig::setAccesslog);
        ofNullable(dubboConf.getProviderExecutes())
                .ifPresent(providerConfig::setExecutes);
        ofNullable(dubboConf.getProviderRegister())
                .ifPresent(providerConfig::setRegister);
        ofNullable(dubboConf.getProviderWarmup())
                .ifPresent(providerConfig::setWarmup);
        ofNullable(dubboConf.getProviderSerialization())
                .ifPresent(providerConfig::setSerialization);
        ofNullable(dubboConf.getProviderExportAsync())
                .ifPresent(providerConfig::setExportAsync);
        ofNullable(dubboConf.getProviderTimeout())
                .ifPresent(providerConfig::setTimeout);
        ofNullable(dubboConf.getProviderRetries())
                .ifPresent(providerConfig::setRetries);
        ofNullable(dubboConf.getProviderActives())
                .ifPresent(providerConfig::setActives);
        ofNullable(dubboConf.getProviderLoadbalance())
                .ifPresent(providerConfig::setLoadbalance);
        ofNullable(dubboConf.getProviderAsync())
                .ifPresent(providerConfig::setAsync);
        ofNullable(dubboConf.getProviderSent())
                .ifPresent(providerConfig::setSent);
        ofNullable(dubboConf.getProviderMock())
                .ifPresent(providerConfig::setMock);
        ofNullable(dubboConf.getProviderMerger())
                .ifPresent(providerConfig::setMerger);
        ofNullable(dubboConf.getProviderCache())
                .ifPresent(providerConfig::setCache);
        ofNullable(dubboConf.getProviderValidation())
                .ifPresent(providerConfig::setValidation);
        ofNullable(dubboConf.getProviderForks())
                .ifPresent(providerConfig::setForks);
        ofNullable(dubboConf.getProviderHost())
                .ifPresent(providerConfig::setHost);
        ofNullable(dubboConf.getProviderContextpath())
                .ifPresent(providerConfig::setContextpath);
        ofNullable(dubboConf.getProviderThreadpool())
                .ifPresent(providerConfig::setThreadpool);
        ofNullable(dubboConf.getProviderThreadname())
                .ifPresent(providerConfig::setThreadname);
        ofNullable(dubboConf.getProviderThreads())
                .ifPresent(providerConfig::setThreads);
        ofNullable(dubboConf.getProviderIothreads())
                .ifPresent(providerConfig::setIothreads);
        ofNullable(dubboConf.getProviderAlive())
                .ifPresent(providerConfig::setAlive);
        ofNullable(dubboConf.getProviderQueues())
                .ifPresent(providerConfig::setQueues);
        ofNullable(dubboConf.getProviderAccepts())
                .ifPresent(providerConfig::setAccepts);
        ofNullable(dubboConf.getProviderCodec())
                .ifPresent(providerConfig::setCodec);
        ofNullable(dubboConf.getProviderCharset())
                .ifPresent(providerConfig::setCharset);
        ofNullable(dubboConf.getProviderPayload())
                .ifPresent(providerConfig::setPayload);
        ofNullable(dubboConf.getProviderBuffer())
                .ifPresent(providerConfig::setBuffer);
        ofNullable(dubboConf.getProviderTransporter())
                .ifPresent(providerConfig::setTransporter);
        ofNullable(dubboConf.getProviderExchanger())
                .ifPresent(providerConfig::setExchanger);
        ofNullable(dubboConf.getProviderDispatcher())
                .ifPresent(providerConfig::setDispatcher);
        ofNullable(dubboConf.getProviderNetworker())
                .ifPresent(providerConfig::setNetworker);
        ofNullable(dubboConf.getProviderServer())
                .ifPresent(providerConfig::setServer);
        ofNullable(dubboConf.getProviderClient())
                .ifPresent(providerConfig::setClient);
        ofNullable(dubboConf.getProviderTelnet())
                .ifPresent(providerConfig::setTelnet);
        ofNullable(dubboConf.getProviderPrompt())
                .ifPresent(providerConfig::setPrompt);
        ofNullable(dubboConf.getProviderStatus())
                .ifPresent(providerConfig::setStatus);
        ofNullable(dubboConf.getProviderWait())
                .ifPresent(providerConfig::setWait);
        ofNullable(dubboConf.getProviderExportThreadNum())
                .ifPresent(providerConfig::setExportThreadNum);
        ofNullable(dubboConf.getProviderExportBackground())
                .ifPresent(providerConfig::setExportBackground);
        ofNullable(dubboConf.getProviderParameters())
                .ifPresent(providerConfig::setParameters);

        return providerConfig;
    }

    /**
     * generate consumer config
     *
     * @param dubboConf
     * @return
     */
    public static ConsumerConfig generateConsumerConfig(DubboConf dubboConf) {
        assertConf(dubboConf);

        ConsumerConfig consumerConfig = new ConsumerConfig();

        ofNullable(dubboConf.getConsumerId())
                .ifPresent(consumerConfig::setId);
        ofNullable(dubboConf.getProviderTimeout())
                .ifPresent(consumerConfig::setTimeout);
        ofNullable(dubboConf.getConsumerRetries())
                .ifPresent(consumerConfig::setRetries);
        ofNullable(dubboConf.getConsumerActives())
                .ifPresent(consumerConfig::setActives);
        ofNullable(dubboConf.getConsumerLoadbalance())
                .ifPresent(consumerConfig::setLoadbalance);
        ofNullable(dubboConf.getConsumerAsync())
                .ifPresent(consumerConfig::setAsync);
        ofNullable(dubboConf.getConsumerSent())
                .ifPresent(consumerConfig::setSent);
        ofNullable(dubboConf.getConsumerMock())
                .ifPresent(consumerConfig::setMock);
        ofNullable(dubboConf.getConsumerMerger())
                .ifPresent(consumerConfig::setMerger);
        ofNullable(dubboConf.getConsumerCache())
                .ifPresent(consumerConfig::setCache);
        ofNullable(dubboConf.getConsumerValidation())
                .ifPresent(consumerConfig::setValidation);
        ofNullable(dubboConf.getConsumerForks())
                .ifPresent(consumerConfig::setForks);
        ofNullable(dubboConf.getConsumerInterfaceName())
                .ifPresent(consumerConfig::setInterface);
        ofNullable(dubboConf.getConsumerVersion())
                .ifPresent(consumerConfig::setVersion);
        ofNullable(dubboConf.getConsumerGroup())
                .ifPresent(consumerConfig::setGroup);
        ofNullable(dubboConf.getConsumerStub())
                .ifPresent(consumerConfig::setStub);
        ofNullable(dubboConf.getConsumerProxy())
                .ifPresent(consumerConfig::setProxy);
        ofNullable(dubboConf.getConsumerCluster())
                .ifPresent(consumerConfig::setCluster);
        consumerConfig.setFilter(NO_EXCEPTION_FILTER.name);
        ofNullable(dubboConf.getConsumerListener())
                .ifPresent(consumerConfig::setListener);
        ofNullable(dubboConf.getConsumerOwner())
                .ifPresent(consumerConfig::setOwner);
        ofNullable(dubboConf.getConsumerConnections())
                .ifPresent(consumerConfig::setConnections);
        ofNullable(dubboConf.getConsumerLayer())
                .ifPresent(consumerConfig::setLayer);
        ofNullable(dubboConf.getConsumerOnconnect())
                .ifPresent(consumerConfig::setOnconnect);
        ofNullable(dubboConf.getConsumerOndisconnect())
                .ifPresent(consumerConfig::setOndisconnect);
        ofNullable(dubboConf.getConsumerCallbacks())
                .ifPresent(consumerConfig::setCallbacks);
        ofNullable(dubboConf.getConsumerScope())
                .ifPresent(consumerConfig::setScope);
        ofNullable(dubboConf.getConsumerTag())
                .ifPresent(consumerConfig::setTag);
        ofNullable(dubboConf.getConsumerAuth())
                .ifPresent(consumerConfig::setAuth);
        ofNullable(dubboConf.getConsumerCheck())
                .ifPresent(consumerConfig::setCheck);
        ofNullable(dubboConf.getConsumerInit())
                .ifPresent(consumerConfig::setInit);
        ofNullable(dubboConf.getConsumerGeneric())
                .ifPresent(consumerConfig::setGeneric);
        ofNullable(dubboConf.getConsumerLazy())
                .ifPresent(consumerConfig::setLazy);
        ofNullable(dubboConf.getConsumerReconnect())
                .ifPresent(consumerConfig::setReconnect);
        ofNullable(dubboConf.getConsumerSticky())
                .ifPresent(consumerConfig::setSticky);
        ofNullable(dubboConf.getConsumerProvidedBy())
                .ifPresent(consumerConfig::setProvidedBy);
        ofNullable(dubboConf.getConsumerRouter())
                .ifPresent(consumerConfig::setRouter);
        ofNullable(dubboConf.getConsumerReferAsync())
                .ifPresent(consumerConfig::setReferAsync);
        ofNullable(dubboConf.getConsumerClient())
                .ifPresent(consumerConfig::setClient);
        ofNullable(dubboConf.getConsumerThreadpool())
                .ifPresent(consumerConfig::setThreadpool);
        ofNullable(dubboConf.getConsumerCorethreads())
                .ifPresent(consumerConfig::setCorethreads);
        ofNullable(dubboConf.getConsumerThreads())
                .ifPresent(consumerConfig::setThreads);
        ofNullable(dubboConf.getConsumerQueues())
                .ifPresent(consumerConfig::setQueues);
        ofNullable(dubboConf.getConsumerShareconnections())
                .ifPresent(consumerConfig::setShareconnections);
        ofNullable(dubboConf.getConsumerUrlMergeProcessor())
                .ifPresent(consumerConfig::setUrlMergeProcessor);
        ofNullable(dubboConf.getConsumerReferThreadNum())
                .ifPresent(consumerConfig::setReferThreadNum);
        ofNullable(dubboConf.getConsumerReferBackground())
                .ifPresent(consumerConfig::setReferBackground);

        return consumerConfig;
    }

    /**
     * assert params
     *
     * @param conf
     */
    private static void assertConf(DubboConf conf) {
        if (conf == null) {
            throw new RuntimeException("conf can't be null");
        }
    }

}
