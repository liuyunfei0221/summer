package com.blue.file.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rest配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "risk")
public class RiskControlDeploy {

    private Long illegalExpireSeconds;


    private Integer illegalCapacity;


    public RiskControlDeploy() {
    }

    public Long getIllegalExpireSeconds() {
        return illegalExpireSeconds;
    }

    public void setIllegalExpireSeconds(Long illegalExpireSeconds) {
        this.illegalExpireSeconds = illegalExpireSeconds;
    }

    public Integer getIllegalCapacity() {
        return illegalCapacity;
    }

    public void setIllegalCapacity(Integer illegalCapacity) {
        this.illegalCapacity = illegalCapacity;
    }

}
