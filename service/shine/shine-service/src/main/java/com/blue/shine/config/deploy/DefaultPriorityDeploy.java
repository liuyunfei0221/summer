package com.blue.shine.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * default priority config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "shine")
public class DefaultPriorityDeploy {

    private Integer priority;

    public DefaultPriorityDeploy() {
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    @Override
    public String toString() {
        return "DefaultPriorityDeploy{" +
                "priority=" + priority +
                '}';
    }

}
