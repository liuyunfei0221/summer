package com.blue.secure.service.inter;

import com.blue.base.model.base.Access;
import com.blue.secure.api.model.*;
import com.blue.secure.model.AuthGenElement;
import com.blue.secure.model.MemberAuth;
import reactor.core.publisher.Mono;

/**
 * secure service
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue", "GrazieInspection"})
public interface SecureService {

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    void refreshSystemAuthorityInfos();

    /**
     * login by client
     *
     * @param clientLoginParam
     * @return
     */
    Mono<MemberAuth> loginByClient(ClientLoginParam clientLoginParam);

    /**
     * login by wechat mini program
     *
     * @param miniProLoginParam
     * @return
     */
    Mono<MemberAuth> loginByMiniPro(MiniProLoginParam miniProLoginParam);

    /**
     * login by wechat
     *
     * @param wechatProLoginParam
     * @return
     */
    Mono<MemberAuth> loginByWechat(WechatProLoginParam wechatProLoginParam);

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
     * @param authGenElement
     * @return
     */
    Mono<MemberAuth> generateAuthMono(AuthGenElement authGenElement);

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
