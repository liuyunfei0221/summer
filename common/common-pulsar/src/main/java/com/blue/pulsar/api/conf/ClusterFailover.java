package com.blue.pulsar.api.conf;

import java.util.List;
import java.util.Map;

/**
 * cluster failover attrs
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class ClusterFailover {

    private String primaryShard;

    private List<String> secondaryShards;

    private Long failoverDelayMillis;

    private Long switchBackDelayMillis;

    private Long checkIntervalMillis;

    private Map<String, String> secondaryTlsTrustCertsFilePaths;

    private String certFilePath;

    private String keyFilePath;

    public ClusterFailover() {
    }

    public String getPrimaryShard() {
        return primaryShard;
    }

    public void setPrimaryShard(String primaryShard) {
        this.primaryShard = primaryShard;
    }

    public List<String> getSecondaryShards() {
        return secondaryShards;
    }

    public void setSecondaryShards(List<String> secondaryShards) {
        this.secondaryShards = secondaryShards;
    }

    public Long getFailoverDelayMillis() {
        return failoverDelayMillis;
    }

    public void setFailoverDelayMillis(Long failoverDelayMillis) {
        this.failoverDelayMillis = failoverDelayMillis;
    }

    public Long getSwitchBackDelayMillis() {
        return switchBackDelayMillis;
    }

    public void setSwitchBackDelayMillis(Long switchBackDelayMillis) {
        this.switchBackDelayMillis = switchBackDelayMillis;
    }

    public Long getCheckIntervalMillis() {
        return checkIntervalMillis;
    }

    public void setCheckIntervalMillis(Long checkIntervalMillis) {
        this.checkIntervalMillis = checkIntervalMillis;
    }

    public Map<String, String> getSecondaryTlsTrustCertsFilePaths() {
        return secondaryTlsTrustCertsFilePaths;
    }

    public void setSecondaryTlsTrustCertsFilePaths(Map<String, String> secondaryTlsTrustCertsFilePaths) {
        this.secondaryTlsTrustCertsFilePaths = secondaryTlsTrustCertsFilePaths;
    }

    public String getCertFilePath() {
        return certFilePath;
    }

    public void setCertFilePath(String certFilePath) {
        this.certFilePath = certFilePath;
    }

    public String getKeyFilePath() {
        return keyFilePath;
    }

    public void setKeyFilePath(String keyFilePath) {
        this.keyFilePath = keyFilePath;
    }

    @Override
    public String toString() {
        return "ClusterFailover{" +
                "primaryShard='" + primaryShard + '\'' +
                ", secondaryShards=" + secondaryShards +
                ", failoverDelayMillis=" + failoverDelayMillis +
                ", switchBackDelayMillis=" + switchBackDelayMillis +
                ", checkIntervalMillis=" + checkIntervalMillis +
                ", secondaryTlsTrustCertsFilePaths=" + secondaryTlsTrustCertsFilePaths +
                ", certFilePath='" + certFilePath + '\'' +
                ", keyFilePath='" + keyFilePath + '\'' +
                '}';
    }

}
