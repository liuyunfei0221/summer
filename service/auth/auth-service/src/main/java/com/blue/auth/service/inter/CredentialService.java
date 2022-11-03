package com.blue.auth.service.inter;

import com.blue.auth.api.model.CredentialInfo;
import com.blue.auth.repository.entity.Credential;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * credential service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface CredentialService {

    /**
     * insert credential batch
     *
     * @param credentials
     * @return
     */
    void insertCredentials(List<Credential> credentials);

    /**
     * batch update credential by ids
     *
     * @param credential
     * @param ids
     */
    void updateCredentialByIds(String credential, List<Long> ids);

    /**
     * delete credential
     *
     * @param id
     * @return
     */
    void deleteCredential(Long id);

    /**
     * update access
     *
     * @param memberId
     * @param credentialTypes
     * @param access
     * @return
     */
    boolean updateAccess(Long memberId, List<String> credentialTypes, String access);

    /**
     * get by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    Optional<Credential> getCredentialOptByCredentialAndType(String credential, String credentialType);

    /**
     * get mono by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    Mono<Credential> getCredentialByCredentialAndType(String credential, String credentialType);

    /**
     * select mono by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    Mono<List<Credential>> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes);

    /**
     * select info mono by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    Mono<List<CredentialInfo>> selectCredentialInfoByCredentialAndTypes(String credential, List<String> credentialTypes);

    /**
     * get by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    Optional<Credential> getCredentialOptByMemberIdAndType(Long memberId, String credentialType);

    /**
     * get mono by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    Mono<Credential> getCredentialByMemberIdAndType(Long memberId, String credentialType);

    /**
     * select mono by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    Mono<List<Credential>> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes);

    /**
     * select info mono by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    Mono<List<CredentialInfo>> selectCredentialInfoByMemberIdAndTypes(Long memberId, List<String> credentialTypes);

}
