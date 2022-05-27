package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcCredentialService;
import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.service.inter.CredentialService;
import com.blue.base.model.exps.BlueException;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.util.Logger;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.auth.converter.AuthModelConverters.CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER;
import static com.blue.base.constant.base.ResponseElement.DATA_NOT_EXIST;
import static reactor.core.publisher.Mono.just;
import static reactor.util.Loggers.getLogger;

/**
 * rpc credential provider
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "JavaDoc", "AlibabaServiceOrDaoClassShouldEndWithImpl"})
@DubboService(interfaceClass = RpcCredentialService.class,
        version = "1.0",
        methods = {
                @Method(name = "getCredentialByMemberIdAndType", async = true),
                @Method(name = "selectCredentialByMemberIdAndTypes", async = true),
                @Method(name = "getCredentialByCredentialAndType", async = true),
                @Method(name = "selectCredentialByCredentialAndTypes", async = true)
        })
public class RpcCredentialServiceProvider implements RpcCredentialService {

    private static final Logger LOGGER = getLogger(RpcResourceServiceProvider.class);

    private final CredentialService credentialService;

    private final Scheduler scheduler;

    public RpcCredentialServiceProvider(CredentialService credentialService, Scheduler scheduler) {
        this.credentialService = credentialService;
        this.scheduler = scheduler;
    }

    /**
     * get by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    @Override
    public CompletableFuture<CredentialInfo> getCredentialByMemberIdAndType(Long memberId, String credentialType) {
        LOGGER.info("CompletableFuture<CredentialInfo> getCredentialByMemberIdAndType(Long memberId, String credentialType), memberId = {}, credentialType = {}", memberId, credentialType);
        return just(true).subscribeOn(scheduler)
                .flatMap(v -> credentialService.getCredentialMonoByMemberIdAndType(memberId, credentialType))
                .flatMap(cOpt -> cOpt.map(CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(DATA_NOT_EXIST)))
                .toFuture();
    }

    /**
     * select by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    @Override
    public CompletableFuture<List<CredentialInfo>> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes) {
        LOGGER.info("CompletableFuture<List<CredentialInfo>> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes), memberId = {}, credentialTypes = {}", memberId, credentialTypes);
        return just(true)
                .subscribeOn(scheduler)
                .flatMap(v -> credentialService.selectCredentialInfoMonoByMemberIdAndTypes(memberId, credentialTypes))
                .toFuture();
    }

    /**
     * get info by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    @Override
    public CompletableFuture<CredentialInfo> getCredentialByCredentialAndType(String credential, String credentialType) {
        LOGGER.info("CompletableFuture<CredentialInfo> getCredentialByCredentialAndType(String credential, String credentialType), credential = {}, credentialType = {}", credential, credentialType);
        return just(true).subscribeOn(scheduler)
                .flatMap(v -> credentialService.getCredentialMonoByCredentialAndType(credential, credentialType))
                .flatMap(cOpt -> cOpt.map(CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER)
                        .map(Mono::just)
                        .orElseThrow(() -> new BlueException(DATA_NOT_EXIST)))
                .toFuture();
    }

    /**
     * select info by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    @Override
    public CompletableFuture<List<CredentialInfo>> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes) {
        LOGGER.info("CompletableFuture<List<CredentialInfo>> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes), credential = {}, credentialTypes = {}", credential, credentialTypes);
        return just(true)
                .subscribeOn(scheduler)
                .flatMap(v -> credentialService.selectCredentialInfoMonoByCredentialAndTypes(credential, credentialTypes))
                .toFuture();
    }

}