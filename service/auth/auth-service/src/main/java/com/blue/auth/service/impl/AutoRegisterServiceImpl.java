package com.blue.auth.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.constant.auth.LoginType;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.remote.consumer.RpcMemberRegistryServiceConsumer;
import com.blue.auth.service.inter.AutoRegisterService;
import com.blue.auth.service.inter.ControlService;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import reactor.util.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

import static com.blue.base.common.base.BlueRandomGenerator.generateRandom;
import static com.blue.base.constant.base.RandomType.ALPHANUMERIC;
import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.constant.auth.LoginType.*;
import static java.util.Optional.ofNullable;
import static reactor.util.Loggers.getLogger;

/**
 * auto register member service impl
 *
 * @author liuyunfei
 * @apiNote
 */
@SuppressWarnings({"JavaDoc"})
@Service
public class AutoRegisterServiceImpl implements AutoRegisterService {

    private static final Logger LOGGER = getLogger(AutoRegisterServiceImpl.class);

    private final RpcMemberRegistryServiceConsumer rpcMemberRegistryServiceConsumer;

    private final ControlService controlService;

    public AutoRegisterServiceImpl(RpcMemberRegistryServiceConsumer rpcMemberRegistryServiceConsumer, ControlService controlService) {
        this.rpcMemberRegistryServiceConsumer = rpcMemberRegistryServiceConsumer;
        this.controlService = controlService;
    }

    private static final int RANDOM_NAME_LEN = 8;

    private static final Map<String, BiConsumer<CredentialInfo, MemberRegistryParam>> REGISTRY_PARAM_PACKAGERS = new HashMap<>(LoginType.values().length);

    static {
        REGISTRY_PARAM_PACKAGERS.put(PHONE_VERIFY_AUTO_REGISTER.identity, (credentialInfo, registryParam) -> {
            registryParam.setPhone(credentialInfo.getCredential());
            registryParam.setName(generateRandom(ALPHANUMERIC, RANDOM_NAME_LEN));
        });

        REGISTRY_PARAM_PACKAGERS.put(PHONE_PWD.identity, (credentialInfo, registryParam) -> {
            registryParam.setPhone(credentialInfo.getCredential());
            registryParam.setName(generateRandom(ALPHANUMERIC, RANDOM_NAME_LEN));
        });

        REGISTRY_PARAM_PACKAGERS.put(EMAIL_VERIFY_AUTO_REGISTER.identity, (credentialInfo, registryParam) -> {
            registryParam.setEmail(credentialInfo.getCredential());
            registryParam.setName(generateRandom(ALPHANUMERIC, RANDOM_NAME_LEN));
        });

        REGISTRY_PARAM_PACKAGERS.put(EMAIL_PWD.identity, (credentialInfo, registryParam) -> {
            registryParam.setEmail(credentialInfo.getCredential());
            registryParam.setName(generateRandom(ALPHANUMERIC, RANDOM_NAME_LEN));
        });

        REGISTRY_PARAM_PACKAGERS.put(WECHAT_AUTO_REGISTER.identity, (credentialInfo, registryParam) -> {
            registryParam.setEmail(credentialInfo.getCredential());
            registryParam.setName(generateRandom(ALPHANUMERIC, RANDOM_NAME_LEN));
        });

        REGISTRY_PARAM_PACKAGERS.put(MINI_PRO_AUTO_REGISTER.identity, (credentialInfo, registryParam) -> {
            registryParam.setEmail(credentialInfo.getCredential());
            registryParam.setName(generateRandom(ALPHANUMERIC, RANDOM_NAME_LEN));
        });

        //TODO
        REGISTRY_PARAM_PACKAGERS.put(NOT_LOGGED_IN.identity, null);
    }

    private static final BiConsumer<CredentialInfo, MemberRegistryParam> REGISTRY_PARAM_PACKAGER = (credentialInfo, registryParam) ->
            ofNullable(credentialInfo)
                    .map(CredentialInfo::getType)
                    .filter(BlueChecker::isNotBlank)
                    .map(REGISTRY_PARAM_PACKAGERS::get)
                    .orElseThrow(() -> new BlueException(INVALID_IDENTITY))
                    .accept(credentialInfo, registryParam);


    private static final Function<List<CredentialInfo>, MemberRegistryParam> REGISTRY_PARAM_CONVERTER = credentials -> {
        MemberRegistryParam memberRegistryParam = new MemberRegistryParam();

        credentials.forEach(credential -> REGISTRY_PARAM_PACKAGER.accept(credential, memberRegistryParam));

        return memberRegistryParam;
    };


    /**
     * auto register for a new member
     *
     * @param credentials
     * @param roleId
     * @return
     */
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo autoRegisterMemberInfo(List<CredentialInfo> credentials, Long roleId) {
        LOGGER.info("MemberBasicInfo autoRegisterMemberInfo(List<CredentialInfo> credentials), credentials = {}", credentials);

        MemberBasicInfo memberBasicInfo = rpcMemberRegistryServiceConsumer.autoRegisterMemberBasic(REGISTRY_PARAM_CONVERTER.apply(credentials));

        controlService.initMemberAuthInfo(new MemberCredentialInfo(memberBasicInfo.getId(), credentials), roleId);

        return memberBasicInfo;
    }

}
