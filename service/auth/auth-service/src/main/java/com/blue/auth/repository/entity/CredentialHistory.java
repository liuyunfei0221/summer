package com.blue.auth.repository.entity;

import com.blue.base.constant.auth.CredentialType;
import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * credential history
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class CredentialHistory implements Serializable {

    private static final long serialVersionUID = -171615015283398350L;

    @Id
    private Long id;

    /**
     * member id
     */
    private Long memberId;

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
     * extra infos
     */
    private String extra;

    private Long createTime;

    public CredentialHistory() {
    }

    public CredentialHistory(Long id, Long memberId, String credential, String type,
                             String extra, Long createTime) {
        this.id = id;
        this.memberId = memberId;
        this.credential = credential;
        this.type = type;
        this.extra = extra;
        this.createTime = createTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
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

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CredentialHistory{" +
                "id=" + id +
                ", memberId=" + memberId +
                ", credential='" + credential + '\'' +
                ", type='" + type + '\'' +
                ", extra='" + extra + '\'' +
                ", createTime=" + createTime +
                '}';
    }

}