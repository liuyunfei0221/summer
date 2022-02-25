package com.blue.base.common.base;

import com.blue.base.model.base.KeyPair;
import com.blue.base.model.exps.BlueException;
import org.springframework.util.FastByteArrayOutputStream;
import reactor.util.Logger;

import javax.crypto.Cipher;
import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import static com.blue.base.common.base.RsaProcessor.HandleMode.*;
import static com.blue.base.constant.base.ResponseElement.*;
import static java.lang.Math.min;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.Base64.getDecoder;
import static java.util.Base64.getEncoder;
import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;
import static reactor.util.Loggers.getLogger;

/**
 * rsa util
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "UnusedAssignment"})
public final class RsaProcessor {

    private static final Logger LOGGER = getLogger(RsaProcessor.class);

    private static final String KEY_ALGORITHM = "RSA";
    public static final String SIGN_ALGORITHM = "SHA1WithRSA";
    private static final Charset DEFAULT_CHARSET = UTF_8;

    private static final Base64.Encoder ENCODER = getEncoder();
    private static final Base64.Decoder DECODER = getDecoder();

    private static final Supplier<Cipher> CIPHER_SUP = () -> {
        try {
            return Cipher.getInstance(KEY_ALGORITHM);
        } catch (Exception e) {
            LOGGER.error("CIPHER_SUP get(), failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };

    private static final Supplier<KeyFactory> KEY_FACTORY_SUP = () -> {
        try {
            return KeyFactory.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("KEY_FACTORY_SUP get(), failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };

    private static final Supplier<Signature> SIGNATURE_SUP = () -> {
        try {
            return Signature.getInstance(SIGN_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("SIGNATURE_SUP get(), failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };


    private static final Supplier<KeyPairGenerator> KEY_PAIR_GEN_SUP = () -> {
        try {
            return KeyPairGenerator.getInstance(KEY_ALGORITHM);
        } catch (NoSuchAlgorithmException e) {
            LOGGER.error("KEY_PAIR_GEN_SUP get(), failed, e = {}", e);
            throw new BlueException(INTERNAL_SERVER_ERROR);
        }
    };

    /**
     * params asserter
     */
    private static final BiConsumer<String, String> PAR_ASSERT = (data, secKey) -> {
        if (data == null || "".equals(data))
            throw new BlueException(EMPTY_PARAM);
        if (secKey == null || "".equals(secKey))
            throw new BlueException(BAD_REQUEST);
    };

    /**
     * Segmented processing encryption and decryption
     *
     * @param source
     * @param cipher
     * @param handleMode
     * @return
     */
    private static byte[] handleBySegment(byte[] source, Cipher cipher, HandleMode handleMode) {
        try (FastByteArrayOutputStream outputStream = new FastByteArrayOutputStream()) {

            int len = source.length;
            int limit = 0;
            int rows = min(len, handleMode.length);
            byte[] segmentData;
            int lastStep = len - rows;

            while (limit < len) {
                if (limit > lastStep)
                    rows = len - limit;

                segmentData = cipher.doFinal(source, limit, rows);
                limit += rows;
                outputStream.write(segmentData);
            }

            segmentData = null;
            source = null;
            return outputStream.toByteArray();
        } catch (Exception e) {
            LOGGER.error("byte[] handleBySegment(byte[] source, Cipher cipher, HandleMode handleMode) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * Subsequent processing sign and verification
     *
     * @param source
     * @param signature
     * @param handleMode
     */
    private static Signature signBySegment(byte[] source, Signature signature, HandleMode handleMode) {
        try {
            int len = source.length;
            int limit = 0;
            int rows = min(len, handleMode.length);
            int lastStep = len - rows;

            while (limit < len) {
                if (limit > lastStep)
                    rows = len - limit;

                signature.update(source, limit, rows);
                limit += rows;
            }

            source = null;
            return signature;
        } catch (Exception e) {
            LOGGER.error("Signature signBySegment(byte[] source, Signature signature, HandleMode handleMode) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * encrypt by private key
     *
     * @param data
     * @param priKey
     * @return
     */
    public static String encryptByPrivateKey(String data, String priKey) {
        PAR_ASSERT.accept(data, priKey);

        Cipher cipher = CIPHER_SUP.get();
        try {
            cipher.init(ENCRYPT_MODE, KEY_FACTORY_SUP.get()
                    .generatePrivate(new PKCS8EncodedKeySpec(DECODER.decode(priKey))));

            return ENCODER.encodeToString(handleBySegment(data.getBytes(DEFAULT_CHARSET), cipher, ENCRYPT));
        } catch (Exception e) {
            LOGGER.error("String encryptByPrivateKey(String data, String priKey) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * decrypt by public key
     *
     * @param secData
     * @param pubKey
     * @return
     */
    public static String decryptByPublicKey(String secData, String pubKey) {
        PAR_ASSERT.accept(secData, pubKey);

        Cipher cipher = CIPHER_SUP.get();
        try {
            cipher.init(DECRYPT_MODE, KEY_FACTORY_SUP.get()
                    .generatePublic(new X509EncodedKeySpec(DECODER.decode(pubKey))));

            return new String(handleBySegment(DECODER.decode(secData), cipher, DECRYPT), DEFAULT_CHARSET);
        } catch (Exception e) {
            LOGGER.error("String decryptByPublicKey(String secData, String pubKey) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * encrypt by public key
     *
     * @param data
     * @param pubKey
     * @return
     */
    public static String encryptByPublicKey(String data, String pubKey) {
        PAR_ASSERT.accept(data, pubKey);

        Cipher cipher = CIPHER_SUP.get();
        try {
            cipher.init(ENCRYPT_MODE, KEY_FACTORY_SUP.get()
                    .generatePublic(new X509EncodedKeySpec(DECODER.decode(pubKey))));

            return ENCODER.encodeToString(handleBySegment(data.getBytes(DEFAULT_CHARSET), cipher, ENCRYPT));
        } catch (Exception e) {
            LOGGER.error("String encryptByPublicKey(String data, String pubKey) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * decrypt by private key
     *
     * @param secData
     * @param priKey
     * @return
     */
    public static String decryptByPrivateKey(String secData, String priKey) {
        PAR_ASSERT.accept(secData, priKey);

        Cipher cipher = CIPHER_SUP.get();
        try {
            cipher.init(DECRYPT_MODE, KEY_FACTORY_SUP.get()
                    .generatePrivate(new PKCS8EncodedKeySpec(DECODER.decode(priKey))));

            return new String(handleBySegment(DECODER.decode(secData), cipher, DECRYPT), DEFAULT_CHARSET);
        } catch (Exception e) {
            LOGGER.error("String decryptByPrivateKey(String secData, String priKey) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * sign by private key
     *
     * @param data
     * @param priKey
     * @return
     */
    public static String sign(String data, String priKey) {
        PAR_ASSERT.accept(data, priKey);

        Signature signature = SIGNATURE_SUP.get();
        try {
            signature.initSign(KEY_FACTORY_SUP.get()
                    .generatePrivate(new PKCS8EncodedKeySpec(DECODER.decode(priKey))));

            return new String(ENCODER.encode(signBySegment(data.getBytes(DEFAULT_CHARSET), signature, SIGN).sign()), DEFAULT_CHARSET);
        } catch (Exception e) {
            LOGGER.error("String sign(String data, String priKey) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * verify by public key
     *
     * @param data
     * @param sign
     * @param pubKey
     * @return
     */
    public static boolean verify(String data, String sign, String pubKey) {
        PAR_ASSERT.accept(data, pubKey);
        if (sign == null || "".equals(sign))
            throw new BlueException(BAD_REQUEST);

        Signature signature = SIGNATURE_SUP.get();
        try {
            signature.initVerify(KEY_FACTORY_SUP.get()
                    .generatePublic(new X509EncodedKeySpec(DECODER.decode(pubKey))));

            return signBySegment(data.getBytes(DEFAULT_CHARSET), signature, VERIFY).verify(DECODER.decode(sign.getBytes(DEFAULT_CHARSET)));
        } catch (Exception e) {
            LOGGER.error("boolean verify(String data, String sign, String pubKey) failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

    /**
     * generate key pair
     *
     * @return
     */
    public static KeyPair initKeyPair() {
        KeyPairGenerator generator = KEY_PAIR_GEN_SUP.get();
        generator.initialize(INIT.length);
        java.security.KeyPair keyPair = generator.generateKeyPair();

        return new KeyPair(
                ENCODER.encodeToString(keyPair.getPublic().getEncoded()),
                ENCODER.encodeToString(keyPair.getPrivate().getEncoded())
        );
    }


    /**
     * mode
     */
    enum HandleMode {
        /**
         * init
         */
        INIT(512),
        /**
         * encrypt
         */
        ENCRYPT(32),
        /**
         * decrypt
         */
        DECRYPT(64),
        /**
         * sign
         */
        SIGN(32),
        /**
         * verify
         */
        VERIFY(64);

        public final int length;

        HandleMode(int length) {
            this.length = length;
        }
    }

}
