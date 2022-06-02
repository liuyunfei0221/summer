package com.blue.auth.service.impl;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.component.auto.MemberParamPackagerProcessor;
import com.blue.auth.remote.consumer.RpcMemberAuthServiceConsumer;
import com.blue.auth.service.inter.AutoRegisterService;
import com.blue.auth.service.inter.ControlService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.List;
import java.util.function.BiFunction;

import static org.springframework.transaction.annotation.Isolation.REPEATABLE_READ;
import static reactor.util.Loggers.getLogger;

/**
 * auto register member service impl
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class AutoRegisterServiceImpl implements AutoRegisterService {

    private static final Logger LOGGER = getLogger(AutoRegisterServiceImpl.class);

    private MemberParamPackagerProcessor memberParamPackagerProcessor;

    private final RpcMemberAuthServiceConsumer rpcMemberAuthServiceConsumer;

    private final ControlService controlService;

    public AutoRegisterServiceImpl(MemberParamPackagerProcessor memberParamPackagerProcessor, RpcMemberAuthServiceConsumer rpcMemberAuthServiceConsumer, ControlService controlService) {
        this.memberParamPackagerProcessor = memberParamPackagerProcessor;
        this.rpcMemberAuthServiceConsumer = rpcMemberAuthServiceConsumer;
        this.controlService = controlService;
    }

    private final BiFunction<List<CredentialInfo>, String, MemberRegistryParam> REGISTRY_PARAM_CONVERTER = (credentials, source) -> {
        MemberRegistryParam memberRegistryParam = new MemberRegistryParam();
        credentials.forEach(credential -> memberParamPackagerProcessor.packageCredentialInfoToRegistryParam(credential, memberRegistryParam));

        return memberRegistryParam;
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
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo autoRegisterMemberInfo(List<CredentialInfo> credentials, Long roleId, String source) {
        LOGGER.info("MemberBasicInfo autoRegisterMemberInfo(List<CredentialInfo> credentials), credentials = {}, source = {}", credentials, source);

        MemberBasicInfo memberBasicInfo = rpcMemberAuthServiceConsumer.autoRegisterMemberBasic(REGISTRY_PARAM_CONVERTER.apply(credentials, source));

        controlService.initMemberAuthInfo(new MemberCredentialInfo(memberBasicInfo.getId(), credentials), roleId);

        return memberBasicInfo;
    }

}
