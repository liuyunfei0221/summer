package com.blue.member.remote.provider;

import com.blue.member.api.inter.RpcMemberService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.service.inter.MemberBasicService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.constant.base.CommonException.DATA_NOT_EXIST_EXP;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;
import static java.util.stream.Collectors.toList;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc member provider
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcMemberService.class, version = "1.0", methods = {
        @Method(name = "selectMemberBasicMonoByPrimaryKey", async = true),
        @Method(name = "selectMemberBasicMonoByIds", async = true),
        @Method(name = "selectMemberBasicByPhone", async = true),
        @Method(name = "selectMemberBasicByEmail", async = true)
})
public class RpcMemberServiceProvider implements RpcMemberService {

    private static final Logger LOGGER = getLogger(RpcMemberServiceProvider.class);

    private final MemberBasicService memberBasicService;

    public RpcMemberServiceProvider(MemberBasicService memberBasicService) {
        this.memberBasicService = memberBasicService;
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> selectMemberBasicMonoByPrimaryKey(Long id) {
        LOGGER.info("CompletableFuture<Optional<MemberBasicInfo>> selectMemberBasicMonoByPrimaryKey(Long id), id = {},", id);
        return memberBasicService.selectMemberBasicMonoByPrimaryKey(id)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> DATA_NOT_EXIST_EXP.exp))
                .toFuture();
    }

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<List<MemberBasicInfo>> selectMemberBasicMonoByIds(List<Long> ids) {
        LOGGER.info("CompletableFuture<List<MemberBasicInfo>> selectMemberBasicMonoByIds(List<Long> ids), ids = {},", ids);
        return memberBasicService.selectMemberBasicMonoByIds(ids)
                .flatMap(l -> just(l.stream().map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .collect(toList())))
                .toFuture();
    }

    /**
     * query member basic by phone
     *
     * @param phone
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> selectMemberBasicByPhone(String phone) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> selectMemberBasicByPhone(String phone), phone = {},", phone);
        return memberBasicService.selectMemberBasicMonoByPhone(phone)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> DATA_NOT_EXIST_EXP.exp))
                .toFuture();
    }

    /**
     * query member basic by email
     *
     * @param email
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> selectMemberBasicByEmail(String email) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> selectMemberBasicByEmail(String email), email = {},", email);
        return memberBasicService.selectMemberBasicMonoByEmail(email)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> DATA_NOT_EXIST_EXP.exp))
                .toFuture();
    }

}
