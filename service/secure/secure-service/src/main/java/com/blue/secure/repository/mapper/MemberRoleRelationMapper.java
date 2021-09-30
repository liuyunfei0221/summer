package com.blue.secure.repository.mapper;

import com.blue.secure.repository.entity.MemberRoleRelation;
import org.apache.ibatis.annotations.Param;

/**
 * member role relation mapper
 *
 * @author DarkBlue
 */
@SuppressWarnings({"unused", "AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc", "JavaDoc", "UnusedReturnValue"})
public interface MemberRoleRelationMapper {

    /**
     * insert relation
     *
     * @param record
     * @return
     */
    int insertSelective(MemberRoleRelation record);

    /**
     * update relation
     *
     * @param record
     * @return
     */
    int updateByPrimaryKeySelective(MemberRoleRelation record);

    /**
     * get member's role id by member's id
     *
     * @param memberId
     * @return
     */
    Long getRoleIdByMemberId(@Param("memberId") Long memberId);

    /**
     * get member role relation  by member's id
     *
     * @param memberId
     * @return
     */
    MemberRoleRelation getMemberRoleRelationByMemberId(@Param("memberId") Long memberId);

}