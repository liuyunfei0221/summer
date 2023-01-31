package com.blue.basic.component.encoder.api.common;

import com.blue.basic.common.base.AesProcessor;
import org.slf4j.Logger;

import java.util.concurrent.atomic.AtomicBoolean;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * string column encoder
 *
 * @author liuyunfei
 *
 * <p>
 * init by
 * @see com.blue.basic.component.encoder.ioc.StringColumnEncoderInitializer
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class StringColumnEncoder {

    private static final Logger LOGGER = getLogger(StringColumnEncoder.class);

    private static final AtomicBoolean COMPLETE = new AtomicBoolean(false);

    private static AesProcessor AES_PROCESSOR;

    /**
     * init encoder
     *
     * @param salt
     */
    public static void init(String salt) {
        if (!COMPLETE.compareAndSet(false, true)) {
            LOGGER.error("StringColumnEncoder already init");
            return;
        }

        if (isBlank(salt))
            throw new RuntimeException("salt can't be blank");

        AES_PROCESSOR = new AesProcessor(salt);

        LOGGER.warn("StringColumnEncoder init.");
    }

    /**
     * encrypt String
     *
     * @param originalData
     * @return
     */
    public static String encryptString(String originalData) {
        return AES_PROCESSOR.encrypt(originalData);
    }

    /**
     * decrypt String
     *
     * @param encryptData
     * @return
     */
    public static String decryptString(String encryptData) {
        return AES_PROCESSOR.decrypt(encryptData);
    }

}