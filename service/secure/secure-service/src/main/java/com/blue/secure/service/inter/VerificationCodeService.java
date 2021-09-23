package com.blue.secure.service.inter;

/**
 * 验证码业务接口
 *
 * @author liuyunfei
 * @date 2021/8/18
 * @apiNote
 */
@SuppressWarnings("JavaDoc")
public interface VerificationCodeService {

    /**
     * 根据手机号获取短信验证码并发送至对应手机号
     *
     * @param phone
     * @return
     */
    String getSmsVerificationCodeWithSend(String phone);

    /**
     * 校验短信验证码
     *
     * @param phone
     * @param verificationCode
     * @return
     */
    boolean assertSmsVerificationCode(String phone, String verificationCode);

}
