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

    private Long cacheExpireSeconds;

    public Long getCacheExpireSeconds() {
        return cacheExpireSeconds;
    }

    public void setCacheExpireSeconds(Long cacheExpireSeconds) {
        this.cacheExpireSeconds = cacheExpireSeconds;
    }

    @Override
    public String toString() {
        return "MemberDeploy{" +
                "cacheExpireSeconds=" + cacheExpireSeconds +
                '}';
    }

}
