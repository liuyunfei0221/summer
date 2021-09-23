package com.blue.redis.ioc;

import com.blue.redis.api.conf.RedisConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.util.Logger;

import static com.blue.redis.api.generator.BlueRedisGenerator.*;
import static reactor.util.Loggers.getLogger;

/**
 * redis配置类
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "SpringFacetCodeInspection", "SpringJavaInjectionPointsAutowiringInspection"})
@ConditionalOnBean(value = {RedisConf.class})
@Configuration
public class BlueRedisConfiguration {

    private static final Logger LOGGER = getLogger(BlueRedisConfiguration.class);

    @Bean
    RedisConfiguration redisConfiguration(RedisConf redisConf) {
        LOGGER.info("RedisConfiguration redisConfiguration(), redisConf = {}", redisConf);
        return generateConfiguration(redisConf);
    }

    @Bean
    LettuceConnectionFactory lettuceConnectionFactory(RedisConf redisConf, RedisConfiguration redisConfiguration) {
        LOGGER.info("LettuceConnectionFactory lettuceConnectionFactory(RedisConfiguration redisConfiguration), redisConf = {}", redisConf);
        return generateLettuceConnectionFactory(redisConf, redisConfiguration, generateLettuceClientConfiguration(redisConf, generateGenericObjectPoolConfig(redisConf), generateClientOptions(redisConf)));
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return generateRedisTemplate(lettuceConnectionFactory);
    }

    @Bean
    StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return generateStringRedisTemplate(lettuceConnectionFactory);
    }

    @Bean
    ReactiveStringRedisTemplate reactiveStringRedisTemplate(RedisConf redisConf, LettuceConnectionFactory lettuceConnectionFactory) {
        return generateReactiveStringRedisTemplate(redisConf, lettuceConnectionFactory);
    }

}
