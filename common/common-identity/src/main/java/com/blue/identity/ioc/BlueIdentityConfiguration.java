package com.blue.identity.ioc;

import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.component.BlueIdentityProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * conf snowflake bean
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {IdentityConf.class})
@AutoConfiguration
public class BlueIdentityConfiguration {

    private static final Logger LOGGER = getLogger(BlueIdentityConfiguration.class);

    @Bean
    BlueIdentityProcessor blueIdentityProcessor(IdentityConf identityConf) {
        LOGGER.info("BlueIdentityProcessor blueIdentityProcessor(IdentityConf identityConf), identityConf = {}", identityConf);
        return new BlueIdentityProcessor(identityConf);
    }

}
