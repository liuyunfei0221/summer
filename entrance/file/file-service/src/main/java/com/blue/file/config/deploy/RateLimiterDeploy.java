package com.blue.file.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 限流配置
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "limit")
public class RateLimiterDeploy {

    /**
     * 填充速率
     */
    private int replenishRate;

    /**
     * 突发容量
     */
    private int burstCapacity;

    public RateLimiterDeploy() {
    }

    public int getReplenishRate() {
        return replenishRate;
    }

    public void setReplenishRate(int replenishRate) {
        this.replenishRate = replenishRate;
    }

    public int getBurstCapacity() {
        return burstCapacity;
    }

    public void setBurstCapacity(int burstCapacity) {
        this.burstCapacity = burstCapacity;
    }

    @Override
    public String toString() {
        return "RateLimiterDeploy{" +
                "replenishRate=" + replenishRate +
                ", burstCapacity=" + burstCapacity +
                '}';
    }

}
