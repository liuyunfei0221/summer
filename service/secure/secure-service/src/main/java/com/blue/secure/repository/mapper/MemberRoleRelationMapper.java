package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.MemberRoleRelation;
import org.apache.ibatis.annotations.Param;

/**
 * 成员角色关联关系持久层
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc", "UnusedReturnValue"})
public interface MemberRoleRelationMapper {

    /**
     * 新增成员角色关联
     *
     * @param record
     * @return
     */
    int insertSelective(MemberRoleRelation record);

    /**
     * 更新成员角色关联
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(MemberRoleRelation record);

    /**
     * 根据成员id获取成员角色id
     *
     * @param memberId
     * @return
     */
    Long getRoleIdByMemberId(@Param("memberId") Long memberId);

    /**
     * 根据成员id获取成员角色关联信息
     *
     * @param memberId
     * @return
     */
    MemberRoleRelation getMemberRoleRelationByMemberId(@Param("memberId") Long memberId);

}