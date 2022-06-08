package com.blue.member.config.blue;

import com.blue.base.component.encoder.api.conf.EncoderConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * string encoder config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "encode")
public class BlueEncoderConfig extends EncoderConfParams {
}
