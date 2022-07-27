package com.blue.auth.config.auth;

import com.blue.auth.api.component.jwt.api.conf.MemberJwtConf;
import com.blue.auth.api.component.jwt.api.conf.MemberJwtConfParams;
import com.blue.auth.component.access.AccessBatchExpireProcessor;
import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.deploy.AuthDeploy;
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
 * @author liuyunfei
 */
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
        LOGGER.info("authDeploy = {}", authDeploy);
        return new MemberJwtConfParams(authDeploy.getGlobalMaxExpiresMillis(), authDeploy.getGlobalMinExpiresMillis(), authDeploy.getGlobalRefreshExpiresMillis(),
                authDeploy.getSignKey(), authDeploy.getGammaSecrets());
    }

    @Bean
    AccessBatchExpireProcessor authBatchExpireProcessor(StringRedisTemplate stringRedisTemplate) {
        LOGGER.info("authDeploy = {}", authDeploy);
        return new AccessBatchExpireProcessor(stringRedisTemplate,
                authDeploy.getBatchExpireMaxPerHandle(),
                authDeploy.getBatchExpireScheduledCorePoolSize(), authDeploy.getBatchExpireScheduledInitialDelayMillis(),
                authDeploy.getBatchExpireScheduledDelayMillis(), authDeploy.getBatchExpireQueueCapacity());
    }

    @Bean
    AccessInfoCache authInfoCache(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessBatchExpireProcessor authBatchExpireProcessor) {
        LOGGER.info("authDeploy = {}", authDeploy);
        return new AccessInfoCache(reactiveStringRedisTemplate, authBatchExpireProcessor, scheduler, authDeploy.getRefresherCorePoolSize(),
                authDeploy.getRefresherMaximumPoolSize(), authDeploy.getRefresherKeepAliveSeconds(), authDeploy.getRefresherBlockingQueueCapacity(),
                authDeploy.getGlobalMinExpiresMillis(), authDeploy.getLocalExpiresMillis(), authDeploy.getMillisLeftToHandleExpire(), authDeploy.getLocalCacheCapacity());
    }

}
