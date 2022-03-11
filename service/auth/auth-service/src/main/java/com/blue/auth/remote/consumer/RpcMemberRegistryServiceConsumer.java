package com.blue.auth.remote.consumer;

import com.blue.member.api.inter.RpcMemberRegistryService;
import com.blue.member.api.model.MemberBasicInfo;
import com.blue.member.api.model.MemberRegistryParam;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.util.Logger;

import static reactor.util.Loggers.getLogger;

/**
 * rpc member registry reference
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam"})
@Component
public class RpcMemberRegistryServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcMemberRegistryServiceConsumer.class);

    @DubboReference(version = "1.0", providedBy = {"summer-member"}, methods = {
            @Method(name = "autoRegisterMemberBasic", async = false)
    })
    private RpcMemberRegistryService rpcMemberRegistryService;

    /**
     * member register for auto registry or third party login
     *
     * @param memberRegistryParam
     * @return
     */
    public MemberBasicInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam) {
        LOGGER.info("MemberInfo autoRegisterMemberBasic(MemberRegistryParam memberRegistryParam), memberRegistryParam = {}", memberRegistryParam);
        return rpcMemberRegistryService.autoRegisterMemberBasic(memberRegistryParam);
    }

}
