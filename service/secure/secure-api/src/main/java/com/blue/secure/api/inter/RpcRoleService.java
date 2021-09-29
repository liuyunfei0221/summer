package com.blue.secure.api.inter;

import com.blue.base.model.base.Access;

/**
 * rpc role interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcRoleService {

    /**
     * assign default roles to member
     *
     * @param memberId
     */
    void insertDefaultMemberRoleRelation(Long memberId);

    /**
     * update member's auth by access
     *
     * @param access
     * @param roleId
     * @return
     */
    void updateMemberRoleByAccess(Access access, Long roleId);

    /**
     * update member's auth by member id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    void updateMemberRoleById(Long memberId, Long roleId, Long operatorId);

}
