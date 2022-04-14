package com.blue.auth.repository.mapper;

import com.blue.auth.repository.entity.MemberRoleRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member role relation mapper
 *
 * @author liuyunfei
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "UnusedReturnValue"})
public interface MemberRoleRelationMapper {

    int insert(MemberRoleRelation record);

    int insertSelective(MemberRoleRelation record);

    int updateByPrimaryKey(MemberRoleRelation record);

    int updateByPrimaryKeySelective(MemberRoleRelation record);

    int deleteByPrimaryKey(Long id);

    MemberRoleRelation selectByPrimaryKey(Long id);

    Long getRoleIdByMemberId(@Param("memberId") Long memberId);

    MemberRoleRelation getByMemberId(@Param("memberId") Long memberId);

    List<MemberRoleRelation> selectByMemberIds(@Param("memberIds") List<Long> memberIds);

    Long countByMemberId(@Param("memberId") Long memberId);

    Long countByRoleId(@Param("roleId") Long roleId);

}