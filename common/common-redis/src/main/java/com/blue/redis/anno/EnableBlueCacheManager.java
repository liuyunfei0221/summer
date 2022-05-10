package com.blue.redis.anno;

import com.blue.redis.ioc.BlueCacheManagerConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * enable trans
 *
 * @author liuyunfei
 */
@Target(TYPE)
@Retention(RUNTIME)
@Configuration
@Import(BlueCacheManagerConfiguration.class)
public @interface EnableBlueCacheManager {
}
