package com.blue.secure.api.model;


import java.io.Serializable;

/**
 * login params for wechat mini pro
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "GrazieInspection"})
public final class MiniProLoginParam implements Serializable {

    private static final long serialVersionUID = 305317890968502939L;

    /**
     * user encrypted data
     */
    private String encryptedData;

    /**
     * encrypt algorithm iv
     */
    private String iv;

    /**
     * js code
     */
    private String jsCode;


    public MiniProLoginParam() {
    }

    public MiniProLoginParam(String encryptedData, String iv, String jsCode) {
        this.encryptedData = encryptedData;
        this.iv = iv;
        this.jsCode = jsCode;
    }

    public String getEncryptedData() {
        return encryptedData;
    }

    public void setEncryptedData(String encryptedData) {
        this.encryptedData = encryptedData;
    }

    public String getIv() {
        return iv;
    }

    public void setIv(String iv) {
        this.iv = iv;
    }

    public String getJsCode() {
        return jsCode;
    }

    public void setJsCode(String jsCode) {
        this.jsCode = jsCode;
    }

    @Override
    public String toString() {
        return "MiniProParam{" +
                "encryptedData='" + encryptedData + '\'' +
                ", iv='" + iv + '\'' +
                ", jsCode='" + jsCode + '\'' +
                '}';
    }

}
