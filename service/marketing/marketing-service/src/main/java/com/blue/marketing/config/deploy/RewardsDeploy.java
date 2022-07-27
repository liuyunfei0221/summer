package com.blue.marketing.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * rewards config deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "rewards")
public class RewardsDeploy {

    private long maxBlockingMillis;

    private int maxBackup;

    public RewardsDeploy() {
    }

    public long getMaxBlockingMillis() {
        return maxBlockingMillis;
    }

    public void setMaxBlockingMillis(long maxBlockingMillis) {
        this.maxBlockingMillis = maxBlockingMillis;
    }

    public int getMaxBackup() {
        return maxBackup;
    }

    public void setMaxBackup(int maxBackup) {
        this.maxBackup = maxBackup;
    }

    @Override
    public String toString() {
        return "RewardsDeploy{" +
                "maxBlockingMillis=" + maxBlockingMillis +
                ", maxBackup=" + maxBackup +
                '}';
    }

}
