package com.blue.secure.service.impl;

import com.blue.base.constant.base.Status;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.secure.api.model.ClientLoginParam;
import com.blue.secure.remote.consumer.RpcMemberServiceConsumer;
import com.blue.secure.service.inter.MemberAuthService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * 用户业务实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "DuplicatedCode", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private static final Logger LOGGER = getLogger(MemberAuthServiceImpl.class);

    private final RpcMemberServiceConsumer rpcMemberServiceConsumer;

    public MemberAuthServiceImpl(RpcMemberServiceConsumer rpcMemberServiceConsumer) {
        this.rpcMemberServiceConsumer = rpcMemberServiceConsumer;
    }

    /**
     * 加解密
     */
    private static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    /**
     * 密码校验器
     */
    private static final BiConsumer<String, MemberBasicInfo> PWD_ASSERTER = (access, mb) -> {
        if (access == null || mb == null || !ENCODER.matches(access, mb.getPassword()))
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "账号或密码错误");
    };

    /**
     * member状态断言
     */
    private static final Consumer<MemberBasicInfo> MEMBER_STATUS_ASSERTER = memberBasicInfo -> {
        if (Status.VALID.status != memberBasicInfo.getStatus())
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "账号已冻结");
    };

    /**
     * 根据手机号获取成员并校验密码及状态用于登录返回
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam) {
        LOGGER.info("getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (clientLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "登录信息不能为空");

        //TODO 校验验证码
        //TODO 校验短信验证码

        return rpcMemberServiceConsumer.getMemberBasicByPhone(clientLoginParam.getIdentity())
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    MEMBER_STATUS_ASSERTER.accept(memberBasicInfo);

                    memberBasicInfo.setPassword("");
                    return just(memberBasicInfo);
                });
    }

    /**
     * 根据手机号获取成员并校验密码及状态用于登录返回
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> getMemberByPhoneWithAssertPwd(ClientLoginParam clientLoginParam) {
        LOGGER.info("getMemberByPhoneWithAssertVerify(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (clientLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "登录信息不能为空");

        //TODO 校验验证码

        return rpcMemberServiceConsumer.getMemberBasicByPhone(clientLoginParam.getIdentity())
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    PWD_ASSERTER.accept(clientLoginParam.getAccess(), memberBasicInfo);
                    MEMBER_STATUS_ASSERTER.accept(memberBasicInfo);

                    memberBasicInfo.setPassword("");
                    return just(memberBasicInfo);
                });
    }

    /**
     * 根据邮箱地址获取成员并校验密码及状态用于登录返回
     *
     * @param clientLoginParam
     * @return
     */
    @Override
    public Mono<MemberBasicInfo> getMemberByEmailWithAssertPwd(ClientLoginParam clientLoginParam) {
        LOGGER.info("getMemberByEmailWithAssertPwd(ClientLoginParam clientLoginParam), clientLoginParam = {}", clientLoginParam);
        if (clientLoginParam == null)
            throw new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "登录信息不能为空");

        //TODO 校验验证码

        return rpcMemberServiceConsumer.getMemberBasicByEmail(clientLoginParam.getIdentity())
                .flatMap(memberBasicInfo -> {
                    LOGGER.info("memberBasicInfo = {}", memberBasicInfo);
                    PWD_ASSERTER.accept(clientLoginParam.getAccess(), memberBasicInfo);
                    MEMBER_STATUS_ASSERTER.accept(memberBasicInfo);

                    memberBasicInfo.setPassword("");
                    return just(memberBasicInfo);
                });
    }

}
