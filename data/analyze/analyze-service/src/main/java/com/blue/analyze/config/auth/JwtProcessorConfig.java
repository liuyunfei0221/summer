package com.blue.analyze.config.auth;

import com.blue.analyze.config.deploy.JwtDeploy;
import com.blue.auth.api.conf.auth.MemberJwtConf;
import com.blue.auth.api.conf.auth.MemberJwtConfParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * jwt processor config
 *
 * @author DarkBlue
 */
@Configuration
public class JwtProcessorConfig {

    private static final Logger LOGGER = getLogger(JwtProcessorConfig.class);

    private final JwtDeploy jwtDeploy;

    public JwtProcessorConfig(JwtDeploy jwtDeploy) {
        this.jwtDeploy = jwtDeploy;
    }

    @Bean
    MemberJwtConf memberJwtConf() {
        LOGGER.info("jwtDeploy = {}", jwtDeploy);
        return new MemberJwtConfParams(jwtDeploy.getGlobalMaxExpireMillis(), jwtDeploy.getGlobalMinExpireMillis(),
                jwtDeploy.getSignKey(), jwtDeploy.getGammaSecrets());
    }

}
