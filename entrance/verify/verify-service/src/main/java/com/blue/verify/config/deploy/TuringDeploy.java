package com.blue.verify.config.deploy;

import com.blue.basic.constant.verify.VerifyType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * turing deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "turing")
public class TuringDeploy {

    private Integer allow;

    private Long intervalMillis;

    private List<VerifyType> verifyTypes;

    public TuringDeploy() {
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

    public List<VerifyType> getVerifyTypes() {
        return verifyTypes;
    }

    public void setVerifyTypes(List<VerifyType> verifyTypes) {
        this.verifyTypes = verifyTypes;
    }

    @Override
    public String toString() {
        return "TuringDeploy{" +
                "allow=" + allow +
                ", intervalMillis=" + intervalMillis +
                ", verifyTypes=" + verifyTypes +
                '}';
    }

}
