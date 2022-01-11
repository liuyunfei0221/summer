package com.blue.gateway.config.universal;

import com.blue.gateway.config.deploy.WebDeploy;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.HttpMessageReader;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.reactive.function.server.HandlerStrategies;

import java.util.List;


/**
 * web config
 *
 * @author DarkBlue
 */
@Configuration
public class WebConfig {

    @Bean
    HttpMessageConverters httpMessageConverters() {
        return new HttpMessageConverters(new MappingJackson2HttpMessageConverter());
    }

    @Bean
    List<HttpMessageReader<?>> httpMessageReaders(WebDeploy webDeploy) {
        return HandlerStrategies.builder()
                .codecs(serverCodecConfigurer ->
                        serverCodecConfigurer.defaultCodecs().maxInMemorySize(webDeploy.getMaxInMemorySize()))
                .build().messageReaders();
    }

}
