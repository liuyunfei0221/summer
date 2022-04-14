package com.blue.finance.config.blue;

import com.blue.curator.api.conf.DistributedLockConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * curator config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "curator")
public class BlueCuratorConfig extends DistributedLockConfParams {
}
