package com.blue.auth.service.inter;

import com.blue.auth.repository.entity.Credential;
import com.blue.base.constant.verify.VerifyType;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * credential service
 *
 * @author liuyunfei
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface CredentialService {

    /**
     * get by credential and type
     *
     * @param credential
     * @param type
     * @return
     */
    Optional<Credential> getCredentialByCredentialAndType(String credential, String type);

    /**
     * get mono by credential and type
     *
     * @param credential
     * @param type
     * @return
     */
    Mono<Optional<Credential>> getCredentialMonoByCredentialAndType(String credential, String type);

    /**
     * get by member id and type
     *
     * @param memberId
     * @param type
     * @return
     */
    Optional<Credential> getCredentialByMemberIdAndType(Long memberId, String type);

    /**
     * get mono by member id and type
     *
     * @param memberId
     * @param type
     * @return
     */
    Mono<Optional<Credential>> getCredentialMonoByMemberIdAndType(Long memberId, String type);

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
     * @param verifyType
     * @param access
     * @return
     */
    Boolean updateAccess(Long memberId, VerifyType verifyType, String access);

}
