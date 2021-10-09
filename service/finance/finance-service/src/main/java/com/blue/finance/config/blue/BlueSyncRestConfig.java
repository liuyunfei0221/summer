package com.blue.finance.config.blue;

import com.blue.base.component.syncrest.api.conf.SyncRestConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * sync rest config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "syncrest")
public class BlueSyncRestConfig extends SyncRestConfParams {
}
