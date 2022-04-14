package com.blue.member.remote.provider;

import com.blue.member.api.inter.RpcMemberRegistryService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import com.blue.member.service.inter.MemberRegistryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * rpc member registry provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "DefaultAnnotationParam"})
@DubboService(interfaceClass = RpcMemberRegistryService.class, version = "1.0", methods = {
        @Method(name = "autoRegisterMemberBasic", async = false)
})
public class RpcMemberRegistryProvider implements RpcMemberRegistryService {

    private static final Logger LOGGER = getLogger(RpcMemberRegistryProvider.class);

    private final MemberRegistryService memberRegistryService;

    public RpcMemberRegistryProvider(MemberRegistryService memberRegistryService) {
        this.memberRegistryService = memberRegistryService;
    }

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    @Override

    public MemberBasicInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryParam = {},", memberRegistryParam);
        return memberRegistryService.autoRegisterMemberBasic(memberRegistryParam);
    }

}
