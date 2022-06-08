package com.blue.member.service.impl;

import com.blue.auth.api.model.MemberRoleInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.remote.consumer.RpcRoleServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberAuthorityService;
import com.blue.member.service.inter.MemberBasicService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.ConstantProcessor.getSortTypeByIdentity;
import static com.blue.base.constant.common.ResponseElement.EMPTY_PARAM;
import static com.blue.base.constant.common.ResponseElement.INVALID_PARAM;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_INFO;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * member authority service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberAuthorityServiceImpl implements MemberAuthorityService {

    private static final Logger LOGGER = getLogger(MemberAuthorityServiceImpl.class);

    private final MemberBasicService memberBasicService;

    private final RpcRoleServiceConsumer rpcRoleServiceConsumer;

    public MemberAuthorityServiceImpl(MemberBasicService memberBasicService, RpcRoleServiceConsumer rpcRoleServiceConsumer) {
        this.memberBasicService = memberBasicService;
        this.rpcRoleServiceConsumer = rpcRoleServiceConsumer;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberBasicSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<MemberBasicCondition> CONDITION_PROCESSOR = condition -> {
        if (isNull(condition))
            return new MemberBasicCondition();

        condition.setSortAttribute(
                ofNullable(condition.getSortAttribute())
                        .filter(BlueChecker::isNotBlank)
                        .map(SORT_ATTRIBUTE_MAPPING::get)
                        .filter(BlueChecker::isNotBlank)
                        .orElseThrow(() -> new BlueException(INVALID_PARAM)));

        condition.setSortType(getSortTypeByIdentity(condition.getSortType()).identity);

        return condition;
    };

    /**
     * select member's authority info by page and condition
     *
     * @param pageModelRequest
     * @return
     */
    @Override
    public Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityPageMonoByPageAndCondition(PageModelRequest<MemberBasicCondition> pageModelRequest) {
        LOGGER.info("Mono<PageModelResponse<MemberAuthorityInfo>> selectMemberAuthorityPageMonoByPageAndCondition(PageModelRequest<MemberCondition> pageModelRequest), " +
                "pageModelRequest = {}", pageModelRequest);
        if (isNull(pageModelRequest))
            throw new BlueException(EMPTY_PARAM);

        MemberBasicCondition memberBasicCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(memberBasicService.selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberBasicCondition), memberBasicService.countMemberBasicMonoByCondition(memberBasicCondition))
                .flatMap(tuple2 -> {
                    List<MemberBasic> members = tuple2.getT1();
                    Mono<List<MemberAuthorityInfo>> memberAuthorityInfosMono = members.size() > 0 ?
                            rpcRoleServiceConsumer.selectRoleInfoByMemberIds(members.stream().map(MemberBasic::getId).collect(toList()))
                                    .flatMap(relationInfos -> {
                                        Map<Long, RoleInfo> memberIdAndRoleInfoMapping = relationInfos.stream().collect(toMap(MemberRoleInfo::getMemberId, MemberRoleInfo::getRoleInfo, (a, b) -> b));
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
