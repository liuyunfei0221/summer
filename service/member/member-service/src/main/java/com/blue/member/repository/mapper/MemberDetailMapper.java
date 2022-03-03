package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberDetail;

/**
 * member detail dao
 *
 * @author blue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MemberDetailMapper {

    int insert(MemberDetail record);

    int insertSelective(MemberDetail record);

    int updateByPrimaryKey(MemberDetail record);

    int updateByPrimaryKeySelective(MemberDetail record);

    int deleteByPrimaryKey(Long id);

    MemberDetail selectByPrimaryKey(Long id);

}