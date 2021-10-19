package com.blue.member.service.impl;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberCondition;
import com.blue.member.remote.consumer.RpcRoleServiceConsumer;
import com.blue.member.service.inter.MemberAuthorityService;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * @author liuyunfei
 * @date 2021/10/19
 * @apiNote
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class MemberAuthorityServiceImpl implements MemberAuthorityService {

    private static final Logger LOGGER = getLogger(MemberAuthorityServiceImpl.class);

    private final MemberBasicService memberBasicService;

    private final RpcRoleServiceConsumer rpcRoleServiceConsumer;

    public MemberAuthorityServiceImpl(MemberBasicService memberBasicService, RpcRoleServiceConsumer rpcRoleServiceConsumer) {
        this.memberBasicService = memberBasicService;
        this.rpcRoleServiceConsumer = rpcRoleServiceConsumer;
    }

    /**
     * select member's authority info by page and condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest), pageModelRequest = {}", pageModelRequest);

        MemberCondition condition = pageModelRequest.getParam();


        return null;
    }

}
