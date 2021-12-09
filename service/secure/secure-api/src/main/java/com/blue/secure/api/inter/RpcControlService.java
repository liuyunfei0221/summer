package com.blue.secure.api.inter;

/**
 * rpc control interface
 *
 * @author DarkBlue
 */
@SuppressWarnings({"JavaDoc", "unused"})
public interface RpcControlService {

    /**
     * assign default roles to member
     *
     * @param memberId
     */
    void insertDefaultMemberRoleRelation(Long memberId);

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