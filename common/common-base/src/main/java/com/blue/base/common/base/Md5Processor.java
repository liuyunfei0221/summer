package com.blue.base.common.base;


import reactor.util.Logger;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.DigestUtils.md5DigestAsHex;
import static reactor.util.Loggers.getLogger;


/**
 * md5 util
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "unused"})
public final class Md5Processor {

    private static final Logger LOGGER = getLogger(Md5Processor.class);

    /**
     * 生成md5
     *
     * @param originalData
     * @return
     */
    public static String encrypt(String originalData) {
        if (isBlank(originalData))
            throw new RuntimeException("originalData can't be blank");

        try {
            return md5DigestAsHex(originalData.getBytes(UTF_8));
        } catch (Exception e) {
            LOGGER.error("String encrypt(String originalData) failed, e = {}", e);
            throw new RuntimeException("String encrypt(String originalData) failed, e = " + e);
        }
    }

}
