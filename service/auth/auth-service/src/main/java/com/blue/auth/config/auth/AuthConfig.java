package com.blue.auth.config.auth;

import com.blue.auth.api.component.jwt.api.conf.MemberJwtConf;
import com.blue.auth.api.component.jwt.api.conf.MemberJwtConfParams;
import com.blue.auth.component.access.AccessBatchExpireProcessor;
import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.deploy.AuthDeploy;
import com.blue.auth.event.producer.AccessExpireProducer;
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
                authDeploy.getSignKey(), authDeploy.getGammaSecrets());
    }

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Bean
    AccessInfoCache authInfoCache(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessExpireProducer accessExpireProducer) {
        LOGGER.info("jwtDeploy = {}", authDeploy);
        return new AccessInfoCache(reactiveStringRedisTemplate, accessExpireProducer, scheduler, authDeploy.getRefresherCorePoolSize(),
                authDeploy.getRefresherMaximumPoolSize(), authDeploy.getRefresherKeepAliveSeconds(), authDeploy.getRefresherBlockingQueueCapacity(),
                authDeploy.getGlobalMinExpireMillis(), authDeploy.getLocalExpireMillis(), authDeploy.getLocalCacheCapacity());
    }

    @Bean
    AccessBatchExpireProcessor authBatchExpireProcessor(StringRedisTemplate stringRedisTemplate) {
        LOGGER.info("jwtDeploy = {}", authDeploy);
        return new AccessBatchExpireProcessor(stringRedisTemplate,
                authDeploy.getBatchExpireMaxPerHandle(),
                authDeploy.getBatchExpireScheduledCorePoolSize(), authDeploy.getBatchExpireScheduledInitialDelayMillis(),
                authDeploy.getBatchExpireScheduledDelayMillis(), authDeploy.getBatchExpireQueueCapacity());
    }

}
