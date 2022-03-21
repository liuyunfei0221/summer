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
    private Long maxUriLength;

    /**
     * max header count
     */
    private Long maxHeaderCount;

    /**
     * max header length
     */
    private Long maxHeaderLength;

    /**
     * max content length
     */
    private Long maxContentLength;

    /**
     * max headers size
     */
    private Long maxHeadersSize;

    /**
     * max memory size
     */
    private Long maxInMemorySize;

    /**
     * max disk usage per part
     */
    private Long maxDiskUsagePerPart;

    /**
     * max part count
     */
    private Long maxParts;

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

    public Long getMaxUriLength() {
        return maxUriLength;
    }

    public void setMaxUriLength(Long maxUriLength) {
        this.maxUriLength = maxUriLength;
    }

    public Long getMaxHeaderCount() {
        return maxHeaderCount;
    }

    public void setMaxHeaderCount(Long maxHeaderCount) {
        this.maxHeaderCount = maxHeaderCount;
    }

    public Long getMaxHeaderLength() {
        return maxHeaderLength;
    }

    public void setMaxHeaderLength(Long maxHeaderLength) {
        this.maxHeaderLength = maxHeaderLength;
    }

    public Long getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(Long maxContentLength) {
        this.maxContentLength = maxContentLength;
    }

    public Long getMaxHeadersSize() {
        return maxHeadersSize;
    }

    public void setMaxHeadersSize(Long maxHeadersSize) {
        this.maxHeadersSize = maxHeadersSize;
    }

    public Long getMaxInMemorySize() {
        return maxInMemorySize;
    }

    public void setMaxInMemorySize(Long maxInMemorySize) {
        this.maxInMemorySize = maxInMemorySize;
    }

    public Long getMaxDiskUsagePerPart() {
        return maxDiskUsagePerPart;
    }

    public void setMaxDiskUsagePerPart(Long maxDiskUsagePerPart) {
        this.maxDiskUsagePerPart = maxDiskUsagePerPart;
    }

    public Long getMaxParts() {
        return maxParts;
    }

    public void setMaxParts(Long maxParts) {
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
