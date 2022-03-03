package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberRealName;

/**
 * member real name dao
 *
 * @author blue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MemberRealNameMapper {

    int insert(MemberRealName record);

    int insertSelective(MemberRealName record);

    int updateByPrimaryKey(MemberRealName record);

    int updateByPrimaryKeySelective(MemberRealName record);

    int deleteByPrimaryKey(Long id);

    MemberRealName selectByPrimaryKey(Long id);

}