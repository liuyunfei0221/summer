package com.blue.secure.config.auth;

import com.blue.secure.api.conf.auth.MemberJwtConf;
import com.blue.secure.api.conf.auth.MemberJwtConfParams;
import com.blue.secure.component.auth.AuthBatchExpireProcessor;
import com.blue.secure.component.auth.AuthInfoCache;
import com.blue.secure.config.deploy.AuthDeploy;
import com.blue.secure.event.producer.AuthExpireProducer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * jwt config
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Configuration
public class AuthConfig {

    private static final Logger LOGGER = getLogger(AuthConfig.class);

    private final Scheduler scheduler;

    private final AuthDeploy authDeploy;

    public AuthConfig(Scheduler scheduler, AuthDeploy authDeploy) {
        this.scheduler = scheduler;
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
    AuthInfoCache authInfoCache(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AuthExpireProducer authExpireProducer) {
        LOGGER.info("jwtDeploy = {}", authDeploy);
        return new AuthInfoCache(reactiveStringRedisTemplate, authExpireProducer, scheduler, authDeploy.getRefresherCorePoolSize(),
                authDeploy.getRefresherMaximumPoolSize(), authDeploy.getRefresherKeepAliveSeconds(), authDeploy.getRefresherBlockingQueueCapacity(),
                authDeploy.getGlobalMinExpireMillis(), authDeploy.getLocalExpireMillis(), authDeploy.getLocalCacheCapacity());
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
