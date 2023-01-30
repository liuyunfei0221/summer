package com.blue.identity.ioc;

import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;

import static com.blue.identity.api.generator.BlueIdentityProcessorGenerator.generateBlueIdentityProcessor;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * conf snowflake bean
 *
 * @author liuyunfei
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@ConditionalOnBean(value = {IdentityConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueIdentityConfiguration {

    @Bean
    BlueIdentityProcessor blueIdentityProcessor(IdentityConf identityConf) {
        return generateBlueIdentityProcessor(identityConf);
    }

}
