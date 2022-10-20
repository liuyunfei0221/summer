package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * verify template config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "verify-template")
public class VerifyTemplateDeploy {

    private Long cacheExpiresSecond;

    public Long getCacheExpiresSecond() {
        return cacheExpiresSecond;
    }

    public void setCacheExpiresSecond(Long cacheExpiresSecond) {
        this.cacheExpiresSecond = cacheExpiresSecond;
    }

    @Override
    public String toString() {
        return "VerifyTemplateDeploy{" +
                "cacheExpiresSecond=" + cacheExpiresSecond +
                '}';
    }

}
