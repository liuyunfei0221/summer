package com.blue.base.model.base;

import java.io.Serializable;

/**
 * public key - private key pair
 *
 * @author DarkBlue
 */
public final class KeyPair implements Serializable {

    private static final long serialVersionUID = -591395585005157171L;

    /**
     * public key
     */
    private final String pubKey;

    /**
     * private key
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
