package com.blue.gateway.config.deploy;

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
