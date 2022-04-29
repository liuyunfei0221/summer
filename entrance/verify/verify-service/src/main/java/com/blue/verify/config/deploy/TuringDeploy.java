package com.blue.verify.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * turing deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "turing")
public class TuringDeploy {

    private Long expire;

    public TuringDeploy() {
    }

    public Long getExpire() {
        return expire;
    }

    public void setExpire(Long expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "TuringDeploy{" +
                "expire=" + expire +
                '}';
    }

}
