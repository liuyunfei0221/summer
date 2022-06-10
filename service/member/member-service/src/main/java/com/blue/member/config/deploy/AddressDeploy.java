package com.blue.member.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * address config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "address")
public class AddressDeploy {

    private Long max;

    public AddressDeploy() {
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "AddressDeploy{" +
                "max=" + max +
                '}';
    }

}
