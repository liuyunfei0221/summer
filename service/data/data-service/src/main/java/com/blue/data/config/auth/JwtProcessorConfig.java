package com.blue.data.config.auth;

import com.blue.data.config.deploy.JwtDeploy;
import com.blue.secure.component.auth.api.MemberJwtConf;
import com.blue.secure.component.auth.api.MemberJwtConfParams;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * jwt prosessor config
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
                jwtDeploy.getSignKey(), jwtDeploy.getGammaSecrets(), jwtDeploy.getIssuer(), jwtDeploy.getSubject(), jwtDeploy.getAudience());
    }

}
