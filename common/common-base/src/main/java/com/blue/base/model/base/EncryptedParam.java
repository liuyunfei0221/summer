package com.blue.base.model.base;

import java.io.Serializable;

/**
 * 用于加密数据封装
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class EncryptedParam implements Serializable {

    private static final long serialVersionUID = 6773955220682431886L;
    /**
     * 已加密的数据(json)
     */
    private String encrypted;

    /**
     * 针对加密数据的签名
     */
    private String sign;


    public EncryptedParam() {
    }

    public EncryptedParam(String encrypted, String sign) {
        this.encrypted = encrypted;
        this.sign = sign;
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "EncryptDTO{" +
                "encrypted='" + encrypted + '\'' +
                ", sign='" + sign + '\'' +
                '}';
    }

}
