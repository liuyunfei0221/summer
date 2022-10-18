package com.blue.member.service.impl;

import com.blue.auth.api.model.MemberRoleInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.common.PageModelRequest;
import com.blue.basic.model.common.PageModelResponse;
import com.blue.basic.model.exps.BlueException;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.component.credential.CredentialCollectProcessor;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.remote.consumer.RpcFinanceControlServiceConsumer;
import com.blue.member.remote.consumer.RpcRoleServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberAuthService;
import com.blue.member.service.inter.MemberBasicService;
import com.blue.member.service.inter.MemberDetailService;
import com.blue.member.service.inter.RealNameService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.common.base.ConstantProcessor.getSortTypeByIdentity;
import static com.blue.basic.constant.common.ResponseElement.*;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_BASIC_INFO;
import static com.blue.member.converter.MemberModelConverters.MEMBER_REGISTRY_INFO_2_MEMBER_BASIC;
import static java.util.Collections.emptyList;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.core.publisher.Mono.just;
import static reactor.core.publisher.Mono.zip;
import static reactor.util.Loggers.getLogger;

/**
 * member register service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private static final Logger LOGGER = getLogger(MemberAuthServiceImpl.class);

    private final MemberBasicService memberBasicService;

    private final MemberDetailService memberDetailService;

    private final RealNameService realNameService;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final CredentialCollectProcessor credentialCollectProcessor;

    private final RpcRoleServiceConsumer rpcRoleServiceConsumer;

    private final RpcFinanceControlServiceConsumer rpcFinanceControlServiceConsumer;

    public MemberAuthServiceImpl(MemberBasicService memberBasicService, MemberDetailService memberDetailService, RealNameService realNameService,
                                 BlueIdentityProcessor blueIdentityProcessor, CredentialCollectProcessor credentialCollectProcessor,
                                 RpcRoleServiceConsumer rpcRoleServiceConsumer, RpcFinanceControlServiceConsumer rpcFinanceControlServiceConsumer) {
        this.memberBasicService = memberBasicService;
        this.memberDetailService = memberDetailService;
        this.realNameService = realNameService;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.credentialCollectProcessor = credentialCollectProcessor;
        this.rpcRoleServiceConsumer = rpcRoleServiceConsumer;
        this.rpcFinanceControlServiceConsumer = rpcFinanceControlServiceConsumer;
    }

    private static final Map<String, String> SORT_ATTRIBUTE_MAPPING = Stream.of(MemberBasicSortAttribute.values())
            .collect(toMap(e -> e.attribute, e -> e.column, (a, b) -> a));

    private static final UnaryOperator<MemberBasicCondition> CONDITION_PROCESSOR = c -> {
        if (isNull(c))
            return new MemberBasicCondition();

        c.setSortAttribute(
                ofNullable(c.getSortAttribute())
                        .filter(BlueChecker::isNotBlank)
                        .map(SORT_ATTRIBUTE_MAPPING::get)
                        .filter(BlueChecker::isNotBlank)
                        .orElseThrow(() -> new BlueException(INVALID_PARAM)));

        c.setSortType(getSortTypeByIdentity(c.getSortType()).identity);

        return c;
    };

    /**
     * member register for auto registry or third party session
     *
     * @param memberRegistryParam
     * @return
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo registerMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo registerMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);
        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);

        if (isEmpty(credentialCollectProcessor.collect(memberBasic, memberRegistryParam.getAccess())))
            throw new BlueException(BAD_REQUEST);

        long id = blueIdentityProcessor.generate(MemberBasic.class);
        memberBasic.setId(id);

        rpcFinanceControlServiceConsumer.initMemberFinanceInfo(new MemberFinanceInfo(id));

        MemberBasicInfo memberBasicInfo = memberBasicService.insertMemberBasic(memberBasic);
        LOGGER.info("autoRegisterMemberBasic -> memberBasicInfo = {}", memberBasicInfo);

        memberDetailService.initMemberDetail(id);
        realNameService.initRealName(id);

        return memberBasicInfo;
    }

    /**
     * package credential attribute to member basic
     *
     * @param credentialTypes
     * @param credential
     * @param memberId
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId) {
        LOGGER.info("MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId), credentialTypes = {}, credential = {}, memberId = {}",
                credentialTypes, credential, memberId);

        MemberBasic memberBasic = memberBasicService.getMemberBasic(memberId);
        credentialCollectProcessor.packageCredentialAttr(credentialTypes, credential, memberBasic);

        return memberBasicService.updateMemberBasic(memberBasic);
    }

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

        MemberBasicCondition memberBasicCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getCondition());

        return zip(memberBasicService.selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberBasicCondition), memberBasicService.countMemberBasicMonoByCondition(memberBasicCondition))
                .flatMap(tuple2 -> {
                    List<MemberBasic> members = tuple2.getT1();
                    Long count = tuple2.getT2();
                    Mono<List<MemberAuthorityInfo>> memberAuthorityInfosMono = members.size() > 0 ?
                            rpcRoleServiceConsumer.selectRoleInfoByMemberIds(members.stream().map(MemberBasic::getId).collect(toList()))
                                    .flatMap(relationInfos -> {
                                        Map<Long, List<RoleInfo>> memberIdAndRoleInfoMapping = relationInfos.stream().collect(toMap(MemberRoleInfo::getMemberId, MemberRoleInfo::getRoleInfos, (a, b) -> b));
                                        return just(members.stream()
                                                .map(memberBasic ->
                                                        new MemberAuthorityInfo(MEMBER_BASIC_2_MEMBER_BASIC_INFO.apply(memberBasic), memberIdAndRoleInfoMapping.get(memberBasic.getId()))
                                                ).collect(toList()));
                                    })
                            :
                            just(emptyList());

                    return memberAuthorityInfosMono
                            .flatMap(memberAuthorityInfos ->
                                    just(new PageModelResponse<>(memberAuthorityInfos, count)));
                });
    }

}
