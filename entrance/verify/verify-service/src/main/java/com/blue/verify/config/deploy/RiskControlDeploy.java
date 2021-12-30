package com.blue.verify.config.deploy;

import com.blue.caffeine.constant.ExpireStrategy;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * risk control deploy
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "risk")
public class RiskControlDeploy {

    private Integer illegalCapacity;

    private Long illegalExpireSeconds;

    private ExpireStrategy expireStrategy;

    public RiskControlDeploy() {
    }

    public Integer getIllegalCapacity() {
        return illegalCapacity;
    }

    public void setIllegalCapacity(Integer illegalCapacity) {
        this.illegalCapacity = illegalCapacity;
    }

    public Long getIllegalExpireSeconds() {
        return illegalExpireSeconds;
    }

    public void setIllegalExpireSeconds(Long illegalExpireSeconds) {
        this.illegalExpireSeconds = illegalExpireSeconds;
    }

    public ExpireStrategy getExpireStrategy() {
        return expireStrategy;
    }

    public void setExpireStrategy(ExpireStrategy expireStrategy) {
        this.expireStrategy = expireStrategy;
    }

    @Override
    public String toString() {
        return "RiskControlDeploy{" +
                "illegalCapacity=" + illegalCapacity +
                ", illegalExpireSeconds=" + illegalExpireSeconds +
                ", expireStrategy=" + expireStrategy +
                '}';
    }

}
