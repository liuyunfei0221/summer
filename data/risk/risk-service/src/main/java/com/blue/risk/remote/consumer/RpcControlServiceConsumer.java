package com.blue.risk.remote.consumer;

import com.blue.base.model.base.Access;
import com.blue.auth.api.inter.RpcControlService;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberCredentialInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.util.Logger;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;


/**
 * rpc control consumer
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl", "unused", "DefaultAnnotationParam", "FieldCanBeLocal"})
@Component
public class RpcControlServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcControlServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-member"},
            methods = {
                    @Method(name = "initMemberAuthInfo", async = false),
                    @Method(name = "getAuthorityByAccess", async = true),
                    @Method(name = "getAuthorityByMemberId", async = true)
            })
    private RpcControlService rpcControlService;

    /**
     * init auth infos for a new member
     *
     * @param memberCredentialInfo
     */
    public void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo) {
        LOGGER.info("void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo), memberCredentialInfo = {}", memberCredentialInfo);
        rpcControlService.initMemberAuthInfo(memberCredentialInfo);
    }

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    public Mono<AuthorityBaseOnRole> getAuthorityByAccess(Access access) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByAccess(Access access), access = {}", access);
        return fromFuture(rpcControlService.getAuthorityByAccess(access));
    }

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    public Mono<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId) {
        LOGGER.info("Mono<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId), memberId = {}", memberId);
        return fromFuture(rpcControlService.getAuthorityByMemberId(memberId));
    }

}
