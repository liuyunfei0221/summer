package com.blue.finance.config.blue;

import com.blue.curator.api.conf.DistributedLockConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * zk分布锁配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "curator")
public class BlueCuratorConfig extends DistributedLockConfParams {
}
