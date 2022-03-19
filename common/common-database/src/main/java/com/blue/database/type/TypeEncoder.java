package com.blue.database.type;

import com.blue.base.common.base.AesProcessor;

import java.io.File;
import java.util.List;
import java.util.Properties;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;

@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
final class TypeEncoder {

    private static final transient AesProcessor AES_PROCESSOR;

    /**
     * data prop location
     */
    private static final transient String
            MESSAGES_URI = "classpath:config/data.properties";

    /**
     * salt key
     */
    private static final transient String SALT_KEY = "salt";

    static {
        List<File> files = getFiles(MESSAGES_URI, false);
        if (files == null || files.size() < 1)
            throw new RuntimeException("data.properties is not exist");

        Properties properties = loadProp(files.get(0));

        String salt = properties.getProperty(SALT_KEY);
        if (isBlank(salt))
            throw new RuntimeException("salt can't be blank");

        AES_PROCESSOR = new AesProcessor(salt);
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
