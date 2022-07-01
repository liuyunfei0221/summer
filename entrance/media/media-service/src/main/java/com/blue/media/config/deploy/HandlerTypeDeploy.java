package com.blue.media.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * handler type deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "handlertype")
public class HandlerTypeDeploy {

    private String type;

    public HandlerTypeDeploy() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "HandlerTypeDeploy{" +
                "type='" + type + '\'' +
                '}';
    }

}
