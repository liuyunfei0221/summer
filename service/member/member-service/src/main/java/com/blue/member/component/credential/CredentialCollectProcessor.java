package com.blue.member.component.credential;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.component.credential.inter.CredentialCollector;
import com.blue.member.repository.entity.MemberBasic;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

import static com.blue.basic.common.base.BlueChecker.isEmpty;
import static org.springframework.core.Ordered.HIGHEST_PRECEDENCE;
import static reactor.util.Loggers.getLogger;

/**
 * collect processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SameParameterValue", "JavaDoc", "AliControlFlowStatementWithoutBraces"})
@Component
@Order(HIGHEST_PRECEDENCE)
public class CredentialCollectProcessor implements ApplicationListener<ContextRefreshedEvent> {

    private static final Logger LOGGER = getLogger(CredentialCollectProcessor.class);

    private List<CredentialCollector> collectors;

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        ApplicationContext applicationContext = contextRefreshedEvent.getApplicationContext();
        Map<String, CredentialCollector> beansOfType = applicationContext.getBeansOfType(CredentialCollector.class);
        if (isEmpty(beansOfType))
            throw new RuntimeException("collectors is empty");

        collectors = new ArrayList<>(beansOfType.values());
    }

    /**
     * collector
     */
    private final BiFunction<MemberBasic, String, List<CredentialInfo>> CREDENTIALS_COLLECTOR = (memberBasic, access) -> {
        List<CredentialInfo> credentials = new LinkedList<>();

        collectors.forEach(collector -> collector.collect(memberBasic, access, credentials));

        return credentials;
    };

    /**
     * collect
     *
     * @param memberBasic
     * @param access
     * @return
     */
    public List<CredentialInfo> collect(MemberBasic memberBasic, String access) {
        LOGGER.info("List<CredentialInfo> handle(MemberBasic memberBasic, String access), memberBasic = {}, access = {}",
                memberBasic, access);

        return CREDENTIALS_COLLECTOR.apply(memberBasic, access);
    }

    /**
     * package credential attribute to member basic
     *
     * @param credentialTypes
     * @param credential
     * @param memberBasic
     */
    public void packageCredentialAttr(List<String> credentialTypes, String credential, MemberBasic memberBasic) {
        LOGGER.info("void packageCredentialAttr(List<String> credentialTypes, String credential, MemberBasic memberBasic), credentialTypes = {}, credential = {}, memberBasic = {}",
                credentialTypes, credential, memberBasic);

        collectors.forEach(collector -> collector.packageCredentialAttr(credentialTypes, credential, memberBasic));
    }


}