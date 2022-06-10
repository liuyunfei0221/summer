package com.blue.member.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * card config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "card")
public class CardDeploy {

    private Long max;

    public CardDeploy() {
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "CardDeploy{" +
                "max=" + max +
                '}';
    }

}
