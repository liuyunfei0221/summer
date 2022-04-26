package com.blue.auth.service.inter;

import com.blue.auth.api.model.AccessAssert;
import com.blue.auth.api.model.AccessAsserted;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberPayload;
import com.blue.auth.model.MemberAccess;
import com.blue.auth.model.MemberAuth;
import com.blue.base.model.base.Access;
import reactor.core.publisher.Mono;

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
    Mono<AccessAsserted> assertAccessMono(AccessAssert accessAssert);

    /**
     * generate member auth
     *
     * @param memberId
     * @param credentialType
     * @param deviceType
     * @return
     */
    Mono<MemberAuth> generateAuthMono(Long memberId, String credentialType, String deviceType);

    /**
     * generate member auth with auto register
     *
     * @param memberId
     * @param roleId
     * @param credentialType
     * @param deviceType
     * @return
     */
    Mono<MemberAuth> generateAuthMono(Long memberId, Long roleId, String credentialType, String deviceType);

    /**
     * generate member access
     *
     * @param memberId
     * @param credentialType
     * @param deviceType
     * @return
     */
    Mono<MemberAccess> generateAccessMono(Long memberId, String credentialType, String deviceType);

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
     * @param roleId
     * @param operatorId
     * @return
     */
    Mono<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId);

    /**
     * update member sec key by access
     *
     * @param access
     * @return
     */
    Mono<String> updateSecKeyByAccess(Access access);

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    Mono<AuthorityBaseOnRole> getAuthorityMonoByAccess(Access access);

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    Mono<AuthorityBaseOnRole> getAuthorityMonoByMemberId(Long memberId);

}
