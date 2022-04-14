package com.blue.auth.api.inter;

import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.base.model.base.Access;
import com.blue.auth.api.model.AuthorityBaseOnRole;

import java.util.concurrent.CompletableFuture;

/**
 * rpc control interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcControlService {

    /**
     * init auth infos for a new member
     *
     * @param memberCredentialInfo
     */
    void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo);

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    void updateMemberRoleById(Long memberId, Long roleId, Long operatorId);

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    CompletableFuture<AuthorityBaseOnRole> getAuthorityByAccess(Access access);

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    CompletableFuture<AuthorityBaseOnRole> getAuthorityByMemberId(Long memberId);

}
