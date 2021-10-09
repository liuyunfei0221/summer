package com.blue.base.config.blue;

import com.blue.database.api.conf.TransConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * expression trans config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "trans")
public class BlueTransConfig extends TransConfParams {
}
