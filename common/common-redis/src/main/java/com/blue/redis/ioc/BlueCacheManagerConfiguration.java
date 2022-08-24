package com.blue.redis.ioc;

import com.blue.redis.api.conf.RedisConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import static com.blue.redis.api.generator.BlueRedisGenerator.generateCacheManager;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * cache manager configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {RedisConf.class})
@Order(HIGHEST_PRECEDENCE)
public class BlueCacheManagerConfiguration {

    @Bean
    CacheManager cacheManager(RedisConf redisConf, LettuceConnectionFactory lettuceConnectionFactory) {
        return generateCacheManager(redisConf, lettuceConnectionFactory);
    }

}
