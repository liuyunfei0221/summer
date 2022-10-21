package com.blue.base.config.universal;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.List;


/**
 * web config
 *
 * @author liuyunfei
 */
@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Bean
    List<HttpMessageReader<?>> httpMessageReaders(ServerCodecConfigurer configurer) {
        return configurer.getReaders();
    }

    @Bean
    List<HttpMessageWriter<?>> HttpMessageWriters(ServerCodecConfigurer configurer) {
        return configurer.getWriters();
    }

}
