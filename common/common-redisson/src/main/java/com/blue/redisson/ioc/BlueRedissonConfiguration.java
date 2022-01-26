package com.blue.redisson.ioc;

import com.blue.redisson.api.conf.RedissonConf;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.util.Logger;

import static com.blue.redisson.api.generator.BlueRedissonGenerator.generateRedissonClient;
import static reactor.util.Loggers.getLogger;

/**
 * redisson configuration
 *
 * @author liuyunfei
 * @date 2021/8/24
 * @apiNote
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "SpringJavaInjectionPointsAutowiringInspection", "SpringFacetCodeInspection"})
@ConditionalOnBean(value = RedissonConf.class)
@Configuration
public class BlueRedissonConfiguration {

    private static final Logger LOGGER = getLogger(BlueRedissonConfiguration.class);

    @Bean
    RedissonClient redissonClient(RedissonConf redissonConf) {
        LOGGER.info("RedissonClient redissonClient(RedissonConf redissonConf), redissonConf = {}", redissonConf);
        return generateRedissonClient(redissonConf);
    }

}
