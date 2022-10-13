package com.blue.media.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * message template config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "mtemplate")
public class MessageTemplateDeploy {

    private Long cacheExpiresSecond;

    public Long getCacheExpiresSecond() {
        return cacheExpiresSecond;
    }

    public void setCacheExpiresSecond(Long cacheExpiresSecond) {
        this.cacheExpiresSecond = cacheExpiresSecond;
    }

    @Override
    public String toString() {
        return "MessageTemplateDeploy{" +
                "cacheExpiresSecond=" + cacheExpiresSecond +
                '}';
    }

}
