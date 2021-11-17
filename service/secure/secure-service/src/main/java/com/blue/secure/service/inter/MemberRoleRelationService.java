package com.blue.secure.service.inter;


import com.blue.secure.repository.entity.MemberRoleRelation;
import reactor.core.publisher.Mono;

import java.util.List;
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
     * get role id mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<Optional<Long>> getRoleIdMonoByMemberId(Long memberId);

    /**
     * select member-role-relation by member ids
     *
     * @param memberIds
     * @return
     */
    Mono<List<MemberRoleRelation>> selectRelationMonoByMemberIds(List<Long> memberIds);

    /**
     * count relation by member id
     *
     * @param memberId
     * @return
     */
    long countRelationByMemberId(Long memberId);

    /**
     * count relation by role id
     *
     * @param roleId
     * @return
     */
    long countRelationByRoleId(Long roleId);

    /**
     * insert member role relation
     *
     * @param memberRoleRelation
     */
    void insertMemberRoleRelation(MemberRoleRelation memberRoleRelation);

    /**
     * update member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     */
    void updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId);

}
