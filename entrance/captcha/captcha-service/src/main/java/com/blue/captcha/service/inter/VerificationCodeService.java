package com.blue.captcha.service.inter;

/**
 * verification service
 *
 * @author liuyunfei
 * @date 2021/8/18
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface VerificationCodeService {

    /**
     * generate a verification and send
     *
     * @param phone
     * @return
     */
    String generateSmsVerificationCodeWithSend(String phone);

    /**
     * assert verification
     *
     * @param phone
     * @param verificationCode
     * @return
     */
    boolean assertSmsVerificationCode(String phone, String verificationCode);

}
