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
import java.util.function.Function;

import static com.blue.base.constant.base.ResponseElement.BAD_REQUEST;
import static com.blue.base.constant.base.ResponseMessage.DATA_NOT_EXIST;
import static reactor.util.Loggers.getLogger;

/**
 * rpc member provider
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

    public RpcMemberServiceProvider(MemberBasicService memberBasicService) {
        this.memberBasicService = memberBasicService;
    }

    private static final Function<MemberBasic, MemberBasicInfo> MEMBER_BASIC_2_MEMBER_BASIC_INFO = MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;

    /**
     * query member basic by phone
     *
     * @param phone
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicByPhone(String phone) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> getMemberBasicByPhone(String phone), phone = {},", phone);
        return memberBasicService.getByPhone(phone)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message)))
                .toFuture();
    }

    /**
     * query member basic by email
     *
     * @param email
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicByEmail(String email) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> getMemberBasicByEmail(String email), email = {},", email);
        return memberBasicService.getByEmail(email)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(BAD_REQUEST.status, BAD_REQUEST.code, DATA_NOT_EXIST.message)))
                .toFuture();
    }

}
