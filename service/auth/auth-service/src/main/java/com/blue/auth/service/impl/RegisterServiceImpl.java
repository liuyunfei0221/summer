package com.blue.auth.service.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.component.auto.MemberParamPackagerProcessor;
import com.blue.auth.remote.consumer.RpcMemberControlServiceConsumer;
import com.blue.auth.service.inter.AuthControlService;
import com.blue.auth.service.inter.RegisterService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberInitParam;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.function.BiFunction;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;

/**
 * auto register member service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class RegisterServiceImpl implements RegisterService {

    private static final Logger LOGGER = getLogger(RegisterServiceImpl.class);

    private MemberParamPackagerProcessor memberParamPackagerProcessor;

    private final RpcMemberControlServiceConsumer rpcMemberControlServiceConsumer;

    private final AuthControlService authControlService;

    public RegisterServiceImpl(MemberParamPackagerProcessor memberParamPackagerProcessor, RpcMemberControlServiceConsumer rpcMemberControlServiceConsumer, AuthControlService authControlService) {
        this.memberParamPackagerProcessor = memberParamPackagerProcessor;
        this.rpcMemberControlServiceConsumer = rpcMemberControlServiceConsumer;
        this.authControlService = authControlService;
    }

    private final BiFunction<List<CredentialInfo>, String, MemberInitParam> REGISTRY_PARAM_CONVERTER = (credentials, source) -> {
        MemberInitParam memberInitParam = new MemberInitParam();
        credentials.forEach(credential -> memberParamPackagerProcessor.packageCredentialInfoToRegistryParam(credential, memberInitParam));

        return memberInitParam;
    };

    /**
     * auto register for a new member
     *
     * @param credentials
     * @param roleId
     * @param source
     * @return
     */
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInterval = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo registerMemberBasic(List<CredentialInfo> credentials, Long roleId, String source) {
        LOGGER.info("credentials = {}, source = {}", credentials, source);

        MemberBasicInfo memberBasicInfo = rpcMemberControlServiceConsumer.initMemberBasic(REGISTRY_PARAM_CONVERTER.apply(credentials, source));

        authControlService.initMemberAuthInfo(new MemberCredentialInfo(memberBasicInfo.getId(), credentials), roleId);

        return memberBasicInfo;
    }

}
