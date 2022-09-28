package com.blue.auth.model;

import com.blue.basic.inter.Asserter;
import com.blue.basic.model.exps.BlueException;

import java.io.Serializable;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.common.base.ConstantProcessor.assertRsaDecryptMode;
import static com.blue.basic.constant.common.ResponseElement.INVALID_PARAM;

/**
 * encrypted data params
 *
 * @author liuyunfei
 */
@SuppressWarnings("unused")
public final class EncryptedDataParam implements Serializable, Asserter {

    private static final long serialVersionUID = 4361380313864449316L;

    /**
     * encrypted data json
     */
    private String encrypted;

    /**
     * secKey
     */
    private String secKey;

    /**
     * decrypt mode
     *
     * @see com.blue.basic.constant.common.RsaDecryptMode
     */
    private Integer rsaDecryptMode;

    public EncryptedDataParam() {
    }

    public EncryptedDataParam(String encrypted, String secKey, Integer rsaDecryptMode) {
        this.encrypted = encrypted;
        this.secKey = secKey;
        this.rsaDecryptMode = rsaDecryptMode;
    }

    @Override
    public void asserts() {
        if (isBlank(this.encrypted) || isBlank(this.secKey))
            throw new BlueException(INVALID_PARAM);

        assertRsaDecryptMode(this.rsaDecryptMode, false);
    }

    public String getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(String encrypted) {
        this.encrypted = encrypted;
    }

    public String getSecKey() {
        return secKey;
    }

    public void setSecKey(String secKey) {
        this.secKey = secKey;
    }

    public Integer getRsaDecryptMode() {
        return rsaDecryptMode;
    }

    public void setRsaDecryptMode(Integer rsaDecryptMode) {
        this.rsaDecryptMode = rsaDecryptMode;
    }

    @Override
    public String toString() {
        return "EncryptedDataParam{" +
                "encrypted='" + encrypted + '\'' +
                ", secKey='" + secKey + '\'' +
                ", rsaDecryptMode=" + rsaDecryptMode +
                '}';
    }

}
