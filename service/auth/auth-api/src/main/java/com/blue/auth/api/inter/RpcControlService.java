package com.blue.auth.api.inter;

import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.api.model.MemberRoleRelationParam;
import com.blue.base.model.common.Access;

import java.util.concurrent.CompletableFuture;

/**
 * rpc control interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcControlService {

    /**
     * init auth info for a new member
     *
     * @param memberCredentialInfo
     */
    void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo);

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager
     *
     * @param memberRoleRelationParam
     * @return
     */
    AuthorityBaseOnRole updateAuthorityByMemberSync(MemberRoleRelationParam memberRoleRelationParam);

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    CompletableFuture<Boolean> refreshMemberRoleById(Long memberId, Long roleId, Long operatorId);

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
