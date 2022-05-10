package com.blue.redis.ioc;

import com.blue.redis.api.conf.RedisConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;

import static com.blue.redis.api.generator.BlueRedisGenerator.generateCacheManager;

/**
 * cache manager configuration
 *
 * @author liuyunfei
 */
@ConditionalOnBean(value = {RedisConf.class})
public class BlueCacheManagerConfiguration {

    @Bean
    public CacheManager cacheManager(RedisConf redisConf, LettuceConnectionFactory lettuceConnectionFactory) {
        return generateCacheManager(redisConf, lettuceConnectionFactory);
    }

}
