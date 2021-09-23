package com.blue.secure.service.inter;

import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.api.model.ClientLoginParam;
import reactor.core.publisher.Mono;


/**
 * 成员业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberAuthService {

    /**
     * 根据手机号获取成员并校验短信验证码及状态用于登录返回
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberBasicInfo> getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam);

    /**
     * 根据手机号获取成员并校验密码及状态用于登录返回
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberBasicInfo> getMemberByPhoneWithAssertPwd(ClientLoginParam clientLoginParam);

    /**
     * 根据邮箱地址获取成员并校验密码及状态用于登录返回
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberBasicInfo> getMemberByEmailWithAssertPwd(ClientLoginParam clientLoginParam);

}
