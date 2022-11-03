package com.blue.auth.remote.provider;

import com.blue.auth.api.inter.RpcCredentialService;
import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.service.inter.CredentialService;
import com.blue.basic.model.exps.BlueException;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Method;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static com.blue.auth.converter.AuthModelConverters.CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER;
import static com.blue.basic.constant.common.ResponseElement.DATA_NOT_EXIST;
import static reactor.core.publisher.Mono.*;

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

    private final CredentialService credentialService;

    public RpcCredentialServiceProvider(CredentialService credentialService) {
        this.credentialService = credentialService;
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
        return just(true)
                .flatMap(v -> credentialService.getCredentialByMemberIdAndType(memberId, credentialType))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER)
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
        return just(true)
                .flatMap(v -> credentialService.selectCredentialInfoByMemberIdAndTypes(memberId, credentialTypes))
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
        return just(true)
                .flatMap(v -> credentialService.getCredentialByCredentialAndType(credential, credentialType))
                .switchIfEmpty(defer(() -> error(() -> new BlueException(DATA_NOT_EXIST))))
                .map(CREDENTIAL_2_CREDENTIAL_INFO_CONVERTER)
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
        return just(true)
                .flatMap(v -> credentialService.selectCredentialInfoByCredentialAndTypes(credential, credentialTypes))
                .toFuture();
    }

}
