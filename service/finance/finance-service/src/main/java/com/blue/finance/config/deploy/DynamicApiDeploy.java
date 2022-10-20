package com.blue.finance.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * dynamic api deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "dynamic-api")
public class DynamicApiDeploy {

    private transient String path;

    private transient Long blockingMillis;

    public DynamicApiDeploy() {
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getBlockingMillis() {
        return blockingMillis;
    }

    public void setBlockingMillis(Long blockingMillis) {
        this.blockingMillis = blockingMillis;
    }

}
