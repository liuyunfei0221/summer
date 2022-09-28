package com.blue.basic.constant.common;

import com.blue.basic.common.base.RsaProcessor;

import java.util.function.BinaryOperator;

/**
 * rsa decrypt mode
 *
 * @author liuyunfei
 */
public enum RsaDecryptMode {

    /**
     * decrypt by public key
     */
    BY_PUBLIC_KEY(1, RsaProcessor::decryptByPublicKey, "decrypt by public key"),

    /**
     * decrypt by private key
     */
    BY_PRIVATE_KEY(2, RsaProcessor::decryptByPrivateKey, "decrypt by private key");

    public final int identity;

    public final BinaryOperator<String> decipher;

    public final String disc;

    RsaDecryptMode(int identity, BinaryOperator<String> decipher, String disc) {
        this.identity = identity;
        this.decipher = decipher;
        this.disc = disc;
    }

}
