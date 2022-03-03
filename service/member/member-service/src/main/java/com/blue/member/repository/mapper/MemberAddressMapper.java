package com.blue.member.repository.mapper;

import com.blue.member.repository.entity.MemberAddress;

public interface MemberAddressMapper {

    int deleteByPrimaryKey(Long id);

    int insert(MemberAddress record);

    int insertSelective(MemberAddress record);

    MemberAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MemberAddress record);

    int updateByPrimaryKey(MemberAddress record);
}