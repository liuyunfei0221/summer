package com.blue.secure.config.auth;

import com.blue.secure.component.auth.AuthBatchExpireProcessor;
import com.blue.secure.component.auth.AuthInfoCacher;
import com.blue.secure.component.auth.api.MemberJwtConf;
import com.blue.secure.component.auth.api.MemberJwtConfParams;
import com.blue.secure.config.deploy.AuthDeploy;
import com.blue.secure.config.mq.producer.AuthExpireProducer;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * jwt处理器配置
 *
 * @author DarkBlue
 */
@Configuration
public class AuthConfig {

    private static final Logger LOGGER = getLogger(AuthConfig.class);

    private final AuthDeploy authDeploy;

    public AuthConfig(AuthDeploy authDeploy) {
        this.authDeploy = authDeploy;
    }

    @Bean
    MemberJwtConf memberJwtConf() {
        LOGGER.info("memberJwtConf = {}", authDeploy);
        return new MemberJwtConfParams(authDeploy.getGlobalMaxExpireMillis(), authDeploy.getGlobalMinExpireMillis(),
                authDeploy.getSignKey(), authDeploy.getGammaSecrets(), authDeploy.getIssuer(), authDeploy.getSubject(), authDeploy.getAudience());
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    AuthInfoCacher jwtCacher(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AuthExpireProducer authExpireProducer) {
        LOGGER.info("jwtDeploy = {}", authDeploy);
        return new AuthInfoCacher(reactiveStringRedisTemplate, authExpireProducer, authDeploy.getRefresherCorePoolSize(),
                authDeploy.getRefresherMaximumPoolSize(), authDeploy.getRefresherKeepAliveTime(), authDeploy.getRefresherBlockingQueueCapacity(),
                authDeploy.getGlobalMinExpireMillis(), authDeploy.getLocalExpireMillis(), authDeploy.getLocalCacherCapacity());
    }

    @Bean
    AuthBatchExpireProcessor authBatchExpireProcessor(StringRedisTemplate stringRedisTemplate) {
        LOGGER.info("jwtDeploy = {}", authDeploy);
        return new AuthBatchExpireProcessor(stringRedisTemplate,
                authDeploy.getBatchExpireMaxPerHandle(),
                authDeploy.getBatchExpireScheduledCorePoolSize(), authDeploy.getBatchExpireScheduledInitialDelayMillis(),
                authDeploy.getBatchExpireScheduledDelayMillis(), authDeploy.getBatchExpireQueueCapacity());
    }

}
