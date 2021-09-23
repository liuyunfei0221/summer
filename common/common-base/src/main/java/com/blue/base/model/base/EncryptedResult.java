package com.blue.base.model.base;

import java.io.Serializable;

/**
 * 用于加密数据响应
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class EncryptedResult implements Serializable {

    private static final long serialVersionUID = 4448482455540785133L;

    /**
     * 已加密的数据(json)
     */
    private String encrypted;

    public EncryptedResult() {
    }

    public EncryptedResult(String encrypted) {
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
