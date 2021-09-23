package com.blue.file.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 加密数据过期时间配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "encrypt")
public class EncryptDeploy {

    private long expire;

    public EncryptDeploy() {
    }

    public long getExpire() {
        return expire;
    }

    public void setExpire(long expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "BlueEncryptDeploy{" +
                "expire=" + expire +
                '}';
    }

}
