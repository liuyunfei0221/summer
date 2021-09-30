package com.blue.secure.service.inter;


import com.blue.secure.repository.entity.MemberRoleRelation;

import java.util.Optional;

/**
 * member role relation service
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberRoleRelationService {

    /**
     * get role id by member id
     *
     * @param memberId
     * @return
     */
    Optional<Long> getRoleIdByMemberId(Long memberId);

    /**
     * update member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     */
    void updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId);

    /**
     * insert member role relation
     *
     * @param memberRoleRelation
     */
    void insertMemberRoleRelation(MemberRoleRelation memberRoleRelation);

}
