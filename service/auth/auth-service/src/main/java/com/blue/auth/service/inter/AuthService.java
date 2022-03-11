package com.blue.auth.service.inter;

import com.blue.base.model.base.Access;
import com.blue.auth.api.model.AssertAuth;
import com.blue.auth.api.model.AuthAsserted;
import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.model.MemberAuth;
import reactor.core.publisher.Mono;

/**
 * auth service
 *
 * @author DarkBlue
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
     * assert auth
     *
     * @param assertAuth
     * @return
     */
    Mono<AuthAsserted> assertAuthMono(AssertAuth assertAuth);

    /**
     * generate member auth
     *
     * @param memberId
     * @param loginType
     * @param deviceType
     * @return
     */
    Mono<MemberAuth> generateAuthMono(Long memberId, String loginType, String deviceType);

    /**
     * generate member auth with auto register
     *
     * @param memberId
     * @param roleId
     * @param loginType
     * @param deviceType
     * @return
     */
    Mono<MemberAuth> generateAuthMono(Long memberId, Long roleId, String loginType, String deviceType);

    /**
     * invalid auth by access
     *
     * @param access
     * @return
     */
    Mono<Boolean> invalidAuthByAccess(Access access);

    /**
     * invalid auth by jwt
     *
     * @param jwt
     * @return
     */
    Mono<Boolean> invalidAuthByJwt(String jwt);

    /**
     * invalid auth by member id
     *
     * @param memberId
     * @return
     */
    Mono<Boolean> invalidAuthByMemberId(Long memberId);

    /**
     * invalid local auth by key id
     *
     * @param keyId
     * @return
     */
    Mono<Boolean> invalidLocalAuthByKeyId(String keyId);

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    void refreshMemberRoleById(Long memberId, Long roleId, Long operatorId);

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
