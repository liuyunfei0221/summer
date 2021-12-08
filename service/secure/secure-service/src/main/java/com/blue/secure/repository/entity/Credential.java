package com.blue.secure.repository.entity;

/**
 * @author blue
 */
@SuppressWarnings("unused")
public class Credential {

    /**
     * id
     */
    private Long id;

    /**
     * member id
     */
    private Long memberId;

    /**
     * identity str / json
     */
    private String identity;

    /**
     * login type: 1-SMS_VERIFY, 2-PHONE_PWD, 3-EMAIL_PWD, 4-WECHAT, 5-MINI_PRO, 6-NOT_LOGGED_IN
     */
    private String loginType;

    /**
     * data status: 1-valid 0-invalid
     */
    private Integer status;

    /**
     * password / json
     */
    private String password;

    /**
     * data create time
     */
    private Long createTime;

    /**
     * data update time
     */
    private Long updateTime;

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

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity == null ? null : identity.trim();
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
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

}