package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * response deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "response")
public class ResponseDeploy {

    private List<String> existenceBodyTypes;

    public ResponseDeploy() {
    }

    public List<String> getExistenceBodyTypes() {
        return existenceBodyTypes;
    }

    public void setExistenceBodyTypes(List<String> existenceBodyTypes) {
        this.existenceBodyTypes = existenceBodyTypes;
    }

    @Override
    public String toString() {
        return "ResponseDeploy{" +
                "existenceBodyTypes=" + existenceBodyTypes +
                '}';
    }

}
