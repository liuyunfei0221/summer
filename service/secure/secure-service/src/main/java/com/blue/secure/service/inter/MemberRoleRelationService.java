package com.blue.secure.service.inter;


import com.blue.secure.repository.entity.MemberRoleRelation;

import java.util.Optional;

/**
 * 用户角色关联业务接口
 *
 * @author DarkBlue
 */
@SuppressWarnings("JavaDoc")
public interface MemberRoleRelationService {

    /**
     * 根据成员id获取用户角色id
     *
     * @param memberId
     * @return
     */
    Optional<Long> getRoleIdByMemberId(Long memberId);

    /**
     * 更新成员对应的角色id
     *
     * @param memberId
     * @param roleId
     * @param operatorId
     */
    void updateMemberRoleRelation(Long memberId, Long roleId, Long operatorId);

    /**
     * 添加成员角色关联
     *
     * @param memberRoleRelation
     */
    void insertMemberRoleRelation(MemberRoleRelation memberRoleRelation);

}
