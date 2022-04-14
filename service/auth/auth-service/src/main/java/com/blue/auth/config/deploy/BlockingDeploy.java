package com.blue.auth.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * business thread blocking time config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "blocking")
public class BlockingDeploy {

    private Long blockingMillis;

    public BlockingDeploy() {
    }

    public Long getBlockingMillis() {
        return blockingMillis;
    }

    public void setBlockingMillis(Long blockingMillis) {
        this.blockingMillis = blockingMillis;
    }

}
