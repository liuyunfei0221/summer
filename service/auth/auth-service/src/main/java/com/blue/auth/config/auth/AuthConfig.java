package com.blue.auth.config.auth;

import com.blue.auth.component.access.AccessBatchExpireProcessor;
import com.blue.auth.component.access.AccessInfoCache;
import com.blue.auth.config.deploy.AccessDeploy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
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

    private final AccessDeploy accessDeploy;

    public AuthConfig(AccessDeploy accessDeploy) {
        this.accessDeploy = accessDeploy;
    }

    @Bean
    AccessBatchExpireProcessor accessBatchExpireProcessor(StringRedisTemplate stringRedisTemplate) {
        LOGGER.info("accessDeploy = {}", accessDeploy);
        return new AccessBatchExpireProcessor(stringRedisTemplate, accessDeploy.getBatchExpireMaxPerHandle(),
                accessDeploy.getBatchExpireScheduledCorePoolSize(), accessDeploy.getBatchExpireScheduledInitialDelayMillis(),
                accessDeploy.getBatchExpireScheduledDelayMillis(), accessDeploy.getBatchExpireQueueCapacity());
    }

    @Bean
    AccessInfoCache accessInfoCache(ReactiveStringRedisTemplate reactiveStringRedisTemplate, AccessBatchExpireProcessor accessBatchExpireProcessor) {
        LOGGER.info("accessDeploy = {}", accessDeploy);
        return new AccessInfoCache(reactiveStringRedisTemplate, accessBatchExpireProcessor, accessDeploy.getRefresherCorePoolSize(),
                accessDeploy.getRefresherMaximumPoolSize(), accessDeploy.getRefresherKeepAliveSeconds(), accessDeploy.getRefresherBlockingQueueCapacity(),
                accessDeploy.getGlobalExpiresMillis(), accessDeploy.getLocalExpiresMillis(), accessDeploy.getLocalCacheCapacity());
    }

}
