package com.blue.base.config.universal;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;

import java.util.List;

import static com.blue.basic.common.base.CommonFunctions.HTTP_MESSAGE_CONVERTERS;


/**
 * web config
 *
 * @author liuyunfei
 */
@Configuration
public class WebConfig implements WebFluxConfigurer {

    @Bean
    HttpMessageConverters httpMessageConverters() {
        return HTTP_MESSAGE_CONVERTERS;
    }

    @Bean
    List<HttpMessageReader<?>> httpMessageReaders(ServerCodecConfigurer configurer) {
        return configurer.getReaders();
    }

    @Bean
    List<HttpMessageWriter<?>> httpMessageWriters(ServerCodecConfigurer configurer) {
        return configurer.getWriters();
    }

}
