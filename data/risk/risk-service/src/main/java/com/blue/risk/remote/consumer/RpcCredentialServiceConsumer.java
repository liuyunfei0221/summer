package com.blue.risk.remote.consumer;

import com.blue.auth.api.inter.RpcCredentialService;
import com.blue.auth.api.model.CredentialInfo;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.Method;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;

import static reactor.core.publisher.Mono.fromFuture;
import static reactor.util.Loggers.getLogger;

/**
 * rpc credential reference
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
@Component
public class RpcCredentialServiceConsumer {

    private static final Logger LOGGER = getLogger(RpcCredentialServiceConsumer.class);

    @DubboReference(version = "1.0",
            providedBy = {"summer-auth"},
            methods = {
                    @Method(name = "getCredentialByMemberIdAndType", async = true),
                    @Method(name = "selectCredentialByMemberIdAndTypes", async = true),
                    @Method(name = "getCredentialByCredentialAndType", async = true),
                    @Method(name = "selectCredentialByCredentialAndTypes", async = true)
            })
    private RpcCredentialService rpcCredentialService;

    private final Scheduler scheduler;

    public RpcCredentialServiceConsumer(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    /**
     * get by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    public Mono<CredentialInfo> getCredentialByMemberIdAndType(Long memberId, String credentialType) {
        LOGGER.info("Mono<CredentialInfo> getCredentialByMemberIdAndType(Long memberId, String credentialType), memberId = {}, credentialType = {}", memberId, credentialType);
        return fromFuture(rpcCredentialService.getCredentialByMemberIdAndType(memberId, credentialType)).subscribeOn(scheduler);
    }

    /**
     * select by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    public Mono<List<CredentialInfo>> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes) {
        LOGGER.info("Mono<List<CredentialInfo>> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes), memberId = {}, credentialTypes = {}", memberId, credentialTypes);
        return fromFuture(rpcCredentialService.selectCredentialByMemberIdAndTypes(memberId, credentialTypes)).subscribeOn(scheduler);
    }

    /**
     * get info by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    public Mono<CredentialInfo> getCredentialByCredentialAndType(String credential, String credentialType) {
        LOGGER.info("Mono<CredentialInfo> getCredentialByCredentialAndType(String credential, String credentialType), credential = {}, credentialType = {}", credential, credentialType);
        return fromFuture(rpcCredentialService.getCredentialByCredentialAndType(credential, credentialType)).subscribeOn(scheduler);
    }

    /**
     * select info by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    public Mono<List<CredentialInfo>> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes) {
        LOGGER.info("Mono<List<CredentialInfo>> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes), memberId = {}, credentialTypes = {}", credential, credentialTypes);
        return fromFuture(rpcCredentialService.selectCredentialByCredentialAndTypes(credential, credentialTypes)).subscribeOn(scheduler);
    }

}
