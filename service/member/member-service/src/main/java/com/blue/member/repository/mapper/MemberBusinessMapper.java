package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberBusiness;

/**
 * member business dao
 *
 * @author blue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MemberBusinessMapper {

    int insert(MemberBusiness record);

    int insertSelective(MemberBusiness record);

    int updateByPrimaryKey(MemberBusiness record);

    int updateByPrimaryKeySelective(MemberBusiness record);

    int deleteByPrimaryKey(Long id);

    MemberBusiness selectByPrimaryKey(Long id);

}