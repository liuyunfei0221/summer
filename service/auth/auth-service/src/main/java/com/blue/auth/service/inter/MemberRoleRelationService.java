package com.blue.auth.service.inter;


import com.blue.auth.repository.entity.MemberRoleRelation;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * member role relation service
 *
 * @author liuyunfei
 */
@SuppressWarnings({"JavaDoc", "UnusedReturnValue", "unused"})
public interface MemberRoleRelationService {

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
     * @param roleIds
     * @param operatorId
     * @return
     */
    int updateMemberRoleRelations(Long memberId, List<Long> roleIds, Long operatorId);

    /**
     * update member role relation
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     * @return
     */
    int deleteMemberRoleRelation(Long memberId, Long roleId, Long operatorId);

    /**
     * get role id by member id
     *
     * @param memberId
     * @return
     */
    List<Long> selectRoleIdsByMemberId(Long memberId);

    /**
     * get role id mono by member id
     *
     * @param memberId
     * @return
     */
    Mono<List<Long>> selectRoleIdsMonoByMemberId(Long memberId);

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

}
