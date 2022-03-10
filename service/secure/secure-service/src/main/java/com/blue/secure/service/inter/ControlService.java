package com.blue.secure.service.inter;

import com.blue.secure.api.model.*;
import com.blue.secure.model.*;
import reactor.core.publisher.Mono;

/**
 * config role,resource,relation
 *
 * @author liuyunfei
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused", "UnusedReturnValue"})
public interface ControlService {

    /**
     * get authority base on role by role id
     *
     * @param roleId
     * @return
     */
    Mono<AuthorityBaseOnRole> selectAuthorityMonoByRoleId(Long roleId);

    /**
     * get authority base on resource by res id
     *
     * @param resId
     * @return
     */
    Mono<AuthorityBaseOnResource> selectAuthorityMonoByResId(Long resId);

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    Mono<Void> refreshSystemAuthorityInfos();

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    int updateMemberRoleById(Long memberId, Long roleId, Long operatorId);

    /**
     * init secure infos for a new member
     *
     * @param memberCredentialInfo
     */
    void initMemberSecureInfo(MemberCredentialInfo memberCredentialInfo);

    /**
     * init secure infos for a new member
     *
     * @param memberCredentialInfo
     * @param roleId
     */
    void initMemberSecureInfo(MemberCredentialInfo memberCredentialInfo, Long roleId);

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     * @return
     */
    Mono<Void> updateDefaultRole(Long id, Long operatorId);

    /**
     * insert a new role
     *
     * @param roleInsertParam
     * @param operatorId
     * @return
     */
    Mono<RoleInfo> insertRole(RoleInsertParam roleInsertParam, Long operatorId);

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    Mono<RoleInfo> updateRole(RoleUpdateParam roleUpdateParam, Long operatorId);

    /**
     * delete a exist role
     *
     * @param id
     * @param operatorId
     * @return
     */
    Mono<RoleInfo> deleteRole(Long id, Long operatorId);

    /**
     * insert resource
     *
     * @param resourceInsertParam
     * @param operatorId
     * @return
     */
    Mono<ResourceInfo> insertResource(ResourceInsertParam resourceInsertParam, Long operatorId);

    /**
     * update a exist resource
     *
     * @param resourceUpdateParam
     * @param operatorId
     * @return
     */
    Mono<ResourceInfo> updateResource(ResourceUpdateParam resourceUpdateParam, Long operatorId);

    /**
     * delete a exist resource
     *
     * @param id
     * @param operatorId
     * @return
     */
    Mono<ResourceInfo> deleteResource(Long id, Long operatorId);

    /**
     * update authority base on role / generate role-resource-relations
     *
     * @param roleResRelationParam
     * @param operatorId
     * @return
     */
    Mono<AuthorityBaseOnRole> updateAuthorityByRole(RoleResRelationParam roleResRelationParam, Long operatorId);

    /**
     * update authority base on member / update member-role-relations
     *
     * @param memberRoleRelationParam
     * @param operatorId
     * @return
     */
    Mono<AuthorityBaseOnRole> updateAuthorityByMember(MemberRoleRelationParam memberRoleRelationParam, Long operatorId);

}
