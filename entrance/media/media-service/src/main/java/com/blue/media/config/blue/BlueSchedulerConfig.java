package com.blue.media.config.blue;

import com.blue.base.component.scheduler.api.conf.SchedulerConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * scheduler config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "scheduler")
public class BlueSchedulerConfig extends SchedulerConfParams {
}
