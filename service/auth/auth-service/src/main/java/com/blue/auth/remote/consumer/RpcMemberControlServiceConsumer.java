package com.blue.auth.remote.consumer;

import com.blue.member.api.inter.RpcMemberControlService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberInitParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * rpc member control reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcMemberControlServiceConsumer {

    @DubboReference(version = "1.0",
            providedBy = {"summer-member"},
            methods = {
                    @Method(name = "initMemberBasic", async = false),
                    @Method(name = "updateMemberCredentialAttr", async = false)
            })
    private RpcMemberControlService rpcMemberControlService;

    /**
     * member register for auto registry or third party session
     *
     * @param memberInitParam
     * @return
     */
    public MemberBasicInfo initMemberBasic(MemberInitParam memberInitParam) {
        return rpcMemberControlService.initMemberBasic(memberInitParam);
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
        return rpcMemberControlService.updateMemberCredentialAttr(credentialTypes, credential, memberId);
    }

}
