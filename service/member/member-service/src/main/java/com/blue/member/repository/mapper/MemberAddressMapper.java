package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberAddress;

/**
 * member address dao
 *
 * @author blue
 */
@SuppressWarnings("AlibabaAbstractMethodOrInterfaceMethodMustUseJavadoc")
public interface MemberAddressMapper {

    int insert(MemberAddress record);

    int insertSelective(MemberAddress record);

    int updateByPrimaryKey(MemberAddress record);

    int updateByPrimaryKeySelective(MemberAddress record);

    int deleteByPrimaryKey(Long id);

    MemberAddress selectByPrimaryKey(Long id);

}