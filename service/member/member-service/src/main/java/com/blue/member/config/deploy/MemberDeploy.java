package com.blue.member.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * card config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "member")
public class MemberDeploy {

    private Long cacheExpiresSecond;

    public Long getCacheExpiresSecond() {
        return cacheExpiresSecond;
    }

    public void setCacheExpiresSecond(Long cacheExpiresSecond) {
        this.cacheExpiresSecond = cacheExpiresSecond;
    }

    @Override
    public String toString() {
        return "MemberDeploy{" +
                "cacheExpiresSecond=" + cacheExpiresSecond +
                '}';
    }

}
