package com.blue.message.config.universal;

import com.blue.message.config.deploy.CorsDeploy;
import org.slf4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.time.Duration;
import java.util.List;

import static java.time.temporal.ChronoUnit.SECONDS;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;

/**
 * cors config
 *
 * @author liuyunfei
 */
@Configuration
public class CorsConfig {

    private static final Logger LOGGER = getLogger(CorsConfig.class);

    private final CorsDeploy corsDeploy;

    public CorsConfig(CorsDeploy corsDeploy) {
        this.corsDeploy = corsDeploy;
    }

    public static final List<String> DEFAULT_ALLOWED_ORIGINS = of("*");

    public static final List<String> DEFAULT_ALLOWED_METHODS = of("*");

    public static final List<String> DEFAULT_ALLOWED_HEADERS = of("*");

    public static final List<String> DEFAULT_EXPOSED_HEADERS = of();

    private static final boolean DEFAULT_ALLOWED_CREDENTIALS = true;

    private static final long DEFAULT_MAX_AGE_SECONDS = 1728000L;

    private static final List<String> DEFAULT_ALLOWED_ORIGIN_PATTERNS = of("/**");


    @Bean
    @Order(HIGHEST_PRECEDENCE)
    CorsWebFilter corsWebFilter() {

        LOGGER.info("corsWebFilter(), corsDeploy = {}", corsDeploy);

        CorsConfiguration corsConfiguration = new CorsConfiguration();

        ofNullable(corsDeploy.getAllowedOrigins())
                .orElse(DEFAULT_ALLOWED_ORIGINS)
                .forEach(corsConfiguration::addAllowedOrigin);

        ofNullable(corsDeploy.getAllowedMethods())
                .orElse(DEFAULT_ALLOWED_METHODS)
                .forEach(corsConfiguration::addAllowedMethod);

        ofNullable(corsDeploy.getAllowedHeaders())
                .orElse(DEFAULT_ALLOWED_HEADERS)
                .forEach(corsConfiguration::addAllowedHeader);

        ofNullable(corsDeploy.getExposedHeaders())
                .orElse(DEFAULT_EXPOSED_HEADERS)
                .forEach(corsConfiguration::addExposedHeader);

        corsConfiguration.setAllowCredentials(
                ofNullable(corsDeploy.getAllowCredentials())
                        .orElse(DEFAULT_ALLOWED_CREDENTIALS));

        corsConfiguration.setMaxAge(
                Duration.of(ofNullable(corsDeploy.getMaxAgeSeconds())
                        .orElse(DEFAULT_MAX_AGE_SECONDS), SECONDS));


        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();

        ofNullable(corsDeploy.getAllowedOriginPatterns())
                .orElse(DEFAULT_ALLOWED_ORIGIN_PATTERNS)
                .forEach(p -> urlBasedCorsConfigurationSource.registerCorsConfiguration(p, corsConfiguration));

        return new CorsWebFilter(urlBasedCorsConfigurationSource);
    }
}
