package com.blue.member.remote.provider;

import com.blue.base.model.exps.BlueException;
import com.blue.member.api.inter.RpcMemberBasicService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.service.inter.MemberBasicService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.base.constant.base.ResponseElement.DATA_NOT_EXIST;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc member basic provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcMemberBasicService.class,
        version = "1.0",
        methods = {
                @Method(name = "selectMemberBasicInfoMonoByPrimaryKey", async = true),
                @Method(name = "selectMemberBasicInfoMonoByIds", async = true),
                @Method(name = "selectMemberBasicInfoByPhone", async = true),
                @Method(name = "selectMemberBasicInfoByEmail", async = true)
        })
public class RpcMemberBasicServiceProvider implements RpcMemberBasicService {

    private static final Logger LOGGER = getLogger(RpcMemberBasicServiceProvider.class);

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
    public CompletableFuture<MemberBasicInfo> selectMemberBasicInfoMonoByPrimaryKey(Long id) {
        LOGGER.info("CompletableFuture<Optional<MemberBasicInfo>> selectMemberBasicInfoMonoByPrimaryKey(Long id), id = {},", id);
        return just(id).subscribeOn(scheduler)
                .flatMap(memberBasicService::getMemberBasicMono)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(DATA_NOT_EXIST)))
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
        LOGGER.info("CompletableFuture<List<MemberBasicInfo>> selectMemberBasicInfoMonoByIds(List<Long> ids), ids = {},", ids);
        return just(ids).subscribeOn(scheduler)
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
    public CompletableFuture<MemberBasicInfo> selectMemberBasicInfoByPhone(String phone) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> selectMemberBasicInfoByPhone(String phone), phone = {},", phone);
        return just(phone).subscribeOn(scheduler)
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
    public CompletableFuture<MemberBasicInfo> selectMemberBasicInfoByEmail(String email) {
        LOGGER.info("CompletableFuture<MemberBasicInfo> selectMemberBasicInfoByEmail(String email), email = {},", email);
        return just(email).subscribeOn(scheduler)
                .flatMap(memberBasicService::getMemberBasicMonoByEmail)
                .flatMap(mbOpt -> mbOpt.map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(DATA_NOT_EXIST)))
                .toFuture();
    }

}
