package com.blue.secure.service.impl;

import com.blue.base.common.base.BlueChecker;
import com.blue.base.constant.secure.LoginType;
import com.blue.base.model.exps.BlueException;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.secure.remote.consumer.RpcMemberRegistryServiceConsumer;
import com.blue.secure.repository.entity.Credential;
import com.blue.secure.service.inter.AutoRegisterService;
import com.blue.secure.service.inter.CredentialService;
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

import static com.blue.base.constant.base.ResponseElement.INVALID_IDENTITY;
import static com.blue.base.constant.secure.LoginType.*;
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

    private final CredentialService credentialService;

    public AutoRegisterServiceImpl(RpcMemberRegistryServiceConsumer rpcMemberRegistryServiceConsumer, CredentialService credentialService) {
        this.rpcMemberRegistryServiceConsumer = rpcMemberRegistryServiceConsumer;
        this.credentialService = credentialService;
    }

    private static final Map<String, BiConsumer<Credential, MemberRegistryParam>> REGISTRY_PARAM_PACKAGERS = new HashMap<>(LoginType.values().length);

    static {
        REGISTRY_PARAM_PACKAGERS.put(SMS_VERIFY.identity, (credential, registryParam) ->
                registryParam.setPhone(credential.getCredential()));

        REGISTRY_PARAM_PACKAGERS.put(SMS_VERIFY_AUTO_REGISTER.identity, (credential, registryParam) ->
                registryParam.setPhone(credential.getCredential()));

        REGISTRY_PARAM_PACKAGERS.put(PHONE_PWD.identity, (credential, registryParam) ->
                registryParam.setPhone(credential.getCredential()));

        REGISTRY_PARAM_PACKAGERS.put(EMAIL_PWD.identity, (credential, registryParam) ->
                registryParam.setEmail(credential.getCredential()));

        REGISTRY_PARAM_PACKAGERS.put(WECHAT.identity, (credential, registryParam) ->
                registryParam.setEmail(credential.getCredential()));

        REGISTRY_PARAM_PACKAGERS.put(MINI_PRO.identity, (credential, registryParam) ->
                registryParam.setEmail(credential.getCredential()));

        //TODO
        REGISTRY_PARAM_PACKAGERS.put(NOT_LOGGED_IN.identity, null);
    }

    private static final BiConsumer<Credential, MemberRegistryParam> REGISTRY_PARAM_PACKAGER = (credential, registryParam) ->
            ofNullable(credential)
                    .map(Credential::getType)
                    .filter(BlueChecker::isNotBlank)
                    .map(REGISTRY_PARAM_PACKAGERS::get)
                    .orElseThrow(() -> new BlueException(INVALID_IDENTITY))
                    .accept(credential, registryParam);


    private static final Function<List<Credential>, MemberRegistryParam> REGISTRY_PARAM_CONVERTER = credentials -> {
        MemberRegistryParam memberRegistryParam = new MemberRegistryParam();

        credentials.forEach(credential -> REGISTRY_PARAM_PACKAGER.accept(credential, memberRegistryParam));

        return memberRegistryParam;
    };


    /**
     * auto register for a new member
     *
     * @param credentials
     * @return
     */
    @Override
    @GlobalTransactional(propagation = io.seata.tm.api.transaction.Propagation.REQUIRED,
            rollbackFor = Exception.class, lockRetryInternal = 1, lockRetryTimes = 1, timeoutMills = 30000)
    @Transactional(propagation = org.springframework.transaction.annotation.Propagation.REQUIRED, isolation = Isolation.REPEATABLE_READ,
            rollbackFor = Exception.class, timeout = 30)
    public MemberBasicInfo autoRegisterMemberInfo(List<Credential> credentials) {
        LOGGER.info("MemberBasicInfo autoRegisterMemberInfo(List<Credential> credentials), credentials = {}", credentials);

        MemberBasicInfo memberBasicInfo = rpcMemberRegistryServiceConsumer.autoRegisterMemberBasic(REGISTRY_PARAM_CONVERTER.apply(credentials));
        Long id = memberBasicInfo.getId();

        credentials.forEach(credential -> credential.setMemberId(id));
        credentialService.insertCredentials(credentials);

        return memberBasicInfo;
    }

}
