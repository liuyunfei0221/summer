package com.blue.auth.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * control config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "control")
public class ControlDeploy {

    private int allow;

    private long intervalMillis;

    public ControlDeploy() {
    }

    public int getAllow() {
        return allow;
    }

    public void setAllow(int allow) {
        this.allow = allow;
    }

    public long getIntervalMillis() {
        return intervalMillis;
    }

    public void setIntervalMillis(long intervalMillis) {
        this.intervalMillis = intervalMillis;
    }

    @Override
    public String toString() {
        return "ControlDeploy{" +
                "allow=" + allow +
                ", intervalMillis=" + intervalMillis +
                '}';
    }

}
