package com.blue.member.service.impl;

import com.blue.base.model.base.PageModelRequest;
import com.blue.base.model.base.PageModelResponse;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberCondition;
import com.blue.member.remote.consumer.RpcRoleServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberAuthorityService;
import com.blue.member.service.inter.MemberBasicService;
import com.blue.secure.api.model.MemberRoleRelationInfo;
import com.blue.secure.api.model.RoleInfo;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;

import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_INFO;
import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * member authority service impl
 *
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
    public Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityPageMonoByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityPageMonoByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);

        MemberCondition memberCondition = pageModelRequest.getParam();

        return memberBasicService.countMemberBasicMonoByCondition(memberCondition)
                .flatMap(memberCount -> {
                    Mono<List<MemberBasic>> listMono = memberCount > 0L ? memberBasicService.selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberCondition) : just(emptyList());
                    return zip(listMono, just(memberCount));
                })
                .flatMap(tuple2 -> {
                    List<MemberBasic> members = tuple2.getT1();
                    Mono<List<MemberAuthorityInfo>> memberAuthorityInfosMono = members.size() > 0 ?
                            rpcRoleServiceConsumer.selectRoleInfoByMemberIds(members.stream().map(MemberBasic::getId).collect(toList()))
                                    .flatMap(relationInfos -> {
                                        Map<Long, RoleInfo> memberIdAndRoleInfoMapping = relationInfos.stream().collect(toMap(MemberRoleRelationInfo::getMemberId, MemberRoleRelationInfo::getRoleInfo, (a, b) -> b));
                                        return just(members.stream()
                                                .map(memberBasic ->
                                                        new MemberAuthorityInfo(MEMBER_BASIC_2_MEMBER_INFO.apply(memberBasic), memberIdAndRoleInfoMapping.get(memberBasic.getId()))
                                                ).collect(toList()));
                                    })
                            :
                            just(emptyList());

                    return memberAuthorityInfosMono
                            .flatMap(memberAuthorityInfos ->
                                    just(new PageModelResponse<>(memberAuthorityInfos, tuple2.getT2())));
                });
    }

}