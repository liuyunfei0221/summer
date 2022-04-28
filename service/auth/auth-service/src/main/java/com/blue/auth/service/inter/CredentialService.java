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
     * get by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    Optional<Credential> getCredentialByCredentialAndType(String credential, String credentialType);

    /**
     * get mono by credential and type
     *
     * @param credential
     * @param credentialType
     * @return
     */
    Mono<Optional<Credential>> getCredentialMonoByCredentialAndType(String credential, String credentialType);

    /**
     * select by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    List<Credential> selectCredentialByCredentialAndTypes(String credential, List<String> credentialTypes);

    /**
     * select mono by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    Mono<List<Credential>> selectCredentialMonoByCredentialAndTypes(String credential, List<String> credentialTypes);

    /**
     * select info mono by credential and types
     *
     * @param credential
     * @param credentialTypes
     * @return
     */
    Mono<List<CredentialInfo>> selectCredentialInfoMonoByCredentialAndTypes(String credential, List<String> credentialTypes);

    /**
     * get by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    Optional<Credential> getCredentialByMemberIdAndType(Long memberId, String credentialType);

    /**
     * get mono by member id and type
     *
     * @param memberId
     * @param credentialType
     * @return
     */
    Mono<Optional<Credential>> getCredentialMonoByMemberIdAndType(Long memberId, String credentialType);

    /**
     * select by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    List<Credential> selectCredentialByMemberIdAndTypes(Long memberId, List<String> credentialTypes);

    /**
     * select mono by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    Mono<List<Credential>> selectCredentialMonoByMemberIdAndTypes(Long memberId, List<String> credentialTypes);

    /**
     * select info mono by member id and types
     *
     * @param memberId
     * @param credentialTypes
     * @return
     */
    Mono<List<CredentialInfo>> selectCredentialInfoMonoByMemberIdAndTypes(Long memberId, List<String> credentialTypes);

    /**
     * insert credential batch
     *
     * @param credentials
     * @return
     */
    void insertCredentials(List<Credential> credentials);

    /**
     * insert credential
     *
     * @param credential
     * @return
     */
    void insertCredential(Credential credential);

    /**
     * batch update credential by ids
     *
     * @param credential
     * @param ids
     */
    void updateCredentialByIds(String credential, List<Long> ids);

    /**
     * update a exist role
     *
     * @param credential
     * @return
     */
    void updateCredential(Credential credential);

    /**
     * delete role
     *
     * @param id
     * @return
     */
    void deleteCredentialById(Long id);

    /**
     * update access
     *
     * @param memberId
     * @param credentialTypes
     * @param access
     * @return
     */
    boolean updateAccess(Long memberId, List<String> credentialTypes, String access);

}
