package com.blue.gateway.config.universal;

import com.blue.gateway.common.BlueRandomLoadBalancer;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClients;
import org.springframework.cloud.loadbalancer.core.ReactorLoadBalancer;
import org.springframework.cloud.loadbalancer.core.ServiceInstanceListSupplier;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

import static org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory.PROPERTY_NAME;

/**
 * ribbon配置类
 *
 * @author DarkBlue
 */
@LoadBalancerClients(defaultConfiguration = LoadBalanceConfig.class)
public class LoadBalanceConfig {

    @Bean
    ReactorLoadBalancer<ServiceInstance> reactorLoadBalancer(Environment environment,
                                                             LoadBalancerClientFactory loadBalancerClientFactory) {
        String name = environment.getProperty(PROPERTY_NAME);
        return new BlueRandomLoadBalancer(loadBalancerClientFactory.getLazyProvider(name, ServiceInstanceListSupplier.class));
    }

}
