package com.blue.auth.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * member address config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "question")
public class QuestionDeploy {

    private Long max;

    public QuestionDeploy() {
    }

    public Long getMax() {
        return max;
    }

    public void setMax(Long max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return "QuestionDeploy{" +
                "max=" + max +
                '}';
    }

}
