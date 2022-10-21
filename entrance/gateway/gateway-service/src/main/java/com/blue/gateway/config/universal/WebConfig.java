package com.blue.gateway.config.universal;

import com.blue.gateway.config.deploy.WebDeploy;
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

    private final WebDeploy webDeploy;

    public WebConfig(WebDeploy webDeploy) {
        this.webDeploy = webDeploy;
    }

    @Override
    public void configureHttpMessageCodecs(ServerCodecConfigurer configurer) {
        configurer.defaultCodecs().maxInMemorySize(webDeploy.getMaxInMemorySize());
        WebFluxConfigurer.super.configureHttpMessageCodecs(configurer);
    }

    @Bean
    List<HttpMessageReader<?>> httpMessageReaders(ServerCodecConfigurer configurer) {
        return configurer.getReaders();
    }

    @Bean
    List<HttpMessageWriter<?>> HttpMessageWriters(ServerCodecConfigurer configurer) {
        return configurer.getWriters();
    }

}
