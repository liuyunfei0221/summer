package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * web config
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "web")
public class WebDeploy {

    private Integer maxInMemorySize;

    public WebDeploy() {
    }

    public Integer getMaxInMemorySize() {
        return maxInMemorySize;
    }

    public void setMaxInMemorySize(Integer maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    @Override
    public String toString() {
        return "WebDeploy{" +
                "maxInMemorySize=" + maxInMemorySize +
                '}';
    }

}
