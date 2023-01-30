package com.blue.event.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rate limiter config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "limit")
public class RateLimiterDeploy {

    /**
     * replenish rate
     */
    private int replenishRate;

    /**
     * burst capacity
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