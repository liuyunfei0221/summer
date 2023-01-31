package com.blue.basic.model.common;

import java.io.Serializable;

/**
 * encrypted response data
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EncryptedResponse implements Serializable {

    private static final long serialVersionUID = 4448482455540785133L;

    /**
     * encrypted data json
     */
    private String encrypted;

    public EncryptedResponse() {
    }

    public EncryptedResponse(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    @Override
    public String toString() {
        return "ResEncryptVO{" +
                "encrypted='" + encrypted + '\'' +
                '}';
    }

}