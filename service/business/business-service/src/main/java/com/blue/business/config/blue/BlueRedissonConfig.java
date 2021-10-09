package com.blue.business.config.blue;

import com.blue.redisson.api.conf.BaseRedissonConfParams;
import org.redisson.connection.balancer.LoadBalancer;
import org.redisson.connection.balancer.RoundRobinLoadBalancer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redisson config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "redisson")
public class BlueRedissonConfig extends BaseRedissonConfParams {

    @Override
    public LoadBalancer getLoadBalancer() {
        return new RoundRobinLoadBalancer();
    }

}
