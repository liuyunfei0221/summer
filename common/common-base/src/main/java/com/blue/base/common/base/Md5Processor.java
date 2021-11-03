package com.blue.base.common.base;


import reactor.util.Logger;

import static com.blue.base.constant.base.CommonException.BAD_REQUEST_EXP;
import static com.blue.base.constant.base.CommonException.CRYPT_FAILED_EXP;
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
     * generate md5
     *
     * @param originalData
     * @return
     */
    public static String encrypt(String originalData) {
        if (isBlank(originalData))
            throw BAD_REQUEST_EXP.exp;

        try {
            return md5DigestAsHex(originalData.getBytes(UTF_8));
        } catch (Exception e) {
            LOGGER.error("String encrypt(String originalData) failed, e = {}", e);
            throw CRYPT_FAILED_EXP.exp;
        }
    }

}
