package com.blue.auth.api.model;

import java.io.Serializable;

/**
 * member credential info without member id
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CredentialInfo implements Serializable {

    private static final long serialVersionUID = -1783008101958304961L;

    private String credential;

    private String type;

    private String access;

    /**
     * data status: 1-valid 0-invalid
     */
    private Integer status;

    private String extra;

    public CredentialInfo() {
    }

    public CredentialInfo(String credential, String type, String access, Integer status, String extra) {
        this.credential = credential;
        this.type = type;
        this.access = access;
        this.status = status;
        this.extra = extra;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = access;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "CredentialInfo{" +
                "credential='" + credential + '\'' +
                ", type='" + type + '\'' +
                ", access='" + ":)" + '\'' +
                ", status='" + status + '\'' +
                ", extra='" + extra + '\'' +
                '}';
    }

}



