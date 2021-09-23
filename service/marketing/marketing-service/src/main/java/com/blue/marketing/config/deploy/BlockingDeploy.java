package com.blue.marketing.config.deploy;

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

    private long millis;

    public BlockingDeploy() {
    }

    public long getMillis() {
        return millis;
    }

    public void setMillis(long millis) {
        this.millis = millis;
    }
}
