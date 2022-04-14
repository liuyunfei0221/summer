package com.blue.auth.service.inter;


import com.blue.auth.repository.entity.MemberRoleRelation;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

/**
 * member role relation service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue"})
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
     * @return
     */
    int insertMemberRoleRelation(MemberRoleRelation memberRoleRelation);

    /**
     * insert member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    int insertMemberRoleRelation(Long memberId, Long roleId, Long operatorId);

    /**
     * update member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    int updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId);

}
