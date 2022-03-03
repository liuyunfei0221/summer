package com.blue.database.type;

import com.blue.base.common.base.AesProcessor;

import java.io.File;
import java.util.List;
import java.util.Properties;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.common.base.FileGetter.getFiles;
import static com.blue.base.common.base.PropertiesProcessor.loadProp;

@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
final class StringEncoderHolder {

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
        if (files == null || files.size() != 1)
            throw new RuntimeException("data.properties is not exist or more than 1");

        Properties properties = loadProp(files.get(0));

        String salt = properties.getProperty(SALT_KEY);
        if (isBlank(salt))
            throw new RuntimeException("salt can't be blank");

        AES_PROCESSOR = new AesProcessor(salt);
    }

    /**
     * get encoder
     *
     * @return
     */
    public static AesProcessor getProcessor() {
        return AES_PROCESSOR;
    }

}
