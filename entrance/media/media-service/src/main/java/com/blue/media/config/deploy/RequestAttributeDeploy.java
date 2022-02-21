package com.blue.media.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * request attr deploy
 *
 * @author DarkBlue
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

    /**
     * max headers size
     */
    private Integer maxHeadersSize;

    /**
     * max memory size
     */
    private Integer maxInMemorySize;

    /**
     * max disk usage per part
     */
    private Long maxDiskUsagePerPart;

    /**
     * max part count
     */
    private Integer maxParts;

    /**
     * logging details?
     */
    private Boolean enableLoggingRequestDetails;

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

    public Integer getMaxHeadersSize() {
        return maxHeadersSize;
    }

    public void setMaxHeadersSize(Integer maxHeadersSize) {
        this.maxHeadersSize = maxHeadersSize;
    }

    public Integer getMaxInMemorySize() {
        return maxInMemorySize;
    }

    public void setMaxInMemorySize(Integer maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    public Long getMaxDiskUsagePerPart() {
        return maxDiskUsagePerPart;
    }

    public void setMaxDiskUsagePerPart(Long maxDiskUsagePerPart) {
        this.maxDiskUsagePerPart = maxDiskUsagePerPart;
    }

    public Integer getMaxParts() {
        return maxParts;
    }

    public void setMaxParts(Integer maxParts) {
        this.maxParts = maxParts;
    }

    public Boolean getEnableLoggingRequestDetails() {
        return enableLoggingRequestDetails;
    }

    public void setEnableLoggingRequestDetails(Boolean enableLoggingRequestDetails) {
        this.enableLoggingRequestDetails = enableLoggingRequestDetails;
    }

    @Override
    public String toString() {
        return "RequestAttributeDeploy{" +
                "validContentTypes=" + validContentTypes +
                ", maxUriLength=" + maxUriLength +
                ", maxHeaderCount=" + maxHeaderCount +
                ", maxHeaderLength=" + maxHeaderLength +
                ", maxContentLength=" + maxContentLength +
                ", maxHeadersSize=" + maxHeadersSize +
                ", maxInMemorySize=" + maxInMemorySize +
                ", maxDiskUsagePerPart=" + maxDiskUsagePerPart +
                ", maxParts=" + maxParts +
                ", enableLoggingRequestDetails=" + enableLoggingRequestDetails +
                '}';
    }

}
