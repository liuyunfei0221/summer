package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;
import reactor.util.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static javax.crypto.Cipher.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.util.Loggers.getLogger;

/**
 * AES util
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "JavaDoc"})
public final class AesProcessor {

    private static final Logger LOGGER = getLogger(AesProcessor.class);

    private static final int KEY_LEN = 128;

    private static final String RAN_ALGORITHM = "SHA1PRNG";
    private static final String ALGORITHM = "AES";

    private final transient Cipher ENCRYPT;
    private final transient Cipher DECRYPT;

    private static final Base64.Encoder ENCODER = getEncoder();
    private static final Base64.Decoder DECODER = getDecoder();

    public AesProcessor(String salt) {
        if (isBlank(salt))
            throw new RuntimeException("salt can't be blank");

        try {
            SecureRandom secureRandom = SecureRandom.getInstance(RAN_ALGORITHM);
            secureRandom.setSeed(salt.getBytes());

            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_LEN, secureRandom);

            SecretKey secretKey = keyGenerator.generateKey();
            Key key = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);

            ENCRYPT = getInstance(ALGORITHM);
            ENCRYPT.init(ENCRYPT_MODE, key);

            DECRYPT = getInstance(ALGORITHM);
            DECRYPT.init(DECRYPT_MODE, key);
        } catch (Exception e) {
            LOGGER.error("construct failed, e = {}", e);
            throw new RuntimeException("construct failed, e = " + e);
        }
    }

    /**
     * encrypt data
     *
     * @param originalData
     * @return
     */
    public String encrypt(String originalData) {
        if (isNull(originalData))
            throw new BlueException(EMPTY_PARAM);

        try {
            return ENCODER.encodeToString(ENCRYPT.doFinal(originalData.getBytes(UTF_8)));
        } catch (Exception e) {
            LOGGER.error("encrypt failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * decrypt data
     *
     * @param encryptData
     * @return
     */
    public String decrypt(String encryptData) {
        if (isNull(encryptData))
            throw new BlueException(EMPTY_PARAM);

        try {
            return new String(DECRYPT.doFinal(DECODER.decode(encryptData.getBytes(UTF_8))), UTF_8);
        } catch (Exception e) {
            LOGGER.error("decrypt failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * encrypt data
     *
     * @param originalData
     * @return
     */
    public byte[] encrypt(byte[] originalData) {
        if (isNull(originalData))
            throw new BlueException(EMPTY_PARAM);

        try {
            return ENCRYPT.doFinal(originalData);
        } catch (Exception e) {
            LOGGER.error("encrypt failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * decrypt data
     *
     * @param encryptData
     * @return
     */
    public byte[] decrypt(byte[] encryptData) {
        if (isNull(encryptData))
            throw new BlueException(EMPTY_PARAM);

        try {
            return DECRYPT.doFinal(encryptData);
        } catch (Exception e) {
            LOGGER.error("decrypt failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

}
