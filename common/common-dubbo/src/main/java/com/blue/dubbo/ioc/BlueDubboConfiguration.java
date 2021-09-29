package com.blue.dubbo.ioc;

import com.blue.dubbo.api.conf.DubboConf;
import org.apache.dubbo.config.*;
import org.springframework.context.annotation.Bean;

import static com.blue.dubbo.api.generator.BlueDubboGenerator.*;

/**
 * dubbo components configuration
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
        return generateMetadataReportConfig(dubboConf);
    }

    @Bean
    RegistryConfig registryConfig() {
        return generateRegistryConfig(dubboConf);
    }

    @Bean
    MonitorConfig monitorConfig() {
        return generateMonitorConfig(dubboConf);
    }

    @Bean
    ApplicationConfig applicationConfig() {
        return generateApplicationConfig(dubboConf);
    }

    @Bean
    ProtocolConfig protocolConfig() {
        return generateProtocolConfig(dubboConf);
    }

    @Bean
    ProviderConfig providerConfig() {
        return generateProviderConfig(dubboConf);
    }

    @Bean
    ConsumerConfig consumerConfig() {
        return generateConsumerConfig(dubboConf);
    }

}
