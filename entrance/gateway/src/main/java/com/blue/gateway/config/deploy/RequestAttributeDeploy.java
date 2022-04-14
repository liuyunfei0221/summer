package com.blue.gateway.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * request attr deploy
 *
 * @author liuyunfei
 */
@Component
@ConfigurationProperties(prefix = "request")
public class RequestAttributeDeploy {

    /**
     * valid content types
     */
    private List<String> validContentTypes;

    /**
     * max uri length
     */
    private Integer maxUriLength;

    /**
     * max header count
     */
    private Integer maxHeaderCount;

    /**
     * max header length
     */
    private Integer maxHeaderLength;

    /**
     * max content length
     */
    private Integer maxContentLength;

    public RequestAttributeDeploy() {
    }

    public List<String> getValidContentTypes() {
        return validContentTypes;
    }

    public void setValidContentTypes(List<String> validContentTypes) {
        this.validContentTypes = validContentTypes;
    }

    public Integer getMaxUriLength() {
        return maxUriLength;
    }

    public void setMaxUriLength(Integer maxUriLength) {
        this.maxUriLength = maxUriLength;
    }

    public Integer getMaxHeaderCount() {
        return maxHeaderCount;
    }

    public void setMaxHeaderCount(Integer maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public Integer getMaxHeaderLength() {
        return maxHeaderLength;
    }

    public void setMaxHeaderLength(Integer maxHeaderLength) {
        this.maxHeaderLength = maxHeaderLength;
    }

    public Integer getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(Integer maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    @Override
    public String toString() {
        return "BlueRequestAttributeDeploy{" +
                "validContentTypes=" + validContentTypes +
                ", maxUriLength=" + maxUriLength +
                ", maxHeaderCount=" + maxHeaderCount +
                ", maxHeaderLength=" + maxHeaderLength +
                ", maxContentLength=" + maxContentLength +
                '}';
    }

}
