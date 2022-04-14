package com.blue.portal.config.blue;

import com.blue.base.component.reactrest.api.conf.ReactRestConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * reactive rest config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "reactrest")
public class BlueReactRestConfig extends ReactRestConfParams {
}
