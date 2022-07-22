package com.blue.auth.service.inter;

import com.blue.auth.api.model.*;
import com.blue.auth.model.MemberAccess;
import com.blue.auth.model.MemberAuth;
import com.blue.basic.model.common.Access;
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
     * @param roleIds
     * @param credentialType
     * @param deviceType
     * @return
     */
    Mono<MemberAuth> generateAuthMono(Long memberId, List<Long> roleIds, String credentialType, String deviceType);

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
     * get member's authorities by access
     *
     * @param access
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> selectAuthoritiesMonoByAccess(Access access);

    /**
     * get member's authorities by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<AuthorityBaseOnRole>> selectAuthoritiesMonoByMemberId(Long memberId);

    /**
     * get member's authority by access
     *
     * @param access
     * @return
     */
    Mono<MemberAuthority> getAuthorityMonoByAccess(Access access);

    /**
     * get member's authority by member id
     *
     * @param memberId
     * @return
     */
    Mono<MemberAuthority> getAuthorityMonoByMemberId(Long memberId);

}
