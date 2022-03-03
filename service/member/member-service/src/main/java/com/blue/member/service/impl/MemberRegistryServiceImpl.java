package com.blue.member.service.impl;

import com.blue.base.model.exps.BlueException;
import com.blue.finance.api.model.MemberFinanceInfo;
import com.blue.identity.common.BlueIdentityProcessor;
import com.blue.member.api.model.MemberInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.component.credential.InitCredentialInfoProcessor;
import com.blue.member.remote.consumer.RpcControlServiceConsumer;
import com.blue.member.remote.consumer.RpcFinanceAccountServiceConsumer;
import com.blue.member.repository.entity.MemberBasic;
import com.blue.member.service.inter.MemberBasicService;
import com.blue.member.service.inter.MemberRegistryService;
import com.blue.secure.api.model.MemberCredentialInfo;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import static com.blue.base.common.base.BlueChecker.isNull;
import static com.blue.base.constant.base.ResponseElement.EMPTY_PARAM;
import static com.blue.member.converter.MemberModelConverters.MEMBER_REGISTRY_INFO_2_MEMBER_BASIC;
import static reactor.util.Loggers.getLogger;

/**
 * member register service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Service
public class MemberRegistryServiceImpl implements MemberRegistryService {

    private static final Logger LOGGER = getLogger(MemberRegistryServiceImpl.class);

    private final MemberBasicService memberBasicService;

    private final BlueIdentityProcessor blueIdentityProcessor;

    private final InitCredentialInfoProcessor initCredentialInfoProcessor;

    private final RpcControlServiceConsumer rpcControlServiceConsumer;

    private final RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer;

    public MemberRegistryServiceImpl(MemberBasicService memberBasicService, BlueIdentityProcessor blueIdentityProcessor,
                                     InitCredentialInfoProcessor initCredentialInfoProcessor, RpcControlServiceConsumer rpcControlServiceConsumer,
                                     RpcFinanceAccountServiceConsumer rpcFinanceAccountServiceConsumer) {
        this.memberBasicService = memberBasicService;
        this.blueIdentityProcessor = blueIdentityProcessor;
        this.initCredentialInfoProcessor = initCredentialInfoProcessor;
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
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberInfo registerMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo registerMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);
        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);

        long id = blueIdentityProcessor.generate(MemberBasic.class);
        memberBasic.setId(id);

        //init secure info
        rpcControlServiceConsumer.initMemberSecureInfo(new MemberCredentialInfo(id, initCredentialInfoProcessor.generateCredentialInfos(memberBasic, memberRegistryParam.getAccess())));

        //init finance account
        rpcFinanceAccountServiceConsumer.initMemberFinanceInfo(new MemberFinanceInfo(id));

        @SuppressWarnings("UnnecessaryLocalVariable")
        MemberInfo memberInfo = memberBasicService.insertMemberBasic(memberBasic);

//        if (1 == 1)
//            throw new BlueException(500, 500, "test rollback");

        return memberInfo;
    }

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    @Override
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo simpleRegisterMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryDTO = {}", memberRegistryParam);

        if (isNull(memberRegistryParam))
            throw new BlueException(EMPTY_PARAM);

        MemberBasic memberBasic = MEMBER_REGISTRY_INFO_2_MEMBER_BASIC.apply(memberRegistryParam);

        long id = blueIdentityProcessor.generate(MemberBasic.class);
        memberBasic.setId(id);

        //init finance account
        rpcFinanceAccountServiceConsumer.initMemberFinanceInfo(new MemberFinanceInfo(id));

        @SuppressWarnings("UnnecessaryLocalVariable")
        MemberInfo memberInfo = memberBasicService.insertMemberBasic(memberBasic);

//        if (1 == 1)
//            throw new BlueException(500, 500, "test rollback");

        return memberInfo;
    }

}
