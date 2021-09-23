package com.blue.dubbo.ioc;

import com.blue.dubbo.api.conf.DubboConf;
import org.apache.dubbo.config.*;
import org.springframework.context.annotation.Bean;

import static com.blue.dubbo.api.generator.BlueDubboGenerator.*;

/**
 * dubbo配置
 *
 * @author liuyunfei
 * @date 2021/8/24
 * @apiNote
 */
public class BlueDubboConfiguration {

    private final DubboConf dubboConf;

    public BlueDubboConfiguration(DubboConf dubboConf) {
        this.dubboConf = dubboConf;
    }

    @Bean
    MetadataReportConfig metadataReportConfig() {
        return createMetadataReportConfig(dubboConf);
    }

    @Bean
    RegistryConfig registryConfig() {
        return createRegistryConfig(dubboConf);
    }

    @Bean
    MonitorConfig monitorConfig() {
        return createMonitorConfig(dubboConf);
    }

    @Bean
    ApplicationConfig applicationConfig() {
        return createApplicationConfig(dubboConf);
    }

    @Bean
    ProtocolConfig protocolConfig() {
        return createProtocolConfig(dubboConf);
    }

    @Bean
    ProviderConfig providerConfig() {
        return createProviderConfig(dubboConf);
    }

    @Bean
    ConsumerConfig consumerConfig() {
        return createConsumerConfig(dubboConf);
    }

}
