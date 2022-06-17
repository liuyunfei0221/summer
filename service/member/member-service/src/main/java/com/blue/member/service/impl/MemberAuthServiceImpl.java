package com.blue.member.service.impl;

import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.api.model.MemberRoleInfo;
import com.blue.auth.api.model.RoleInfo;
import com.blue.base.common.base.BlueChecker;
import com.blue.base.model.common.PageModelRequest;
import com.blue.base.model.common.PageModelResponse;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.identity.component.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.component.credential.CredentialCollectProcessor;
import com.blue.member.constant.MemberBasicSortAttribute;
import com.blue.member.model.MemberAuthorityInfo;
import com.blue.member.model.MemberBasicCondition;
import com.blue.member.remote.consumer.RpcAuthControlServiceConsumer;
import com.blue.member.remote.consumer.RpcFinanceControlServiceConsumer;
import com.blue.member.remote.consumer.RpcRoleServiceConsumer;
import com.blue.member.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberAuthService;
import com.blue.member.service.inter.MemberBasicService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.common.base.ConstantProcessor.getSortTypeByIdentity;
import static com.blue.base.constant.common.ResponseElement.*;
import static com.blue.base.constant.verify.BusinessType.REGISTER;
import static com.blue.base.constant.verify.VerifyType.MAIL;
import static com.blue.base.constant.verify.VerifyType.SMS;
import static com.blue.member.converter.MemberModelConverters.MEMBER_BASIC_2_MEMBER_INFO;
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

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final CredentialCollectProcessor credentialCollectProcessor;

    private final RpcRoleServiceConsumer rpcRoleServiceConsumer;

    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final RpcAuthControlServiceConsumer rpcAuthControlServiceConsumer;

    private final RpcFinanceControlServiceConsumer rpcFinanceControlServiceConsumer;

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    public MemberAuthServiceImpl(MemberBasicService memberBasicService, BlueIdentityProcessor blueIdentityProcessor, CredentialCollectProcessor credentialCollectProcessor,
                                 RpcRoleServiceConsumer rpcRoleServiceConsumer, RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer,
                                 RpcAuthControlServiceConsumer rpcAuthControlServiceConsumer, RpcFinanceControlServiceConsumer rpcFinanceControlServiceConsumer) {
        this.memberBasicService = memberBasicService;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.credentialCollectProcessor = credentialCollectProcessor;
        this.rpcRoleServiceConsumer = rpcRoleServiceConsumer;
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.rpcAuthControlServiceConsumer = rpcAuthControlServiceConsumer;
        this.rpcFinanceControlServiceConsumer = rpcFinanceControlServiceConsumer;
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
     * member registry
     *
     * @param memberRegistryParam
     * @return
     */
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo registerMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo registerMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);
        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);
        memberRegistryParam.asserts();

        if (!rpcVerifyHandleServiceConsumer.validate(SMS, REGISTER, memberRegistryParam.getPhone(), memberRegistryParam.getPhoneVerify(), true)
                .toFuture().join())
            throw new BlueException(VERIFY_IS_INVALID);
        if (!rpcVerifyHandleServiceConsumer.validate(MAIL, REGISTER, memberRegistryParam.getEmail(), memberRegistryParam.getEmailVerify(), true)
                .toFuture().join())
            throw new BlueException(VERIFY_IS_INVALID);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);

        long id = blueIdentityProcessor.generate(MemberBasic.class);
        memberBasic.setId(id);

        //init auth info
        rpcAuthControlServiceConsumer.initMemberAuthInfo(new MemberCredentialInfo(id, credentialCollectProcessor.collect(memberBasic, memberRegistryParam.getAccess())));

        //init finance account
        rpcFinanceControlServiceConsumer.initMemberFinanceInfo(new MemberFinanceInfo(id));

        @SuppressWarnings("UnnecessaryLocalVariable")
        MemberBasicInfo memberBasicInfo = memberBasicService.insertMemberBasic(memberBasic);

//        if (1 == 1)
//            throw new BlueException(666, 666, "test rollback");

        return memberBasicInfo;
    }

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    @SuppressWarnings("CommentedOutCode")
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo simpleRegisterMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);
        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);

        if (isEmpty(credentialCollectProcessor.collect(memberBasic, memberRegistryParam.getAccess())))
            throw new BlueException(BAD_REQUEST);

        long id = blueIdentityProcessor.generate(MemberBasic.class);
        memberBasic.setId(id);

        //init finance account
        rpcFinanceControlServiceConsumer.initMemberFinanceInfo(new MemberFinanceInfo(id));

        @SuppressWarnings("UnnecessaryLocalVariable")
        MemberBasicInfo memberBasicInfo = memberBasicService.insertMemberBasic(memberBasic);

//        if (1 == 1)
//            throw new BlueException(666, 666, "test rollback");

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

        Optional<MemberBasic> memberBasicOpt = memberBasicService.getMemberBasic(memberId);
        if (memberBasicOpt.isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        MemberBasic memberBasic = memberBasicOpt.get();

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

        MemberBasicCondition memberBasicCondition = CONDITION_PROCESSOR.apply(pageModelRequest.getParam());

        return zip(memberBasicService.selectMemberBasicMonoByLimitAndCondition(pageModelRequest.getLimit(), pageModelRequest.getRows(), memberBasicCondition), memberBasicService.countMemberBasicMonoByCondition(memberBasicCondition))
                .flatMap(tuple2 -> {
                    List<MemberBasic> members = tuple2.getT1();
                    Mono<List<MemberAuthorityInfo>> memberAuthorityInfosMono = members.size() > 0 ?
                            rpcRoleServiceConsumer.selectRoleInfoByMemberIds(members.stream().map(MemberBasic::getId).collect(toList()))
                                    .flatMap(relationInfos -> {
                                        Map<Long, List<RoleInfo>> memberIdAndRoleInfoMapping = relationInfos.stream().collect(toMap(MemberRoleInfo::getMemberId, MemberRoleInfo::getRoleInfos, (a, b) -> b));
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
