package com.blue.portal.config.blue;

import com.blue.base.component.rest.api.conf.RestConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * reactive rest config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "rest")
public class BlueRestConfig extends RestConfParams {
}