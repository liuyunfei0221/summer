package com.blue.auth.api.component.jwt.ioc;

import com.blue.auth.api.component.jwt.api.conf.MemberJwtConf;
import com.blue.auth.api.model.MemberPayload;
import com.blue.jwt.component.JwtProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.auth.api.component.jwt.api.generator.BlueMemberJwtProcessorGenerator.generate;

/**
 * member jwt configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {MemberJwtConf.class})
@AutoConfiguration
public class BlueMemberJwtConfiguration {

    @Bean
    JwtProcessor<MemberPayload> jwtProcessor(MemberJwtConf memberJwtConf) {
        return generate(memberJwtConf);
    }

}
