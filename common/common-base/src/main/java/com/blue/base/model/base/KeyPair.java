package com.blue.base.model.base;

import java.io.Serializable;

/**
 * 密钥对
 *
 * @author DarkBlue
 */
public final class KeyPair implements Serializable {

    private static final long serialVersionUID = -591395585005157171L;

    /**
     * 公钥
     */
    private final String pubKey;

    /**
     * 私钥
     */
    private final String priKey;

    public KeyPair(String pubKey, String priKey) {
        this.pubKey = pubKey;
        this.priKey = priKey;
    }

    public String getPubKey() {
        return pubKey;
    }

    public String getPriKey() {
        return priKey;
    }

    @Override
    public String toString() {
        return "KeyPairDTO{" +
                "pubKey='" + pubKey + '\'' +
                ", priKey='" + priKey + '\'' +
                '}';
    }

}
