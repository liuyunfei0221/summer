package com.blue.base.component.syncrest.ioc;

import com.blue.base.component.syncrest.api.conf.SyncRestConf;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import static com.blue.base.component.syncrest.api.generator.BlueSyncRestGenerator.createRestTemplate;

/**
 * rest configuration
 *
 * @author liuyunfei
 * @date 2021/9/9
 * @apiNote
 */
@ConditionalOnBean(value = {SyncRestConf.class})
@Configuration
public class BlueSyncRestConfiguration {

    @Bean
    public RestTemplate restTemplate(SyncRestConf syncRestConf) {
        return createRestTemplate(syncRestConf);
    }

}