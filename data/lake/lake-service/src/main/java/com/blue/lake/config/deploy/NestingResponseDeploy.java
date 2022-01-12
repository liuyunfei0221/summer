package com.blue.lake.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * nesting deploy
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "nesting")
public class NestingResponseDeploy {

    private List<String> uris;

    private String response;

    public NestingResponseDeploy() {
    }

    public List<String> getUris() {
        return uris;
    }

    public void setUris(List<String> uris) {
        this.uris = uris;
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
                ", response='" + response + '\'' +
                '}';
    }

}
