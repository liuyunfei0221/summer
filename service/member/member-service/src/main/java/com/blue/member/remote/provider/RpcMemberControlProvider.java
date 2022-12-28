package com.blue.member.remote.provider;

import com.blue.member.api.inter.RpcMemberControlService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberInitParam;
import com.blue.member.service.inter.MemberControlService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;

/**
 * rpc member control provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcMemberControlService.class,
        version = "1.0",
        methods = {
                @Method(name = "initMemberBasic", async = false),
                @Method(name = "updateMemberCredentialAttr", async = false)
        })
public class RpcMemberControlProvider implements RpcMemberControlService {

    private final MemberControlService memberControlService;

    public RpcMemberControlProvider(MemberControlService memberControlService) {
        this.memberControlService = memberControlService;
    }

    /**
     * member register for auto registry or third party session
     *
     * @param memberInitParam
     * @return
     */
    @Override
    public MemberBasicInfo initMemberBasic(MemberInitParam memberInitParam) {
        return memberControlService.initMemberBasic(memberInitParam);
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
        return memberControlService.updateMemberCredentialAttr(credentialTypes, credential, memberId);
    }

}
