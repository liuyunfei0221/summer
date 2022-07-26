package com.blue.hbase.api.conf;

import java.util.List;
import java.util.Map;

/**
 * hbase config params
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public class HbaseConfParams implements HbaseConf {

    private Map<String, String> config;

    private List<String> resourceLocations;

    private Boolean quietMode;

    private Boolean restrictSystemProps;

    private Boolean allowNullValueProperties;

    private Integer corePoolSize;

    private Integer maximumPoolSize;

    private Long keepAliveSeconds;

    private Integer blockingQueueCapacity;

    private String threadNamePre;

    public HbaseConfParams() {
    }

    @Override
    public Map<String, String> getConfig() {
        return config;
    }

    @Override
    public List<String> getResourceLocations() {
        return resourceLocations;
    }

    @Override
    public Boolean getQuietMode() {
        return quietMode;
    }

    @Override
    public Boolean getRestrictSystemProps() {
        return restrictSystemProps;
    }

    @Override
    public Boolean getAllowNullValueProperties() {
        return allowNullValueProperties;
    }

    @Override
    public Integer getCorePoolSize() {
        return corePoolSize;
    }

    @Override
    public Integer getMaximumPoolSize() {
        return maximumPoolSize;
    }

    @Override
    public Long getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    @Override
    public Integer getBlockingQueueCapacity() {
        return blockingQueueCapacity;
    }

    @Override
    public String getThreadNamePre() {
        return threadNamePre;
    }

    public void setConfig(Map<String, String> config) {
        this.config = config;
    }

    public void setResourceLocations(List<String> resourceLocations) {
        this.resourceLocations = resourceLocations;
    }

    public void setQuietMode(Boolean quietMode) {
        this.quietMode = quietMode;
    }

    public void setRestrictSystemProps(Boolean restrictSystemProps) {
        this.restrictSystemProps = restrictSystemProps;
    }

    public void setAllowNullValueProperties(Boolean allowNullValueProperties) {
        this.allowNullValueProperties = allowNullValueProperties;
    }

    public void setCorePoolSize(Integer corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    public void setMaximumPoolSize(Integer maximumPoolSize) {
        this.maximumPoolSize = maximumPoolSize;
    }

    public void setKeepAliveSeconds(Long keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

    public void setBlockingQueueCapacity(Integer blockingQueueCapacity) {
        this.blockingQueueCapacity = blockingQueueCapacity;
    }

    public void setThreadNamePre(String threadNamePre) {
        this.threadNamePre = threadNamePre;
    }

    @Override
    public String toString() {
        return "HbaseConfParams{" +
                "config=" + config +
                ", resourceLocations=" + resourceLocations +
                ", quietMode=" + quietMode +
                ", restrictSystemProps=" + restrictSystemProps +
                ", allowNullValueProperties=" + allowNullValueProperties +
                ", corePoolSize=" + corePoolSize +
                ", maximumPoolSize=" + maximumPoolSize +
                ", keepAliveSeconds=" + keepAliveSeconds +
                ", blockingQueueCapacity=" + blockingQueueCapacity +
                ", threadNamePre='" + threadNamePre + '\'' +
                '}';
    }

}
