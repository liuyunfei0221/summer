package com.blue.secure.api.model;

import com.blue.base.model.base.Access;

import java.io.Serializable;

/**
 * auth校验结果
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthAsserted implements Serializable {

    private static final long serialVersionUID = -3287463425057208346L;

    /**
     * 是否需认证
     */
    private boolean certificate;

    /**
     * 前置是否不解密
     */
    private boolean preUnDecryption;

    /**
     * 后置是否不加密
     */
    private boolean postUnEncryption;

    /**
     * 是否有请求体 1有 0没有
     */
    private boolean existenceRequestBody;

    /**
     * 是否有响应体 1有 0没有
     */
    private boolean existenceResponseBody;

    /**
     * 数据解密私钥
     */
    private String secKey;

    /**
     * 用户信息
     */
    private Access access;

    /**
     * 信息提示
     */
    private String message;

    /**
     * 仅提供用于序列化,不推荐使用,基于set无法保证参数正确性
     */
    @Deprecated
    public AuthAsserted() {
    }

    public AuthAsserted(boolean certificate, boolean preUnDecryption, boolean postUnEncryption, boolean existenceRequestBody, boolean existenceResponseBody, String secKey, Access access, String message) {
        if (secKey == null || access == null)
            throw new RuntimeException("secKey,accessInfo均不能为空");

        this.certificate = certificate;
        this.preUnDecryption = preUnDecryption;
        this.postUnEncryption = postUnEncryption;
        this.existenceRequestBody = existenceRequestBody;
        this.existenceResponseBody = existenceResponseBody;
        this.secKey = secKey;
        this.access = access;
        this.message = message;
    }

    public boolean getCertificate() {
        return certificate;
    }

    public void setCertificate(boolean certificate) {
        this.certificate = certificate;
    }

    public boolean getPreUnDecryption() {
        return preUnDecryption;
    }

    public void setPreUnDecryption(boolean preUnDecryption) {
        this.preUnDecryption = preUnDecryption;
    }

    public boolean getPostUnEncryption() {
        return postUnEncryption;
    }

    public void setPostUnEncryption(boolean postUnEncryption) {
        this.postUnEncryption = postUnEncryption;
    }

    public boolean getExistenceRequestBody() {
        return existenceRequestBody;
    }

    public void setExistenceRequestBody(boolean existenceRequestBody) {
        this.existenceRequestBody = existenceRequestBody;
    }

    public boolean getExistenceResponseBody() {
        return existenceResponseBody;
    }

    public void setExistenceResponseBody(boolean existenceResponseBody) {
        this.existenceResponseBody = existenceResponseBody;
    }

    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    public Access getAccessInfo() {
        return access;
    }

    public void setAccessInfo(Access access) {
        this.access = access;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "AuthAsserted{" +
                "certificate=" + certificate +
                ", preUnDecryption=" + preUnDecryption +
                ", postUnEncryption=" + postUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", secKey='" + secKey + '\'' +
                ", access=" + access +
                ", message='" + message + '\'' +
                '}';
    }

}
