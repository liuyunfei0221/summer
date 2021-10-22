package com.blue.captcha.service.impl;

import com.blue.captcha.service.inter.VerificationCodeService;
import org.springframework.stereotype.Service;

/**
 * verification service impl
 *
 * @author liuyunfei
 * @date 2021/8/18
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    /**
     * generate a verification and send
     *
     * @param phone
     * @return
     */
    @Override
    public String generateSmsVerificationCodeWithSend(String phone) {
        return null;
    }

    /**
     * assert verification
     *
     * @param phone
     * @param verificationCode
     * @return
     */
    @Override
    public boolean assertSmsVerificationCode(String phone, String verificationCode) {
        return false;
    }

}
