package com.blue.member.remote.provider;

import com.blue.base.model.exps.BlueException;
import com.blue.member.api.inter.RpcMemberService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.converter.MemberModelConverters;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberBasicService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;

import static com.blue.base.common.reactive.ReactiveCommonFunctions.converterMonoToFuture;
import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static reactor.util.Loggers.getLogger;

/**
 * 认证鉴权RPC实现
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcMemberService.class, version = "1.0", methods = {
        @Method(name = "getMemberBasicByPhone", async = true),
        @Method(name = "getMemberBasicByEmail", async = true)
})
public class RpcMemberServiceProvider implements RpcMemberService {

    private static final Logger LOGGER = getLogger(RpcMemberServiceProvider.class);

    private final MemberBasicService memberBasicService;

    private final ExecutorService executorService;

    public RpcMemberServiceProvider(MemberBasicService memberBasicService, ExecutorService executorService) {
        this.memberBasicService = memberBasicService;
        this.executorService = executorService;
    }

    /**
     * DTO转换器
     */
    private static final Function<MemberBasic, MemberBasicInfo> MEMBER_BASIC_2_MEMBER_BASIC_INFO = MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;

    /**
     * 根据手机号获取成员关键信息
     *
     * @param phone
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicByPhone(String phone) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> getMemberBasicByPhone(String phone), phone = {},", phone);
        return converterMonoToFuture(memberBasicService.getByPhone(phone)
                .flatMap(mbOpt -> {
                    LOGGER.info("mbOpt = {},", mbOpt);
                    return mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                            .map(Mono::just)
                            .orElseThrow(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "帐户名或密码错误"));
                }), executorService);
    }

    /**
     * 根据邮箱地址获取成员关键信息
     *
     * @param email
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicByEmail(String email) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> getMemberBasicByEmail(String email), email = {},", email);
        return converterMonoToFuture(memberBasicService.getByEmail(email)
                .flatMap(mbOpt -> {
                    LOGGER.info("mbOpt = {},", mbOpt);
                    return mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                            .map(Mono::just)
                            .orElseThrow(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, "帐户名或密码错误"));
                }), executorService);
    }

}
