package com.blue.auth.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * member address config
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "history")
public class CredentialHistoryDeploy {

    private Integer limit;

    public CredentialHistoryDeploy() {
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    @Override
    public String toString() {
        return "CredentialHistoryDeploy{" +
                "limit=" + limit +
                '}';
    }

}
