package com.blue.risk.config.auth;

import com.blue.auth.api.component.jwt.api.conf.MemberJwtConf;
import com.blue.auth.api.component.jwt.api.conf.MemberJwtConfParams;
import com.blue.risk.config.deploy.JwtDeploy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * jwt config
 *
 * @author liuyunfei
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
