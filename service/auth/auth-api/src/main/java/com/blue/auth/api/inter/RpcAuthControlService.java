package com.blue.auth.api.inter;

import com.blue.auth.api.model.AuthorityBaseOnRole;
import com.blue.auth.api.model.MemberCredentialInfo;
import com.blue.auth.api.model.MemberRoleRelationInsertOrDeleteParam;
import com.blue.auth.api.model.MemberRoleRelationUpdateParam;
import com.blue.base.model.common.Access;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * rpc auth control interface
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcAuthControlService {

    /**
     * init auth info for a new member
     *
     * @param memberCredentialInfo
     */
    void initMemberAuthInfo(MemberCredentialInfo memberCredentialInfo);

    /**
     * add authority base on member / insert member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    AuthorityBaseOnRole insertAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam);

    /**
     * update authority base on member / update member-role-relations sync with trans / not support for manager
     *
     * @param memberRoleRelationUpdateParam
     * @return
     */
    List<AuthorityBaseOnRole> updateAuthoritiesByMemberSync(MemberRoleRelationUpdateParam memberRoleRelationUpdateParam);

    /**
     * delete authority base on member / delete member-role-relations sync
     *
     * @param memberRoleRelationInsertOrDeleteParam
     * @return
     */
    AuthorityBaseOnRole deleteAuthorityByMemberSync(MemberRoleRelationInsertOrDeleteParam memberRoleRelationInsertOrDeleteParam);

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleIds
     * @return
     */
    CompletableFuture<Boolean> refreshMemberRoleById(Long memberId, List<Long> roleIds);

    /**
     * query authority by access
     *
     * @param access
     * @return
     */
    CompletableFuture<List<AuthorityBaseOnRole>> selectAuthorityByAccess(Access access);

    /**
     * query authority by member id
     *
     * @param memberId
     * @return
     */
    CompletableFuture<List<AuthorityBaseOnRole>> selectAuthorityByMemberId(Long memberId);

}
