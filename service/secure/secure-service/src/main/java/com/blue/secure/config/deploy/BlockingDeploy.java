package com.blue.secure.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 业务阻塞超时配置参数类
 *
 * @author DarkBlue
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
