package com.blue.base.anno;

import org.springframework.beans.factory.support.BeanNameGenerator;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static org.springframework.context.annotation.FilterType.CUSTOM;

/**
 * Custom startup items for summer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "SpellCheckingInspection"})
@Target(TYPE)
@Retention(RUNTIME)
@Documented
@Inherited
@SpringBootConfiguration
@EnableAutoConfiguration
@ComponentScan(excludeFilters = {@ComponentScan.Filter(type = CUSTOM, classes = {TypeExcludeFilter.class}),
        @ComponentScan.Filter(type = CUSTOM, classes = {AutoConfigurationExcludeFilter.class})})
public @interface SummerSpringBootApplication {

    @AliasFor(annotation = EnableAutoConfiguration.class)
    Class<?>[] exclude() default {};

    @AliasFor(annotation = EnableAutoConfiguration.class)
    String[] excludeName() default {
            "org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration",
            "org.springframework.boot.autoconfigure.jdbc.JdbcTemplateAutoConfiguration",
            "org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration",
            "org.apache.shardingsphere.shardingjdbc.spring.boot.SpringBootConfiguration",
            "org.springframework.boot.autoconfigure.hazelcast.HazelcastAutoConfiguration",


            "org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.redis.RedisReactiveAutoConfiguration",


            "org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.mongo.MongoDatabaseFactoryConfiguratio",
            "org.springframework.boot.autoconfigure.data.mongo.MongoReactiveDataAutoConfiguration",


            "org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration",
            "org.springframework.boot.autoconfigure.data.elasticsearch.ReactiveElasticsearchRestClientAutoConfiguration",


            "org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration",


            "org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JAutoConfiguration",
            "org.springframework.cloud.circuitbreaker.resilience4j.ReactiveResilience4JAutoConfiguration",
            "io.github.resilience4j.bulkhead.autoconfigure.BulkheadAutoConfiguration",
            "io.github.resilience4j.bulkhead.autoconfigure.BulkheadMetricsAutoConfiguration",
            "io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerAutoConfiguration",
            "io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerMetricsAutoConfiguration",
            "io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakerStreamEventsAutoConfiguration",
            "io.github.resilience4j.circuitbreaker.autoconfigure.CircuitBreakersHealthIndicatorAutoConfiguration",
            "io.github.resilience4j.ratelimiter.autoconfigure.RateLimiterAutoConfiguratio",
            "io.github.resilience4j.ratelimiter.autoconfigure.RateLimiterMetricsAutoConfiguration",
            "io.github.resilience4j.ratelimiter.autoconfigure.RateLimitersHealthIndicatorAutoConfiguration",
            "io.github.resilience4j.retry.autoconfigure.RetryAutoConfiguration",
            "io.github.resilience4j.retry.autoconfigure.RetryMetricsAutoConfiguration",
            "io.github.resilience4j.timelimiter.autoconfigure.TimeLimiterAutoConfiguration",
            "io.github.resilience4j.timelimiter.autoconfigure.TimeLimiterMetricsAutoConfiguratio"
    };

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackages")
    String[] scanBasePackages() default {"com.blue"};

    @AliasFor(annotation = ComponentScan.class, attribute = "basePackageClasses")
    Class<?>[] scanBasePackageClasses() default {};

    @AliasFor(annotation = ComponentScan.class, attribute = "nameGenerator")
    Class<? extends BeanNameGenerator> nameGenerator() default BeanNameGenerator.class;

    @AliasFor(annotation = Configuration.class)
    boolean proxyBeanMethods() default true;

}
