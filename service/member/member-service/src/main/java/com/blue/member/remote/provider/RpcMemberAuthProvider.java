package com.blue.member.remote.provider;

import com.blue.member.api.inter.RpcMemberAuthService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.service.inter.MemberAuthService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;

/**
 * rpc member registry provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcMemberAuthService.class,
        version = "1.0",
        methods = {
                @Method(name = "registerMemberBasic", async = false),
                @Method(name = "updateMemberCredentialAttr", async = false)
        })
public class RpcMemberAuthProvider implements RpcMemberAuthService {

    private final MemberAuthService memberAuthService;

    public RpcMemberAuthProvider(MemberAuthService memberAuthService) {
        this.memberAuthService = memberAuthService;
    }

    /**
     * member register for auto registry or third party session
     *
     * @param memberRegistryParam
     * @return
     */
    @Override
    public MemberBasicInfo registerMemberBasic(MemberRegistryParam memberRegistryParam) {
        return memberAuthService.registerMemberBasic(memberRegistryParam);
    }

    /**
     * package credential attribute to member basic
     *
     * @param credentialTypes
     * @param credential
     * @param memberId
     * @return
     */
    @Override
    public MemberBasicInfo updateMemberCredentialAttr(List<String> credentialTypes, String credential, Long memberId) {
        return memberAuthService.updateMemberCredentialAttr(credentialTypes, credential, memberId);
    }

}
