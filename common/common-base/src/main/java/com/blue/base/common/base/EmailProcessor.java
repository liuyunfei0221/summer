package com.blue.base.common.base;


import com.blue.base.model.exps.BlueException;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static org.apache.commons.lang3.StringUtils.split;

/**
 * email address util
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class EmailProcessor {

    private static final String SPLIT = "@";
    private static final int MIN_ELE_LEN = 2;

    /**
     * get prefix from email
     *
     * @param email
     * @return
     */
    public static String parsePrefix(String email) {

        if (isBlank(email))
            throw new BlueException(BAD_REQUEST);

        String[] elements = split(email, SPLIT);

        if (elements.length < MIN_ELE_LEN)
            throw new BlueException(BAD_REQUEST);

        return elements[0];
    }

}