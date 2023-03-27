package com.blue.basic.common.base;

import com.blue.basic.model.exps.BlueException;

import static com.blue.basic.common.base.BlueChecker.isBlank;
import static com.blue.basic.constant.common.ResponseElement.BAD_REQUEST;
import static com.blue.basic.constant.common.Symbol.AT;
import static com.blue.basic.constant.common.Symbol.HYPHEN;
import static org.apache.commons.lang3.StringUtils.split;
import static org.apache.commons.lang3.StringUtils.substring;

/**
 * element processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
public final class BasicElementProcessor {

    private static final int MIN_EMAIL_LEN = 2;
    private static final int MIN_PHONE_LEN = 4;

    /**
     * assert phone
     *
     * @param phone
     */
    public static void assertPhone(String phone) {
        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST);

        if (!phone.contains(HYPHEN.identity))
            throw new BlueException(BAD_REQUEST);
    }

    /**
     * assert email
     *
     * @param email
     */
    public static void assertEmail(String email) {
        if (isBlank(email))
            throw new BlueException(BAD_REQUEST);

        if (!email.contains(AT.identity))
            throw new BlueException(BAD_REQUEST);
    }

    /**
     * get last 4 nos from phone
     *
     * @param phone
     * @return
     */
    public static String parseLast4noForPhone(String phone) {
        if (isBlank(phone))
            throw new BlueException(BAD_REQUEST);

        int len = phone.length();

        if (len > MIN_PHONE_LEN)
            return substring(phone, len - MIN_PHONE_LEN);

        return phone;
    }

    /**
     * get prefix from email
     *
     * @param email
     * @return
     */
    public static String parsePrefixForEmail(String email) {

        if (isBlank(email))
            throw new BlueException(BAD_REQUEST);

        String[] elements = split(email, AT.identity);

        if (elements.length < MIN_EMAIL_LEN)
            throw new BlueException(BAD_REQUEST);

        return elements[0];
    }


}
