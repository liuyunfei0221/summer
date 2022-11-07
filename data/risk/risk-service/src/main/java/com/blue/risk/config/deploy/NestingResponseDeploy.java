package com.blue.risk.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * nesting deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "nesting")
public class NestingResponseDeploy {

    private transient List<String> uris;

    private transient List<Integer> statuses;

    private transient String response;

    public NestingResponseDeploy() {
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
    }

    public List<Integer> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Integer> statuses) {
        this.statuses = statuses;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    @Override
    public String toString() {
        return "NestingResponseDeploy{" +
                "uris=" + uris +
                ", statuses=" + statuses +
                ", response='" + response + '\'' +
                '}';
    }

}
