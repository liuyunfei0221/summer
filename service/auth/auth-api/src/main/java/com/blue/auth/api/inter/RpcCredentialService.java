package com.blue.auth.api.inter;

import com.blue.auth.api.model.CredentialInfo;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc credential interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcCredentialService {

    /**
     * get by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    CompletableFuture<CredentialInfo> getCredentialByMemberIdAndType(Long memberId, String credentialType);

    /**
     * select by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    CompletableFuture<List<CredentialInfo>> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes);

    /**
     * get info by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    CompletableFuture<CredentialInfo> getCredentialByCredentialAndType(String credential, String credentialType);

    /**
     * select info by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    CompletableFuture<List<CredentialInfo>> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes);

}
