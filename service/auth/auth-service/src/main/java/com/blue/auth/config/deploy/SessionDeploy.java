package com.blue.auth.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * session  deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "session")
public class SessionDeploy {

    private Integer allow;

    private Long intervalMillis;

    public SessionDeploy() {
    }

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }

    public Long getIntervalMillis() {
        return intervalMillis;
    }

    public void setIntervalMillis(Long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    @Override
    public String toString() {
        return "SessionDeploy{" +
                "allow=" + allow +
                ", intervalMillis=" + intervalMillis +
                '}';
    }

}
