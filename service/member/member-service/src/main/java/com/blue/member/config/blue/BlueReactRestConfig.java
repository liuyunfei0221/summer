package com.blue.member.config.blue;

import com.blue.base.component.reactrest.api.conf.ReactRestConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * reactive rest config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "reactrest")
public class BlueReactRestConfig extends ReactRestConfParams {
}
