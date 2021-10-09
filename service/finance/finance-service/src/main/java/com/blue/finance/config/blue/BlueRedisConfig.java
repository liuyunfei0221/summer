package com.blue.finance.config.blue;

import com.blue.redis.api.conf.RedisConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * redis config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "cache")
public class BlueRedisConfig extends RedisConfParams {
}
