package com.blue.auth.api.model;

import com.blue.base.model.common.Access;
import com.blue.base.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;

/**
 * auth assert result
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AliControlFlowStatementWithoutBraces"})
public final class AccessAsserted implements Serializable {

    private static final long serialVersionUID = -3287463425057208346L;

    /**
     * certificate resource?
     */
    private boolean certificate;

    /**
     * decrypt request param?
     */
    private boolean requestUnDecryption;

    /**
     * encrypt response result?
     */
    private boolean responseUnEncryption;

    /**
     * has request body?
     */
    private boolean existenceRequestBody;

    /**
     * has response body?
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
    public AccessAsserted() {
    }

    public AccessAsserted(boolean certificate, boolean requestUnDecryption, boolean responseUnEncryption, boolean existenceRequestBody, boolean existenceResponseBody, String secKey, Access access, String message) {
        if (isNull(secKey) || isNull(access))
            throw new BlueException(BAD_REQUEST);

        this.certificate = certificate;
        this.requestUnDecryption = requestUnDecryption;
        this.responseUnEncryption = responseUnEncryption;
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

    public boolean getRequestUnDecryption() {
        return requestUnDecryption;
    }

    public void setRequestUnDecryption(boolean requestUnDecryption) {
        this.requestUnDecryption = requestUnDecryption;
    }

    public boolean getResponseUnEncryption() {
        return responseUnEncryption;
    }

    public void setResponseUnEncryption(boolean responseUnEncryption) {
        this.responseUnEncryption = responseUnEncryption;
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

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
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
                ", requestUnDecryption=" + requestUnDecryption +
                ", responseUnEncryption=" + responseUnEncryption +
                ", existenceRequestBody=" + existenceRequestBody +
                ", existenceResponseBody=" + existenceResponseBody +
                ", secKey='" + secKey + '\'' +
                ", access=" + access +
                ", message='" + message + '\'' +
                '}';
    }

}
