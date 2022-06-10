package com.blue.auth.remote.consumer;

import com.blue.member.api.inter.RpcMemberAuthService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * rpc member registry reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcMemberAuthServiceConsumer {

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
        return rpcMemberAuthService.updateMemberCredentialAttr(credentialTypes, credential, memberId);
    }

}
