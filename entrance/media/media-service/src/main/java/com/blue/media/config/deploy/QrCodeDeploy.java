package com.blue.media.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * qr code config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "qr-code")
public class QrCodeDeploy {

    private Long cacheExpiresSecond;

    public Long getCacheExpiresSecond() {
        return cacheExpiresSecond;
    }

    public void setCacheExpiresSecond(Long cacheExpiresSecond) {
        this.cacheExpiresSecond = cacheExpiresSecond;
    }

    @Override
    public String toString() {
        return "QrCodeDeploy{" +
                "cacheExpiresSecond=" + cacheExpiresSecond +
                '}';
    }

}
