package com.blue.event.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * risk control deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "risk")
public class RiskControlDeploy {

    private Long illegalExpiresSecond;

    public RiskControlDeploy() {
    }

    public Long getIllegalExpiresSecond() {
        return illegalExpiresSecond;
    }

    public void setIllegalExpiresSecond(Long illegalExpiresSecond) {
        this.illegalExpiresSecond = illegalExpiresSecond;
    }

    @Override
    public String toString() {
        return "RiskControlDeploy{" +
                ", illegalExpiresSecond=" + illegalExpiresSecond +
                '}';
    }

}
