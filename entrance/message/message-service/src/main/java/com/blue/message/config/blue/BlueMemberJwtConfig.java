package com.blue.message.config.blue;

import com.blue.auth.api.component.jwt.api.conf.MemberJwtConfParams;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * jwt config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "jwt")
public class BlueMemberJwtConfig extends MemberJwtConfParams {
}
