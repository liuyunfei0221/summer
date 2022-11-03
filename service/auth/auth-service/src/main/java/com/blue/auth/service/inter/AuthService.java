package com.blue.auth.service.inter;

import com.blue.auth.api.model.*;
import com.blue.auth.model.EncryptedDataParam;
import com.blue.auth.model.MemberAccess;
import com.blue.auth.model.MemberAuth;
import com.blue.basic.model.common.Access;
import com.blue.basic.model.common.Session;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * auth service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface AuthService {

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    void refreshSystemAuthorityInfos();

    /**
     * assert access
     *
     * @param accessAssert
     * @return
     */
    Mono<AccessAsserted> assertAccess(AccessAssert accessAssert);

    /**
     * generate member auth
     *
     * @param memberId
     * @param credentialType
     * @param deviceType
     * @return
     */
    Mono<MemberAuth> generateAuth(Long memberId, String credentialType, String deviceType);

    /**
     * generate member auth with auto register
     *
     * @param memberId
     * @param roleIds
     * @param credentialType
     * @param deviceType
     * @return
     */
    Mono<MemberAuth> generateAuth(Long memberId, List<Long> roleIds, String credentialType, String deviceType);

    /**
     * generate member access
     *
     * @param memberId
     * @param credentialType
     * @param deviceType
     * @return
     */
    Mono<MemberAccess> generateAccess(Long memberId, String credentialType, String deviceType);

    /**
     * refresh jwt by member payload
     *
     * @param memberPayload
     * @return
     */
    Mono<MemberAccess> refreshAccessByMemberPayload(MemberPayload memberPayload);

    /**
     * refresh jwt by refresh token
     *
     * @param refresh
     * @return
     */
    Mono<MemberAccess> refreshAccessByRefresh(String refresh);

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    Mono<Boolean> invalidateAuthByAccess(Access access);

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    Mono<Boolean> invalidateAuthByJwt(String jwt);

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    Mono<Boolean> invalidateAuthByMemberId(Long memberId);

    /**
     * invalid local access by key id
     *
     * @param keyId
     * @return
     */
    Mono<Boolean> invalidateLocalAccessByKeyId(String keyId);

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleIds
     * @return
     */
    Mono<Boolean> refreshMemberRoleById(Long memberId, List<Long> roleIds);

    /**
     * update member role info by member id
     *
     * @param memberId
     * @return
     */
    Mono<Boolean> refreshMemberRoleById(Long memberId);

    /**
     * update member sec key by access
     *
     * @param access
     * @return
     */
    Mono<String> updateSecKeyByAccess(Access access);

    /**
     * jwt -> payload
     *
     * @param authentication
     * @return
     */
    Mono<MemberPayload> parsePayload(String authentication);

    /**
     * jwt -> access
     *
     * @param authentication
     * @return
     */
    Mono<Access> parseAccess(String authentication);

    /**
     * jwt -> session
     *
     * @param authentication
     * @return
     */
    Mono<Session> parseSession(String authentication);

    /**
     * encrypted -> data
     *
     * @param encryptedDataParam
     * @return
     */
    Mono<String> parseEncrypted(EncryptedDataParam encryptedDataParam);

    /**
     * get member's authorities by access
     *
     * @param access
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByAccess(Access access);

    /**
     * get member's authorities by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> selectAuthoritiesByMemberId(Long memberId);

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    Mono<MemberAuthority> getAuthorityByAccess(Access access);

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    Mono<MemberAuthority> getAuthorityByMemberId(Long memberId);

}
