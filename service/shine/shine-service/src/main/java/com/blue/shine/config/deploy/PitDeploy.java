package com.blue.shine.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * pit config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "pit")
public class PitDeploy {

    private String time;

    public PitDeploy() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "PitDeploy{" +
                "time='" + time + '\'' +
                '}';
    }

}
