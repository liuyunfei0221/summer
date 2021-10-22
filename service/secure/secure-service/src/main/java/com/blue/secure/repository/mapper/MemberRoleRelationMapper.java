package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.MemberRoleRelation;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * member role relation mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "UnusedReturnValue"})
public interface MemberRoleRelationMapper {

    int insertSelective(MemberRoleRelation record);

    int updateByPrimaryKeySelective(MemberRoleRelation record);

    Long getRoleIdByMemberId(@Param("memberId") Long memberId);

    MemberRoleRelation getByMemberId(@Param("memberId") Long memberId);

    List<MemberRoleRelation> selectByMemberIds(@Param("memberIds") List<Long> memberIds);

}