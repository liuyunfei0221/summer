package com.blue.redis.ioc;

import com.blue.redis.api.conf.RedisConf;
import com.blue.redis.component.*;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import static com.blue.redis.api.generator.BlueBitMarkerGenerator.generateBitMarker;
import static com.blue.redis.api.generator.BlueRateLimiterGenerator.generateLeakyBucketRateLimiter;
import static com.blue.redis.api.generator.BlueRateLimiterGenerator.generateTokenBucketRateLimiter;
import static com.blue.redis.api.generator.BlueRedisGenerator.*;
import static com.blue.redis.api.generator.BlueValidatorGenerator.generateValidator;
import static com.blue.redis.api.generator.BlueValueMarkerGenerator.generateValueMarker;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.util.Loggers.getLogger;

/**
 * redis configuration
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AlibabaRemoveCommentedCode"})
@ConditionalOnBean(value = {RedisConf.class})
@AutoConfiguration
@Order(HIGHEST_PRECEDENCE)
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
        return generateConnectionFactory(redisConf, redisConfiguration, generateLettuceClientConfiguration(redisConf, generateGenericObjectPoolConfig(redisConf), generateClientOptions(redisConf)));
    }

    @Bean
    RedisTemplate<Object, Object> redisTemplate(RedisConf redisConf, LettuceConnectionFactory lettuceConnectionFactory) {
        return generateObjectRedisTemplate(redisConf, lettuceConnectionFactory);
    }

    @Bean
    StringRedisTemplate stringRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {
        return generateStringRedisTemplate(lettuceConnectionFactory);
    }

    @Bean
    ReactiveStringRedisTemplate reactiveStringRedisTemplate(RedisConf redisConf, LettuceConnectionFactory lettuceConnectionFactory) {
        return generateReactiveStringRedisTemplate(redisConf, lettuceConnectionFactory);
    }

    @Bean
    BlueBitMarker blueBitMarker(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return generateBitMarker(reactiveStringRedisTemplate, scheduler);
    }

    @Bean
    BlueLeakyBucketRateLimiter blueLeakyBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return generateLeakyBucketRateLimiter(reactiveStringRedisTemplate, scheduler);
    }

    @Bean
    BlueTokenBucketRateLimiter blueTokenBucketRateLimiter(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return generateTokenBucketRateLimiter(reactiveStringRedisTemplate, scheduler);
    }

    @Bean
    BlueValidator blueValidator(ReactiveStringRedisTemplate reactiveStringRedisTemplate, Scheduler scheduler) {
        return generateValidator(reactiveStringRedisTemplate, scheduler);
    }

    @Bean
    BlueValueMarker blueValueMarker(StringRedisTemplate stringRedisTemplate) {
        return generateValueMarker(stringRedisTemplate);
    }

}
