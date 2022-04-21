package com.blue.member.service.impl;

import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.remote.consumer.RpcControlServiceConsumer;
import com.blue.member.remote.consumer.RpcFinanceAccountServiceConsumer;
import com.blue.member.remote.consumer.RpcVerifyHandleServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberBasicService;
import com.blue.member.service.inter.MemberAuthService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.List;
import java.util.Optional;

import static com.blue.base.common.base.BlueChecker.isEmpty;
import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.*;
import static com.blue.base.constant.verify.BusinessType.REGISTER;
import static com.blue.base.constant.verify.VerifyType.MAIL;
import static com.blue.base.constant.verify.VerifyType.SMS;
import static com.blue.member.component.credential.CredentialCollectProcessor.collect;
import static com.blue.member.component.credential.CredentialCollectProcessor.packageCredentialAttr;
import static com.blue.member.converter.MemberModelConverters.MEMBER_REGISTRY_INFO_2_MEMBER_BASIC;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.util.Loggers.getLogger;

/**
 * member register service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces", "DefaultAnnotationParam"})
@Service
public class MemberAuthServiceImpl implements MemberAuthService {

    private static final Logger LOGGER = getLogger(MemberAuthServiceImpl.class);

    private final MemberBasicService memberBasicService;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer;

    private final RpcControlServiceConsumer rpcControlServiceConsumer;

    private final RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer;

    public MemberAuthServiceImpl(MemberBasicService memberBasicService, BlueIdentityProcessor blueIdentityProcessor, RpcVerifyHandleServiceConsumer rpcVerifyHandleServiceConsumer,
                                 RpcControlServiceConsumer rpcControlServiceConsumer, RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer) {
        this.memberBasicService = memberBasicService;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.rpcVerifyHandleServiceConsumer = rpcVerifyHandleServiceConsumer;
        this.rpcControlServiceConsumer = rpcControlServiceConsumer;
        this.rpcFinanceAccountServiceConsumer = rpcFinanceAccountServiceConsumer;
    }

    /**
     * member registry
     *
     * @param memberRegistryParam
     * @return
     */
    @SuppressWarnings("CommentedOutCode")
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 60000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 60)
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
        rpcControlServiceConsumer.initMemberAuthInfo(new MemberCredentialInfo(id, collect(memberBasic, memberRegistryParam.getAccess())));

        //init finance account
        rpcFinanceAccountServiceConsumer.initMemberFinanceInfo(new MemberFinanceInfo(id));

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
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 60)
    public MemberBasicInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo simpleRegisterMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);
        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);

        if (isEmpty(collect(memberBasic, memberRegistryParam.getAccess())))
            throw new BlueException(BAD_REQUEST);

        long id = blueIdentityProcessor.generate(MemberBasic.class);
        memberBasic.setId(id);

        //init finance account
        rpcFinanceAccountServiceConsumer.initMemberFinanceInfo(new MemberFinanceInfo(id));

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
            rollbackFor = Exception.class, timeout = 60)
    public MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId) {
        LOGGER.info("MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId), credentialTypes = {}, credential = {}, memberId = {}",
                credentialTypes, credential, memberId);

        Optional<MemberBasic> memberBasicOpt = memberBasicService.getMemberBasicByPrimaryKey(memberId);
        if (memberBasicOpt.isEmpty())
            throw new BlueException(DATA_NOT_EXIST);

        MemberBasic memberBasic = memberBasicOpt.get();

        packageCredentialAttr(credentialTypes, credential, memberBasic);

        return memberBasicService.updateMemberBasic(memberBasic);
    }

}
