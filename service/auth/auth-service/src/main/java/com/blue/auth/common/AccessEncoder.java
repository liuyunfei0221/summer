package com.blue.auth.common;

import com.blue.base.constant.common.BlueNumericalValue;
import com.blue.base.model.exps.BlueException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.function.BiPredicate;
import java.util.function.UnaryOperator;

import static com.blue.base.common.base.BlueChecker.isNotBlank;
import static com.blue.base.constant.common.ResponseElement.BAD_REQUEST;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * BCrypt encoder
 *
 * @author liuyunfei
 */
@SuppressWarnings({"AliControlFlowStatementWithoutBraces", "JavaDoc"})
public final class AccessEncoder {

    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    private static final UnaryOperator<String> ACCESS_ENCODER = access -> {
        if (isBlank(access))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access can't be blank");
        if (access.length() > BlueNumericalValue.ACS_LEN_MAX.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too long");
        if (access.length() < BlueNumericalValue.ACS_LEN_MIN.value)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "access length is too short");

        return ENCODER.encode(access);
    };

    private static final BiPredicate<String, String> ACCESS_MATCHER = (access, encryptedAccess) ->
            isNotBlank(access) && isNotBlank(encryptedAccess) && ENCODER.matches(access, encryptedAccess);

    /**
     * encrypt access(password)
     *
     * @param access
     * @return
     */
    public static String encryptAccess(String access) {
        return ACCESS_ENCODER.apply(access);
    }

    /**
     * match access
     *
     * @param access
     * @param encryptedAccess
     * @return
     */
    public static boolean matchAccess(String access, String encryptedAccess) {
        return ACCESS_MATCHER.test(access, encryptedAccess);
    }

}
