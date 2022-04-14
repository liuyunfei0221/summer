package com.blue.base.model.base;

import java.io.Serializable;

/**
 * encrypted request params
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EncryptedRequest implements Serializable {

    private static final long serialVersionUID = 6773955220682431886L;

    /**
     * encrypted data json
     */
    private String encrypted;

    /**
     * signature
     */
    private String signature;

    public EncryptedRequest() {
    }

    public EncryptedRequest(String encrypted, String signature) {
        this.encrypted = encrypted;
        this.signature = signature;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "EncryptedData{" +
                "encrypted='" + encrypted + '\'' +
                ", signature='" + signature + '\'' +
                '}';
    }

}
