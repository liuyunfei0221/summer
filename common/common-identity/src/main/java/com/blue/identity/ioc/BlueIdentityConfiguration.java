package com.blue.identity.ioc;

import com.blue.identity.api.conf.IdentityConf;
import com.blue.identity.common.BlueIdentityProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * conf snowflake bean
 *
 * @author DarkBlue
 */
@ConditionalOnBean(value = {IdentityConf.class})
@Configuration
public class BlueIdentityConfiguration {

    private static final Logger LOGGER = getLogger(BlueIdentityConfiguration.class);

    @Bean
    public BlueIdentityProcessor blueIdentityProcessor(IdentityConf identityConf) {
        LOGGER.info("BlueIdentityProcessor blueIdentityProcessor(IdentityConf identityConf), identityConf = {}", identityConf);
        return new BlueIdentityProcessor(identityConf);
    }

}
