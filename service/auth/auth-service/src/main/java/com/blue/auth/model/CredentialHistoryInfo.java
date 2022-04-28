package com.blue.auth.model;

import java.io.Serializable;

/**
 * credential history info for rest
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CredentialHistoryInfo implements Serializable {

    private static final long serialVersionUID = -7325639123024046375L;

    /**
     * credential
     */
    private String credential;

    private Long createTime;

    public CredentialHistoryInfo() {
    }

    public CredentialHistoryInfo(String credential, Long createTime) {
        this.credential = credential;
        this.createTime = createTime;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CredentialHistoryInfo{" +
                "credential='" + credential + '\'' +
                ", createTime=" + createTime +
                '}';
    }

}
