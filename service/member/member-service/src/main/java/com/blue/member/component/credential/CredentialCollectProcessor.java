package com.blue.member.component.credential;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.member.component.credential.inter.CredentialCollector;
import com.blue.member.repository.entity.MemberBasic;
import reactor.util.Logger;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import static com.blue.base.common.base.ClassGetter.getClassesByPackage;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Stream.of;
import static reactor.util.Loggers.getLogger;

/**
 * collect processor
 *
 * @author liuyunfei
 */
@SuppressWarnings({"SameParameterValue", "JavaDoc"})
public final class CredentialCollectProcessor {

    private static final Logger LOGGER = getLogger(CredentialCollectProcessor.class);

    /**
     * collectors package
     */
    private static final String DIR_NAME = "com.blue.member.component.credential.impl";

    private static final List<CredentialCollector> COLLECTORS = generatorCollectors(DIR_NAME);

    /**
     * init collectors
     *
     * @param dirName
     * @return
     */
    private static List<CredentialCollector> generatorCollectors(String dirName) {
        List<Class<?>> classes = getClassesByPackage(dirName, true);
        LOGGER.info("List<CredentialCollector> generatorCollectors(String dirName), dirName = {}, classes = {}", dirName, classes);
        String credentialCollectorName = CredentialCollector.class.getName();
        return classes
                .stream()
                .filter(clz ->
                        !clz.isInterface() &&
                                of(clz.getInterfaces()).anyMatch(inter -> credentialCollectorName.equals(inter.getName())))
                .map(clz -> {
                    try {
                        LOGGER.info("generatorCollectors(String dirName), Load credential collector class, clz = {}", clz.getName());
                        return (CredentialCollector) clz.getConstructor().newInstance();
                    } catch (Exception e) {
                        LOGGER.info("generatorCollectors(String dirName), Load credential collector class failed, clz = {}, e = {}", clz.getName(), e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toList());
    }

    /**
     * collector
     */
    private static final BiFunction<MemberBasic, String, List<CredentialInfo>> CREDENTIALS_COLLECTOR = (memberBasic, access) -> {
        List<CredentialInfo> credentials = new LinkedList<>();

        COLLECTORS.forEach(collector -> collector.collect(memberBasic, access, credentials));

        return credentials;
    };

    /**
     * collect
     *
     * @param memberBasic
     * @param access
     * @return
     */
    public static List<CredentialInfo> collect(MemberBasic memberBasic, String access) {
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
    public static void packageCredentialAttr(List<String> credentialTypes, String credential, MemberBasic memberBasic) {
        LOGGER.info("void packageCredentialAttr(List<String> credentialTypes, String credential, MemberBasic memberBasic), credentialTypes = {}, credential = {}, memberBasic = {}",
                credentialTypes, credential, memberBasic);

        COLLECTORS.forEach(collector -> collector.packageCredentialAttr(credentialTypes, credential, memberBasic));
    }


}