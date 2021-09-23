package com.blue.file.config.deploy;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 请求数据大小配置参数类
 *
 * @author DarkBlue
 */
@Component
@ConfigurationProperties(prefix = "request")
public class RequestAttributeDeploy {

    /**
     * 合法数据类型
     */
    private List<String> validContentTypes;

    /**
     * 最大uri长度
     */
    private Integer maxUriLength;

    /**
     * 最大header数量
     */
    private Integer maxHeaderCount;

    /**
     * 最大header长度
     */
    private Integer maxHeaderLength;

    /**
     * 最大内容长度
     */
    private Long maxContentLength;

    /**
     * 最大文件大小
     */
    private Integer maxHeadersSize;

    /**
     * 最大内存使用
     */
    private Integer maxInMemorySize;

    /**
     * 磁盘每次使用最大/每文件
     */
    private Integer maxDiskUsagePerPart;

    /**
     * 最大parts数量
     */
    private Integer maxParts;

    /**
     * 日志
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

    public Long getMaxContentLength() {
        return maxContentLength;
    }

    public void setMaxContentLength(Long maxContentLength) {
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

    public Integer getMaxDiskUsagePerPart() {
        return maxDiskUsagePerPart;
    }

    public void setMaxDiskUsagePerPart(Integer maxDiskUsagePerPart) {
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
