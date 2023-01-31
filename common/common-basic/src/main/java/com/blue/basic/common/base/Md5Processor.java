package com.blue.basic.common.base;


import com.blue.basic.model.exps.BlueException;
import reactor.util.Logger;

import static com.blue.basic.constant.common.ResponseElement.*;
import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.util.DigestUtils.md5DigestAsHex;
import static reactor.util.Loggers.getLogger;


/**
 * md5 util
 *
 * @author liuyunfei
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
            throw new BlueException(BAD_REQUEST);

        try {
            return md5DigestAsHex(originalData.getBytes(UTF_8));
        } catch (Exception e) {
            LOGGER.error("encrypt failed, e = {}", e);
            throw new BlueException(DECRYPTION_FAILED);
        }
    }

}