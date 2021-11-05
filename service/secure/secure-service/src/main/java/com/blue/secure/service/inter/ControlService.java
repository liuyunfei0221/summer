package com.blue.secure.service.inter;

import com.blue.base.model.base.Access;
import com.blue.base.model.base.IdentityParam;
import com.blue.secure.api.model.ResourceInfo;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.ResourceInsertParam;
import com.blue.secure.model.ResourceUpdateParam;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.model.RoleUpdateParam;
import reactor.core.publisher.Mono;

/**
 * config role,resource,relation
 *
 * @author liuyunfei
 * @date 2021/11/1
 * @apiNote
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface ControlService {

    /**
     * refresh resource key/info or role-resource-relation
     *
     * @return
     */
    void refreshSystemAuthorityInfos();

    /**
     * update member role info by access
     *
     * @param access
     * @param roleId
     * @return
     */
    void updateMemberRoleByAccess(Access access, Long roleId);

    /**
     * update member role info by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    void updateMemberRoleById(Long memberId, Long roleId, Long operatorId);

    /**
     * set a default role to member
     *
     * @param memberId
     */
    void insertDefaultMemberRoleRelation(Long memberId);

    /**
     * update default role by role id
     *
     * @param id
     * @param operatorId
     */
    void updateDefaultRole(Long id, Long operatorId);

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
     * @param identityParam
     * @param operatorId
     * @return
     */
    RoleInfo deleteRole(IdentityParam identityParam, Long operatorId);

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

}
