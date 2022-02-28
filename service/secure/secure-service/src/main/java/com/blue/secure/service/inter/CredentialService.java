package com.blue.secure.service.inter;

import com.blue.secure.repository.entity.Credential;

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
     * get by member id and type
     *
     * @param memberId
     * @param type
     * @return
     */
    Optional<Credential> getCredentialByMemberIdAndType(Long memberId, String type);

    /**
     * insert a new role
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

}
