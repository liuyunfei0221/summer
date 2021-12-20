package com.blue.base.common.base;

import com.blue.base.model.exps.BlueException;
import reactor.util.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.SecureRandom;
import java.util.Base64;

import static com.blue.base.constant.base.ResponseElement.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static reactor.util.Loggers.getLogger;

/**
 * AES util
 *
 * @author DarkBlue
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "unused", "JavaDoc"})
public final class AesProcessor {

    private static final Logger LOGGER = getLogger(AesProcessor.class);

    private static final int KEY_LEN = 128;

    private static final String RAN_ALGORITHM = "SHA1PRNG";
    private static final String ALGORITHM = "AES";

    private final Cipher ENCRYPT;
    private final Cipher DECRYPT;

    private static final Base64.Encoder ENCODER = getEncoder();
    private static final Base64.Decoder DECODER = getDecoder();

    public AesProcessor(String salt) {
        if (isBlank(salt))
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "salt can't be blank");

        try {
            SecureRandom secureRandom = SecureRandom.getInstance(RAN_ALGORITHM);
            secureRandom.setSeed(salt.getBytes());

            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_LEN, secureRandom);

            SecretKey secretKey = keyGenerator.generateKey();
            Key key = new SecretKeySpec(secretKey.getEncoded(), ALGORITHM);

            ENCRYPT = Cipher.getInstance(ALGORITHM);
            ENCRYPT.init(Cipher.ENCRYPT_MODE, key);

            DECRYPT = Cipher.getInstance(ALGORITHM);
            DECRYPT.init(Cipher.DECRYPT_MODE, key);
        } catch (Exception e) {
            LOGGER.error("AesProcessor(String salt) failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR.status, INTERNAL_SERVER_ERROR.code, "AesProcessor(String salt) failed, e = " + e);
        }
    }

    /**
     * encrypt data
     *
     * @param originalData
     * @return
     */
    public String encrypt(String originalData) {
        if (isBlank(originalData))
            throw new BlueException(EMPTY_PARAM);

        try {
            return ENCODER.encodeToString(ENCRYPT.doFinal(originalData.getBytes(UTF_8)));
        } catch (Exception e) {
            LOGGER.error("String encrypt(String originalData) failed, e = {}", e);
            throw new BlueException(RSA_FAILED);
        }
    }

    /**
     * decrypt data
     *
     * @param encryptData
     * @return
     */
    public String decrypt(String encryptData) {
        if (isBlank(encryptData))
            throw new BlueException(EMPTY_PARAM);

        try {
            return new String(DECRYPT.doFinal(DECODER.decode(encryptData.getBytes(UTF_8))), UTF_8);
        } catch (Exception e) {
            LOGGER.error("String decrypt(String encryptData) failed, e = {}", e);
            throw new BlueException(RSA_FAILED);
        }
    }


    /**
     * encrypt data
     *
     * @param originalData
     * @return
     */
    public byte[] encrypt(byte[] originalData) {
        if (originalData == null)
            throw new BlueException(EMPTY_PARAM);

        try {
            return ENCRYPT.doFinal(originalData);
        } catch (Exception e) {
            LOGGER.error("byte[] encrypt(byte[] originalData) failed, e = {}", e);
            throw new BlueException(RSA_FAILED);
        }
    }

    /**
     * decrypt data
     *
     * @param encryptData
     * @return
     */
    public byte[] decrypt(byte[] encryptData) {
        if (encryptData == null)
            throw new BlueException(EMPTY_PARAM);

        try {
            return DECRYPT.doFinal(encryptData);
        } catch (Exception e) {
            LOGGER.error("byte[] decrypt(byte[] encryptData) failed, e = {}", e);
            throw new BlueException(RSA_FAILED);
        }
    }

}
