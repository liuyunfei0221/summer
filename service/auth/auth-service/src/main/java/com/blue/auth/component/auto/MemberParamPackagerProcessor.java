package com.blue.auth.component.auto;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.component.auto.inter.MemberParamByAutoLoginPackager;
import com.blue.basic.common.base.BlueChecker;
import com.blue.basic.model.exps.BlueException;
import com.blue.member.api.model.MemberRegistryParam;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.Map;
import java.util.function.BiConsumer;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static com.blue.basic.common.base.BlueChecker.isNull;
import static com.blue.basic.constant.common.ResponseElement.INVALID_IDENTITY;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toMap;
import static reactor.util.Loggers.getLogger;

/**
 * session processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
public class MemberParamPackagerProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(MemberParamPackagerProcessor.class);

    /**
     * credential type -> session handler
     */
    private Map<String, MemberParamByAutoLoginPackager> packagers;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, MemberParamByAutoLoginPackager> beansOfType = applicationContext.getBeansOfType(MemberParamByAutoLoginPackager.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("packagers is empty");

        packagers = beansOfType.values().stream()
                .collect(toMap(lh -> lh.targetType().identity, lh -> lh, (a, b) -> a));
    }

    private final BiConsumer<CredentialInfo, MemberRegistryParam> REGISTRY_PARAM_PACKAGER = (credentialInfo, registryParam) ->
            ofNullable(credentialInfo)
                    .map(CredentialInfo::getType)
                    .filter(BlueChecker::isNotBlank)
                    .map(packagers::get)
                    .orElseThrow(() -> new BlueException(INVALID_IDENTITY))
                    .packageCredentialInfoToRegistryParam(credentialInfo, registryParam);

    /**
     * package credential to member register param
     *
     * @param credentialInfo
     * @param memberRegistryParam
     */
    public void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberRegistryParam memberRegistryParam) {
        LOGGER.info("void packageCredentialInfoToRegistryParam(CredentialInfo credentialInfo, MemberRegistryParam memberRegistryParam), credentialInfo = {}, memberRegistryParam = {}",
                credentialInfo, memberRegistryParam);

        if (isNull(memberRegistryParam))
            throw new BlueException(INVALID_IDENTITY);

        REGISTRY_PARAM_PACKAGER.accept(credentialInfo, memberRegistryParam);
    }

}
