package com.blue.database.type;

import com.blue.base.common.base.AesProcessor;
import org.slf4j.Logger;

import java.io.File;
import java.util.List;
import java.util.Properties;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * string column encoder
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class StringColumnEncoder {

    private static final Logger LOGGER = getLogger(StringColumnEncoder.class);

    private static final AesProcessor AES_PROCESSOR;

    /**
     * data prop location
     */
    private static final String
            MESSAGES_URI = "classpath:config/data.properties";

    /**
     * salt key
     */
    private static final String SALT_KEY = "salt";

    static {
        List<File> files = getFiles(MESSAGES_URI, false);
        if (isEmpty(files))
            throw new RuntimeException("data.properties is not exist");

        Properties properties = loadProp(files.get(0));

        String salt = properties.getProperty(SALT_KEY);
        if (isBlank(salt))
            throw new RuntimeException("salt can't be blank");

        AES_PROCESSOR = new AesProcessor(salt);

        LOGGER.warn("TypeEncoder init.");
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
