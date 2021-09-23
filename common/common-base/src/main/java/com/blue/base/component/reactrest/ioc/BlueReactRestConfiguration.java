package com.blue.base.component.reactrest.ioc;

import com.blue.base.component.reactrest.api.conf.ReactRestConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static com.blue.base.component.reactrest.api.generator.BlueReactRestGenerator.createWebClient;

/**
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@ConditionalOnBean(value = {ReactRestConf.class})
@Configuration
public class BlueReactRestConfiguration {

    @Bean
    WebClient webClient(ReactRestConf reactRestConf) {
       return createWebClient(reactRestConf);
    }

}