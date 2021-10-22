package com.blue.captcha.config.universal;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;


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

}
