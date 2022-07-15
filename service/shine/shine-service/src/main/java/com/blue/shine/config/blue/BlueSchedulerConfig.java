package com.blue.shine.config.blue;

import com.blue.basic.component.scheduler.api.conf.SchedulerConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * scheduler config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "scheduler")
public class BlueSchedulerConfig extends SchedulerConfParams {
}
