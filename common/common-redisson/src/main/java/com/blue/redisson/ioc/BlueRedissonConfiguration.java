package com.blue.redisson.ioc;

import com.blue.redisson.api.conf.RedissonConf;
import com.blue.redisson.component.SynchronizedProcessor;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import reactor.util.Logger;

import static com.blue.redisson.api.generator.BlueRedissonGenerator.generateRedissonClient;
import static com.blue.redisson.api.generator.BlueRedissonGenerator.generateSynchronizedProcessor;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.util.Loggers.getLogger;

/**
 * redisson configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode", "SpringJavaInjectionPointsAutowiringInspection"})
@ConditionalOnBean(value = RedissonConf.class)
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
public class BlueRedissonConfiguration {

    private static final Logger LOGGER = getLogger(BlueRedissonConfiguration.class);

    @Bean
    RedissonClient redissonClient(RedissonConf redissonConf) {
        LOGGER.info("redissonConf = {}", redissonConf);
        return generateRedissonClient(redissonConf);
    }

    @Bean
    SynchronizedProcessor synchronizedProcessor(RedissonClient redissonClient, RedissonConf redissonConf) {
        LOGGER.info("redissonClient = {}, redissonConf = {}", redissonClient, redissonConf);
        return generateSynchronizedProcessor(redissonClient, redissonConf);
    }

}
