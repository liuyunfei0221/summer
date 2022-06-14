package com.blue.registry.config.web;

import com.blue.registry.config.deploy.WebSocketDeploy;
import io.undertow.server.DefaultByteBufferPool;
import io.undertow.websockets.jsr.WebSocketDeploymentInfo;
import org.springframework.boot.web.embedded.undertow.UndertowServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * web config
 *
 * @author liuyunfei
 */
@Configuration
public class WebConfig {

    private final WebSocketDeploy webSocketDeploy;

    public WebConfig(WebSocketDeploy webSocketDeploy) {
        this.webSocketDeploy = webSocketDeploy;
    }

    @Bean
    WebServerFactoryCustomizer<UndertowServletWebServerFactory> publicWebServerFactoryCustomizer() {
        return factory -> factory.addDeploymentInfoCustomizers(deploymentInfo -> {
            WebSocketDeploymentInfo webSocketDeploymentInfo = new WebSocketDeploymentInfo();
            webSocketDeploymentInfo.setBuffers(new DefaultByteBufferPool(webSocketDeploy.getDirect(), webSocketDeploy.getBufferSize()));
            deploymentInfo.addServletContextAttribute("io.undertow.websockets.jsr.WebSocketDeploymentInfo", webSocketDeploymentInfo);
        });
    }

}
