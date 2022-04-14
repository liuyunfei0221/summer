package com.blue.base.common.base;


import com.blue.base.model.exps.BlueException;

import static com.blue.base.common.base.BlueChecker.isBlank;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * phone no util
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class PhoneProcessor {

    private static final int MIN_LEN = 4;

    /**
     * get last 4 nos from phone
     *
     * @param phone
     * @return
     */
    public static String parseLast4no(String phone) {

        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST);

        int len = phone.length();

        if (len > MIN_LEN)
            return substring(phone, len - MIN_LEN);

        return phone;
    }

}
