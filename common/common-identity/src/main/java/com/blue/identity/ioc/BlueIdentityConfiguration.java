package com.blue.identity.ioc;

import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import static com.blue.identity.api.generator.BlueIdentityProcessorGenerator.generateBlueIdentityProcessor;

/**
 * conf snowflake bean
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {IdentityConf.class})
@AutoConfiguration
public class BlueIdentityConfiguration {

    @Bean
    BlueIdentityProcessor blueIdentityProcessor(IdentityConf identityConf) {
        return generateBlueIdentityProcessor(identityConf);
    }

}
