package com.blue.media.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * encrypt config
 *
 * @author liuyunfei
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
