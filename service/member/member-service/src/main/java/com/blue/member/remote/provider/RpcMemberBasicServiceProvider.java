package com.blue.member.remote.provider;

import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.inter.RpcMemberBasicService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.service.inter.MemberBasicService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.basic.constant.common.ResponseElement.DATA_NOT_EXIST;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;
import static reactor.core.publisher.Mono.*;

/**
 * rpc member basic provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcMemberBasicService.class,
        version = "1.0",
        methods = {
                @Method(name = "getMemberBasicInfo", async = true),
                @Method(name = "selectMemberBasicInfoByIds", async = true),
                @Method(name = "getMemberBasicInfoByPhone", async = true),
                @Method(name = "getMemberBasicInfoByEmail", async = true)
        })
public class RpcMemberBasicServiceProvider implements RpcMemberBasicService {

    private final MemberBasicService memberBasicService;

    public RpcMemberBasicServiceProvider(MemberBasicService memberBasicService) {
        this.memberBasicService = memberBasicService;
    }

    /**
     * query member by id
     *
     * @param id
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicInfo(Long id) {
        return memberBasicService.getMemberBasic(id)
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
    public CompletableFuture<List<MemberBasicInfo>> selectMemberBasicInfoByIds(List<Long> ids) {
        return memberBasicService.selectMemberBasicInfoByIds(ids).toFuture();
    }

    /**
     * query member basic by phone
     *
     * @param phone
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> getMemberBasicInfoByPhone(String phone) {
        return memberBasicService.getMemberBasicByPhone(phone)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
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
        return just(email)
                .flatMap(memberBasicService::getMemberBasicByEmail)
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(MEMBER_BASIC_2_MEMBER_BASIC_INFO)
                .toFuture();
    }

    /**
     * update member status
     *
     * @param id
     * @param status
     * @return
     */
    @Override
    public CompletableFuture<MemberBasicInfo> updateMemberBasicStatus(Long id, Integer status) {
        return just(memberBasicService.updateMemberBasicStatus(id, status)).toFuture();
    }

    /**
     * update member status batch
     *
     * @param ids
     * @param status
     * @return
     */
    @Override
    public CompletableFuture<List<MemberBasicInfo>> updateMemberBasicStatusBatch(List<Long> ids, Integer status) {
        return just(memberBasicService.updateMemberBasicStatusBatch(ids, status)).toFuture();
    }

}
