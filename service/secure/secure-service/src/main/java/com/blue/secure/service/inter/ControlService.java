package com.blue.secure.service.inter;

import com.blue.base.model.base.Access;
import com.blue.secure.api.model.RoleInfo;
import com.blue.secure.model.RoleInsertParam;
import com.blue.secure.model.RoleUpdateParam;

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
    RoleInfo insertRole(RoleInsertParam roleInsertParam, Long operatorId);

    /**
     * update a exist role
     *
     * @param roleUpdateParam
     * @param operatorId
     * @return
     */
    RoleInfo updateRole(RoleUpdateParam roleUpdateParam, Long operatorId);
}
