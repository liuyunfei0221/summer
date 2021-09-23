package com.blue.secure.service.impl;

import com.blue.secure.service.inter.VerificationCodeService;
import org.springframework.stereotype.Service;

/**
 * 验证码业务接口
 *
 * @author liuyunfei
 * @date 2021/8/18
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
@Service
public class VerificationCodeServiceImpl implements VerificationCodeService {

    /**
     * 根据手机号获取短信验证码并发送至对应手机号
     *
     * @param phone
     * @return
     */
    @Override
    public String getSmsVerificationCodeWithSend(String phone) {
        return null;
    }

    /**
     * 校验短信验证码
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
