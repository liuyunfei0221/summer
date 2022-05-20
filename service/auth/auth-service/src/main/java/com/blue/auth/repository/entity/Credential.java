package com.blue.auth.repository.entity;

import com.blue.base.constant.auth.CredentialType;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;

/**
 * credential entity
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class Credential implements Serializable {

    private static final long serialVersionUID = 1182928716554934110L;

    private Long id;

    /**
     * credential
     */
    private String credential;

    /**
     * credential type: SV-SMS_VERIFY, PP-PHONE_PWD, EP-EMAIL_PWD, WE-WECHAT, MP-MINI_PRO, NLI-NOT_LOGGED_IN
     *
     * @see CredentialType
     */
    private String type;

    /**
     * encrypted password(str)/info(json)
     */
    private String access;

    /**
     * member id
     */
    private Long memberId;

    /**
     * extra info
     */
    private String extra;

    /**
     * data status: 1-valid 0-invalid
     */
    private Integer status;

    private Long createTime;

    private Long updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = isNull(credential) ? null : credential.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = isNull(type) ? null : type.trim();
    }

    public String getAccess() {
        return access;
    }

    public void setAccess(String access) {
        this.access = isNull(access) ? null : access.trim();
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = isNull(extra) ? null : extra.trim();
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "Credential{" +
                "id=" + id +
                ", credential='" + credential + '\'' +
                ", type='" + type + '\'' +
                ", access='" + ":)" + '\'' +
                ", memberId=" + memberId +
                ", extra='" + extra + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                '}';
    }
}