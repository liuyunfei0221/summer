package com.blue.secure.api.inter;

import com.blue.base.model.base.Access;
import com.blue.secure.api.model.AuthorityBaseOnRole;
import com.blue.secure.api.model.MemberCredentialInfo;

import java.util.concurrent.CompletableFuture;

/**
 * rpc control interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcControlService {

    /**
     * init secure infos for a new member
     *
     * @param memberCredentialInfo
     */
    void initMemberSecureInfo(MemberCredentialInfo memberCredentialInfo);

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
