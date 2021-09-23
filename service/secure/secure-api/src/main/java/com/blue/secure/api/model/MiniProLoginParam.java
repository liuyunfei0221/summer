package com.blue.secure.api.model;


import java.io.Serializable;

/**
 * 小程序登录参数
 *
 * @author DarkBlue
 */
@SuppressWarnings("unused")
public final class MiniProLoginParam implements Serializable {

    private static final long serialVersionUID = 305317890968502939L;

    /**
     * 包括敏感数据在内的完整用户信息的加密数据
     */
    private String encryptedData;

    /**
     * 加密算法的初始向量
     */
    private String iv;

    /**
     * 登录时获取的 code
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
