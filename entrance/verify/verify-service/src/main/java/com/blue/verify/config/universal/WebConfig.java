package com.blue.verify.config.universal;

import com.blue.verify.config.deploy.WebDeploy;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.web.reactive.config.WebFluxConfigurer;


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

}
