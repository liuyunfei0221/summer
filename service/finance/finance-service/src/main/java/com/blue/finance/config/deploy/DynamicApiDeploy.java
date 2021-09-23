package com.blue.finance.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 动态端点配置项
 *
 * @author DarkBlue
 */
@SuppressWarnings("SpellCheckingInspection")
@Component
@ConfigurationProperties(prefix = "dynamicapi")
public class DynamicApiDeploy {

    private String path;

    private Long blockingMillis;

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
