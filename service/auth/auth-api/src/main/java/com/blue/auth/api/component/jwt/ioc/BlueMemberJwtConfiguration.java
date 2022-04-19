package com.blue.auth.api.component.jwt.ioc;

import com.blue.auth.api.component.jwt.api.conf.MemberJwtConf;
import com.blue.auth.api.model.MemberPayload;
import com.blue.jwt.common.JwtProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.blue.auth.api.component.jwt.api.generator.BlueMemberJwtProcessorGenerator.generate;

/**
 * member jwt configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {MemberJwtConf.class})
@Configuration
public class BlueMemberJwtConfiguration {

    @Bean
    public JwtProcessor<MemberPayload> jwtProcessor(MemberJwtConf memberJwtConf) {
        return generate(memberJwtConf);
    }

}
