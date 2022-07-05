package com.blue.member.remote.provider;

import com.blue.base.model.exps.BlueException;
import com.blue.member.api.inter.RpcMemberBasicService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.service.inter.MemberBasicService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.constant.common.ResponseElement.DATA_NOT_EXIST;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;
import static reactor.core.publisher.Mono.just;

/**
 * rpc member basic provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcMemberBasicService.class,
        version = "1.0",
        methods = {
                @Method(name = "getMemberBasicInfoMonoByPrimaryKey", async = true),
                @Method(name = "selectMemberBasicInfoMonoByIds", async = true),
                @Method(name = "getMemberBasicInfoByPhone", async = true),
                @Method(name = "getMemberBasicInfoByEmail", async = true)
        })
public class RpcMemberBasicServiceProvider implements RpcMemberBasicService {

    private final MemberBasicService memberBasicService;

    private final Scheduler scheduler;

    public RpcMemberBasicServiceProvider(MemberBasicService memberBasicService, Scheduler scheduler) {
        this.memberBasicService = memberBasicService;
        this.scheduler = scheduler;
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicInfoMonoByPrimaryKey(Long id) {
        return just(id).publishOn(scheduler)
                .flatMap(memberBasicService::getMemberBasicMono)
                .map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                .toFuture();
    }

    /**
     * select member basic by ids
     *
     * @param ids
     * @return
     */
    @Override
    public CompletableFuture<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids) {
        return just(ids).publishOn(scheduler)
                .flatMap(memberBasicService::selectMemberBasicInfoMonoByIds)
                .toFuture();
    }

    /**
     * query member basic by phone
     *
     * @param phone
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicInfoByPhone(String phone) {
        return just(phone).publishOn(scheduler)
                .flatMap(memberBasicService::getMemberBasicMonoByPhone)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(DATA_NOT_EXIST)))
                .toFuture();
    }

    /**
     * query member basic by email
     *
     * @param email
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicInfoByEmail(String email) {
        return just(email).publishOn(scheduler)
                .flatMap(memberBasicService::getMemberBasicMonoByEmail)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(DATA_NOT_EXIST)))
                .toFuture();
    }

}
