package com.blue.member.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * validator type deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "validator-type")
public class ValidatorTypeDeploy {

    private String type;

    public ValidatorTypeDeploy() {
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
