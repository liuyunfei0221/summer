package com.blue.secure.api.model;

import com.blue.base.model.base.Access;

import java.io.Serializable;

/**
 * auth assert result
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AuthAsserted implements Serializable {

    private static final long serialVersionUID = -3287463425057208346L;

    /**
     * certificate resource?
     */
    private boolean certificate;

    /**
     * decrypt request param?
     */
    private boolean preUnDecryption;

    /**
     * encrypt response result?
     */
    private boolean postUnEncryption;

    /**
     * exist request body?
     */
    private boolean existenceRequestBody;

    /**
     * exist response body?
     */
    private boolean existenceResponseBody;

    /**
     * sec key / private key
     */
    private String secKey;

    /**
     * member access info
     */
    private Access access;

    /**
     * message
     */
    private String message;

    /**
     * It is only provided for serialization and is not recommended.
     * The correctness of parameters cannot be guaranteed based on setter
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
