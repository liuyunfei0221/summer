package com.blue.auth.remote.consumer;

import com.blue.member.api.inter.RpcMemberAuthService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import java.util.List;

import static reactor.util.Loggers.getLogger;

/**
 * rpc member registry reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcMemberAuthServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcMemberAuthServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-member"},
            methods = {
                    @Method(name = "autoRegisterMemberBasic", async = false),
                    @Method(name = "updateMemberCredentialAttr", async = false)
            })
    private RpcMemberAuthService rpcMemberAuthService;

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    public MemberBasicInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryParam = {}", memberRegistryParam);
        return rpcMemberAuthService.autoRegisterMemberBasic(memberRegistryParam);
    }

    /**
     * package credential attribute to member basic
     *
     * @param credentialTypes
     * @param credential
     * @param memberId
     * @return
     */
    public MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId) {
        LOGGER.info("MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId), memberRegistryParam = {}, credentialTypes = {}, credential = {}, memberId = {}",
                credentialTypes, credential, memberId);
        return rpcMemberAuthService.updateMemberCredentialAttr(credentialTypes, credential, memberId);
    }

}
